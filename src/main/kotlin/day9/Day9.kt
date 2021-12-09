package day9

import java.io.File

typealias Grid = List<List<Int>>
typealias Point = Pair<Int, Int>
typealias Location = Pair<Point, Int>

fun main() {
    val input = File("src/main/kotlin/day9/input.txt").readLines().map { it.map { x -> x.code - '0'.code } }
    val result1 = part1(input)
    println(result1)
    val result2 = part2(input)
    println(result2)
}

fun part1(grid: Grid): Int =
    lowPoints(grid).sumOf { 1 + it.second }

fun part2(grid: Grid): Int {
    val m = mutableMapOf<Location, MutableSet<Point>>()
    lowPoints(grid).forEach {
        m[it] = mutableSetOf()
        spreadFrom(grid, it.first, m[it]!!)
    }
    val (a, b, c) = m.mapValues { it.value.size }.values.sorted().takeLast(3)
    return a * b * c
}

fun spreadFrom(grid: Grid, from: Point, points: MutableSet<Point>) {
    val rows = grid.size
    val cols = grid[0].size
    val ps = adjacent(grid, rows, cols, from.first, from.second)
        .filter { it.second < 9 }
        .map { it.first }
        .filter { it !in points }
    if (ps.isEmpty()) return
    points.addAll(ps)
    ps.forEach { spreadFrom(grid, it, points) }
}

fun lowPoints(grid: Grid): List<Location> {
    val rows = grid.size
    val cols = grid[0].size
    val ps = mutableListOf<Location>()
    (0 until rows).forEach { row ->
        (0 until cols).forEach { col ->
            val xs = adjacent(grid, rows, cols, row, col)
            val x = grid[row][col]
            if (x < xs.minOf { it.second }) ps.add(Pair(Pair(row, col), x))
        }
    }
    return ps
}

fun adjacent(grid: Grid, rows: Int, cols: Int, row: Int, col: Int): List<Location> {
    val ps = mutableListOf<Location>()
    if (row > 0) ps.add(Pair(Pair(row - 1, col), grid[row - 1][col]))
    if (row < rows - 1) ps.add(Pair(Pair(row + 1, col), grid[row + 1][col]))
    if (col > 0) ps.add(Pair(Pair(row, col - 1), grid[row][col - 1]))
    if (col < cols - 1) ps.add(Pair(Pair(row, col + 1), grid[row][col + 1]))
    return ps
}
