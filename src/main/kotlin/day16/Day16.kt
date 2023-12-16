package day16

import Day
import Utils.Board

class Day16(private val input: String) : Day() {
    override fun part1(): Long {
        //empty space (.), mirrors (/ and \), and splitters (| and -).

        val board = Board.fromStringLines(input.lines())


        return numberOfBeams(board, Pair(Pair(0, 0), Direction.EAST)).toLong()
    }

    private fun numberOfBeams(board: Board<Char>, startingLight: Pair<Pair<Int, Int>, Direction>): Int {

        val intBoard = MutableList(input.lines().size) { _ ->
            MutableList(input.lines().first().length) { _ ->
                0
            }
        }
        val lightQueue = ArrayDeque<Pair<Pair<Int, Int>, Direction>>()

        var nextDirs = getNextDirs(board, startingLight)
        nextDirs.forEach {
            lightQueue.add(Pair(startingLight.first, it))
        }

        intBoard[startingLight.first.second][startingLight.first.first] = 1

        val uniqueLights = mutableSetOf<Pair<Pair<Int, Int>, Direction>>()
        while (lightQueue.isNotEmpty()) {
            val currLight = lightQueue.removeFirst()
            if (currLight in uniqueLights) {
                continue
            } else {
                uniqueLights.add(currLight)
            }
            val (pos, dir) = currLight
            val (x, y) = pos
            val (dx, dy) = when (dir) {
                Direction.NORTH -> Pair(0, -1)
                Direction.EAST -> Pair(1, 0)
                Direction.SOUTH -> Pair(0, 1)
                Direction.WEST -> Pair(-1, 0)
            }

            val nextPos = Pair(x + dx, y + dy)

            if (nextPos.first < 0 || nextPos.first >= board.board[0].size || nextPos.second < 0 || nextPos.second >= board.board.size) {
                // light is out of bounds
                continue
            } else {
                intBoard[nextPos.second][nextPos.first] = 1
            }

            nextDirs = getNextDirs(board, nextPos to dir)
            nextDirs.forEach {
                lightQueue.add(Pair(nextPos, it))
            }
        }


        return intBoard.flatten().sumOf { it }
    }

    enum class Direction {
        NORTH, EAST, SOUTH, WEST
    }


    private fun getNextDirs(board: Board<Char>, light: Pair<Pair<Int, Int>, Direction>): List<Direction> {
        return when (board.board[light.first.second][light.first.first]) {
            '/' -> when (light.second) {
                Direction.NORTH -> listOf(Direction.EAST)
                Direction.EAST -> listOf(Direction.NORTH)
                Direction.SOUTH -> listOf(Direction.WEST)
                Direction.WEST -> listOf(Direction.SOUTH)
            }

            '\\' -> when (light.second) {
                Direction.NORTH -> listOf(Direction.WEST)
                Direction.EAST -> listOf(Direction.SOUTH)
                Direction.SOUTH -> listOf(Direction.EAST)
                Direction.WEST -> listOf(Direction.NORTH)
            }
            // here we need to split into two lights
            '|' -> when (light.second) {
                Direction.NORTH -> listOf(Direction.NORTH)
                Direction.EAST -> listOf(Direction.NORTH, Direction.SOUTH)
                Direction.SOUTH -> listOf(Direction.SOUTH)
                Direction.WEST -> listOf(Direction.NORTH, Direction.SOUTH)
            }

            '-' -> when (light.second) {
                Direction.NORTH -> listOf(Direction.EAST, Direction.WEST)
                Direction.EAST -> listOf(Direction.EAST)
                Direction.SOUTH -> listOf(Direction.EAST, Direction.WEST)
                Direction.WEST -> listOf(Direction.WEST)
            }

            '.' -> when (light.second) {
                Direction.NORTH -> listOf(Direction.NORTH)
                Direction.EAST -> listOf(Direction.EAST)
                Direction.SOUTH -> listOf(Direction.SOUTH)
                Direction.WEST -> listOf(Direction.WEST)
            }

            else -> throw Exception("Unknown char ${board.board[light.first.second][light.first.first]}")

        }
    }

    override fun part2(): Long {
        val board = Board.fromStringLines(input.lines())

        val numberOfBeamsList = mutableListOf<Int>()
        for (y in 0 until board.board.size) {
            numberOfBeamsList.add(numberOfBeams(board, Pair(Pair(0, y), Direction.EAST)))
            numberOfBeamsList.add(numberOfBeams(board, Pair(Pair(board.board[0].size - 1, y), Direction.WEST)))
        }
        for (x in 0 until board.board[0].size) {
            numberOfBeamsList.add(numberOfBeams(board, Pair(Pair(x, 0), Direction.SOUTH)))
            numberOfBeamsList.add(numberOfBeams(board, Pair(Pair(x, board.board.size - 1), Direction.NORTH)))
        }

        return numberOfBeamsList.max().toLong()

    }
}