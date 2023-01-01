package org.ascii.chess.util

import org.ascii.chess.ChessBox

enum class Players {
    BLACK, WHITE;

    fun isEqual(token: ChessBox?): Boolean {
        return if (token!!.chessToken == null) false else token.chessToken!!.piece == this
    }
}