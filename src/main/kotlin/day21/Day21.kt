package day21

import Day
import Utils.Direction

class Day21(private val input: String) : Day() {
    override fun part1(): Long {
        //He gives you an up-to-date map (your puzzle input) of his starting position (S), garden plots (.), and rocks (#). For example:
        //he'd like to know which garden plots he can reach with exactly his remaining 64 steps.
        val board = Utils.Board.fromStringLines(input.lines())

        // the starting position is the S in the board
        val sy = board.rows.indexOfFirst { it.contains('S') }
        val sx = board.rows[sy].indexOf('S')
        val sPosition = Pair(sx, sy)
        return bfs(sPosition, 64, board).toLong()
    }

    override fun part2(): Long {
        val board = Utils.Board.fromStringLines(input.lines())

        // corners, n of each
        val corner1 = bfs(Pair(0, 0), 64, board)
        val corner2 = bfs(Pair(130, 130), 64, board)
        val corner3 = bfs(Pair(0, 130), 64, board)
        val corner4 = bfs(Pair(130, 0), 64, board)
        val cornerSum = corner1 + corner2 + corner3 + corner4

        // weird halves, 1 of each
        val half1 = bfs(Pair(0, 65), 130, board)
        val half2 = bfs(Pair(130, 65), 130, board)
        val half3 = bfs(Pair(65, 0), 130, board)
        val half4 = bfs(Pair(65, 130), 130, board)
        val halfSum = half1 + half2 + half3 + half4

        // another set of weird halves, n-1 of each
        val weirdHalf1 = bfs(Pair(0, 0), 195, board)
        val weirdHalf2 = bfs(Pair(130, 130), 195, board)
        val weirdHalf3 = bfs(Pair(0, 130), 195, board)
        val weirdHalf4 = bfs(Pair(130, 0), 195, board)
        val weirdHalfSum = weirdHalf1 + weirdHalf2 + weirdHalf3 + weirdHalf4

        val evenBoardPositions = bfs(Pair(0, 0), 260, board) // 7262
        val oddBoardPositions = bfs(Pair(0, 0), 259, board) // 7255

        val steps = 26_501_365L
        val n = steps / board.width // 202_300

        val numberOfEvenBoards = n * n // number of even boards
        val numberOfOddBoards = (n - 1) * (n - 1) // number of odd boards
        val c = numberOfEvenBoards * evenBoardPositions + numberOfOddBoards * oddBoardPositions


        val solution = halfSum + n * cornerSum + c + (n - 1) * weirdHalfSum
        return solution
        // 1188219773749817 too high
        // 594115423713876 still too high
        // 594115391548176 right answer finally
    }


    // returns the number of steps you need to cover the full board
    private fun bfs(sPosition: Pair<Int, Int>, board: Utils.Board<Char>): Int {
        val visited = mutableMapOf<Pair<Int, Int>, Int>()
        val positionsQueue2 = mutableListOf(sPosition)
        visited[sPosition] = 0
        while (positionsQueue2.isNotEmpty()) {
            val position = positionsQueue2.removeAt(0)
            listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST).map { direction ->
                when (direction) {
                    Direction.NORTH -> Pair(position.first, position.second - 1)
                    Direction.EAST -> Pair(position.first + 1, position.second)
                    Direction.SOUTH -> Pair(position.first, position.second + 1)
                    Direction.WEST -> Pair(position.first - 1, position.second)
                }

            }.filterNot { visited.contains(it) }.filter { board.inBounds(it.first, it.second) }
                .filter { board.board[it.second][it.first] != '#' }.forEach { pair ->
                    positionsQueue2.add(pair)
                    visited[pair] = visited[position]!! + 1
                }
        }
        return visited.values.max()
    }


    // returns the number of positions you can reach in steps steps
    private fun bfs(sPosition: Pair<Int, Int>, steps: Int, board: Utils.Board<Char>): Int {

        val positionsQueue = mutableSetOf<Pair<Int, Int>>()
        positionsQueue.add(sPosition)
        val sizes = mutableListOf<Long>()
        (1..steps).forEach { _ ->
            sizes.add(positionsQueue.size.toLong())
            val newPositionsQueue = mutableSetOf<Pair<Int, Int>>()

            positionsQueue.forEach { position ->
                listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST).map { direction ->
                    when (direction) {
                        Direction.NORTH -> Pair(position.first, position.second - 1)
                        Direction.EAST -> Pair(position.first + 1, position.second)
                        Direction.SOUTH -> Pair(position.first, position.second + 1)
                        Direction.WEST -> Pair(position.first - 1, position.second)
                    }

                }.filter { board.inBounds(it.first, it.second) && board.board[it.second][it.first] != '#' }
                    .forEach {
                        newPositionsQueue.add(it)
                    }
            }
            positionsQueue.clear()
            positionsQueue.addAll(newPositionsQueue)
        }
        return positionsQueue.size

    }
}