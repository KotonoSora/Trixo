package local.trixo.engine.domain.model

enum class RunEndCause {
    X_WINS,
    O_WINS,
    DRAW
}

/**
 * One played game, engine-owned. Field-for-field compatible with core's
 * RunRecord so the ui adapters map without a Room migration:
 * - mode = difficulty name (EASY/MEDIUM/HARD/VERY_HARD) or GameMode
 * - level = board size
 * - pipesPassed = move count (repurposed Flappy field, no schema change)
 * - shieldUses = hint uses, multiplierUses = undo uses, autoPlayUses = 0
 * - deathX/deathY always null (no death position in TicTacToe)
 */
data class RunRecord(
    val mode: String,
    val level: Int,
    val score: Int,
    val reward: Int,
    val durationMs: Long,
    val pipesPassed: Int,
    val endCause: RunEndCause,
    val shieldUses: Int = 0,
    val multiplierUses: Int = 0,
    val autoPlayUses: Int = 0,
    val deathX: Float? = null,
    val deathY: Float? = null,
    val startedAt: Long,
    val endedAt: Long
) {
    /** Display projection for the leaderboard, which shows score/reward/time. */
    fun toScoreEntry(): ScoreEntry =
        ScoreEntry(score = score, reward = reward, timestamp = endedAt)
}
