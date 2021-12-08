package day8

import java.io.File

typealias Line = Pair<List<String>, List<String>>

val digitToChars = mapOf(
    0 to listOf('a', 'b', 'c', 'e', 'f', 'g'),
    1 to listOf('c', 'f'),
    2 to listOf('a', 'c', 'd', 'e', 'g'),
    3 to listOf('a', 'c', 'd', 'f', 'g'),
    4 to listOf('b', 'c', 'd', 'f'),
    5 to listOf('a', 'b', 'd', 'f', 'g'),
    6 to listOf('a', 'b', 'd', 'e', 'f', 'g'),
    7 to listOf('a', 'c', 'f'),
    8 to listOf('a', 'b', 'c', 'd', 'e', 'f', 'g'),
    9 to listOf('a', 'b', 'c', 'd', 'f', 'g'),
)

val sizeToDigit = mapOf(
    2 to listOf(1),
    3 to listOf(7),
    4 to listOf(4),
    5 to listOf(2, 3, 5),
    6 to listOf(0, 6, 9),
    7 to listOf(8),
)

val unique = sizeToDigit.filterValues { it.size == 1 }.keys

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
    input.sumOf { (_, output) -> output.count { it.length in unique } }

fun part2(input: List<Line>): Int =
    input.sumOf { (xs, output) ->
        // to begin with, each position could be represented by of 'a' to 'g'
        val m = mapOf(
            'a' to 'a'..'g',
            'b' to 'a'..'g',
            'c' to 'a'..'g',
            'd' to 'a'..'g',
            'e' to 'a'..'g',
            'f' to 'a'..'g',
            'g' to 'a'..'g',
        ).mapValues { it.value.toList() }.toMutableMap()

        // constrain by known lengths of 1, 4, 7, 8
        xs.filter { it.length in unique }.forEach { x ->
            sizeToDigit[x.length]!!
                .map { digitToChars[it]!! }
                .first()
                .forEach { y -> m[y] = m[y]!!.filter { it in x } }
        }

        // deduce position 'a'
        m['a'] = m['a']!!.filter { it !in m['c']!! && it !in m['f']!! }

        // constrain remaining positions by what's known about positions 'a', 'c' and 'f'
        listOf('b', 'd', 'e', 'g').forEach { x ->
            m[x] = m[x]!!.filter { it != m['a']!!.first() && it !in m['c']!! }
        }

        // constrain remaining positions by what's known about positions 'b' and 'd'
        listOf('e', 'g').forEach { x ->
            m[x] = m[x]!!.filter { it !in m['b']!! }
        }

        // position 'c' is the only char that 6 doesn't have that both 0 and 9 do have
        // position 'd' is the only char that 0 doesn't have that both 6 and 9 do have
        // position 'e' is the only char that 9 doesn't have that both 0 and 6 do have
        val size6 = xs.filter { it.length == 6 }.map { it.toCharArray().sorted() }
        m['c'] = m['c']!!.filter { x -> size6.count { x in it } < 3 }
        m['f'] = m['f']!!.filter { x -> x !in m['c']!! }
        m['d'] = m['d']!!.filter { x -> size6.count { x in it } < 3 }
        m['b'] = m['b']!!.filter { x -> x !in m['d']!! }
        m['e'] = m['e']!!.filter { x -> size6.count { x in it } < 3 }
        m['g'] = m['g']!!.filter { x -> x !in m['e']!! }

        // convert output
        val n = m.mapValues { (_, xs) -> xs.first() }
        output.joinToString("") {
            sizeToDigit[it.length]!!
                .map { x -> Pair(x, digitToChars[x]!!.map { y -> n[y]!! }.sorted()) }
                .filter { x -> x.second == it.toCharArray().sorted() }
                .map { x -> x.first }
                .first()
                .toString()
        }.toInt()
    }
