package day17

import DayTest
import day16.Day16
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
        solvePart1(day)
    }

    @Test
    fun part2() {
        solvePart2(day)
    }

    @Test
    fun crucibleTest() {
        val crucible = Day17.Crucible(0, 0, 0, Day16.Direction.NORTH, 3)
        val possibleDirectionsToMove = crucible.possibleDirectionsToMovePart1()

        Assertions.assertTrue(possibleDirectionsToMove.size == 2)
        Assertions.assertTrue(possibleDirectionsToMove.contains(Pair(Day16.Direction.EAST, 1)))
        Assertions.assertTrue(possibleDirectionsToMove.contains(Pair(Day16.Direction.WEST, 1)))
    }

    @Test
    fun crucibleTestPart2() {
        val crucible = Day17.Crucible(0, 0, 0, Day16.Direction.NORTH, 1)
        val possibleDirectionsToMove = crucible.possibleDirectionsToMovePart2()

        Assertions.assertTrue(possibleDirectionsToMove.size == 1)
        Assertions.assertTrue(possibleDirectionsToMove.contains(Pair(Day16.Direction.NORTH, 2)))
    }

}