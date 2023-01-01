package org.ascii.chess

import org.ascii.chess.util.Cursor
import org.ascii.chess.util.Text

class ChessBoard(cursor: Cursor) : Cursor(cursor.column, cursor.row, cursor.colors) {
    val chessBoard = Array(8) { arrayOfNulls<ChessBox>(8) }
    fun draw(message: String?) {
        val a = arrayOf("╤", "═══", "╔", "╗")
        val b = arrayOf("│", "   ", "║")
        val c = arrayOf("┼", "───", "╟", "╢")
        val d = arrayOf("╧", "═══", "╚", "╝")
        val chess = StringBuilder()
        for (i in 0..16) {
            for (j in 0..16) {
                if (i == 0) {
                    when (j) {
                        0 -> chess.append(getText(i, j, a[2]))
                        16 -> chess.append(
                            getText(
                                i,
                                j,
                                a[3]
                            )
                        )
                        else -> chess.append(getText(i, j, a[j % 2]))
                    }
                } else if (i == 16) {
                    when (j) {
                        0 -> chess.append(getText(i, j, d[2]))
                        16 -> chess.append(
                            getText(
                                i,
                                j,
                                d[3]
                            )
                        )
                        else -> chess.append(getText(i, j, d[j % 2]))
                    }
                } else if (i % 2 != 0) {
                    if (j == 0 || j == 16) chess.append(getText(i, j, b[2])) else {
                        if (j % 2 != 0 && chessBoard[i / 2][j / 2] != null && chessBoard[i / 2][j / 2]?.chessToken != null) {
                            chess.append(getText(i / 2, j / 2, chessBoard[i / 2][j / 2]?.chessToken?.text))
                        } else {
                            chess.append(getText(i, j, b[j % 2]))
                        }
                        if (j % 2 == 0) b[1] = if (b[1] == "   ") " ░ " else "   "
                    }
                } else {
                    when (j) {
                        0 -> chess.append(getText(i, j, c[2]))
                        16 -> chess.append(
                            getText(
                                i,
                                j,
                                c[3]
                            )
                        )
                        else -> chess.append(getText(i, j, c[j % 2]))
                    }
                }
            }
            chess.append("\n")
        }
        println(chess.append('\n').append(message))
    }

    private fun getText(i: Int, j: Int, text: String?): String? {
        val z = isSelectedBox(i, j)
        return if (isCursorPoint(i, j)) Text.getColorText(text, colors) else if (z.condition) {
            Text.getColorText(text, chessBoard[z.y][z.x]!!.color)
        } else text
    }

    private fun isSelectedBox(i: Int, j: Int): Val {
        var x = j / 2
        var y = i / 2
        x = if (x == 8) x - 1 else x
        y = if (y == 8) y - 1 else y
        return if (chessBoard[y][x]!!.isSelected) {
            Val(true, x, y)
        } else {
            val i1 = if (j != 0 && j % 2 == 0) j / 2 - 1 else j / 2
            if (chessBoard[y][i1]!!.isSelected) {
                Val(true, i1, y)
            } else {
                val i2 = if (i != 0 && i % 2 == 0) i / 2 - 1 else i / 2
                if (chessBoard[i2][x]!!.isSelected) Val(chessBoard[i2][x]!!.isSelected, x, i2) else Val(
                    chessBoard[i2][i1]!!.isSelected,
                    i1,
                    i2
                )
            }
        }
    }

    data class Val(var condition: Boolean, var x: Int, var y: Int) {}

    private fun isCursorPoint(i: Int, j: Int): Boolean {
        val size = 2
        return if ((j == column * size || j == column * size + size) && i >= row * size && i <= row * size + size
        ) true else (i == row * size || i == row * size + size) && j >= column * size && j <= column * size + size
    }
}