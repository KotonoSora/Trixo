package com.jn.trixo.domain

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

enum class Player { X, O, NONE }
enum class GameResult { NONE, X_WINS, O_WINS, DRAW }
enum class Difficulty(val size: Int, val winReq: Int) {
    EASY(3, 3),
    MEDIUM(9, 4),
    HARD(12, 5),
    VERY_HARD(15, 6)
}

data class GameState(
    val difficulty: Difficulty = Difficulty.EASY,
    val board: List<Player> = List(3 * 3) { Player.NONE },
    val currentPlayer: Player = Player.X,
    val result: GameResult = GameResult.NONE,
    val isAiTurn: Boolean = false,
    val winningLine: List<Int>? = null,
    val hintIndex: Int? = null,
    val history: List<List<Player>> = emptyList(),
    val isPvP: Boolean = false
) {
    val boardSize get() = difficulty.size
    val winRequirement get() = difficulty.winReq
}

class GameViewModel : ViewModel() {
    private val _gameState = MutableStateFlow(GameState())
    val gameState: StateFlow<GameState> = _gameState.asStateFlow()
    private var aiMoveJob: Job? = null
    private var gameSessionId: Long = 0L

    fun playMove(index: Int) {
        val state = _gameState.value
        if (index !in state.board.indices) return
        if (state.board[index] != Player.NONE || state.result != GameResult.NONE || state.isAiTurn) {
            return
        }

        // Save current board to history before making a move
        val currentHistory = state.history.toMutableList()
        currentHistory.add(state.board)

        val newBoard = state.board.toMutableList()
        newBoard[index] = state.currentPlayer
        
        val (result, winLine) = getFullBoardResult(newBoard, state.boardSize, state.winRequirement)
        
        if (result != GameResult.NONE) {
            _gameState.value = state.copy(
                board = newBoard, 
                result = result, 
                winningLine = winLine, 
                hintIndex = null,
                history = currentHistory
            )
        } else {
            val nextPlayer = if (state.currentPlayer == Player.X) Player.O else Player.X
            val isNextAiTurn = !state.isPvP && nextPlayer == Player.O

            _gameState.value = state.copy(
                board = newBoard, 
                currentPlayer = nextPlayer, 
                isAiTurn = isNextAiTurn, 
                hintIndex = null,
                history = currentHistory
            )
            
            if (isNextAiTurn) {
                playAIMove(gameSessionId)
            }
        }
    }

    fun undoMove() {
        val state = _gameState.value
        if (state.history.isEmpty() || state.isAiTurn || state.result != GameResult.NONE) return
        
        val lastBoard = state.history.last()
        _gameState.value = state.copy(
            board = lastBoard,
            history = state.history.dropLast(1),
            currentPlayer = if (state.isPvP) (if (state.currentPlayer == Player.X) Player.O else Player.X) else Player.X,
            result = GameResult.NONE,
            winningLine = null,
            hintIndex = null,
            isAiTurn = false
        )
    }

    fun requestHint() {
        val state = _gameState.value
        if (state.result != GameResult.NONE || state.isAiTurn) return
        
        val move = findBestMove(state.board, state.currentPlayer, state.boardSize, state.winRequirement)
        if (move != -1) {
            _gameState.value = state.copy(hintIndex = move)
        }
    }

    private fun playAIMove(sessionId: Long) {
        aiMoveJob?.cancel()
        aiMoveJob = viewModelScope.launch {
            delay(600) // Simulate "thinking" for a better UX
            val state = _gameState.value
            if (sessionId != gameSessionId) return@launch
            if (state.result != GameResult.NONE || !state.isAiTurn || state.currentPlayer != Player.O) return@launch

            val move = findBestMove(state.board, Player.O, state.boardSize, state.winRequirement)
            if (move != -1) {
                val newBoard = state.board.toMutableList()
                newBoard[move] = Player.O
                
                val (result, winLine) = getFullBoardResult(newBoard, state.boardSize, state.winRequirement)
                _gameState.value = state.copy(
                    board = newBoard,
                    currentPlayer = Player.X,
                    result = result,
                    isAiTurn = false,
                    winningLine = winLine
                )
            }
        }
    }

    private fun findBestMove(board: List<Player>, aiPlayer: Player, size: Int, winReq: Int): Int {
        val humanPlayer = if (aiPlayer == Player.X) Player.O else Player.X
        val availableMoves = board.mapIndexedNotNull { index, player -> if (player == Player.NONE) index else null }
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

    private fun checkLineForWin(board: List<Player>, r: Int, c: Int, player: Player, size: Int, winReq: Int): Boolean {
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

    private fun getFullBoardResult(board: List<Player>, size: Int, winReq: Int): Pair<GameResult, List<Int>?> {
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
                        return Pair(if (p == Player.X) GameResult.X_WINS else GameResult.O_WINS, line)
                    }
                }
            }
        }

        if (board.none { it == Player.NONE }) {
            return Pair(GameResult.DRAW, null)
        }

        return Pair(GameResult.NONE, null)
    }

    fun resetGame(difficulty: Difficulty = _gameState.value.difficulty, isPvP: Boolean = false) {
        aiMoveJob?.cancel()
        gameSessionId++
        _gameState.value = GameState(
            difficulty = difficulty,
            board = List(difficulty.size * difficulty.size) { Player.NONE },
            history = emptyList(),
            isPvP = isPvP
        )
    }

    override fun onCleared() {
        aiMoveJob?.cancel()
        super.onCleared()
    }
}
