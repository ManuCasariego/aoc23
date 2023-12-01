package day17

import DayTest
import org.junit.jupiter.api.Test


class Day17Test : DayTest() {
    private val exampleDay = Day17("17".readTestInput())
    private val day = Day17("17".readInput())


    @Test
    fun part1() {
        solvePart1(day)
    }

    @Test
    fun part2() {
        solvePart2(day)
    }

}