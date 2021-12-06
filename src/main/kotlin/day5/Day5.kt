package day5

import java.io.File
import java.lang.Integer.max

data class Point(val x: Int, val y: Int)

data class Line(val p1: Point, val p2: Point) {
    private val dx = p2.x - p1.x
    private val dy = p2.y - p1.y

    fun isStraight() = dx == 0 || dy == 0

    fun pointsOnLine(): List<Point> {
        val xs =
            if (dx == 0) IntArray(kotlin.math.abs(dy) + 1) { p1.x }.toList()
            else if (dx < 0) p1.x downTo p2.x
            else p1.x..p2.x
        val ys =
            if (dy == 0) IntArray(kotlin.math.abs(dx) + 1) { p1.y }.toList()
            else if (dy < 0) p1.y downTo p2.y
            else p1.y..p2.y
        return xs.zip(ys).map { Point(it.first, it.second) }
    }
}

fun main() {
    val allLines = File("src/main/kotlin/day5/input.txt").readLines().map { parseLine(it) }
    val straightLines = allLines.filter { it.isStraight() }
    val result1 = go(straightLines)
    println(result1)
    val result2 = go(allLines)
    println(result2)
}

fun go(lines: List<Line>): Int {
    val cols = lines.maxOf { max(it.p1.x, it.p2.x) }
    val rows = lines.maxOf { max(it.p1.y, it.p2.y) }
    val grid = (0..rows).map { (0..cols).map { 0 }.toMutableList() }
    lines.forEach { line ->
        line.pointsOnLine().forEach {
            grid[it.y][it.x]++
        }
    }
    return grid.sumOf { row -> row.count { it >= 2 } }
}

fun parseLine(s: String): Line = s.split("->")
    .map { it.split(",") }
    .map { (a, b) -> Point(a.trim().toInt(), b.trim().toInt()) }
    .let { (p1, p2) -> Line(p1, p2) }
