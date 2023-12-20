package day20

import DayTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class Day20Test : DayTest() {
    private val day = Day20("20".readInput())
    private val exampleDay = Day20("20".readTestInput())

    @Test
    fun testSolvePart1WithExampleInput() {
        val part1 = solvePart1(exampleDay)
        val expectedSolution = 32000000L
        Assertions.assertEquals(expectedSolution, part1, "Part 1 with example input should equal $expectedSolution")
    }

    @Test
    fun part1() {
        Assertions.assertEquals(839775244, solvePart1(day))
    }

    @Test
    fun part2() {
        Assertions.assertEquals(207787533680413, solvePart2(day))
    }
}