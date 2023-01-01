package org.ascii.chess.util

enum class Colors(val color: String) {
    RED("\u001b[0;31m"),  // RED
    GREEN("\u001b[0;32m"),  // GREEN
    YELLOW("\u001b[0;33m"),  // YELLOW
    BLUE("\u001b[0;34m"),  // BLUE
    PURPLE("\u001b[0;35m"),  // PURPLE
    CYAN("\u001b[0;36m"),  // CYAN
    WHITE("\u001b[0;97m"),  // WHITE
    CYAN_BRIGHT("\u001b[0;96m"), MAGENTA("\u001B[35m"), ORANGE("\u001b[38;5;208m")

}