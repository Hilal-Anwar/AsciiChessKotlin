package org.ascii.chess.util

object Text {
    fun getColorText(text: String?, colors: Colors): String {
        return colors.color + text + "\u001b[0m"
    }
}