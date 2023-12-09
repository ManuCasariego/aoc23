package day09

import Day

class Day09(private val input: String) : Day() {
    override fun part1(): Long {
        val inputLongs = input.lines().map { line ->
            line.longs()
        }

        return calculatePart1(inputLongs)
    }

    private fun calculatePart1(inputLongs: List<List<Long>>): Long {
        return inputLongs.sumOf { longs ->
            buildNextLevels(longs).reversed().drop(1).sumOf { level ->
                level.last()
            }
        }
    }

    private fun buildNextLevels(input: List<Long>): List<List<Long>> {
        val nextLevels = mutableListOf<List<Long>>()
        // let's go down first
        nextLevels.add(input)
        while (nextLevels.last().size != 1 && !nextLevels.last().all { it == 0L }) {
            val nextLevel = calculateNextLevel(nextLevels.last())
            nextLevels.add(nextLevel)
        }
        return nextLevels
    }

    private fun calculateNextLevel(input: List<Long>): List<Long> {
        return input.dropLast(1).mapIndexed { index, l ->
            input[index + 1] - l
        }
    }

    private fun String.longs() = this.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }
    override fun part2(): Long {
        val inputLongs = input.lines().map { line ->
            line.longs().reversed()
        }
        return calculatePart1(inputLongs)
    }
}