package local.trixo.engine.domain.service

import local.trixo.engine.domain.model.Board
import local.trixo.engine.domain.model.GameResult
import local.trixo.engine.domain.model.Player

/**
 * Domain service: evaluates board positions. Pure rules, no ports.
 * Works on the [Board] value object; the list-based [invoke] overload keeps
 * callers that still hold raw cell lists (e.g. saved state) compiling.
 */
class GameRulesService {
    operator fun invoke(board: Board): Pair<GameResult, List<Int>?> =
        board.decide()

    operator fun invoke(
        cells: List<Player>,
        size: Int,
        winReq: Int
    ): Pair<GameResult, List<Int>?> =
        Board(cells, size, winReq).decide()
}
