package com.kotonosora.blitzxo.di

import android.content.Context
import com.kotonosora.blitzxo.billing.BillingManager
import com.kotonosora.blitzxo.data.TrixoDatabase
import com.kotonosora.blitzxo.data.dataStore
import com.kotonosora.blitzxo.data.repository.GameHistoryRepositoryImpl
import com.kotonosora.blitzxo.data.repository.UserPreferencesRepositoryImpl
import com.kotonosora.blitzxo.domain.repository.GameHistoryRepository
import com.kotonosora.blitzxo.domain.repository.UserPreferencesRepository
import com.kotonosora.blitzxo.domain.usecase.CalculateScoreUseCase
import com.kotonosora.blitzxo.domain.usecase.GetAiMoveUseCase
import com.kotonosora.blitzxo.domain.usecase.GetGameResultUseCase
import com.kotonosora.blitzxo.domain.usecase.RecordGameFinishedUseCase

interface AppContainer {
    val userPreferencesRepository: UserPreferencesRepository
    val gameHistoryRepository: GameHistoryRepository
    val billingManager: BillingManager
    val getAiMoveUseCase: GetAiMoveUseCase
    val getGameResultUseCase: GetGameResultUseCase
    val calculateScoreUseCase: CalculateScoreUseCase
    val recordGameFinishedUseCase: RecordGameFinishedUseCase
}

class AppContainerImpl(private val context: Context) : AppContainer {

    private val database: TrixoDatabase by lazy {
        TrixoDatabase.getDatabase(context)
    }

    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepositoryImpl(context.dataStore)
    }

    override val gameHistoryRepository: GameHistoryRepository by lazy {
        GameHistoryRepositoryImpl(database.gameHistoryDao())
    }

    override val billingManager: BillingManager by lazy {
        BillingManager(context, userPreferencesRepository)
    }

    override val getAiMoveUseCase: GetAiMoveUseCase by lazy {
        GetAiMoveUseCase()
    }

    override val getGameResultUseCase: GetGameResultUseCase by lazy {
        GetGameResultUseCase()
    }

    override val calculateScoreUseCase: CalculateScoreUseCase by lazy {
        CalculateScoreUseCase()
    }

    override val recordGameFinishedUseCase: RecordGameFinishedUseCase by lazy {
        RecordGameFinishedUseCase(userPreferencesRepository, gameHistoryRepository)
    }
}
