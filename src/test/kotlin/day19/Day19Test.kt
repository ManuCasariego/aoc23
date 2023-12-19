package day19

import DayTest
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class Day19Test : DayTest() {
  private val exampleDay = Day19("19".readTestInput())
  private val day = Day19("19".readInput())

  @Test
  fun testSolvePart1WithExampleInput() {
    val part1 = solvePart1(exampleDay)
    val expectedSolution = 19114L
    Assertions.assertEquals(expectedSolution, part1, "Part 1 with example input should equal $expectedSolution")
  }

  @Test
  fun testSolvePart2WithExampleInput() {
    val part2 = solvePart2(exampleDay)
    val expectedSolution = 167409079868000L
    Assertions.assertEquals(expectedSolution, part2, "Part 2 with example input should equal $expectedSolution")
  }

  @Test
  fun part1() {
    Assertions.assertEquals(solvePart1(day),420739)
  }

  @Test
  fun part2() {
    assertEquals(solvePart2(day),130251901420382)

  }


}