package day17

import DayTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class Day17Test : DayTest() {
    private val exampleDay = Day17("17".readTestInput())
    private val day = Day17("17".readInput())

    @Test
    fun testSolvePart1WithExampleInput() {
        val part1 = solvePart1(exampleDay)
        val expectedSolution = 102L
        Assertions.assertEquals(expectedSolution, part1, "Part 1 with example input should equal $expectedSolution")
    }

    @Test
    fun testSolvePart2WithExampleInput() {
        val part2 = solvePart2(exampleDay)
        val expectedSolution = 94L
        Assertions.assertEquals(expectedSolution, part2, "Part 2 with example input should equal $expectedSolution")
    }

    @Test
    fun part1() {
        val part1 = solvePart1(day)
        Assertions.assertEquals(668, part1)
    }

    @Test
    fun part2() {
        val part2 = solvePart2(day)
        Assertions.assertEquals(788, part2)
    }

    @Test
    fun anotherExamplePart2() {
        val anotherExampleDay = Day17(
            """111111111111
999999999991
999999999991
999999999991
999999999991
""".trimIndent()
        )
        val part2 = solvePart2(anotherExampleDay)
        val expectedSolution = 71L
        Assertions.assertEquals(expectedSolution, part2, "Part 2 with example input should equal $expectedSolution")
    }
}