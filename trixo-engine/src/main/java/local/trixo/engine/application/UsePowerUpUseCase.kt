package local.trixo.engine.application

import local.trixo.engine.domain.model.PowerUpType
import local.trixo.engine.domain.repository.PowerUpStore

class UsePowerUpUseCase(private val store: PowerUpStore) {
    suspend operator fun invoke(typeId: String): Boolean {
        PowerUpType.fromId(typeId) ?: return false
        return store.usePowerUp(typeId)
    }
}
