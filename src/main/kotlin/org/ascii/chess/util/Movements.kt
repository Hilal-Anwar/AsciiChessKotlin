package org.ascii.chess.util

import org.ascii.chess.ChessToken

interface Movements {
    fun rookMovement(x: Int, y: Int, color: Players?): ArrayList<IntArray>
    fun bishopMovement(x: Int, y: Int, color: Players?): ArrayList<IntArray>
    fun knightMovement(x: Int, y: Int, color: Players?): ArrayList<IntArray>
    fun queenMovement(x: Int, y: Int, color: Players?): ArrayList<IntArray>
    fun kingMovement(x: Int, y: Int, color: Players?): ArrayList<IntArray>
    fun pawnMovement(x: Int, y: Int, chessToken: ChessToken?, color: Players?): ArrayList<IntArray>
}