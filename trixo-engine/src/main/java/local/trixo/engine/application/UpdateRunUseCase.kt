package local.trixo.engine.application

import local.trixo.engine.domain.model.RunRecord
import local.trixo.engine.domain.repository.RunHistoryRepository

class UpdateRunUseCase(private val runs: RunHistoryRepository) {
    suspend operator fun invoke(id: Long, record: RunRecord) = runs.updateRun(id, record)
}
