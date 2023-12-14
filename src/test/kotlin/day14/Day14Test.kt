package day14

import DayTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class Day14Test : DayTest() {
    private val exampleDay = Day14("14".readTestInput())
    private val day = Day14("14".readInput())

    @Test
    fun testSolvePart1WithExampleInput() {
        val part1 = solvePart1(exampleDay)
        val expectedSolution = 136L
        Assertions.assertEquals(expectedSolution, part1, "Part 1 with example input should equal $expectedSolution")
    }

    @Test
    fun testSolvePart2WithExampleInput() {
        val part2 = solvePart2(exampleDay)
        val expectedSolution = 64L
        Assertions.assertEquals(expectedSolution, part2, "Part 2 with example input should equal $expectedSolution")
    }

    @Test
    fun part1() {
        assert(solvePart1(day) == 106990L)
    }

    @Test
    fun part2() {
        assert(solvePart2(day) == 100531L)
    }

}