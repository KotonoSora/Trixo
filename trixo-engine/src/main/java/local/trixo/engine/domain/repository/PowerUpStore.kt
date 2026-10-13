package local.trixo.engine.domain.repository

/**
 * Narrow power-up wallet. The app's settings repository adapts to this so
 * `:trixo-engine` never depends on Android DataStore or app classes.
 */
interface PowerUpStore {
    suspend fun usePowerUp(typeId: String): Boolean
}
