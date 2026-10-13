package local.trixo.ui.presentation.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import local.core.ui.data.AudioSettings
import local.core.ui.data.CoinAccount
import local.core.ui.data.InventoryStore
import local.trixo.engine.domain.model.Difficulty
import local.trixo.engine.domain.model.GameResult
import local.trixo.engine.domain.model.Player
import local.trixo.engine.domain.model.RunEndCause
import local.trixo.engine.domain.model.RunRecord
import local.trixo.engine.domain.repository.RunHistoryRepository
import local.trixo.ui.infrastructure.persistence.TrixoStatsRepository
import local.trixo.ui.application.RecordGameFinishedUseCase

data class GameState(
    val difficulty: Difficulty = Difficulty.EASY,
    val board: List<Player> = List(3 * 3) { Player.NONE },
    val currentPlayer: Player = Player.X,
    val result: GameResult = GameResult.NONE,
    val isAiTurn: Boolean = false,
    val winningLine: List<Int>? = null,
    val hintIndex: Int? = null,
    val history: List<List<Player>> = emptyList(),
    val isPvP: Boolean = false,
    val score: Int = 0,
    val reward: Int = 0,
    val sessionStartedAt: Long = System.currentTimeMillis()
) {
    val boardSize get() = difficulty.size
    val winRequirement get() = difficulty.winReq
}

data class UserPreferences(
    val coins: Int = 300,
    val hints: Int = 0,
    val undos: Int = 0,
    val soundEnabled: Boolean = true,
    val highScore: Int = 0,
    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0
)

data class MainUiState(
    val userPreferences: UserPreferences = UserPreferences(),
    val gameHistory: List<RunRecord> = emptyList()
)

sealed class MainEvent {
    data class AddCoins(val amount: Int) : MainEvent()
    data class SpendCoins(
        val amount: Int,
        val onSuccess: () -> Unit = {},
        val onFailure: () -> Unit = {}
    ) : MainEvent()

    data class AddHints(val amount: Int) : MainEvent()
    data class ConsumeHint(val onSuccess: () -> Unit = {}, val onFailure: () -> Unit = {}) :
        MainEvent()

    data class AddUndos(val amount: Int) : MainEvent()
    data class ConsumeUndo(val onSuccess: () -> Unit = {}, val onFailure: () -> Unit = {}) :
        MainEvent()

    data class SetSoundEnabled(val enabled: Boolean) : MainEvent()
    data class RecordGameFinished(
        val difficulty: Difficulty,
        val boardSize: Int,
        val moveCount: Int,
        val result: GameResult,
        val score: Int = 0,
        val reward: Int = 0,
        val durationMs: Long = 0L,
        val onNewHighScore: () -> Unit = {}
    ) : MainEvent()
}

class MainViewModel(
    private val coinAccount: CoinAccount,
    private val inventoryStore: InventoryStore,
    private val audioSettings: AudioSettings,
    private val runHistoryRepository: RunHistoryRepository,
    private val statsRepository: TrixoStatsRepository,
    private val recordGameFinishedUseCase: RecordGameFinishedUseCase
) : ViewModel() {

    // Power-up uses in the current game session (reset on record).
    private var sessionHintUses = 0
    private var sessionUndoUses = 0

    val uiState: StateFlow<MainUiState> = combine(
        coinAccount.coinsFlow,
        inventoryStore.inventoryCountFlow("hint"),
        inventoryStore.inventoryCountFlow("undo"),
        audioSettings.sfxEnabledFlow,
        runHistoryRepository.highScoreFlow,
        runHistoryRepository.recentRunsFlow(HISTORY_LIMIT),
        statsRepository.gamesPlayedFlow,
        statsRepository.gamesWonFlow
    ) { args: Array<*> ->
        @Suppress("UNCHECKED_CAST")
        val history = args[5] as List<RunRecord>
        MainUiState(
            UserPreferences(
                coins = args[0] as Int,
                hints = args[1] as Int,
                undos = args[2] as Int,
                soundEnabled = args[3] as Boolean,
                highScore = args[4] as Int,
                gamesPlayed = args[6] as Int,
                gamesWon = args[7] as Int
            ),
            history
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = MainUiState()
    )

    fun onEvent(event: MainEvent) {
        when (event) {
            is MainEvent.AddCoins -> addCoins(event.amount)
            is MainEvent.SpendCoins -> spendCoins(event.amount, event.onSuccess, event.onFailure)
            is MainEvent.AddHints -> addHints(event.amount)
            is MainEvent.ConsumeHint -> consumeHint(event.onSuccess, event.onFailure)
            is MainEvent.AddUndos -> addUndos(event.amount)
            is MainEvent.ConsumeUndo -> consumeUndo(event.onSuccess, event.onFailure)
            is MainEvent.SetSoundEnabled -> setSoundEnabled(event.enabled)
            is MainEvent.RecordGameFinished -> recordGameFinished(event)
        }
    }

    private fun addCoins(amount: Int) {
        viewModelScope.launch {
            coinAccount.addCoins(amount)
        }
    }

    private fun spendCoins(amount: Int, onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}) {
        viewModelScope.launch {
            val spent = coinAccount.spendCoins(amount)
            if (spent) {
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    private fun addHints(amount: Int) {
        viewModelScope.launch {
            repeat(amount) { inventoryStore.addToInventory("hint") }
        }
    }

    private fun consumeHint(onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}) {
        viewModelScope.launch {
            val consumed = inventoryStore.consumeFromInventory("hint")
            if (consumed) {
                sessionHintUses++
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    private fun addUndos(amount: Int) {
        viewModelScope.launch {
            repeat(amount) { inventoryStore.addToInventory("undo") }
        }
    }

    private fun consumeUndo(onSuccess: () -> Unit = {}, onFailure: () -> Unit = {}) {
        viewModelScope.launch {
            val consumed = inventoryStore.consumeFromInventory("undo")
            if (consumed) {
                sessionUndoUses++
                onSuccess()
            } else {
                onFailure()
            }
        }
    }

    private fun setSoundEnabled(enabled: Boolean) {
        viewModelScope.launch {
            // SFX-only game: the single sound toggle drives sfx (music follows
            // for forward-compat if a BGM is ever added).
            audioSettings.toggleSfx(enabled)
            audioSettings.toggleMusic(enabled)
        }
    }

    private fun recordGameFinished(event: MainEvent.RecordGameFinished) {
        viewModelScope.launch {
            val now = System.currentTimeMillis()
            val endCause = when (event.result) {
                GameResult.X_WINS -> RunEndCause.X_WINS
                GameResult.O_WINS -> RunEndCause.O_WINS
                else -> RunEndCause.DRAW
            }
            val isNewHigh = recordGameFinishedUseCase(
                RunRecord(
                    mode = event.difficulty.name,
                    level = event.boardSize,
                    score = event.score,
                    reward = event.reward,
                    durationMs = event.durationMs,
                    pipesPassed = event.moveCount,
                    endCause = endCause,
                    shieldUses = sessionHintUses,
                    multiplierUses = sessionUndoUses,
                    startedAt = now - event.durationMs,
                    endedAt = now
                )
            )
            sessionHintUses = 0
            sessionUndoUses = 0
            if (isNewHigh) {
                event.onNewHighScore()
            }
        }
    }

    companion object {
        const val HISTORY_LIMIT = 50
    }
}
