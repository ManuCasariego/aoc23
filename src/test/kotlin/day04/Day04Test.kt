package day04

import DayTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class Day04Test : DayTest() {
    private val exampleDay = Day04("04".readTestInput())
    private val day = Day04("04".readInput())

    @Test
    fun testSolvePart1WithExampleInput() {
        val part1 = solvePart1(exampleDay)
        val expectedSolution = 13
        Assertions.assertEquals(expectedSolution, part1, "Part 1 with example input should equal $expectedSolution")
    }

    @Test
    fun testSolvePart2WithExampleInput() {
        val part2 = solvePart2(exampleDay)
        val expectedSolution = 30
        Assertions.assertEquals(expectedSolution, part2, "Part 1 with example input should equal $expectedSolution")

    }

    @Test
    fun part1() {
        solvePart1(day)
    }

    @Test
    fun part2() {
        solvePart2(day)
    }

}