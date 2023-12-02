package day02

import Day
import kotlin.math.max

class Day02(private val input: String) : Day() {
    override fun part1(): Int {

        var sumOfPossibleGames = 0

        // restrictions
        val maxRed = 12
        val maxGreen = 13
        val maxBlue = 14

        input.lines().map { line ->
            // we have a new game
            val gameId = line.split(" ")[1].replace(":", "").toInt()
            var possible = true
            Regex("\\d+ red").findAll(line).toList().forEach { matchResult ->
                if (matchResult.value.split(" ")[0].toInt() > maxRed) possible = false
            }
            Regex("\\d+ blue").findAll(line).toList().forEach { matchResult ->
                if (matchResult.value.split(" ")[0].toInt() > maxBlue) possible = false
            }
            Regex("\\d+ green").findAll(line).toList().forEach { matchResult ->
                if (matchResult.value.split(" ")[0].toInt() > maxGreen) possible = false
            }

            // if the game is possible then add it to the sum
            if (possible) sumOfPossibleGames += gameId
        }

        return sumOfPossibleGames
    }

    override fun part2(): Int {

        // restrictions

        return input.lines().sumOf { line ->
            var maxRed = 1
            var maxGreen = 1
            var maxBlue = 1
            // we have a new game
            val gameId = line.split(" ")[1].replace(":", "").toInt()
            var possible = true
            maxRed = max(maxRed, Regex("\\d+ red").findAll(line).toList().maxOf { matchResult ->
                matchResult.value.split(" ")[0].toInt()
            })
            maxGreen = max(maxGreen, Regex("\\d+ green").findAll(line).toList().maxOf { matchResult ->
                matchResult.value.split(" ")[0].toInt()
            })
            maxBlue = max(maxBlue, Regex("\\d+ blue").findAll(line).toList().maxOf { matchResult ->
                matchResult.value.split(" ")[0].toInt()
            })
            maxRed * maxGreen * maxBlue
        }

    }
}