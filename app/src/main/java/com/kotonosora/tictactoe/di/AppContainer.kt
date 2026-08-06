package com.kotonosora.tictactoe.di

import android.content.Context
import com.kotonosora.tictactoe.billing.BillingManager
import com.kotonosora.tictactoe.data.TrixoDatabase
import com.kotonosora.tictactoe.data.dataStore
import com.kotonosora.tictactoe.data.repository.GameHistoryRepositoryImpl
import com.kotonosora.tictactoe.data.repository.UserPreferencesRepositoryImpl
import com.kotonosora.tictactoe.domain.repository.GameHistoryRepository
import com.kotonosora.tictactoe.domain.repository.UserPreferencesRepository
import com.kotonosora.tictactoe.domain.usecase.CalculateScoreUseCase
import com.kotonosora.tictactoe.domain.usecase.GetAiMoveUseCase
import com.kotonosora.tictactoe.domain.usecase.GetGameResultUseCase
import com.kotonosora.tictactoe.domain.usecase.RecordGameFinishedUseCase

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
