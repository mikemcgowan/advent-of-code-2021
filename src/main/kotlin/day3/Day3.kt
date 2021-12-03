package day3

import java.io.File
import kotlin.math.pow

fun main() {
    val input = File("src/main/kotlin/day3/input.txt").readLines()
    val bits = input[0].length
    val result1 = part1(input, bits)
    println(result1)
    val result2 = part2(input)
    println(result2)
}

fun part1(input: List<String>, bits: Int): Long {
    val gamma = input.fold(IntArray(bits)) { acc, line ->
        line.indices.forEach { if (line[it] == '1') acc[it] += 1 }
        acc
    }.map { it > input.size / 2 }
    val epsilon = gamma.map { !it }
    return binaryToDecimal(gamma) * binaryToDecimal(epsilon)
}

fun part2(input: List<String>): Long {
    val oxygen = eliminate(input, 0, true).first().map { it == '1' }
    val co2 = eliminate(input, 0, false).first().map { it == '1' }
    return binaryToDecimal(oxygen) * binaryToDecimal(co2)
}

tailrec fun eliminate(xs: List<String>, i: Int, mostCommon: Boolean): List<String> {
    if (xs.size == 1) return xs
    val (x, y, f) =
        if (mostCommon) Triple('1', '0') { a: Int, b: Int -> a >= b }
        else Triple('0', '1') { a: Int, b: Int -> a <= b }
    val map = xs.map { it[i] }.groupBy { it }.mapValues { it.value.size }
    val filterBy = if (f(map[x]!!, map[y]!!)) x else y
    return eliminate(xs.filter { it[i] == filterBy }, i + 1, mostCommon)
}

fun binaryToDecimal(xs: List<Boolean>): Long = xs
    .zip(xs.indices.reversed())
    .sumOf { if (it.first) 2.0.pow(it.second.toDouble()).toLong() else 0 }
