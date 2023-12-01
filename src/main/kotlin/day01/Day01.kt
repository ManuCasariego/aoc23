package day01

import Day

class Day01(private val input: String) : Day() {
    override fun part1(): Int {
        return input.lines().sumOf { line ->
            val digits = line.filter { it.isDigit() }.map { it.digitToInt() }
            digits.first() * 10 + digits.last()
        }
    }

    override fun part2(): Int {
        return input.lines().sumOf { findFirst(it) * 10 + findLast(it) }
    }

    private fun findFirst(input: String): Int {
        val auxMap = mutableMapOf<Int, Int>()
        listOf(
            "(1|one)",
            "(2|two)",
            "(3|three)",
            "(4|four)",
            "(5|five)",
            "(6|six)",
            "(7|seven)",
            "(8|eight)",
            "(9|nine)"
        ).forEach {
            auxMap[Regex(it).findAll(input).map { matchResult -> matchResult.range.first }.firstOrNull()
                ?: Int.MAX_VALUE] = it.toCharArray()[1].digitToInt()
        }
        return auxMap.minBy { it.key }.value
    }

    private fun findLast(input: String): Int {
        val auxMap = mutableMapOf<Int, Int>()
        listOf(
            "(1|one)",
            "(2|two)",
            "(3|three)",
            "(4|four)",
            "(5|five)",
            "(6|six)",
            "(7|seven)",
            "(8|eight)",
            "(9|nine)"
        ).forEach {
            auxMap[Regex(it).findAll(input).map { matchResult -> matchResult.range.first }.lastOrNull()
                ?: Int.MIN_VALUE] = it.toCharArray()[1].digitToInt()
        }
        return auxMap.maxBy { it.key }.value
    }

}