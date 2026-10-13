package local.trixo.engine.domain.model

enum class PowerUpType(val id: String) {
    HINT("hint"),
    UNDO("undo");

    companion object {
        fun fromId(id: String) = entries.find { it.id == id }
    }
}
