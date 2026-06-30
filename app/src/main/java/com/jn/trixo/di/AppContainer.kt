package com.jn.trixo.di

import android.content.Context
import com.jn.trixo.billing.BillingManager
import com.jn.trixo.data.TrixoDatabase
import com.jn.trixo.data.dataStore
import com.jn.trixo.data.repository.GameHistoryRepositoryImpl
import com.jn.trixo.data.repository.UserPreferencesRepositoryImpl
import com.jn.trixo.domain.repository.GameHistoryRepository
import com.jn.trixo.domain.repository.UserPreferencesRepository
import com.jn.trixo.domain.usecase.CalculateScoreUseCase
import com.jn.trixo.domain.usecase.GetAiMoveUseCase
import com.jn.trixo.domain.usecase.GetGameResultUseCase
import com.jn.trixo.domain.usecase.RecordGameFinishedUseCase

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
