package day10

import Day
import kotlin.math.max

class Day10(private val input: String) : Day() {

    private enum class Direction {
        NORTH, SOUTH, EAST, WEST
    }

    override fun part1(): Long {
        // get S starting position
        val (sX, sY) = getSPosition(input.lines())
        val adjacentPipesToS = getAdjacentPipesToPosition(sX, sY, input.lines())

        // x, y and steps
        val queue = mutableListOf<Triple<Int, Int, Int>>()
        val positionsVisited = mutableSetOf<Pair<Int, Int>>()
        queue.addAll(adjacentPipesToS.map { Triple(it.first, it.second, 1) })
        positionsVisited.add(Pair(sX, sY))
        var maxSteps = 0
        while (queue.isNotEmpty()) {
            val (x, y, steps) = queue.removeFirst()
            maxSteps = max(steps, maxSteps)

            val adjacentLoopPositions = getAdjacentPipesToPosition(x, y, input.lines())
            queue.addAll(adjacentLoopPositions.filterNot { positionsVisited.contains(it) }
                .map { Triple(it.first, it.second, steps + 1) })
            positionsVisited.add(Pair(x, y))
        }

        return maxSteps.toLong()
    }

    private fun getSPosition(lines: List<String>): Pair<Int, Int> {
        val sX = lines.find { it.contains('S') }?.indexOf('S') ?: 0
        val sY = lines.indexOfFirst { it.contains('S') }
        return Pair(sX, sY)
    }

    private fun getAdjacentPipesToPosition(charX: Int, charY: Int, board: List<String>): List<Pair<Int, Int>> {
        val adjacentPipesToPosition = mutableListOf<Pair<Int, Int>>()
        val currentChar = board[charY][charX]
        val possibleDirections =
            when (currentChar) {
                'S' -> listOf(Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST)
                'L' -> listOf(Direction.NORTH, Direction.EAST)
                '7' -> listOf(Direction.SOUTH, Direction.WEST)
                'F' -> listOf(Direction.SOUTH, Direction.EAST)
                'J' -> listOf(Direction.NORTH, Direction.WEST)
                '|' -> listOf(Direction.NORTH, Direction.SOUTH)
                '-' -> listOf(Direction.EAST, Direction.WEST)
                else -> throw Exception("Something went wrong")
            }
        possibleDirections.forEach { direction ->
            var x = charX
            var y = charY

            when (direction) {
                Direction.NORTH -> y--
                Direction.SOUTH -> y++
                Direction.EAST -> x++
                Direction.WEST -> x--
            }

            // checks that didn't go out and needs to check that from where it comes, the next shape can take its flow
            if (y >= 0 && y < board.size && x >= 0 && x < board[0].length) {
                // shapes can be L,7,F, -, |, S or J
                if (direction == Direction.NORTH && (listOf('|', '7', 'F').contains(board[y][x])) ||
                    direction == Direction.SOUTH && (listOf('|', 'J', 'L').contains(board[y][x])) ||
                    direction == Direction.EAST && (listOf('-', '7', 'J').contains(board[y][x])) ||
                    direction == Direction.WEST && (listOf('-', 'L', 'F').contains(board[y][x]))
                ) {
                    adjacentPipesToPosition.add(Pair(x, y))
                }
            }
        }
        return adjacentPipesToPosition
    }

    override fun part2(): Long {
        val (sX, sY) = getSPosition(input.lines())

        // change the S for the actual char
        val sChar = getSChar(input.lines())
        val inputWithoutS = input.replace('S', sChar)

        // get the graph parts
        val graphParts = getGraphParts(inputWithoutS, sX, sY)

        val insidePoints = mutableSetOf<Pair<Int, Int>>()
        inputWithoutS.lines().forEachIndexed { indexY, line ->
            // indexy is y, it grews going south

            var outside = true
            line.forEachIndexed { indexX, char ->
                // indexx is x, it grews going east
                if (!graphParts.contains(Pair(indexX, indexY))) {
                    if (!outside) insidePoints.add(Pair(indexX, indexY))
                } else if (char == '|' || char == 'F' || char == '7') {
                    outside = !outside
                }
            }
        }
        return insidePoints.size.toLong()
    }

    private fun getGraphParts(inputWithoutS: String, sX: Int, sY: Int): Set<Pair<Int, Int>> {
        // I want the graph points
        val graphPoints = mutableSetOf<Pair<Int, Int>>()

        val queue = mutableListOf<Pair<Int, Int>>()
        queue.add(Pair(sX, sY))

        while (queue.isNotEmpty()) {
            val (x, y) = queue.removeFirst()
            graphPoints.add(Pair(x, y))
            val adjacentLoopPositions = getAdjacentPipesToPosition(x, y, inputWithoutS.lines())
            adjacentLoopPositions.forEach {
                if (!graphPoints.contains(it)) {
                    graphPoints.add(it)
                    queue.add(it)
                }
            }
        }
        return graphPoints
    }

    private fun getSChar(lines: List<String>): Char {
        val (sX, sY) = getSPosition(lines)
        val sAdjacentPositions = getAdjacentPipesToPosition(sX, sY, lines)

        val directions = listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST).filter { direction ->
            val x =
                if (direction == Direction.NORTH || direction == Direction.SOUTH) sX else if (direction == Direction.EAST) sX + 1 else sX - 1
            val y =
                if (direction == Direction.EAST || direction == Direction.WEST) sY else if (direction == Direction.SOUTH) sY + 1 else sY - 1
            sAdjacentPositions.contains(Pair(x, y))
        }
        return if (directions.containsAll(listOf(Direction.EAST, Direction.NORTH))) 'L'
        else if (directions.containsAll(listOf(Direction.EAST, Direction.SOUTH))) 'F'
        else if (directions.containsAll(listOf(Direction.WEST, Direction.NORTH))) 'J'
        else if (directions.containsAll(listOf(Direction.WEST, Direction.SOUTH))) '7'
        else throw Exception("Something went wrong")
    }
}