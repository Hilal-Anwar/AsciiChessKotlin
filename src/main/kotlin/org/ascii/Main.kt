package org.ascii

import org.ascii.chess.Game
import org.jline.terminal.TerminalBuilder
import org.jline.utils.InfoCmp
import java.io.IOException

object Main {
    private var TITLE = """
                        
            ░█████╗░░██████╗░█████╗░██╗██╗  ░█████╗░██╗░░██╗███████╗░██████╗░██████╗
            ██╔══██╗██╔════╝██╔══██╗██║██║  ██╔══██╗██║░░██║██╔════╝██╔════╝██╔════╝
            ███████║╚█████╗░██║░░╚═╝██║██║  ██║░░╚═╝███████║█████╗░░╚█████╗░╚█████╗░
            ██╔══██║░╚═══██╗██║░░██╗██║██║  ██║░░██╗██╔══██║██╔══╝░░░╚═══██╗░╚═══██╗
            ██║░░██║██████╔╝╚█████╔╝██║██║  ╚█████╔╝██║░░██║███████╗██████╔╝██████╔╝
            ╚═╝░░╚═╝╚═════╝░░╚════╝░╚═╝╚═╝  ░╚════╝░╚═╝░░╚═╝╚══════╝╚═════╝░╚═════╝░
            
            """

    @Throws(InterruptedException::class)
    @JvmStatic
    fun main(args: Array<String>) {
        loading(StringBuilder("=>"))
        val game = Game()
        game.start()

    }

    @Throws(InterruptedException::class)
    private fun loading(s: StringBuilder) {
        try {
            TerminalBuilder.terminal().use { terminal ->
                while (s.length <= 80) {
                    terminal.puts(InfoCmp.Capability.clear_screen)
                    println(TITLE.indentN(30))
                    s.insert(0, "=")
                    println(s.toString().indentN(25))
                    Thread.sleep(16)
                }
            }
        } catch (e: IOException) {
            throw RuntimeException(e)
        }
    }

    private fun String.indentN(n: Int): String {
        return ((" ".repeat(n))+this)
    }

}