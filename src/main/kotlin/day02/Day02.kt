package day02

import Day
import kotlin.math.max

class Day02(private val input: String) : Day() {
    override fun part1(): Long {

        val restrictionsMap = mapOf(
            "red" to 12L,
            "green" to 13L,
            "blue" to 14L
        )

        return input.lines().sumOf { line ->
            // we have a new game
            val gameId = line.split(" ")[1].replace(":", "").toLong()
            var possible = true
            restrictionsMap.forEach { entry ->
                Regex("\\d+ ${entry.key}").findAll(line).toList().forEach { matchResult ->
                    if (matchResult.value.split(" ")[0].toLong() > entry.value) possible = false
                }
            }
            if (possible) gameId else 0
        }

    }

    override fun part2(): Long {

        return input.lines().sumOf { line ->
            val maxColourMap = mutableMapOf(
                "red" to 1L,
                "blue" to 1L,
                "green" to 1L
            )
            maxColourMap.keys.forEach { key ->
                maxColourMap[key] =
                    max(maxColourMap[key]!!, Regex("\\d+ $key").findAll(line).toList().maxOf { matchResult ->
                        matchResult.value.split(" ")[0].toLong()
                    })
            }
            maxColourMap.values.fold(1L) { a, b -> a * b }.toLong()
        }

    }
}