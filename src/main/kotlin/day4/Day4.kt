package day4

import java.io.File

typealias Grid = List<MutableList<Pair<Int, Boolean>>>

fun main() {
    val input = File("src/main/kotlin/day4/input.txt").readText().split("\n\n")
    val balls = input.first().split(',').map { it.toInt() }
    val grids = input.drop(1).map { parseGrid(it) }
    val winnerIndices = go(balls, grids)
    println(score(grids, winnerIndices.first()))
    println(score(grids, winnerIndices.last()))
}

fun parseGrid(s: String): Grid =
    s.split('\n')
        .map { line ->
            line.split(Regex("""\s+"""))
                .filter { it.isNotBlank() }
                .map { Pair(it.toInt(), false) }
        }
        .map { it.toMutableList() }
        .filter { it.isNotEmpty() }

fun go(balls: List<Int>, grids: List<Grid>): List<Pair<Int, Int>> {
    val winnerIndices = mutableListOf<Pair<Int, Int>>()
    balls.forEach { ball ->
        val incompleteGridIndices = grids.indices.filter { it !in winnerIndices.map { p -> p.first } }
        incompleteGridIndices.forEach { mark(grids[it], ball) }
        incompleteGridIndices.filter { isComplete(grids[it]) }.forEach { winnerIndices.add(Pair(it, ball)) }
    }
    return winnerIndices
}

fun score(grids: List<Grid>, winnerIndex: Pair<Int, Int>): Int {
    val sumUnmarked = grids[winnerIndex.first]
        .flatten()
        .filter { !it.second }
        .sumOf { it.first }
    return sumUnmarked * winnerIndex.second
}

fun mark(grid: Grid, ball: Int) {
    grid.forEach { row ->
        row.indices.forEach { i ->
            if (row[i].first == ball) row[i] = Pair(row[i].first, true)
        }
    }
}

fun isComplete(grid: Grid): Boolean {
    if (grid.any { it.all { p -> p.second } }) return true
    val cols = (0 until grid[0].size).map { col -> grid.map { it[col] } }
    if (cols.any { it.all { p -> p.second } }) return true
    return false;
}
