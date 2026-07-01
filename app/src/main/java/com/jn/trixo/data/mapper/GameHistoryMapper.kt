package com.jn.trixo.data.mapper

import com.jn.trixo.data.history.GameHistoryEntry
import com.jn.trixo.domain.model.GameHistory

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
