package day08

import DayTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test


class Day08Test : DayTest() {
    private val exampleDay = Day08("08".readTestInput())
    private val day = Day08("08".readInput())

    @Test
    fun testSolvePart1WithExampleInput() {
        val dayExamplePart1 = Day08(
            """RL

AAA = (BBB, CCC)
BBB = (DDD, EEE)
CCC = (ZZZ, GGG)
DDD = (DDD, DDD)
EEE = (EEE, EEE)
GGG = (GGG, GGG)
ZZZ = (ZZZ, ZZZ)"""
        )

        val part1 = solvePart1(dayExamplePart1)
        val expectedSolution = 2L
        Assertions.assertEquals(expectedSolution, part1, "Part 1 with example input should equal $expectedSolution")
    }

    @Test
    fun testSolvePart2WithExampleInput() {
        val part2 = solvePart2(exampleDay)
        val expectedSolution = 6L
        Assertions.assertEquals(expectedSolution, part2, "Part 2 with example input should equal $expectedSolution")
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