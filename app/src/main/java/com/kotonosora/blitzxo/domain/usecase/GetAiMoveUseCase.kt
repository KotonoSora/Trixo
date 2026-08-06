package com.kotonosora.blitzxo.domain.usecase

import com.kotonosora.blitzxo.domain.Player

class GetAiMoveUseCase {
    operator fun invoke(board: List<Player>, aiPlayer: Player, size: Int, winReq: Int): Int {
        val humanPlayer = if (aiPlayer == Player.X) Player.O else Player.X
        val availableMoves =
            board.mapIndexedNotNull { index, player -> if (player == Player.NONE) index else null }
        if (availableMoves.isEmpty()) return -1

        // 1. Check for AI win
        for (move in availableMoves) {
            val r = move / size
            val c = move % size
            if (checkLineForWin(board, r, c, aiPlayer, size, winReq)) {
                return move
            }
        }

        // 2. Check for human win and block
        for (move in availableMoves) {
            val r = move / size
            val c = move % size
            if (checkLineForWin(board, r, c, humanPlayer, size, winReq)) {
                return move
            }
        }

        // 3. Take center or near center
        val centerRow = size / 2
        val centerCol = size / 2
        val centerIndex = centerRow * size + centerCol
        if (board[centerIndex] == Player.NONE) {
            return centerIndex
        }

        // 4. Try to pick a move adjacent to existing pieces (makes AI look smarter)
        val adjacentMoves = availableMoves.filter { move ->
            hasAdjacentPiece(board, move, size)
        }
        if (adjacentMoves.isNotEmpty()) {
            return adjacentMoves.random()
        }

        // 5. Random available move
        return availableMoves.random()
    }

    private fun checkLineForWin(
        board: List<Player>,
        r: Int,
        c: Int,
        player: Player,
        size: Int,
        winReq: Int
    ): Boolean {
        val directions = listOf(Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(1, -1))
        for ((dr, dc) in directions) {
            var count = 1
            // check forward
            var i = 1
            while (true) {
                val nr = r + dr * i
                val nc = c + dc * i
                if (nr in 0 until size && nc in 0 until size && board[nr * size + nc] == player) {
                    count++
                    i++
                } else break
            }
            // check backward
            i = 1
            while (true) {
                val nr = r - dr * i
                val nc = c - dc * i
                if (nr in 0 until size && nc in 0 until size && board[nr * size + nc] == player) {
                    count++
                    i++
                } else break
            }
            if (count >= winReq) return true
        }
        return false
    }

    private fun hasAdjacentPiece(board: List<Player>, move: Int, size: Int): Boolean {
        val r = move / size
        val c = move % size
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val nr = r + dr
                val nc = c + dc
                if (nr in 0 until size && nc in 0 until size && board[nr * size + nc] != Player.NONE) {
                    return true
                }
            }
        }
        return false
    }
}
