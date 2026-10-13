package local.trixo.engine.domain.service

import local.trixo.engine.domain.model.Board
import local.trixo.engine.domain.model.Difficulty
import local.trixo.engine.domain.model.GameResult
import local.trixo.engine.domain.model.Player
import kotlin.math.min

/**
 * Domain service: scoring and coin-reward policy. Pure rules, no ports.
 * Returns `score to rewardCoins`.
 */
object RewardPolicy {
    operator fun invoke(
        difficulty: Difficulty,
        result: GameResult,
        board: Board
    ): Pair<Int, Int> {
        if (result == GameResult.NONE) return 0 to 0

        val difficultyMultiplier = when (difficulty) {
            Difficulty.EASY -> 1
            Difficulty.MEDIUM -> 2
            Difficulty.HARD -> 3
            Difficulty.VERY_HARD -> 5
        }

        val score = when (result) {
            GameResult.X_WINS -> (10 + (board.emptyCells * 10)) * difficultyMultiplier
            GameResult.DRAW -> 5 * difficultyMultiplier
            else -> 1 * difficultyMultiplier
        }

        val reward = when (result) {
            GameResult.X_WINS -> min(10 * difficultyMultiplier, 50)
            GameResult.DRAW -> min(5 * difficultyMultiplier, 25)
            else -> 0
        }

        return score to reward
    }

    operator fun invoke(
        difficulty: Difficulty,
        result: GameResult,
        cells: List<Player>,
        size: Int,
        winReq: Int
    ): Pair<Int, Int> =
        invoke(difficulty, result, Board(cells, size, winReq))
}
