package day22

import DayTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class Day22Test : DayTest() {
    private val day = Day22("22".readInput())
    private val exampleDay = Day22("22".readTestInput())

    @Test
    fun testSolvePart1WithExampleInput() {
        val part1 = solvePart1(exampleDay)
        val expectedSolution = 5L
        Assertions.assertEquals(expectedSolution, part1, "Part 1 with example input should equal $expectedSolution")
    }

    @Test
    fun testSolvePart2WithExampleInput() {
        val part2 = solvePart2(exampleDay)
        val expectedSolution = 7L
        Assertions.assertEquals(expectedSolution, part2, "Part 2 with example input should equal $expectedSolution")
    }

    @Test
    fun part1() {
        Assertions.assertEquals(448L, solvePart1(day))
    }

    @Test
    fun part2() {
        Assertions.assertEquals(57770L, solvePart2(day))
    }

}