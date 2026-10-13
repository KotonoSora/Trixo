package local.trixo.engine.domain.service

import local.trixo.engine.domain.model.Board
import local.trixo.engine.domain.model.Player

/**
 * Domain service: picks the AI move. Pure rules, no ports.
 * Strategy: win when possible, block the opponent's win, take center,
 * prefer cells adjacent to existing pieces, otherwise random.
 */
class AiMoveService {
    operator fun invoke(board: Board, aiPlayer: Player): Int {
        val humanPlayer = if (aiPlayer == Player.X) Player.O else Player.X
        val availableMoves = board.availableMoves
        if (availableMoves.isEmpty()) return -1

        // 1. Check for AI win
        for (move in availableMoves) {
            if (board.wouldWinAt(move / board.size, move % board.size, aiPlayer)) {
                return move
            }
        }

        // 2. Check for human win and block
        for (move in availableMoves) {
            if (board.wouldWinAt(move / board.size, move % board.size, humanPlayer)) {
                return move
            }
        }

        // 3. Take center or near center
        if (board[board.centerIndex] == Player.NONE) {
            return board.centerIndex
        }

        // 4. Try to pick a move adjacent to existing pieces (makes AI look smarter)
        val adjacentMoves = availableMoves.filter { board.hasAdjacentPiece(it) }
        if (adjacentMoves.isNotEmpty()) {
            return adjacentMoves.random()
        }

        // 5. Random available move
        return availableMoves.random()
    }

    operator fun invoke(
        cells: List<Player>,
        aiPlayer: Player,
        size: Int,
        winReq: Int
    ): Int = invoke(Board(cells, size, winReq), aiPlayer)
}
