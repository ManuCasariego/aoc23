package day18

import Day
import kotlin.math.absoluteValue

class Day18(private val input: String) : Day() {
    override fun part1(): Long {

        var startingPoint = 0L to 0L
        val edges = mutableListOf<Pair<Long, Long>>()
        edges.add(startingPoint)

        input.lines().forEach { line ->
//        R 6 (#70c710)
            val split = line.split(" ")
            val directionChar = split[0]
            val numberOfMoves = split[1].toInt()
            val (dx, dy) = when (directionChar) {
                "R" -> numberOfMoves to 0
                "L" -> -numberOfMoves to 0
                "U" -> 0 to -numberOfMoves
                "D" -> 0 to numberOfMoves
                else -> throw Exception("Unknown direction $directionChar")
            }
            startingPoint = startingPoint.first + dx to startingPoint.second + dy
            edges.add(startingPoint)
        }
        return calculateArea(edges)
    }


    override fun part2(): Long {
        // The first five hexadecimal digits encode the distance in meters as a five-digit hexadecimal number.
        // The last hexadecimal digit encodes the direction to dig: 0 means R, 1 means D, 2 means L, and 3 means U.

        var startingPoint = 0L to 0L
        val edges = mutableListOf<Pair<Long, Long>>()
        edges.add(startingPoint)
        input.lines().forEach { line ->
//        R 6 (#70c710)
            val hexCode = line.substringAfter("#").substringBefore(")")
            val directionChar = hexCode.last()
            val numberOfMoves = hexCode.substring(0, hexCode.length - 1).toInt(16)
            val (dx, dy) = when (directionChar) {
                '0' -> numberOfMoves to 0
                '2' -> -numberOfMoves to 0
                '3' -> 0 to -numberOfMoves
                '1' -> 0 to numberOfMoves
                else -> throw Exception("Unknown direction $directionChar")
            }
            startingPoint = startingPoint.first + dx to startingPoint.second + dy
            edges.add(startingPoint)
        }

        // Board? Where we go we don't need boards
        return calculateArea(edges)
    }

    private fun calculateArea(edges: MutableList<Pair<Long, Long>>): Long {
        // the area part will end up having 2x total area
        // but in this case
        // ###
        // #.#
        // ###
        // the area calculation will give us 4, but we need to return 9
        // so that's where we add half the perimeter, another 4
        // and the last corner + 1

        var area = 0L
        var perimeter = 0L
        edges.windowed(2).forEach { (a, b) ->
            perimeter += (a.first - b.first).absoluteValue + (a.second - b.second).absoluteValue
            area += (a.first * b.second - a.second * b.first)
        }
        return area.absoluteValue / 2 + perimeter / 2 + 1
    }
}