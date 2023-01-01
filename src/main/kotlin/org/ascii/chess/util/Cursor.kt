package org.ascii.chess.util

open class Cursor(var column: Int, var row: Int, val colors: Colors) {

    fun moveCursorUp() {
        row = fixRow(row - 1)
    }

    fun moveCursorDown() {
        row = fixRow(row + 1)
    }

    fun moveCursorRight() {
        column = fixColumn(column + 1)
    }

    fun moveCursorLeft() {
        column = fixColumn(column - 1)
    }

    private fun fixColumn(x: Int): Int {
        return if (x > 7) 0 else if (x < 0) 7 else x
    }

    private fun fixRow(y: Int): Int {
        return if (y > 7) 0 else if (y < 0) 7 else y
    }
}