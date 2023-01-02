package org.ascii.chess

import org.ascii.chess.util.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.system.exitProcess

open class Game : Display(), Movements {
    /**
     * @author Hilal Anwar on 01-12-2022
     * @project AsciiChess
     */
    private val chessBoard = ChessBoard(Cursor(4, 6, Colors.MAGENTA))
    private var board: Array<Array<ChessBox?>>? = null
    private var possiblePosition = ArrayList<IntArray>()
    private var selectedBox: IntArray? = null
    private var turn: Players = Players.WHITE
    private var message = ""
    private val isKingChecked = false
    private var castlingvalidBlack = true
    private var castlingvalidWhite = true
    private var enPassant = intArrayOf()
    private var castling = ArrayList<IntArray>(2)

    init {
        val cW: Colors = Colors.CYAN_BRIGHT
        val cB: Colors = Colors.YELLOW
        board = chessBoard.chessBoard
        //all the black pieces
        board!![0][0] = ChessBox(ChessToken(ChessPieceType.ROOK, cB, Players.BLACK), false)
        board!![0][1] = ChessBox(ChessToken(ChessPieceType.KNIGHT, cB, Players.BLACK), false)
        board!![0][2] = ChessBox(ChessToken(ChessPieceType.BISHOP, cB, Players.BLACK), false)
        board!![0][3] = ChessBox(ChessToken(ChessPieceType.QUEEN, cB, Players.BLACK), false)
        board!![0][4] = ChessBox(ChessToken(ChessPieceType.KING, cB, Players.BLACK), false)
        board!![0][5] = ChessBox(ChessToken(ChessPieceType.BISHOP, cB, Players.BLACK), false)
        board!![0][6] = ChessBox(ChessToken(ChessPieceType.KNIGHT, cB, Players.BLACK), false)
        board!![0][7] = ChessBox(ChessToken(ChessPieceType.ROOK, cB, Players.BLACK), false)
        board!![1][0] = ChessBox(ChessToken(ChessPieceType.PAWN, cB, Players.BLACK), false)
        board!![1][1] = ChessBox(ChessToken(ChessPieceType.PAWN, cB, Players.BLACK), false)
        board!![1][2] = ChessBox(ChessToken(ChessPieceType.PAWN, cB, Players.BLACK), false)
        board!![1][3] = ChessBox(ChessToken(ChessPieceType.PAWN, cB, Players.BLACK), false)
        board!![1][4] = ChessBox(ChessToken(ChessPieceType.PAWN, cB, Players.BLACK), false)
        board!![1][5] = ChessBox(ChessToken(ChessPieceType.PAWN, cB, Players.BLACK), false)
        board!![1][6] = ChessBox(ChessToken(ChessPieceType.PAWN, cB, Players.BLACK), false)
        board!![1][7] = ChessBox(ChessToken(ChessPieceType.PAWN, cB, Players.BLACK), false)

        // all the white pieces
        board!![7][0] = ChessBox(ChessToken(ChessPieceType.ROOK, cW, Players.WHITE), false)
        board!![7][1] = ChessBox(ChessToken(ChessPieceType.KNIGHT, cW, Players.WHITE), false)
        board!![7][2] = ChessBox(ChessToken(ChessPieceType.BISHOP, cW, Players.WHITE), false)
        board!![7][3] = ChessBox(ChessToken(ChessPieceType.QUEEN, cW, Players.WHITE), false)
        board!![7][4] = ChessBox(ChessToken(ChessPieceType.KING, cW, Players.WHITE), false)
        board!![7][5] = ChessBox(ChessToken(ChessPieceType.BISHOP, cW, Players.WHITE), false)
        board!![7][6] = ChessBox(ChessToken(ChessPieceType.KNIGHT, cW, Players.WHITE), false)
        board!![7][7] = ChessBox(ChessToken(ChessPieceType.ROOK, cW, Players.WHITE), false)
        board!![6][0] = ChessBox(ChessToken(ChessPieceType.PAWN, cW, Players.WHITE), false)
        board!![6][1] = ChessBox(ChessToken(ChessPieceType.PAWN, cW, Players.WHITE), false)
        board!![6][2] = ChessBox(ChessToken(ChessPieceType.PAWN, cW, Players.WHITE), false)
        board!![6][3] = ChessBox(ChessToken(ChessPieceType.PAWN, cW, Players.WHITE), false)
        board!![6][4] = ChessBox(ChessToken(ChessPieceType.PAWN, cW, Players.WHITE), false)
        board!![6][5] = ChessBox(ChessToken(ChessPieceType.PAWN, cW, Players.WHITE), false)
        board!![6][6] = ChessBox(ChessToken(ChessPieceType.PAWN, cW, Players.WHITE), false)
        board!![6][7] = ChessBox(ChessToken(ChessPieceType.PAWN, cW, Players.WHITE), false)
        for (i in 2..5) {
            for (j in 0..7) {
                board!![i][j] = ChessBox(null, false)
            }
        }
    }

