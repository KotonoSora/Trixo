package com.kotonosora.tictactoe.data.mapper

import com.kotonosora.tictactoe.data.history.GameHistoryEntry
import com.kotonosora.tictactoe.domain.model.GameHistory

fun GameHistoryEntry.toDomain(): GameHistory {
    return GameHistory(
        id = id,
        dateTime = dateTime,
        rewardCoins = rewardCoins,
        score = score
    )
}

fun GameHistory.toEntity(): GameHistoryEntry {
    return GameHistoryEntry(
        id = id,
        dateTime = dateTime,
        rewardCoins = rewardCoins,
        score = score
    )
}
