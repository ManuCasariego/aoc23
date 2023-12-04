package day04

import Day
import kotlin.math.pow

class Day04(private val input: String) : Day() {
    override fun part1(): Int {
        return input.lines().sumOf { line ->
            val numberOfMatches = getNumberOfMatches(line)
            if (numberOfMatches == 0) 0 else 2f.pow(numberOfMatches - 1).toInt()
        }
    }

    private fun getNumberOfMatches(line: String): Int {
        // Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        val split = line.split(":").last().split("|")

        val firstNumbers = split.first().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }
        val secondNumbers = split.last().split(" ").filter { it.isNotEmpty() }.map { it.toInt() }

        return secondNumbers.filter { it in firstNumbers }.size
    }

    override fun part2(): Int {
        val scratchCardCopies = input.lines().map { 1 }.toMutableList()

        input.lines().forEachIndexed { index, line ->
            val numberOfMatches = getNumberOfMatches(line)

            (1..numberOfMatches).forEach { dx ->
                if (index + dx < scratchCardCopies.size) scratchCardCopies[index + dx] += scratchCardCopies[index]
            }
        }
        return scratchCardCopies.sum()
    }

}