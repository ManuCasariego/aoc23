package day25

import DayTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class Day25Test : DayTest() {
    private val exampleDay = Day25("25".readTestInput())
    private val day = Day25("25".readInput())


    @Test
    fun testSolvePart1WithExampleInput() {
        val part1 = solvePart1(exampleDay)
        val expectedSolution = 54L
        Assertions.assertEquals(expectedSolution, part1, "Part 1 with example input should equal $expectedSolution")
    }

    @Test
    fun part1() {
        Assertions.assertEquals(518391L, solvePart1(day))
    }

}