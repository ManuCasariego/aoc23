package day21

import Day
import Utils.Board
import Utils.Direction
import Utils.Point2D
import kotlin.math.max

class Day21(private val input: String) : Day() {
    override fun part1(): Long {
        //He gives you an up-to-date map (your puzzle input) of his starting position (S), garden plots (.), and rocks (#). For example:
        //he'd like to know which garden plots he can reach with exactly his remaining 64 steps.
        val board = Board.fromStringLines(input.lines())

        // the starting position is the S in the board
        return bfs(board.getPosition('S'), 64, board).toLong()
    }

    override fun part2(): Long {
        val board = Board.fromStringLines(input.lines())

        // corners, n of each
        val corner1 = bfs(Point2D(0, 0), 64, board)
        val corner2 = bfs(Point2D(130, 130), 64, board)
        val corner3 = bfs(Point2D(0, 130), 64, board)
        val corner4 = bfs(Point2D(130, 0), 64, board)
        val cornerSum = corner1 + corner2 + corner3 + corner4

        // weird halves, 1 of each
        val half1 = bfs(Point2D(0, 65), 130, board)
        val half2 = bfs(Point2D(130, 65), 130, board)
        val half3 = bfs(Point2D(65, 0), 130, board)
        val half4 = bfs(Point2D(65, 130), 130, board)
        val halfSum = half1 + half2 + half3 + half4

        // another set of weird halves, n-1 of each
        val weirdHalf1 = bfs(Point2D(0, 0), 195, board)
        val weirdHalf2 = bfs(Point2D(130, 130), 195, board)
        val weirdHalf3 = bfs(Point2D(0, 130), 195, board)
        val weirdHalf4 = bfs(Point2D(130, 0), 195, board)
        val weirdHalfSum = weirdHalf1 + weirdHalf2 + weirdHalf3 + weirdHalf4

        val evenBoardPositions = bfs(Point2D(0, 0), 260, board) // 7262
        val oddBoardPositions = bfs(Point2D(0, 0), 259, board) // 7255

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
    private fun bfs(s: Point2D, board: Board<Char>): Int {
        val visited = mutableSetOf<Point2D>()
        val queue = mutableListOf(Pair(s, 0))
        var maxDistance = 0
        while (queue.isNotEmpty()) {
            val (position, distance) = queue.removeAt(0)
            maxDistance = max(distance, maxDistance)
            if (visited.contains(position)) continue
            visited.add(position)

            listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST).map { direction ->
                position.move(direction)
            }.filter { board.inBounds(it) && board.get(it) != '#' }
                .forEach {
                    queue.add(Pair(it, distance + 1))
                }
        }
        return maxDistance
    }


    // returns the number of positions you can reach in steps steps
    private fun bfs(s: Point2D, steps: Int, board: Board<Char>): Int {

        val queue = mutableListOf<Pair<Point2D, Int>>()
        val visited = mutableSetOf<Point2D>()
        val solution = mutableSetOf<Point2D>()
        queue.add(Pair(s, 0))

        while (queue.isNotEmpty()) {
            val (position, distance) = queue.removeFirst()
            if (distance > steps) break
            if (visited.contains(position)) continue
            visited.add(position)
            if (distance % 2 == steps % 2) {
                solution.add(position)
            }
            listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST).map { direction ->
                position.move(direction)
            }.filter { board.inBounds(it) && board.get(it) != '#' }
                .forEach { queue.add(Pair(it, distance + 1)) }
        }
        return solution.size
    }
}