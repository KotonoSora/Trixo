package com.jn.trixo.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.jn.trixo.domain.usecase.CalculateScoreUseCase
import com.jn.trixo.domain.usecase.GetAiMoveUseCase
import com.jn.trixo.domain.usecase.GetGameResultUseCase
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.milliseconds

enum class Player { X, O, NONE }
enum class GameResult { NONE, X_WINS, O_WINS, DRAW }
enum class Difficulty(val size: Int, val winReq: Int) {
    EASY(3, 3),
    MEDIUM(9, 4),
    HARD(12, 5),
    VERY_HARD(15, 6)
}

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
    val reward: Int = 0
) {
    val boardSize get() = difficulty.size
    val winRequirement get() = difficulty.winReq
}

sealed class GameEvent {
    data class PlayMove(val index: Int) : GameEvent()
    object UndoMove : GameEvent()
    object RequestHint : GameEvent()
    data class ResetGame(val difficulty: Difficulty, val isPvP: Boolean) : GameEvent()
}

class GameViewModel(
    private val getAiMoveUseCase: GetAiMoveUseCase = GetAiMoveUseCase(),
    private val getGameResultUseCase: GetGameResultUseCase = GetGameResultUseCase(),
    private val calculateScoreUseCase: CalculateScoreUseCase = CalculateScoreUseCase()
) : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    private var aiMoveJob: Job? = null
    private var gameSessionId: Long = 0L

    fun onEvent(event: GameEvent) {
        when (event) {
            is GameEvent.PlayMove -> playMove(event.index)
            GameEvent.UndoMove -> undoMove()
            GameEvent.RequestHint -> requestHint()
            is GameEvent.ResetGame -> resetGame(event.difficulty, event.isPvP)
        }
    }

    private fun playMove(index: Int) {
        val state = _gameState.value
        if (index !in state.board.indices) return
        if (state.board[index] != Player.NONE || state.result != GameResult.NONE || state.isAiTurn) {
            return
        }

        // Save current board to history before making a move
        val currentHistory = state.history.toMutableList()
        currentHistory.add(state.board)

        val newBoard = state.board.toMutableList()
        newBoard[index] = state.currentPlayer

        val (result, winLine) = getGameResultUseCase(
            newBoard,
            state.boardSize,
            state.winRequirement
        )

        if (result != GameResult.NONE) {
            val (score, reward) = calculateScoreUseCase(state.difficulty, result, newBoard)
            _gameState.value = state.copy(
                board = newBoard,
                result = result,
                winningLine = winLine,
                hintIndex = null,
                history = currentHistory,
                score = score,
                reward = reward
            )
        } else {
            val nextPlayer = if (state.currentPlayer == Player.X) Player.O else Player.X
            val isNextAiTurn = !state.isPvP && nextPlayer == Player.O

            _gameState.value = state.copy(
                board = newBoard,
                currentPlayer = nextPlayer,
                isAiTurn = isNextAiTurn,
                hintIndex = null,
                history = currentHistory
            )

            if (isNextAiTurn) {
                playAIMove(gameSessionId)
            }
        }
    }

    private fun undoMove() {
        val state = _gameState.value
        if (state.history.isEmpty() || state.isAiTurn || state.result != GameResult.NONE) return

        val lastBoard = state.history.last()
        _gameState.value = state.copy(
            board = lastBoard,
            history = state.history.dropLast(1),
            currentPlayer = if (state.isPvP) (if (state.currentPlayer == Player.X) Player.O else Player.X) else Player.X,
            result = GameResult.NONE,
            winningLine = null,
            hintIndex = null,
            isAiTurn = false
        )
    }

    private fun requestHint() {
        val state = _gameState.value
        if (state.result != GameResult.NONE || state.isAiTurn) return

        val move =
            getAiMoveUseCase(
                state.board,
                state.currentPlayer,
                state.boardSize,
                state.winRequirement
            )
        if (move != -1) {
            _gameState.value = state.copy(hintIndex = move)
        }
    }

    private fun playAIMove(sessionId: Long) {
        aiMoveJob?.cancel()
        aiMoveJob = viewModelScope.launch {
            delay(300.milliseconds) // Simulate "thinking" for a better UX
            val state = _gameState.value
            if (sessionId != gameSessionId) return@launch
            if (state.result != GameResult.NONE || !state.isAiTurn || state.currentPlayer != Player.O) return@launch

            val move =
                getAiMoveUseCase(state.board, Player.O, state.boardSize, state.winRequirement)
            if (move != -1) {
                val newBoard = state.board.toMutableList()
                newBoard[move] = Player.O

                val (result, winLine) = getGameResultUseCase(
                    newBoard,
                    state.boardSize,
                    state.winRequirement
                )
                val (score, reward) = calculateScoreUseCase(state.difficulty, result, newBoard)
                _gameState.value = state.copy(
                    board = newBoard,
                    currentPlayer = Player.X,
                    result = result,
                    isAiTurn = false,
                    winningLine = winLine,
                    score = score,
                    reward = reward
                )
            }
        }
    }

    private fun resetGame(
        difficulty: Difficulty = _gameState.value.difficulty,
        isPvP: Boolean = false
    ) {
        aiMoveJob?.cancel()
        gameSessionId++
        _gameState.value = GameState(
            difficulty = difficulty,
            board = List(difficulty.size * difficulty.size) { Player.NONE },
            history = emptyList(),
            isPvP = isPvP
        )
    }

    override fun onCleared() {
        aiMoveJob?.cancel()
    }
}