    @Throws(InterruptedException::class)
    fun start() {
        val keyBoardInput = KeyBoardInput(this)
        clearDisplay()
        chessBoard.draw(message)
        while (true) {
            val key = keyBoardInput.keyBoardKey
            when (key) {
                Key.UP -> chessBoard.moveCursorUp()
                Key.DOWN -> chessBoard.moveCursorDown()
                Key.RIGHT -> chessBoard.moveCursorRight()
                Key.LEFT -> chessBoard.moveCursorLeft()
                Key.ENTER -> moves()
                Key.ESC -> exitProcess(-1)
                else -> {}
            }
            if (key != Key.NONE) {
                clearDisplay()
                chessBoard.draw(message)
            }
            keyBoardInput.keyBoardKey = (Key.NONE)
            Thread.sleep(30)
        }
    }

    private fun moves() {
        val x: Int = chessBoard.column
        val y: Int = chessBoard.row
        val chessBox = board!![y][x]
        if (turn.isEqual(chessBox) || selectedBox != null) {
            if (chessBox?.chessToken != null && selectedBox == null) {
                val chessPieceType = chessBox.chessToken!!.chessPieceType
                possiblePosition = when (chessPieceType) {
                    ChessPieceType.KING -> kingMovement(x, y, chessBox.chessToken?.piece)
                    ChessPieceType.QUEEN -> queenMovement(x, y, chessBox.chessToken?.piece)
                    ChessPieceType.BISHOP -> bishopMovement(x, y, chessBox.chessToken?.piece)
                    ChessPieceType.ROOK -> rookMovement(x, y, chessBox.chessToken?.piece)
                    ChessPieceType.KNIGHT -> knightMovement(x, y, chessBox.chessToken?.piece)
                    ChessPieceType.PAWN -> pawnMovement(
                        x, y, chessBox.chessToken,
                        chessBox.chessToken?.piece
                    )
                }
                if (possiblePosition.size != 0) {
                    for (sl in possiblePosition) {
                        board!![sl[1]][sl[0]]?.setSelected(
                            true,
                            if (board!![sl[1]][sl[0]]?.chessToken != null) Colors.RED else Colors.GREEN
                        )
                    }
                    selectedBox = intArrayOf(x, y)
                    if (board!![y][x]?.chessToken != null) {
                        if (!board!![y][x]!!.isSelected) board!![y][x]!!.setSelected(true, Colors.BLUE)
                    }
                }
            } else if (selectedBox != null) {
                if (ifAnyMatching(x, y)) {
                    val _x = selectedBox!![0]
                    val _y = selectedBox!![1]
                    if (y == 0 && board!![_y][_x]?.chessToken?.piece!! == Players.WHITE &&
                        board!![_y][_x]?.chessToken?.chessPieceType == ChessPieceType.PAWN
                    ) board!![y][x] =
                        ChessBox(
                            ChessToken(
                                ChessPieceType.QUEEN,
                                Colors.CYAN_BRIGHT, Players.WHITE
                            ), false
                        ) else if (y == 7 && board!![_y][_x]?.chessToken?.piece!! == Players.BLACK &&
                        board!![_y][_x]?.chessToken?.chessPieceType == ChessPieceType.PAWN
                    ) board!![y][x] =
                        ChessBox(
                            ChessToken(
                                ChessPieceType.QUEEN,
                                Colors.MAGENTA, Players.BLACK
                            ), false
                        ) else if (isEnPassant(y, 3, _y, _x, x, y)) {
                        enPassant = intArrayOf(x, y - 1)
                        board!![y][x] = board!![_y][_x]
                    } else if (isEnPassant(y, 4, _y, _x, x, y)) {
                        enPassant = intArrayOf(x, y + 1)
                        board!![y][x] = board!![_y][_x]
                    } else if (castling.size > 0 && castling[0][0] == x && castling[0][1] == y ||
                        castling.size > 1 && castling[1][0] == x && castling[1][1] == y
                    ) {
                        val pi = board!![_y][_x]?.chessToken!!.piece
                        if (pi == Players.BLACK && castlingvalidBlack) {
                            board!![y][x] = board!![_y][_x]
                            if (x < _x) {
                                board!![y][x + 1] = board!![0][0]
                                board!![0][0] = ChessBox(null, false)
                            } else if (x > _x) {
                                board!![y][x - 1] = board!![0][7]
                                board!![0][7] = ChessBox(null, false)
                            }
                            castlingvalidBlack = false
                            castling = ArrayList()
                        } else if (pi == Players.WHITE && castlingvalidWhite) {
                            board!![y][x] = board!![_y][_x]
                            if (x < _x) {
                                board!![y][x + 1] = board!![7][0]
                                board!![7][0] = ChessBox(null, false)
                            } else if (x > _x) {
                                board!![y][x - 1] = board!![7][7]
                                board!![7][7] = ChessBox(null, false)
                            }
                            castlingvalidWhite = false
                            castling = ArrayList()
                        }
                    } else if (enPassant.isNotEmpty() && enPassant[0] == x && enPassant[1] == y) {
                        board!![enPassant[1]][enPassant[0]] = board!![_y][_x]
                        if (board!![_y][_x]?.chessToken!!.piece == Players.WHITE) board!![_y]!![_x - 1] =
                            ChessBox(
                                null,
                                false
                            ) else if (board!![_y][_x]?.chessToken!!.piece.equals(Players.BLACK)) {
                            board!![_y][_x + 1] = ChessBox(null, false)
                        }
                        enPassant = intArrayOf()
                    } else board!![y][x] = board!![_y][_x]
                    board!![_y][_x] = ChessBox(null, false)
                    turn = if (turn.equals(Players.WHITE)) Players.BLACK else Players.WHITE
                }
                board!![selectedBox!![1]][selectedBox!![0]]!!.setSelected(false, Colors.WHITE)
                for (sl in possiblePosition) {
                    board!![sl[1]][sl[0]]!!.setSelected(false, Colors.WHITE)
                }
                if (enPassant.isNotEmpty()) {
                    board!![enPassant[1]][enPassant[0]]!!.setSelected(false, Colors.WHITE)
                }
                for (ints in castling) {
                    board!![ints[1]][ints[0]]!!.setSelected(false, Colors.WHITE)
                }
                selectedBox = null
            }
            message = ""
        } else if (chessBox?.chessToken != null) {
            message = "Invalid move its," + turn.name + " turn " +
                    chessBox.chessToken?.piece!!.name + " cannot be moved"
        }
    }

