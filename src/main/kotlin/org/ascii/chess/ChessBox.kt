package org.ascii.chess

import org.ascii.chess.util.Colors

class ChessBox(var chessToken: ChessToken?, var isSelected: Boolean) {
    var color: Colors = Colors.WHITE
    fun setSelected(selected: Boolean, colors: Colors) {
        isSelected = selected
        this.color = colors
    }

}