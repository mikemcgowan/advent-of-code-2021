package day8

import java.io.File

typealias Line = Pair<List<String>, List<String>>

fun main() {
    val input = File("src/main/kotlin/day8/input.txt")
        .readLines()
        .map {
            val (a, b) = it.split('|')
            Pair(parse(a), parse(b))
        }
    val result1 = part1(input)
    println(result1)
    val result2 = part2(input)
    println(result2)
}

fun parse(s: String): List<String> = s.split(Regex("""\s+"""))
    .map { it.trim() }
    .filter { it.isNotBlank() }

fun part1(input: List<Line>): Int =
    input.sumOf { (_, output) -> output.count { it.length in listOf(2, 3, 4, 7) } }

fun part2(input: List<Line>): Int =
    input.sumOf { (xs, output) ->
        val ys = xs.map { it.toCharArray().sorted() }

        // 1, 4, 7 and 8 are known by their unique lengths (of 2, 4, 3, 7 respectively)
        val m = mutableMapOf(
            1 to ys.find { it.size == 2 },
            4 to ys.find { it.size == 4 },
            7 to ys.find { it.size == 3 },
            8 to ys.find { it.size == 7 },
        )

        // the 9 is the only size 6 that has all the chars of the 4
        m[9] = ys.find { it.size == 6 && m[4]!!.all { x -> x in it } }

        // the 0 is the only size 6 (apart from 9) that has both the chars of the 1
        m[0] = ys.find { it.size == 6 && it != m[9] && m[1]!!.all { x -> x in it } }

        // the 6 is therefore the only remaining size 6
        m[6] = ys.find { it.size == 6 && it != m[0] && it != m[9] }

        // the 3 is the only size 5 one that has both the chars of the 1
        m[3] = ys.find { it.size == 5 && m[1]!!.all { x -> x in it } }

        // the 5 is the only size 5 for which all its chars appear in the 6
        m[5] = ys.find { it.size == 5 && it.all { x -> x in m[6]!! } }

        // the 2 is therefore the only remaining size 5
        m[2] = ys.find { it.size == 5 && it != m[3] && it != m[5] }

        // transform output
        output
            .map { it.toCharArray().sorted() }
            .map { m.filterValues { x -> x == it }.keys.first() }
            .joinToString("")
            .toInt()
    }
