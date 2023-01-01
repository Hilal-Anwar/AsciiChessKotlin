package org.ascii.chess.util

import java.io.IOException

class KeyBoardInput(display: Display) {
    var keyBoardKey = Key.NONE

    init {
        Thread {
            while (true) {
                try {
                    keyBoardKey = getKeys(display.terminal!!.reader().read())
                } catch (e: IOException) {
                    throw RuntimeException(e)
                }
            }
        }.start()
    }

    companion object {
        private fun getKeys(ch: Int): Key {
            return when (ch) {
                9 -> Key.TAB
                13 -> Key.ENTER
                27 -> Key.ESC
                8 -> Key.BACKSPACE
                65, 119 -> Key.UP
                32 -> Key.SPACE
                66, 115 -> Key.DOWN
                68, 97 -> Key.LEFT
                67, 100 -> Key.RIGHT
                104 -> Key.HOLD
                else -> Key.NONE
            }
        }
    }
}