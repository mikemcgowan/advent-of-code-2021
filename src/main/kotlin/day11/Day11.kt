package day11

import java.io.File

typealias Point = Pair<Int, Int>
typealias Grid = List<MutableList<Pair<Int, Boolean>>>

fun main() {
    val input = File("src/main/kotlin/day11/input.txt").readLines()
    val result1 = part1(parseInput(input))
    println(result1)
    val result2 = part2(parseInput(input))
    println(result2)
}

fun parseInput(input: List<String>): Grid =
    input.map { it.map { x -> Pair(x.code - '0'.code, false) } }.map { it.toMutableList() }

fun part1(grid: Grid): Int {
    val rows = grid.size
    val cols = grid[0].size
    var step = 0
    var count = 0
    do {
        initGrid(rows, cols, grid)
        count += countFlashes(rows, cols, grid)
        zeroEnergy(rows, cols, grid)
        ++step
    } while (step < 100)
    return count
}

fun part2(grid: Grid): Int {
    val rows = grid.size
    val cols = grid[0].size
    var step = 0
    do {
        initGrid(rows, cols, grid)
        val count = countFlashes(rows, cols, grid)
        zeroEnergy(rows, cols, grid)
        ++step
    } while (count != rows * cols)
    return step
}

fun initGrid(rows: Int, cols: Int, grid: Grid) {
    (0 until rows).forEach { row ->
        (0 until cols).forEach { col ->
            grid[row][col] = Pair(grid[row][col].first + 1, false)
        }
    }
}

fun countFlashes(rows: Int, cols: Int, grid: Grid): Int {
    val toFlash = mutableListOf<Point>()
    (0 until rows).forEach { row ->
        (0 until cols).forEach { col ->
            if (grid[row][col].first > 9) toFlash.add(Pair(row, col))
        }
    }
    var count = 0
    while (toFlash.size > 0) {
        doFlash(rows, cols, grid, toFlash)
        count++
    }
    return count
}

fun doFlash(rows: Int, cols: Int, grid: Grid, toFlash: MutableList<Point>) {
    val (row, col) = toFlash.removeFirst()
    grid[row][col] = Pair(grid[row][col].first, true)
    val adjacent = listOf(
        Pair(row - 1, col - 1),
        Pair(row - 1, col + 0),
        Pair(row - 1, col + 1),
        Pair(row + 0, col - 1),
        Pair(row + 0, col + 1),
        Pair(row + 1, col - 1),
        Pair(row + 1, col + 0),
        Pair(row + 1, col + 1),
    )
    adjacent.filter { it.first >= 0 && it.second >= 0 && it.first < rows && it.second < cols }
        .forEach {
            grid[it.first][it.second] = Pair(grid[it.first][it.second].first + 1, grid[it.first][it.second].second)
            if (grid[it.first][it.second].first > 9 && !grid[it.first][it.second].second && !toFlash.contains(Pair(it.first, it.second)))
                toFlash.add(Pair(it.first, it.second))
        }
}

fun zeroEnergy(rows: Int, cols: Int, grid: Grid) {
    (0 until rows).forEach { row ->
        (0 until cols).forEach { col ->
            if (grid[row][col].second) grid[row][col] = Pair(0, false)
            else grid[row][col] = Pair(grid[row][col].first, false)
        }
    }
}
