package day01

import DayTest
import org.junit.jupiter.api.Test


class Day01Test : DayTest() {
    private val exampleDay = Day01("()())")
    private val day = Day01("01".readInput())

    @Test
    fun part1Example() {
        solvePart1(exampleDay)
    }

    @Test
    fun part2Example() {
        solvePart2(exampleDay)
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