package org.ascii.chess

import org.ascii.chess.util.Colors
import org.ascii.chess.util.Players
import org.ascii.chess.util.Text

class ChessToken(val chessPieceType: ChessPieceType, color: Colors, players: Players) {
    private var players: Players
    private var color: Colors
    var piece: Players
        get() = players
        set(players) {
            this.players = players
        }

    init {
        this.color = color
        this.players = players
    }

    fun getColor(): Colors {
        return color
    }

    fun setColor(color: Colors) {
        this.color = color
    }

    val text: String
        get() = " " + Text.getColorText(chessPieceType.text, color) + " "
}