package local.trixo.ui.application

import kotlinx.coroutines.flow.first
import local.core.ui.data.CoinAccount
import local.trixo.engine.domain.model.RunEndCause
import local.trixo.engine.domain.repository.RunHistoryRepository
import local.trixo.ui.infrastructure.persistence.TrixoStatsRepository
import local.trixo.engine.domain.model.RunRecord

/**
 * Records a finished game: stats counters + coin reward + run-history row.
 * High score is derived from runs (core), so this returns whether [record]
 * set a new high (for the milestone sound).
 */
class RecordGameFinishedUseCase(
    private val coinAccount: CoinAccount,
    private val runs: RunHistoryRepository,
    private val stats: TrixoStatsRepository
) {
    suspend operator fun invoke(record: RunRecord): Boolean {
        val previousHigh = runs.highScoreFlow.first()

        stats.incrementGamesPlayed()
        if (record.endCause == RunEndCause.X_WINS) {
            stats.incrementGamesWon()
        }
        if (record.reward > 0) {
            coinAccount.addCoins(record.reward)
        }
        runs.recordRun(record)

        return record.score > 0 && record.score > previousHigh
    }
}
