package local.trixo.engine.domain.model

/**
 * Board value object: owns board shape and win-line rules so game logic
 * never operates on a bare `List<Player>`.
 *
 * Invariants: [cells] always holds exactly `size * size` entries.
 */
data class Board(
    val cells: List<Player>,
    val size: Int,
    val winReq: Int
) {
    init {
        require(cells.size == size * size) {
            "Board needs ${size * size} cells, got ${cells.size}"
        }
        require(winReq in 1..size) {
            "winReq must be within 1..$size, got $winReq"
        }
    }

    operator fun get(index: Int): Player = cells[index]

    val cellCount: Int get() = cells.size

    val availableMoves: List<Int>
        get() = cells.mapIndexedNotNull { index, player ->
            if (player == Player.NONE) index else null
        }

    val isFull: Boolean get() = Player.NONE !in cells

    val emptyCells: Int get() = cells.count { it == Player.NONE }

    val moveCount: Int get() = cellCount - emptyCells

    val centerIndex: Int get() = (size / 2) * size + (size / 2)

    fun withMove(index: Int, player: Player): Board {
        require(index in cells.indices) { "Cell $index out of bounds" }
        require(cells[index] == Player.NONE) { "Cell $index already taken" }
        return copy(cells = cells.toMutableList().also { it[index] = player })
    }

    /**
     * Decides the current result: winning line, draw, or ongoing ([GameResult.NONE]).
     */
    fun decide(): Pair<GameResult, List<Int>?> {
        for (r in 0 until size) {
            for (c in 0 until size) {
                val p = cells[r * size + c]
                if (p == Player.NONE) continue

                for ((dr, dc) in DIRECTIONS) {
                    val line = mutableListOf<Int>()
                    var valid = true
                    for (i in 0 until winReq) {
                        val nr = r + dr * i
                        val nc = c + dc * i
                        if (nr in 0 until size && nc in 0 until size && cells[nr * size + nc] == p) {
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

        if (isFull) return Pair(GameResult.DRAW, null)
        return Pair(GameResult.NONE, null)
    }

    /** True if placing [player] at (r, c) completes a winning line. */
    fun wouldWinAt(row: Int, col: Int, player: Player): Boolean {
        for ((dr, dc) in DIRECTIONS) {
            var count = 1
            var i = 1
            while (true) {
                val nr = row + dr * i
                val nc = col + dc * i
                if (nr in 0 until size && nc in 0 until size && cells[nr * size + nc] == player) {
                    count++
                    i++
                } else break
            }
            i = 1
            while (true) {
                val nr = row - dr * i
                val nc = col - dc * i
                if (nr in 0 until size && nc in 0 until size && cells[nr * size + nc] == player) {
                    count++
                    i++
                } else break
            }
            if (count >= winReq) return true
        }
        return false
    }

    fun hasAdjacentPiece(index: Int): Boolean {
        val r = index / size
        val c = index % size
        for (dr in -1..1) {
            for (dc in -1..1) {
                if (dr == 0 && dc == 0) continue
                val nr = r + dr
                val nc = c + dc
                if (nr in 0 until size && nc in 0 until size && cells[nr * size + nc] != Player.NONE) {
                    return true
                }
            }
        }
        return false
    }

    companion object {
        private val DIRECTIONS = listOf(Pair(0, 1), Pair(1, 0), Pair(1, 1), Pair(1, -1))

        fun empty(size: Int, winReq: Int): Board =
            Board(List(size * size) { Player.NONE }, size, winReq)
    }
}
