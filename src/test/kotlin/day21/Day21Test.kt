package day21

import DayTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class Day21Test : DayTest() {
    private val day = Day21("21".readInput())
    private val exampleDay = Day21("21".readTestInput())

    @Test
    fun testSolvePart1WithExampleInput() {
        val part1 = solvePart1(exampleDay)
        val expectedSolution = 42L
        Assertions.assertEquals(expectedSolution, part1, "Part 1 with example input should equal $expectedSolution")
    }

    @Test
    fun part1() {
        Assertions.assertEquals(3578L, solvePart1(day))
    }

    @Test
    fun part2() {
        Assertions.assertEquals(594115391548176L, solvePart2(day))
    }


}