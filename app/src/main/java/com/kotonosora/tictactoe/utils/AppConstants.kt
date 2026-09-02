package com.kotonosora.tictactoe.utils

import com.kotonosora.tictactoe.ui.theme.NeonCyan
import com.kotonosora.tictactoe.ui.theme.NeonGreen
import com.kotonosora.tictactoe.ui.theme.NeonMagenta
import com.kotonosora.tictactoe.ui.viewmodels.DailyChallenge

object AppConstants {
    object Defaults {
        const val INITIAL_COINS = 300
        const val SOUND_ENABLED = true
        const val INITIAL_HINTS = 0
        const val INITIAL_UNDOS = 0
    }

    object Costs {
        const val HINT_COST = 30
        const val UNDO_COST = 15
    }

    object ScreenTitles {
        const val GAME = "GAME"
        const val COIN_SHOP = "COIN SHOP"
        const val PROGRESS = "PROGRESS"
        const val OPTIONS = "OPTIONS"
        const val CHALLENGES = "CHALLENGES"
        const val HISTORY = "MY HISTORY"
        const val HELP = "HELP"

        const val VICTORY = "VICTORY"
        const val GAME_OVER = "GAME OVER"
        const val DRAW = "DRAW GAME"
        const val RESULT = "RESULT"
    }

    object GameModes {
        const val VS_AI_TITLE = "VS AI (WITH POWERS)"
        const val LOCAL_PVP_TITLE = "LOCAL 2 PLAYERS (NO POWERS)"

        const val EASY_TITLE = "EASY (3x3)"
        const val EASY_DESC = "WIN: 3 IN A ROW"

        const val MEDIUM_TITLE = "MEDIUM (9x9)"
        const val MEDIUM_DESC = "WIN: 4 IN A ROW"

        const val HARD_TITLE = "HARD (12x12)"
        const val HARD_DESC = "WIN: 5 IN A ROW"

        const val INSANE_TITLE = "INSANE (15x15)"
        const val INSANE_DESC = "WIN: 6 IN A ROW"

        const val LOCAL_BATTLE_TITLE = "LOCAL BATTLE (15x15)"
    }

    object Challenges {
        val INITIAL_CHALLENGES = listOf(
            DailyChallenge("1", "GAMER", "Play 5 games", 0, 5, 50, NeonCyan),
            DailyChallenge("2", "WINNER", "Win 2 games", 0, 2, 100, NeonMagenta),
            DailyChallenge("3", "STRATEGIST", "Use 3 hints", 0, 3, 30, NeonGreen)
        )
    }
}
