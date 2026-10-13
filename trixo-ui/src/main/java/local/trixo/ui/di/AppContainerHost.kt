package local.trixo.ui.di

/**
 * Implemented by the host app's [android.app.Application] so UI code can reach
 * the [AppContainer] without depending on the app module (avoids a cycle:
 * `:app` → `:trixo-ui`, never the reverse).
 */
interface AppContainerHost {
    val container: AppContainer
}
