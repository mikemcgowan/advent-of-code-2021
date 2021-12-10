package day10

import java.io.File

val lookup = mapOf(
    '(' to ')',
    '[' to ']',
    '{' to '}',
    '<' to '>',
)

val scoreTablePart1 = mapOf(
    ')' to 3,
    ']' to 57,
    '}' to 1197,
    '>' to 25137,
)

val scoreTablePart2 = mapOf(
    ')' to 1,
    ']' to 2,
    '}' to 3,
    '>' to 4,
)

fun main() {
    val input = File("src/main/kotlin/day10/input.txt").readLines()
    val result1 = part1(input)
    println(result1)
    val result2 = part2(input)
    println(result2)
}

fun part1(input: List<String>): Int =
    input.sumOf { scoreLine(it).first }

fun part2(input: List<String>): Long {
    val incomplete = input.asSequence()
        .map { scoreLine(it) }
        .filter { it.first == 0 }
        .map { scoreAutocomplete(it.second.reversed().map { c -> lookup[c]!! }) }
        .sorted()
        .toList()
    return incomplete[incomplete.size / 2]
}

fun scoreLine(line: String): Pair<Int, List<Char>> {
    val stack = mutableListOf<Char>()
    line.forEach { c ->
        if (c in lookup.keys)
            stack.add(c)
        else if (c in lookup.values && stack.isNotEmpty() && lookup[stack.last()] == c)
            stack.removeLast()
        else if (c in lookup.values)
            return Pair(scoreTablePart1[c]!!, emptyList())
    }
    return Pair(0, stack)
}

fun scoreAutocomplete(xs: List<Char>): Long =
    xs.fold(0) { acc, c -> 5 * acc + scoreTablePart2[c]!! }
