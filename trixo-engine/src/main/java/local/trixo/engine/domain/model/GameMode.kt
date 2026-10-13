package local.trixo.engine.domain.model

object GameMode {
    const val VS_AI = "vs_ai"
    const val LOCAL_PVP = "local_pvp"

    fun of(isPvP: Boolean): String = if (isPvP) LOCAL_PVP else VS_AI
}
