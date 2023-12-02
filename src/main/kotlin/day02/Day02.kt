package day02

import Day
import kotlin.math.max

class Day02(private val input: String) : Day() {
    override fun part1(): Int {

        val restrictionsMap = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )

        return input.lines().sumOf { line ->
            // we have a new game
            val gameId = line.split(" ")[1].replace(":", "").toInt()
            var possible = true
            restrictionsMap.forEach { entry ->
                Regex("\\d+ ${entry.key}").findAll(line).toList().forEach { matchResult ->
                    if (matchResult.value.split(" ")[0].toInt() > entry.value) possible = false
                }
            }
            if (possible) gameId else 0
        }

    }

    override fun part2(): Int {

        return input.lines().sumOf { line ->
            val maxColourMap = mutableMapOf(
                "red" to 1,
                "blue" to 1,
                "green" to 1
            )
            maxColourMap.keys.forEach { key ->
                maxColourMap[key] =
                    max(maxColourMap[key]!!, Regex("\\d+ $key").findAll(line).toList().maxOf { matchResult ->
                        matchResult.value.split(" ")[0].toInt()
                    })
            }
            maxColourMap.values.fold(1) { a, b -> a * b }.toInt()
        }

    }
}