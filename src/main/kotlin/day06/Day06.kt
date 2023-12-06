package day06

import Day

class Day06(private val input: String) : Day() {
    override fun part1(): Long {
        //Time:      7  15   30
        //Distance:  9  40  200
        // three races, first one last 7ms record distance is 9 mm
        // starting speed 0 mm/ms
        return input.lines().first().substringAfter(":").longs()
            .zip(input.lines().last().substringAfter(":").longs())
            .map { (time, record) ->
                getNumberOfCombinationsThatBeatTheRecord(time, record)
            }.reduce(Long::times)
    }

    private fun String.longs() = this.split(" ").filter { it.isNotEmpty() }.map { it.toLong() }

    private fun getNumberOfCombinationsThatBeatTheRecord(time: Long, record: Long): Long {

        val firstTimeThatWorks = (0..time).first { i ->
            (i * (time - i) > record)
        }
        val possibleSolution = (time + 1) - (2 * firstTimeThatWorks)
        return if (possibleSolution > 0) possibleSolution else 0L
    }

    override fun part2(): Long {
        val time = input.lines().first().substringAfter(":").filter { it.isDigit() }.toLong()
        val record = input.lines().last().substringAfter(":").filter { it.isDigit() }.toLong()

        return getNumberOfCombinationsThatBeatTheRecord(time, record)
    }

}