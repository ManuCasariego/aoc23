package day01

import Day

class Day01(private val input: String) : Day() {
    override fun part1(): Long {
        return input.lines().sumOf { line ->
            val digits = line.filter { it.isDigit() }.map { it.digitToInt().toLong() }
            digits.first() * 10 + digits.last()
        }
    }

    override fun part2(): Long {

        return input.lines().sumOf { line ->
            val first = stringToInt(Regex("(1|one|2|two|3|three|4|four|5|five|6|six|7|seven|8|eight|9|nine).*?").find(line)!!.value)
            val last = stringToInt(Regex("(1|eno|2|owt|3|eerht|4|ruof|5|evif|6|xis|7|neves|8|thgie|9|enin).*?").find(line.reversed())!!.value)
            first * 10 + last
        }
    }

    private fun stringToInt(input: String): Long{
        return when (input) {
            "one" -> 1
            "two" -> 2
            "three" -> 3
            "four" -> 4
            "five" -> 5
            "six" -> 6
            "seven" -> 7
            "eight" -> 8
            "nine" -> 9
            "eno"-> 1
            "owt" -> 2
            "eerht" -> 3
            "ruof" -> 4
            "evif" -> 5
            "xis" -> 6
            "neves" -> 7
            "thgie" -> 8
            "enin" -> 9
            else -> input.toLong()
        }
    }

}