import java.io.File

open class DayTest {

    protected fun solvePart1(day: Day): Int {
        val start = System.currentTimeMillis()
        val part1 = day.part1()
        println("Part 1: $part1 in ${System.currentTimeMillis() - start}ms")
        return part1
    }

    protected fun solvePart2(day: Day): Int {
        val start = System.currentTimeMillis()
        val part2 = day.part2()
        println("Part 2: $part2 in ${System.currentTimeMillis() - start}ms")
        return part2
    }

    protected fun String.readInput(): String {
        return File("src/test/kotlin/day${this}/input").readText()
    }

    protected fun String.readTestInput(): String {
        return File("src/test/kotlin/day${this}/testInput").readText()
    }
}