package day01

import Day

class Day01(private val input: String) : Day() {
    override fun part1(): String {
        var level = 0
        input.forEach {
            if (it == '(') {
                level++
            } else if (it == ')')
                level--
        }
        return level.toString()
    }

    override fun part2(): String {
        var position = 1
        var level = 0
        input.forEach { char ->
            if (char == '(') {
                level++
            } else if (char == ')') {
                level--
            }
            if (level == -1) {
                return position.toString()
            }
            position++
        }
        return "Not found"
    }
}