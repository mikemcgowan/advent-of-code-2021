package day14

import java.io.File

var stepsPart1 = 10
var stepsPart2 = 40

fun main() {
    val input = File("src/main/kotlin/day14/input.txt").readLines()
    val template = input[0]
    val lookup1 = input.drop(2).map { line ->
        val (k, v) = line.split("->").map { it.trim() }
        k to v
    }.toMap()
    val lookup2 = lookup1.mapValues { Pair(it.key.first() + it.value, it.value + it.key.last()) }
    val result1a = inefficient(template, lookup1, stepsPart1)
    val result1b = efficient(template, lookup2, stepsPart1)
    assert(result1a.toLong() == result1b)
    println(result1a)
    val result2 = efficient(template, lookup2, stepsPart2)
    println(result2)
}

fun inefficient(template: String, lookup: Map<String, String>, stepLimit: Int): Int {
    var s = template
    val c = template.last()
    var step = 0
    do {
        s = s.windowed(2).joinToString("") { it.first() + lookup[it]!! } + c
        ++step
    } while (step < stepLimit)
    val xs = s.groupBy { it }.mapValues { it.value.size }.values.sorted()
    return xs.last() - xs.first()
}

fun efficient(template: String, lookup: Map<String, Pair<String, String>>, stepLimit: Int): Long {
    var currentPairs = template.windowed(2).groupBy { it }.mapValues { it.value.size.toLong() }
    val tally = template.groupBy { it.toString() }.mapValues { it.value.size.toLong() }.toMutableMap()
    var step = 0
    do {
        val newPairs = mutableMapOf<String, Long>()
        currentPairs.forEach { entry ->
            val (a, b) = lookup[entry.key]!!
            val f = { _: String, c: Long? -> if (c == null) entry.value else c + entry.value }
            newPairs.compute(a, f)
            newPairs.compute(b, f)
            tally.compute(a.last().toString(), f)
        }
        currentPairs = newPairs
        ++step
    } while (step < stepLimit)
    val xs = tally.values.sorted()
    return xs.last() - xs.first()
}
