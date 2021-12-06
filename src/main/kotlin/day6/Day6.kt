package day6

import java.io.File

const val reset = 6
const val newborn = 8

fun main() {
    val input = File("src/main/kotlin/day6/input.txt").readText().split(',').map { it.trim().toInt() }
    val result1 = go(init(input), 0, 80)
    println(result1)
    val result2 = go(init(input), 0, 256)
    println(result2)
}

fun init(initial: List<Int>): MutableMap<Int, Long> {
    val fish = mutableMapOf<Int, Long>()
    (0..newborn).forEach { fish[it] = 0L }
    initial.forEach { fish[it] = fish[it]!! + 1L }
    return fish
}

tailrec fun go(fish: MutableMap<Int, Long>, day: Long, targetDay: Long): Long {
    if (day == targetDay) return fish.values.sum()
    val newbornCount = fish[0]!!
    (0 until newborn).forEach { fish[it] = fish[it + 1]!! }
    fish[reset] = fish[reset]!! + newbornCount
    fish[newborn] = newbornCount
    return go(fish, day + 1, targetDay)
}
