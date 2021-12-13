package day12

import java.io.File

fun main() {
    val input = File("src/main/kotlin/day12/input.txt").readLines()
    val lookup = parseInput(input)
    val result1 = solve(lookup, false)
    println(result1)
    val result2 = solve(lookup, true)
    println(result2)
}

fun parseInput(input: List<String>): Map<String, Set<String>> {
    val m = mutableMapOf<String, Set<String>>()
    input.forEach { line ->
        val (a, b) = line.split('-')
        m.compute(a) { _, xs ->
            val ys = xs ?: emptySet()
            if (b != "start") ys + b else ys
        }
        m.compute(b) { _, xs ->
            val ys = xs ?: emptySet()
            if (a != "start") ys + a else ys
        }
    }
    return m - "end"
}

fun solve(lookup: Map<String, Set<String>>, part2: Boolean): Int {
    val paths = mutableSetOf(listOf("start"))
    go(paths, lookup, part2)
    val pathsWithSmallCaves = paths.filter { path -> path.any { it != "start" && it != "end" && it.lowercase() == it } }
    return pathsWithSmallCaves.size
}

tailrec fun go(paths: MutableSet<List<String>>, lookup: Map<String, Set<String>>, part2: Boolean) {
    val unfinishedPaths = paths.filter { it.last() != "end" }
    var newPaths = false
    unfinishedPaths.forEach { unfinishedPath ->
        paths.remove(unfinishedPath)
        val destinations = lookup[unfinishedPath.last()]!!.filter {
            val small = unfinishedPath.filter { location -> location.lowercase() == location }
            it.uppercase() == it || it !in unfinishedPath
                || (part2 && unfinishedPath.count { x -> x == it } == 1 && small.size == small.toSet().size)
        }
        if (destinations.isNotEmpty()) newPaths = true
        destinations.forEach { paths.add(unfinishedPath + it) }
    }
    if (!newPaths) return
    go(paths, lookup, part2)
}