    private fun isEnPassant(m: Int, n: Int, _y: Int, _x: Int, x: Int, y: Int): Boolean {
        val co: Players = board!![_y][_x]?.chessToken!!.piece
        if (m == n && board!![_y][_x]?.chessToken!!.chessPieceType == ChessPieceType.PAWN && x != 0 &&
            board!![_y][_x]?.chessToken!!.piece == Players.BLACK && board!![y][x - 1]?.chessToken != null &&
            board!![y][x - 1]?.chessToken!!.piece != co
        ) {
            return true
        } else if (m == n && board!![_y][_x]?.chessToken!!.chessPieceType == ChessPieceType.PAWN && x != 0 &&
            board!![_y][_x]?.chessToken!!.piece == Players.BLACK && board!![y][x + 1]?.chessToken != null
            && board!![y][x + 1]?.chessToken!!.piece != co
        ) {
            return true
        } else if (m == n && board!![_y][_x]?.chessToken!!.chessPieceType == ChessPieceType.PAWN && x != 7 &&
            board!![_y][_x]?.chessToken!!.piece == Players.WHITE && board!![y][x + 1]?.chessToken != null &&
            board!![y][x + 1]?.chessToken!!.piece != co
        ) return true else if (m == n && board!![_y][_x]?.chessToken!!.chessPieceType == ChessPieceType.PAWN && x != 7 &&
            board!![_y][_x]?.chessToken!!.piece == Players.WHITE && board!![y][x - 1]?.chessToken != null &&
            board!![y][x - 1]?.chessToken!!.piece != co
        ) return true
        return false
    }

