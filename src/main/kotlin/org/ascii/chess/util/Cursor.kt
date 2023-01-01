package org.ascii.chess.util

open class Cursor(var column: Int, var row: Int, val colors: Colors) {

    fun move_cursor_up() {
        row = fix_row(row - 1)
    }

    fun move_cursor_down() {
        row = fix_row(row + 1)
    }

    fun move_cursor_right() {
        column = fix_column(column + 1)
    }

    fun move_cursor_left() {
        column = fix_column(column - 1)
    }

    private fun fix_column(x: Int): Int {
        return if (x > 7) 0 else if (x < 0) 7 else x
    }

    private fun fix_row(y: Int): Int {
        return if (y > 7) 0 else if (y < 0) 7 else y
    }
}