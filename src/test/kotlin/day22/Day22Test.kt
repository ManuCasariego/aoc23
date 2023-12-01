package day22

import DayTest
import org.junit.jupiter.api.Test
import kotlin.test.assertEquals


class Day22Test : DayTest() {
    private val day = Day22("22".readInput())


    @Test
    fun part1() {
        val part1 = solvePart1(day)
        assertEquals(1824, part1)
    }

    @Test
    fun part2() {
        val part2 = solvePart2(day)
        assertEquals(1937, part2)
    }

}