package com.jn.trixo.domain.usecase

import com.jn.trixo.domain.Difficulty
import com.jn.trixo.domain.GameResult
import com.jn.trixo.domain.Player
import kotlin.math.max

class CalculateScoreUseCase {
    operator fun invoke(
        difficulty: Difficulty,
        result: GameResult,
        board: List<Player>
    ): Pair<Int, Int> {
        if (result == GameResult.NONE) return 0 to 0

        val difficultyMultiplier = when (difficulty) {
            Difficulty.EASY -> 1
            Difficulty.MEDIUM -> 2
            Difficulty.HARD -> 3
            Difficulty.VERY_HARD -> 5
        }

        val emptyCells = board.count { it == Player.NONE }

        val score = when (result) {
            GameResult.X_WINS -> (100 + (emptyCells * 10)) * difficultyMultiplier
            GameResult.DRAW -> 50 * difficultyMultiplier
            else -> 10 * difficultyMultiplier
        }

        val reward = when (result) {
            GameResult.X_WINS -> max(50 * difficultyMultiplier, 50)
            GameResult.DRAW -> max(10 * difficultyMultiplier, 10)
            else -> 0
        }

        return score to reward
    }
}
