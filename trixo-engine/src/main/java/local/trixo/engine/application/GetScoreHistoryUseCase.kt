package local.trixo.engine.application

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import local.trixo.engine.domain.model.ScoreEntry
import local.trixo.engine.domain.repository.RunHistoryRepository

class GetScoreHistoryUseCase(private val runs: RunHistoryRepository) {
    operator fun invoke(limit: Int = DEFAULT_LIMIT): Flow<List<ScoreEntry>> =
        runs.recentRunsFlow(limit).map { list -> list.map { it.toScoreEntry() } }

    companion object {
        const val DEFAULT_LIMIT = 20
    }
}
