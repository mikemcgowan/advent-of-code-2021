package day4

import java.io.File

typealias Grid = List<MutableList<Pair<Int, Boolean>>>

fun main() {
    val input = File("src/main/kotlin/day4/input.txt").readText().split("\n\n")
    val balls = input.first().split(',').map { it.toInt() }
    val result1 = go(balls, input.drop(1).map { parseGrid(it) }, true)
    println(result1)
    val result2 = go(balls, input.drop(1).map { parseGrid(it) }, false)
    println(result2)
}

fun parseGrid(s: String): Grid =
    s.split('\n')
        .map { it.split(Regex("""\s+""")).filter { n -> n.isNotBlank() }.map { n -> Pair(n.toInt(), false) } }
        .map { it.toMutableList() }
        .filter { it.isNotEmpty() }

fun go(balls: List<Int>, grids: List<Grid>, firstWinner: Boolean): Int {
    val winnerIndices = mutableListOf<Pair<Int, Int>>()
    balls.forEach { ball ->
        val incompleteGridIndices = grids.indices.filter { it !in winnerIndices.map { p -> p.first } }
        incompleteGridIndices.forEach { mark(grids[it], ball) }
        incompleteGridIndices.filter { isWinner(grids[it]) }.forEach { winnerIndices.add(Pair(it, ball)) }
    }
    val winnerIndex = if (firstWinner) winnerIndices.first() else winnerIndices.last()
    val sumUnmarked = grids[winnerIndex.first].flatten().filter { p -> !p.second }.sumOf { p -> p.first }
    return sumUnmarked * winnerIndex.second
}

fun mark(grid: Grid, ball: Int) {
    grid.forEach { row ->
        row.indices.forEach { i ->
            if (row[i].first == ball) row[i] = Pair(row[i].first, true)
        }
    }
}

fun isWinner(grid: Grid): Boolean {
    if (grid.any { it.all { p -> p.second } }) return true
    val colCount = grid[0].size
    val cols = (0 until colCount).map { col -> grid.map { it[col] } }
    if (cols.any { it.all { p -> p.second } }) return true
    return false;
}
