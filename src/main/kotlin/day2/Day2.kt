import java.io.File

fun main() {
    val input = File("src/main/kotlin/day2/input.txt").readLines()
    val result1 = part1(input)
    println(result1)
    val result2 = part2(input)
    println(result2)
}

fun part1(input: List<String>): Int {
    val (horiz, depth) = input.fold(Pair(0, 0)) { acc, instr ->
        val (dir, amount) = parseInstr(instr)
        when (dir) {
            "forward" -> Pair(acc.first + amount, acc.second)
            "up" -> Pair(acc.first, acc.second - amount)
            "down" -> Pair(acc.first, acc.second + amount)
            else -> acc
        }
    }
    return horiz * depth
}

fun part2(input: List<String>): Int {
    val (horiz, depth, _) = input.fold(Triple(0, 0, 0)) { acc, instr ->
        val (dir, amount) = parseInstr(instr)
        when (dir) {
            "forward" -> Triple(acc.first + amount, acc.second + acc.third * amount, acc.third)
            "up" -> Triple(acc.first, acc.second, acc.third - amount)
            "down" -> Triple(acc.first, acc.second, acc.third + amount)
            else -> acc
        }
    }
    return horiz * depth
}

fun parseInstr(instr: String): Pair<String, Int> {
    val (dir, amt) = instr.split(' ').take(2)
    return Pair(dir, amt.toInt())
}
