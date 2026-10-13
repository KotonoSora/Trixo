package local.trixo.ui.di

import android.content.Context
import android.content.pm.PackageManager
import local.core.billing.BillingConfig
import local.core.billing.BillingManagerImpl
import local.core.billing.BillingRepository
import local.core.billing.MockBillingManager
import local.core.shop.coins.CoinPackIds
import local.core.shop.coins.CoinPacks
import local.core.ui.data.AudioSettings
import local.core.ui.data.CoinAccount
import local.core.ui.data.InventoryStore
import local.core.ui.data.RoomRunStore
import local.core.ui.data.RunStore
import local.core.ui.data.SettingsRepositoryImpl
import local.core.ui.data.SkinLibrary
import local.core.ui.data.db.GameDatabase
import local.core.ui.data.db.createGameDatabase
import local.core.ui.usecase.GetAudioSettingsUseCase
import local.core.ui.usecase.GetCoinsUseCase
import local.core.ui.usecase.ToggleAudioUseCase
import local.trixo.engine.domain.repository.RunHistoryRepository
import local.trixo.engine.domain.service.AiMoveService
import local.trixo.engine.domain.service.GameRulesService
import local.trixo.engine.application.GetHighScoreUseCase
import local.trixo.engine.application.GetScoreHistoryUseCase
import local.trixo.engine.application.RecordRunUseCase
import local.trixo.engine.application.UpdateRunUseCase
import local.trixo.engine.application.UsePowerUpUseCase
import local.trixo.shop.model.SkinIds
import local.trixo.ui.adapters.asCoinSink
import local.trixo.ui.adapters.asPowerUpStore
import local.trixo.ui.adapters.asRunHistoryRepository
import local.trixo.ui.infrastructure.persistence.TrixoStatsRepository
import local.trixo.ui.application.RecordGameFinishedUseCase

/**
 * Trixo-flavored description seam: Hint/Undo wording instead of the core
 * default (Extra Time/Hint/Undo). Set once here so :core-shop-coins is never
 * forked for copy changes.
 */
private const val TRIXO_COIN_DESC_TEMPLATE =
    "A pack of %d coins used to buy Hint and Undo power-ups."

/**
 * Dependency Injection container for the application. Manual DI, no Hilt/Koin.
 */
interface AppContainer {
    val coinAccount: CoinAccount
    val inventoryStore: InventoryStore
    val skinLibrary: SkinLibrary
    val audioSettings: AudioSettings
    val statsRepository: TrixoStatsRepository
    val runHistoryRepository: RunHistoryRepository
    val billingRepository: BillingRepository
    val appVersionName: String

    val aiMoveService: AiMoveService
    val gameRulesService: GameRulesService
    val recordGameFinishedUseCase: RecordGameFinishedUseCase
    val getHighScoreUseCase: GetHighScoreUseCase
    val getScoreHistoryUseCase: GetScoreHistoryUseCase
    val recordRunUseCase: RecordRunUseCase
    val updateRunUseCase: UpdateRunUseCase
    val getAudioSettingsUseCase: GetAudioSettingsUseCase
    val getCoinsUseCase: GetCoinsUseCase
    val toggleAudioUseCase: ToggleAudioUseCase
    val usePowerUpUseCase: UsePowerUpUseCase
}

/**
 * Default implementation of the [AppContainer] interface.
 */
class DefaultAppContainer(
    private val context: Context,
    private val isDebug: Boolean
) : AppContainer {

    init {
        // Per-game copy seam (core-enhancements fix #2).
        CoinPacks.descriptionTemplate = TRIXO_COIN_DESC_TEMPLATE
    }

    /**
     * Single DataStore-backed store, exposed through narrow ports so each
     * consumer depends only on what it uses. Trixo inventory map (hint/undo)
     * is injected via constructor (core-enhancements fix #1).
     */
    private val settingsStore: SettingsRepositoryImpl by lazy {
        SettingsRepositoryImpl(
            context,
            SkinIds.SKIN_DEFAULT_ID,
            SettingsRepositoryImpl.trixoInventoryKeys()
        )
    }

    override val coinAccount: CoinAccount by lazy { settingsStore }
    override val inventoryStore: InventoryStore by lazy { settingsStore }
    override val skinLibrary: SkinLibrary by lazy { settingsStore }
    override val audioSettings: AudioSettings by lazy { settingsStore }

    override val statsRepository: TrixoStatsRepository by lazy {
        TrixoStatsRepository(context)
    }

    private val gameDatabase: GameDatabase by lazy {
        createGameDatabase(context)
    }

    private val runStore: RunStore by lazy {
        RoomRunStore(gameDatabase.runDao())
    }

    override val runHistoryRepository: RunHistoryRepository by lazy {
        runStore.asRunHistoryRepository()
    }

    override val billingRepository: BillingRepository by lazy {
        val wallet = coinAccount.asCoinSink()
        if (isDebug) {
            MockBillingManager(wallet, CoinPacks.mockProducts, CoinPacks::getCoinAmount)
        } else {
            BillingManagerImpl(
                context,
                wallet,
                BillingConfig(CoinPackIds.ALL, CoinPacks::getCoinAmount)
            )
        }
    }

    override val appVersionName: String by lazy {
        try {
            context.packageManager.getPackageInfo(context.packageName, 0).versionName ?: "1.0"
        } catch (_: PackageManager.NameNotFoundException) {
            "1.0"
        }
    }

    override val aiMoveService: AiMoveService by lazy {
        AiMoveService()
    }

    override val gameRulesService: GameRulesService by lazy {
        GameRulesService()
    }

    override val recordGameFinishedUseCase: RecordGameFinishedUseCase by lazy {
        RecordGameFinishedUseCase(coinAccount, runHistoryRepository, statsRepository)
    }

    override val getHighScoreUseCase: GetHighScoreUseCase by lazy {
        GetHighScoreUseCase(runHistoryRepository)
    }

    override val getScoreHistoryUseCase: GetScoreHistoryUseCase by lazy {
        GetScoreHistoryUseCase(runHistoryRepository)
    }

    override val recordRunUseCase: RecordRunUseCase by lazy {
        RecordRunUseCase(runHistoryRepository)
    }

    override val updateRunUseCase: UpdateRunUseCase by lazy {
        UpdateRunUseCase(runHistoryRepository)
    }

    override val getAudioSettingsUseCase: GetAudioSettingsUseCase by lazy {
        GetAudioSettingsUseCase(audioSettings)
    }

    override val getCoinsUseCase: GetCoinsUseCase by lazy {
        GetCoinsUseCase(coinAccount)
    }

    override val toggleAudioUseCase: ToggleAudioUseCase by lazy {
        ToggleAudioUseCase(audioSettings)
    }

    override val usePowerUpUseCase: UsePowerUpUseCase by lazy {
        UsePowerUpUseCase(inventoryStore.asPowerUpStore())
    }
}
