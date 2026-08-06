package com.kotonosora.blitzxo.domain.usecase

import com.kotonosora.blitzxo.domain.GameResult
import com.kotonosora.blitzxo.domain.Player

class GetGameResultUseCase {
    operator fun invoke(
        board: List<Player>,
        size: Int,
        winReq: Int
    ): Pair<GameResult, List<Int>?> {
        val directions = listOf(Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(1, -1))

        for (r in 0 until size) {
            for (c in 0 until size) {
                val p = board[r * size + c]
                if (p == Player.NONE) continue

                for ((dr, dc) in directions) {
                    val line = mutableListOf<Int>()
                    var valid = true
                    for (i in 0 until winReq) {
                        val nr = r + dr * i
                        val nc = c + dc * i
                        if (nr in 0 until size && nc in 0 until size && board[nr * size + nc] == p) {
                            line.add(nr * size + nc)
                        } else {
                            valid = false
                            break
                        }
                    }
                    if (valid) {
                        return Pair(
                            if (p == Player.X) GameResult.X_WINS else GameResult.O_WINS,
                            line
                        )
                    }
                }
            }
        }

        if (board.none { it == Player.NONE }) {
            return Pair(GameResult.DRAW, null)
        }

        return Pair(GameResult.NONE, null)
    }
}
