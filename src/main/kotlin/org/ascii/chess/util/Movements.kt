package org.ascii.chess.util

import org.ascii.chess.ChessToken

interface Movements {
    fun rook_movement(x: Int, y: Int, color: Players?): ArrayList<IntArray>
    fun bishop_movement(x: Int, y: Int, color: Players?): ArrayList<IntArray>
    fun knight_movement(x: Int, y: Int, color: Players?): ArrayList<IntArray>
    fun queen_movement(x: Int, y: Int, color: Players?): ArrayList<IntArray>
    fun king_movement(x: Int, y: Int, color: Players?): ArrayList<IntArray>
    fun pawn_movement(x: Int, y: Int, chessToken: ChessToken?, color: Players?): ArrayList<IntArray>
}