    private fun ifAnyMatching(x: Int, y: Int): Boolean {
        val z = intArrayOf(x, y)
        for (t in possiblePosition) {
            if (Arrays.equals(t, z)) {
                return true
            }
        }
        for (ints in castling) {
            if (Arrays.equals(z, ints)) return true
        }
        return enPassant.size > 0 && Arrays.equals(z, enPassant)
    }

    override fun rookMovement(x: Int, y: Int, color: Players?): ArrayList<IntArray> {
        var degree_f = 4
        val freedom = arrayOf<IntArray?>(intArrayOf(0, 1), intArrayOf(0, -1), intArrayOf(1, 0), intArrayOf(-1, 0))
        val movement = arrayOf(intArrayOf(x, y), intArrayOf(x, y), intArrayOf(x, y), intArrayOf(x, y))
        val list = ArrayList<IntArray>()
        while (degree_f > 0) {
            for (i in 0..3) {
                if (freedom[i] != null) {
                    val p = freedom[i]!![0] + movement[i][0]
                    val q = freedom[i]!![1] + movement[i][1]
                    if (isValidPoint(p, q, color)) {
                        movement[i][0] = p
                        movement[i][1] = q
                        list.add(intArrayOf(p, q))
                    } else if (isValidPointFilledPosition(p, q, color)) {
                        movement[i][0] = p
                        movement[i][1] = q
                        list.add(intArrayOf(p, q))
                        degree_f--
                        freedom[i] = null
                    } else {
                        degree_f--
                        freedom[i] = null
                        //movement[i] = null;
                    }
                }
            }
        }
        return list
    }

    private fun isValidPoint(x: Int, y: Int, color: Players?): Boolean {
        return x in 0..7 && y >= 0 && y < 8 && board!![y][x]?.chessToken == null
    }

    private fun isValidPointFilledPosition(x: Int, y: Int, color: Players?): Boolean {
        return x in 0..7 && y >= 0 && y < 8 && board!![y][x]?.chessToken != null &&
                !color!!.equals(board!![y][x]?.chessToken!!.piece)
    }

    override fun bishopMovement(x: Int, y: Int, color: Players?): ArrayList<IntArray> {
        var degree_f = 4
        val freedom = arrayOf<IntArray?>(intArrayOf(1, 1), intArrayOf(-1, 1), intArrayOf(1, -1), intArrayOf(-1, -1))
        val movement = arrayOf(intArrayOf(x, y), intArrayOf(x, y), intArrayOf(x, y), intArrayOf(x, y))
        val list = ArrayList<IntArray>()
        while (degree_f > 0) {
            for (i in 0..3) {
                if (freedom[i] != null) {
                    val p = freedom[i]!![0] + movement[i][0]
                    val q = freedom[i]!![1] + movement[i][1]
                    if (isValidPoint(p, q, color)) {
                        movement[i][0] = p
                        movement[i][1] = q
                        list.add(intArrayOf(p, q))
                    } else if (isValidPointFilledPosition(p, q, color)) {
                        movement[i][0] = p
                        movement[i][1] = q
                        list.add(intArrayOf(p, q))
                        degree_f--
                        freedom[i] = null
                    } else {
                        degree_f--
                        freedom[i] = null
                        //movement[i] = null;
                    }
                }
            }
        }
        return list
    }

