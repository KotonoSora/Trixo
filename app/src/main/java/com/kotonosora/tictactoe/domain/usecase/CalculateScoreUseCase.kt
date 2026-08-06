package com.kotonosora.tictactoe.domain.usecase

import com.kotonosora.tictactoe.domain.Difficulty
import com.kotonosora.tictactoe.domain.GameResult
import com.kotonosora.tictactoe.domain.Player
import kotlin.math.min

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
            GameResult.X_WINS -> (10 + (emptyCells * 10)) * difficultyMultiplier
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
}
