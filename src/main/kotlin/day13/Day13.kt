package day13

import java.io.File

typealias Point = Pair<Int, Int>

fun main() {
    val (xs, ys) = File("src/main/kotlin/day13/input.txt").readText().split("\n\n")
    val points = xs.split('\n').map {
        val (a, b) = it.split(',')
        Pair(a.toInt(), b.toInt())
    }
    val folds = ys.split('\n')
        .map { it.drop("fold along ".length) }
        .filter { it.isNotBlank() }
        .map {
            val (a, b) = it.split('=')
            Pair(a.first(), b.toInt())
        }
    val result1 = solve(points.toSet(), folds.take(1))
    println(result1.size)
    val result2 = solve(points.toSet(), folds)
    draw(result2)
}

fun solve(ps: Set<Point>, fs: List<Pair<Char, Int>>): Set<Point> =
    fs.fold(ps) { acc, (a, b) ->
        when (a) {
            'x' -> foldXCoordsAt(b, acc)
            'y' -> foldYCoordsAt(b, acc)
            else -> acc
        }
    }

fun foldXCoordsAt(foldAtX: Int, ps: Set<Point>): Set<Point> =
    ps.map {
        if (it.first > foldAtX) Pair(it.first - 2 * (it.first - foldAtX), it.second)
        else it
    }.toSet()

fun foldYCoordsAt(foldAtY: Int, ps: Set<Point>): Set<Point> =
    ps.map {
        if (it.second > foldAtY) Pair(it.first, it.second - 2 * (it.second - foldAtY))
        else it
    }.toSet()

fun draw(ps: Set<Point>) {
    val maxX = ps.maxByOrNull { it.first }!!.first
    val maxY = ps.maxByOrNull { it.second }!!.second
    (0..maxY).forEach { y ->
        val line = (0..maxX).map { x -> if (Pair(x, y) in ps) '#' else ' ' }
        println(line.joinToString(""))
    }
}