    override fun knightMovement(x: Int, y: Int, color: Players?): ArrayList<IntArray> {
        val freedom = arrayOf(
            intArrayOf(1, 2),
            intArrayOf(-1, 2),
            intArrayOf(1, -2),
            intArrayOf(-1, -2),
            intArrayOf(2, 1),
            intArrayOf(2, -1),
            intArrayOf(-2, 1),
            intArrayOf(-2, 1)
        )
        val movement = arrayOf(
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y)
        )
        val list = ArrayList<IntArray>()
        for (i in 0..7) {
            val p = freedom[i][0] + movement[i][0]
            val q = freedom[i][1] + movement[i][1]
            if (isValidPoint(p, q, color) || isValidPointFilledPosition(p, q, color)) {
                movement[i][0] = p
                movement[i][1] = q
                list.add(intArrayOf(p, q))
            }
        }
        return list
    }

    private fun isValidCastling(x: Int, y: Int, players: Players, direction: String): Boolean {
        if (players == Players.BLACK) {
            if (direction == "left") return board!![y][x - 1]?.chessToken == null && board!![y][x - 2]?.chessToken == null && board!![y][x - 3]?.chessToken == null &&
                    board!![0][0]?.chessToken?.chessPieceType == ChessPieceType.ROOK &&
                    !isKingChecked && board!![0][0]?.chessToken?.piece!! == Players.BLACK
            if (direction == "right") {
                return board!![y][x + 1]?.chessToken == null && board!![y][x + 2]?.chessToken == null
                        && board!![0][7]?.chessToken?.chessPieceType == ChessPieceType.ROOK &&
                        !isKingChecked && board!![0][7]?.chessToken?.piece!! == Players.BLACK
            }
        } else if (players == Players.WHITE)
            if (direction == "left") return board!![y][x - 1]?.chessToken == null
                    && board!![y][x - 2]?.chessToken == null && board!![y][x - 3]?.chessToken == null
                    && board!![7][0]?.chessToken?.chessPieceType == ChessPieceType.ROOK &&
                    !isKingChecked && board!![7][0]?.chessToken?.piece?.equals(Players.WHITE) ?: return if (direction == "right") {
                board!![y][x + 1]?.chessToken == null && board!![y][x + 2]?.chessToken == null && board!![7][7]
                    ?.chessToken?.chessPieceType == ChessPieceType.ROOK &&
                        !isKingChecked && board!![7][7]?.chessToken!!.piece.equals(Players.WHITE)
            } else false
        return false;
    }

    override fun queenMovement(x: Int, y: Int, color: Players?): ArrayList<IntArray> {
        var degree_f = 8
        val freedom = arrayOf<IntArray?>(
            intArrayOf(0, 1),
            intArrayOf(0, -1),
            intArrayOf(1, 0),
            intArrayOf(-1, 0),
            intArrayOf(1, 1),
            intArrayOf(-1, 1),
            intArrayOf(1, -1),
            intArrayOf(-1, -1)
        )
        val movement = arrayOf(
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y)
        )
        val list = ArrayList<IntArray>()
        while (degree_f > 0) {
            for (i in 0..7) {
                if (freedom[i] != null) {
                    val p = freedom[i]!![0] + movement[i][0]
                    val q = freedom[i]!![1] + movement[i][1]
                    if (isValidPoint(p, q, color)) {
                        movement[i][0] = p
                        movement[i][1] = q
                        list.add(intArrayOf(p, q))
                    } else if (isValidPointFilledPosition(p, q, color)) {
                        movement[i][0] = p
                        movement[i][1] = q
                        list.add(intArrayOf(p, q))
                        degree_f--
                        freedom[i] = null
                    } else {
                        degree_f--
                        freedom[i] = null
                        //movement[i] = null;
                    }
                }
            }
        }
        return list
    }

    override fun kingMovement(x: Int, y: Int, color: Players?): ArrayList<IntArray> {
        val freedom = arrayOf(
            intArrayOf(0, 1),
            intArrayOf(0, -1),
            intArrayOf(1, 0),
            intArrayOf(-1, 0),
            intArrayOf(1, 1),
            intArrayOf(-1, 1),
            intArrayOf(1, -1),
            intArrayOf(-1, -1)
        )
        val movement = arrayOf(
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y),
            intArrayOf(x, y)
        )
        val list = ArrayList<IntArray>()
        for (i in 0..7) {
            val p = freedom[i][0] + movement[i][0]
            val q = freedom[i][1] + movement[i][1]
            if (isValidPoint(p, q, color) || isValidPointFilledPosition(p, q, color)) {
                movement[i][0] = p
                movement[i][1] = q
                list.add(movement[i])
            }
        }
        if (castlingvalidBlack || castlingvalidWhite) {
            if (y == 0 && castlingvalidBlack && color!!.equals(Players.BLACK) && isValidCastling(
                    x,
                    y,
                    color,
                    "left"
                )
            ) {
                castling.add(intArrayOf(x - 2, y))
                board!![y][x - 2]!!.setSelected(true, Colors.ORANGE)
            }
            if (y == 0 && castlingvalidBlack && color!! == Players.BLACK && isValidCastling(
                    x,
                    y,
                    color,
                    "right"
                )
            ) {
                castling.add(intArrayOf(x + 2, y))
                board!![y][x + 2]!!.setSelected(true, Colors.ORANGE)
            }
            if (y == 7 && castlingvalidWhite && color!! == Players.WHITE && isValidCastling(
                    x,
                    y,
                    color,
                    "left"
                )
            ) {
                castling.add(intArrayOf(x - 2, y))
                board!![y][x - 2]!!.setSelected(true, Colors.ORANGE)
            }
            if (y == 7 && castlingvalidWhite && color!! == Players.WHITE && isValidCastling(
                    x,
                    y,
                    color,
                    "right"
                )
            ) {
                castling.add(intArrayOf(x + 2, y))
                board!![y][x + 2]!!.setSelected(true, Colors.ORANGE)
            }
        }
        return list
    }

    override fun pawnMovement(x: Int, y: Int, chessToken: ChessToken?, color: Players?): ArrayList<IntArray> {
        val freedom: Array<IntArray>
        val list = ArrayList<IntArray>()
        val w = arrayOf(intArrayOf(0, -1), intArrayOf(1, -1), intArrayOf(-1, -1))
        val b = arrayOf(intArrayOf(0, 1), intArrayOf(1, 1), intArrayOf(-1, 1))
        freedom = if (color!!.equals(Players.WHITE)) w else b
        val movement = arrayOf(intArrayOf(x, y), intArrayOf(x, y), intArrayOf(x, y))
        for (i in 0..2) {
            var p = freedom[i][0] + movement[i][0]
            var q = freedom[i][1] + movement[i][1]
            if (isValidPoint(p, q, color) || isValidPointFilledPosition(p, q, color)) {
                movement[i][0] = p
                movement[i][1] = q
                when (i) {
                    0 -> {
                        if (isValidPoint(p, q, color)) {
                            list.add(movement[i])
                            if (y == 1 || y == 6) {
                                p = freedom[i][0] + movement[i][0]
                                q = freedom[i][1] + movement[i][1]
                                if (isValidPoint(p, q, color)) list.add(intArrayOf(p, q))
                            }
                        }
                    }

                    1, 2 -> {
                        if (isValidPointFilledPosition(p, q, color)) list.add(intArrayOf(p, q))
                    }
                }
            }
            if (enPassant.isNotEmpty() && (y == 3 || y == 4)) {
                board!![enPassant[1]][enPassant[0]]!!.setSelected(true, Colors.ORANGE)
            }
        }
        return list
    }
}