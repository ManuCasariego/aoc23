package day04

import Day
import kotlin.math.pow

class Day04(private val input: String) : Day() {
    override fun part1(): Int {
        val a = input.lines().map { line ->
            val numberOfMatches = getNumberOfMatches(line)
            if (numberOfMatches == 0) 0 else 2f.pow(numberOfMatches - 1).toInt()
        }
        return a.sum()
    }

    private fun getNumberOfMatches(line: String): Int {
        var split = line.split(" ")
//            Card 1: 41 48 83 86 17 | 83 86  6 31 17  9 48 53
        split = split.dropWhile { it == "Card" || it.matches(Regex("(\\d)+:")) || it == "" }

        val firstNumbers = mutableListOf<Int>()
        val secondNumbers = mutableListOf<Int>()
        var foundPipe = false
        for (elem in split) {
            if (elem == "|") {
                foundPipe = true
                continue
            }
            if (elem == "") {
                continue
            }
            if (!foundPipe) {
                firstNumbers.add(elem.toInt())
            } else {
                secondNumbers.add(elem.toInt())
            }
        }

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