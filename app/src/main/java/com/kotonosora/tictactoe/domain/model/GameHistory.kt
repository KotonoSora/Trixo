package com.kotonosora.tictactoe.domain.model

data class GameHistory(
    val id: Long = 0,
    val dateTime: String,
    val rewardCoins: Int,
    val score: Int
)
