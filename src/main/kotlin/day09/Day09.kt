package day09

import Day

class Day09(private val input: String) : Day() {
    override fun part1(): Long {
        val inputLongs = input.lines().map { line ->
            line.longs()
        }

        return inputLongs.sumOf { longs ->
            // new structure
            val nextLevels = buildNextLevels(longs)

            // we got to the all zeroes scenario
            // we add a zero and build our way up
            var currentNumber = 0L

            nextLevels.reversed().drop(1).forEach { level ->
                currentNumber = level.last() + currentNumber
            }
            currentNumber
        }
    }

    private fun buildNextLevels(input: List<Long>): List<MutableList<Long>> {
        val nextLevels = mutableListOf<MutableList<Long>>()
        // let's go down first
        nextLevels.add(input.toMutableList())
        while (nextLevels.last().size != 1 && !nextLevels.last().all { it == 0L }) {
            val nextLevel = calculateNextLevel(nextLevels.last())
            nextLevels.add(nextLevel)
        }
        return nextLevels
    }

    private fun calculateNextLevel(input: List<Long>): MutableList<Long> {
        val nextLevel = mutableListOf<Long>()
        input.forEachIndexed { index, l ->
            if (index != input.size - 1) nextLevel.add(input[index + 1] - l)
        }
        return nextLevel
    }

    private fun String.longs() = this.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
    override fun part2(): Long {
        val inputLongs = input.lines().map { line ->
            line.longs()
        }

        return inputLongs.sumOf { longs ->
            // new structure
            val nextLevels = buildNextLevels(longs)

            // we got to the all zeroes scenario
            // we add a zero and build our way up
            var currentNumber = 0L

            nextLevels.reversed().drop(1).forEach { level ->
                currentNumber = level.first() - currentNumber
            }
            currentNumber
        }

    }
}