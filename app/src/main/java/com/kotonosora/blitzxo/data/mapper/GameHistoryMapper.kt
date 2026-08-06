package com.kotonosora.blitzxo.data.mapper

import com.kotonosora.blitzxo.data.history.GameHistoryEntry
import com.kotonosora.blitzxo.domain.model.GameHistory

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
