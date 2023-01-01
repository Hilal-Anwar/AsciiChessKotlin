package org.ascii.chess.util

import org.jline.terminal.Terminal
import org.jline.terminal.TerminalBuilder
import java.io.IOException

open class Display protected constructor() {
    var terminal: Terminal? = null

    init {
        terminal = try {
            TerminalBuilder.terminal()
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    protected fun clear_display() {
        println("\u001b[H\u001b[J")
    }
}