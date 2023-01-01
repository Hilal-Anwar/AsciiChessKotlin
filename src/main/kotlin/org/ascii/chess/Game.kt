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
    private var possible_position = ArrayList<IntArray>()
    private var selected_box: IntArray? = null
    private var turn: Players = Players.WHITE
    private var message = ""
    private val isKingChecked = false
    private var isCastlingValid_Black = true
    private var isCastlingValid_White = true
    private var enPassant = intArrayOf()
    private var castling = ArrayList<IntArray>(2)

    init {
        init()
    }

    private fun init() {
        val c_w: Colors = Colors.CYAN_BRIGHT
        val c_b: Colors = Colors.YELLOW
        board = chessBoard.chessBoard
        //all the black pieces
        board!![0]!![0] = ChessBox(ChessToken(ChessPieceType.ROOK, c_b, Players.BLACK), false)
        board!![0]!![1] = ChessBox(ChessToken(ChessPieceType.KNIGHT, c_b, Players.BLACK), false)
        board!![0]!![2] = ChessBox(ChessToken(ChessPieceType.BISHOP, c_b, Players.BLACK), false)
        board!![0]!![3] = ChessBox(ChessToken(ChessPieceType.QUEEN, c_b, Players.BLACK), false)
        board!![0]!![4] = ChessBox(ChessToken(ChessPieceType.KING, c_b, Players.BLACK), false)
        board!![0]!![5] = ChessBox(ChessToken(ChessPieceType.BISHOP, c_b, Players.BLACK), false)
        board!![0]!![6] = ChessBox(ChessToken(ChessPieceType.KNIGHT, c_b, Players.BLACK), false)
        board!![0]!![7] = ChessBox(ChessToken(ChessPieceType.ROOK, c_b, Players.BLACK), false)
        board!![1]!![0] = ChessBox(ChessToken(ChessPieceType.PAWN, c_b, Players.BLACK), false)
        board!![1]!![1] = ChessBox(ChessToken(ChessPieceType.PAWN, c_b, Players.BLACK), false)
        board!![1]!![2] = ChessBox(ChessToken(ChessPieceType.PAWN, c_b, Players.BLACK), false)
        board!![1]!![3] = ChessBox(ChessToken(ChessPieceType.PAWN, c_b, Players.BLACK), false)
        board!![1]!![4] = ChessBox(ChessToken(ChessPieceType.PAWN, c_b, Players.BLACK), false)
        board!![1]!![5] = ChessBox(ChessToken(ChessPieceType.PAWN, c_b, Players.BLACK), false)
        board!![1]!![6] = ChessBox(ChessToken(ChessPieceType.PAWN, c_b, Players.BLACK), false)
        board!![1]!![7] = ChessBox(ChessToken(ChessPieceType.PAWN, c_b, Players.BLACK), false)

        // all the white pieces
        board!![7]!![0] = ChessBox(ChessToken(ChessPieceType.ROOK, c_w, Players.WHITE), false)
        board!![7]!![1] = ChessBox(ChessToken(ChessPieceType.KNIGHT, c_w, Players.WHITE), false)
        board!![7]!![2] = ChessBox(ChessToken(ChessPieceType.BISHOP, c_w, Players.WHITE), false)
        board!![7]!![3] = ChessBox(ChessToken(ChessPieceType.QUEEN, c_w, Players.WHITE), false)
        board!![7]!![4] = ChessBox(ChessToken(ChessPieceType.KING, c_w, Players.WHITE), false)
        board!![7]!![5] = ChessBox(ChessToken(ChessPieceType.BISHOP, c_w, Players.WHITE), false)
        board!![7]!![6] = ChessBox(ChessToken(ChessPieceType.KNIGHT, c_w, Players.WHITE), false)
        board!![7]!![7] = ChessBox(ChessToken(ChessPieceType.ROOK, c_w, Players.WHITE), false)
        board!![6]!![0] = ChessBox(ChessToken(ChessPieceType.PAWN, c_w, Players.WHITE), false)
        board!![6]!![1] = ChessBox(ChessToken(ChessPieceType.PAWN, c_w, Players.WHITE), false)
        board!![6]!![2] = ChessBox(ChessToken(ChessPieceType.PAWN, c_w, Players.WHITE), false)
        board!![6]!![3] = ChessBox(ChessToken(ChessPieceType.PAWN, c_w, Players.WHITE), false)
        board!![6]!![4] = ChessBox(ChessToken(ChessPieceType.PAWN, c_w, Players.WHITE), false)
        board!![6]!![5] = ChessBox(ChessToken(ChessPieceType.PAWN, c_w, Players.WHITE), false)
        board!![6]!![6] = ChessBox(ChessToken(ChessPieceType.PAWN, c_w, Players.WHITE), false)
        board!![6]!![7] = ChessBox(ChessToken(ChessPieceType.PAWN, c_w, Players.WHITE), false)
        for (i in 2..5) {
            for (j in 0..7) {
                board!![i]!![j] = ChessBox(null, false)
            }
        }
    }

    @Throws(InterruptedException::class)
    fun start() {
        val keyBoardInput = KeyBoardInput(this)
        clear_display()
        chessBoard.draw(message)
        while (true) {
            val key = keyBoardInput.keyBoardKey
            when (key) {
                Key.UP -> chessBoard.move_cursor_up()
                Key.DOWN -> chessBoard.move_cursor_down()
                Key.RIGHT -> chessBoard.move_cursor_right()
                Key.LEFT -> chessBoard.move_cursor_left()
                Key.ENTER -> moves()
                Key.ESC -> exitProcess(-1)
                else -> {}
            }
            if (!key.equals(Key.NONE)) {
                clear_display()
                chessBoard.draw(message)
            }
            keyBoardInput.keyBoardKey = (Key.NONE)
            Thread.sleep(30)
        }
    }

    private fun moves() {
        val x: Int = chessBoard.column
        val y: Int = chessBoard.row
        val chess_box = board!![y]!![x]
        if (turn.isEqual(chess_box) || selected_box != null) {
            if (chess_box?.chessToken != null && selected_box == null) {
                val chess_piece_type = chess_box.chessToken!!.chessPieceType
                possible_position = when (chess_piece_type) {
                    ChessPieceType.KING -> king_movement(x, y, chess_box?.chessToken?.piece)
                    ChessPieceType.QUEEN -> queen_movement(x, y, chess_box?.chessToken?.piece)
                    ChessPieceType.BISHOP -> bishop_movement(x, y, chess_box?.chessToken?.piece)
                    ChessPieceType.ROOK -> rook_movement(x, y, chess_box?.chessToken?.piece)
                    ChessPieceType.KNIGHT -> knight_movement(x, y, chess_box?.chessToken?.piece)
                    ChessPieceType.PAWN -> pawn_movement(
                        x, y, chess_box?.chessToken,
                        chess_box?.chessToken?.piece
                    )
                }
                if (possible_position.size != 0) {
                    for (sl in possible_position) {
                        board!![sl[1]]!![sl[0]]?.setSelected(
                            true,
                            if (board!![sl[1]]!![sl[0]]?.chessToken != null) Colors.RED else Colors.GREEN
                        )
                    }
                    selected_box = intArrayOf(x, y)
                    if (board!![y]!![x]?.chessToken != null) {
                        if (!board!![y]!![x]!!.isSelected) board!![y]!![x]!!.setSelected(true, Colors.BLUE)
                    }
                }
            } else if (selected_box != null) {
                if (ifAnyMatching(x, y)) {
                    val _x = selected_box!![0]
                    val _y = selected_box!![1]
                    if (y == 0 && board!![_y]!![_x]?.chessToken?.piece!!.equals(Players.WHITE) &&
                        board!![_y]!![_x]?.chessToken?.chessPieceType == ChessPieceType.PAWN
                    ) board!![y]!![x] =
                        ChessBox(
                            ChessToken(
                                ChessPieceType.QUEEN,
                                Colors.CYAN_BRIGHT, Players.WHITE
                            ), false
                        ) else if (y == 7 && board!![_y]!![_x]?.chessToken?.piece!!.equals(Players.BLACK) &&
                        board!![_y]!![_x]?.chessToken?.chessPieceType == ChessPieceType.PAWN
                    ) board!![y]!![x] =
                        ChessBox(
                            ChessToken(
                                ChessPieceType.QUEEN,
                                Colors.MAGENTA, Players.BLACK
                            ), false
                        ) else if (isEnPassant(y, 3, _y, _x, x, y)) {
                        enPassant = intArrayOf(x, y - 1)
                        board!![y]!![x] = board!![_y]!![_x]
                    } else if (isEnPassant(y, 4, _y, _x, x, y)) {
                        enPassant = intArrayOf(x, y + 1)
                        board!![y]!![x] = board!![_y]!![_x]
                    } else if (castling.size > 0 && castling[0][0] == x && castling[0][1] == y ||
                        castling.size > 1 && castling[1][0] == x && castling[1][1] == y
                    ) {
                        val pi = board!![_y]!![_x]?.chessToken!!.piece
                        if (pi.equals(Players.BLACK) && isCastlingValid_Black) {
                            board!![y]!![x] = board!![_y]!![_x]
                            if (x < _x) {
                                board!![y]!![x + 1] = board!![0]!![0]
                                board!![0]!![0] = ChessBox(null, false)
                            } else if (x > _x) {
                                board!![y]!![x - 1] = board!![0]!![7]
                                board!![0]!![7] = ChessBox(null, false)
                            }
                            isCastlingValid_Black = false
                            castling = ArrayList()
                        } else if (pi.equals(Players.WHITE) && isCastlingValid_White) {
                            board!![y]!![x] = board!![_y]!![_x]
                            if (x < _x) {
                                board!![y]!![x + 1] = board!![7]!![0]
                                board!![7]!![0] = ChessBox(null, false)
                            } else if (x > _x) {
                                board!![y]!![x - 1] = board!![7]!![7]
                                board!![7]!![7] = ChessBox(null, false)
                            }
                            isCastlingValid_White = false
                            castling = ArrayList()
                        }
                    } else if (enPassant.size > 0 && enPassant[0] == x && enPassant[1] == y) {
                        board!![enPassant[1]]!![enPassant[0]] = board!![_y]!![_x]
                        if (board!![_y]!![_x]?.chessToken!!.piece.equals(Players.WHITE)) board!![_y]!![_x - 1] =
                            ChessBox(
                                null,
                                false
                            ) else if (board!![_y]!![_x]?.chessToken!!.piece.equals(Players.BLACK)) {
                            board!![_y]!![_x + 1] = ChessBox(null, false)
                        }
                        enPassant = intArrayOf()
                    } else board!![y]!![x] = board!![_y]!![_x]
                    board!![_y]!![_x] = ChessBox(null, false)
                    turn = if (turn.equals(Players.WHITE)) Players.BLACK else Players.WHITE
                }
                board!![selected_box!![1]]!![selected_box!![0]]!!.setSelected(false, Colors.WHITE)
                for (sl in possible_position) {
                    board!![sl[1]]!![sl[0]]!!.setSelected(false, Colors.WHITE)
                }
                if (enPassant.size > 0) {
                    board!![enPassant[1]]!![enPassant[0]]!!.setSelected(false, Colors.WHITE)
                }
                for (ints in castling) {
                    board!![ints[1]]!![ints[0]]!!.setSelected(false, Colors.WHITE)
                }
                selected_box = null
            }
            message = ""
        } else if (chess_box?.chessToken != null) {
            message = "Invalid move its," + turn.name + " turn " +
                    chess_box?.chessToken?.piece!!.name + " cannot be moved"
        }
    }

    private fun isEnPassant(m: Int, n: Int, _y: Int, _x: Int, x: Int, y: Int): Boolean {
        val co: Players? = board!![_y]!![_x]?.chessToken!!.piece
        if (m == n && board!![_y]!![_x]?.chessToken!!.chessPieceType == ChessPieceType.PAWN && x != 0 &&
            board!![_y]!![_x]?.chessToken!!.piece.equals(Players.BLACK) && board!![y]!![x - 1]?.chessToken != null &&
            !board!![y]!![x - 1]?.chessToken!!.piece.equals(
                co
            )
        ) {
            return true
        } else if (m == n && board!![_y]!![_x]?.chessToken!!.chessPieceType == ChessPieceType.PAWN && x != 0 &&
            board!![_y]!![_x]?.chessToken!!.piece.equals(Players.BLACK) && board!![y]!![x + 1]?.chessToken != null
            && !board!![y]!![x + 1]?.chessToken!!.piece.equals(
                co
            )
        ) {
            return true
        } else if (m == n && board!![_y]!![_x]?.chessToken!!.chessPieceType == ChessPieceType.PAWN && x != 7 &&
            board!![_y]!![_x]?.chessToken!!.piece.equals(Players.WHITE) && board!![y]!![x + 1]?.chessToken != null &&
            !board!![y]!![x + 1]?.chessToken!!.piece.equals(
                co
            )
        ) return true else if (m == n && board!![_y]!![_x]?.chessToken!!.chessPieceType == ChessPieceType.PAWN && x != 7 &&
            board!![_y]!![_x]?.chessToken!!.piece.equals(Players.WHITE) && board!![y]!![x - 1]?.chessToken != null &&
            !board!![y]!![x - 1]?.chessToken!!.piece.equals(
                co
            )
        ) return true
        return false
    }

    private fun ifAnyMatching(x: Int, y: Int): Boolean {
        val z = intArrayOf(x, y)
        for (t in possible_position) {
            if (Arrays.equals(t, z)) {
                return true
            }
        }
        for (ints in castling) {
            if (Arrays.equals(z, ints)) return true
        }
        return enPassant.size > 0 && Arrays.equals(z, enPassant)
    }

    override fun rook_movement(x: Int, y: Int, color: Players?): ArrayList<IntArray> {
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
        return x >= 0 && x < 8 && y >= 0 && y < 8 && board!![y]!![x]?.chessToken == null
    }

    private fun isValidPointFilledPosition(x: Int, y: Int, color: Players?): Boolean {
        return x >= 0 && x < 8 && y >= 0 && y < 8 && board!![y]!![x]?.chessToken != null &&
                !color!!.equals(board!![y]!![x]?.chessToken!!.piece)
    }

    override fun bishop_movement(x: Int, y: Int, color: Players?): ArrayList<IntArray> {
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

    override fun knight_movement(x: Int, y: Int, color: Players?): ArrayList<IntArray> {
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
        if (players.equals(Players.BLACK)) {
            if (direction == "left") return board!![y]!![x - 1]?.chessToken == null && board!![y]!![x - 2]?.chessToken == null && board!![y]!![x - 3]?.chessToken == null &&
                    board!![0]!![0]?.chessToken?.chessPieceType == ChessPieceType.ROOK &&
                    !isKingChecked && board!![0]!![0]?.chessToken?.piece!!.equals(Players.BLACK)
            if (direction == "right") {
                return board!![y]!![x + 1]?.chessToken == null && board!![y]!![x + 2]?.chessToken == null
                        && board!![0]!![7]?.chessToken?.chessPieceType == ChessPieceType.ROOK &&
                        !isKingChecked && board!![0]!![7]?.chessToken?.piece!!.equals(Players.BLACK)
            }
        } else if (players.equals(Players.WHITE))
            if (direction == "left") return board!![y]!![x - 1]?.chessToken == null
                    && board!![y]!![x - 2]?.chessToken == null && board!![y]!![x - 3]?.chessToken == null
                    && board!![7]!![0]?.chessToken?.chessPieceType == ChessPieceType.ROOK &&
                    !isKingChecked && board!![7]!![0]?.chessToken?.piece?.equals(Players.WHITE) ?: return if (direction == "right") {
                board!![y]!![x + 1]?.chessToken == null && board!![y]!![x + 2]?.chessToken == null && board!![7]!![7]
                    ?.chessToken?.chessPieceType == ChessPieceType.ROOK &&
                        !isKingChecked && board!![7]!![7]?.chessToken!!.piece.equals(Players.WHITE)
            } else false
        return false;
    }

    override fun queen_movement(x: Int, y: Int, color: Players?): ArrayList<IntArray> {
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

    override fun king_movement(x: Int, y: Int, color: Players?): ArrayList<IntArray> {
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
        if (isCastlingValid_Black || isCastlingValid_White) {
            if (y == 0 && isCastlingValid_Black && color!!.equals(Players.BLACK) && isValidCastling(
                    x,
                    y,
                    color,
                    "left"
                )
            ) {
                castling.add(intArrayOf(x - 2, y))
                board!![y]!![x - 2]!!.setSelected(true, Colors.ORANGE)
            }
            if (y == 0 && isCastlingValid_Black && color!!.equals(Players.BLACK) && isValidCastling(
                    x,
                    y,
                    color,
                    "right"
                )
            ) {
                castling.add(intArrayOf(x + 2, y))
                board!![y]!![x + 2]!!.setSelected(true, Colors.ORANGE)
            }
            if (y == 7 && isCastlingValid_White && color!!.equals(Players.WHITE) && isValidCastling(
                    x,
                    y,
                    color,
                    "left"
                )
            ) {
                castling.add(intArrayOf(x - 2, y))
                board!![y]!![x - 2]!!.setSelected(true, Colors.ORANGE)
            }
            if (y == 7 && isCastlingValid_White && color!!.equals(Players.WHITE) && isValidCastling(
                    x,
                    y,
                    color,
                    "right"
                )
            ) {
                castling.add(intArrayOf(x + 2, y))
                board!![y]!![x + 2]!!.setSelected(true, Colors.ORANGE)
            }
        }
        return list
    }

    override fun pawn_movement(x: Int, y: Int, chessToken: ChessToken?, color: Players?): ArrayList<IntArray> {
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
            if (enPassant.size > 0 && (y == 3 || y == 4)) {
                board!![enPassant[1]]!![enPassant[0]]!!.setSelected(true, Colors.ORANGE)
            }
        }
        return list
    }
}