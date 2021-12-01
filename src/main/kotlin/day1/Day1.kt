import java.io.File

fun main() {
    val input = File("src/main/kotlin/day1/input.txt").readLines().map { it.toInt() }
    val result1 = input.windowed(2).count { it[1] > it[0] }
    println(result1)
    val result2 = input.windowed(3).windowed(2).count { it[1].sum() > it[0].sum() }
    println(result2)
}
