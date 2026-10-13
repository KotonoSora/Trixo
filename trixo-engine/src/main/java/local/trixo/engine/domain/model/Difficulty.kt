package local.trixo.engine.domain.model

enum class Difficulty(val size: Int, val winReq: Int) {
    EASY(3, 3),
    MEDIUM(9, 4),
    HARD(12, 5),
    VERY_HARD(15, 6)
}
