package local.trixo.engine.application

import local.trixo.engine.domain.model.RunRecord
import local.trixo.engine.domain.repository.RunHistoryRepository

class RecordRunUseCase(private val runs: RunHistoryRepository) {
    suspend operator fun invoke(record: RunRecord): Long = runs.recordRun(record)
}
