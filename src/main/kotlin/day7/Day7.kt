package day7

import java.io.File

fun main() {
    val input = File("src/main/kotlin/day7/input.txt").readText().split(',').map { it.trim().toInt() }
    val result1 = part1(input)
    println(result1.minOrNull())
    val result2 = part2(input)
    println(result2.minOrNull())
}

fun part1(input: List<Int>): List<Int> {
    val max = input.maxOrNull()!!
    return (0..max).map { pos -> input.sumOf { kotlin.math.abs(pos - it) } }
}

fun part2(input: List<Int>): List<Int> {
    val max = input.maxOrNull()!!
    val lookup = mutableMapOf(0 to 0)
    (1..max).forEach { lookup[it] = lookup[it - 1]!! + it }
    return (0..max).map { pos -> input.sumOf { lookup[kotlin.math.abs(pos - it)]!! } }
}
