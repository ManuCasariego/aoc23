package day17

import Day
import day16.Day16.*
import java.util.*


class Day17(private val input: String) : Day() {
    override fun part1(): Long {
        // we start top left and the goal is to reach bottom right minimizing heat loss
        // we cannot turn 180 degrees
        // we can keep going forward, turn left or turn right
        // we can at max do 3 steps forward in a row
        // we lose the amount of heat equal to that board position
        // what is the minimum amount of heat we can lose?

        // I would say to try BFS but if there's already a solution that can reach that position in less heat loss we
        // discard that path
        // I don't think that would work since imagine we are at 10,10 and there's another way that reacheed 10,10
        // but the right path is to keep goign left from there and the one that reached 10,10 can't since they already did 3 steps left

        // there's "only" 140x140 positions,
        // let's try djikstra's algorithm

        return solution { crucible:Crucible -> crucible.possibleDirectionsToMovePart1()}
    }


    private fun solution (functionToGetDirections : (Crucible) -> Set<Pair<Direction, Int>>):Long{
        val board = Utils.Board.fromStringLines(input.lines()) { it.digitToInt() }

        // create a min heap map to store min positions
        // we can use a priority queue for this

        val queue = PriorityQueue<Crucible> { a, b -> a.heatLoss.compareTo(b.heatLoss) }
        queue.add(Crucible(0, 0, 0, Direction.EAST, 0))

        val statesSet = mutableSetOf<State>()

        while (!queue.isEmpty()) {
            val crucible = queue.poll()
            if (crucible.x == board.maxX && crucible.y == board.maxY) {
                return crucible.heatLoss.toLong()
            }
            val directions = functionToGetDirections(crucible)
            directions.forEach { (direction, steps) ->
                val (dx, dy) = when (direction) {
                    Direction.NORTH -> Pair(0, -1)
                    Direction.EAST -> Pair(1, 0)
                    Direction.SOUTH -> Pair(0, 1)
                    Direction.WEST -> Pair(-1, 0)
                }
                val nextPos = Pair(crucible.x + dx, crucible.y + dy)
                val state = State(nextPos.first, nextPos.second, direction, steps)
                if (board.inBounds(nextPos.first, nextPos.second) && !statesSet.contains(state)) {
                    statesSet.add(state)
                    val nextHeatLoss = crucible.heatLoss + board.board[nextPos.second][nextPos.first]
                    queue.add(Crucible(nextPos.first, nextPos.second, nextHeatLoss, direction, steps))
                }
            }
        }

        return 0L
    }
    override fun part2(): Long {
        return solution { crucible:Crucible -> crucible.possibleDirectionsToMovePart2()}
    }

    data class State(val x: Int, val y: Int, val direction: Direction, val steps: Int)
    data class Crucible(val x: Int, val y: Int, val heatLoss: Int, val direction: Direction, val steps: Int) {
        fun possibleDirectionsToMovePart1(): Set<Pair<Direction, Int>> {
            return listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST).filter { direction ->
                direction != this.direction.oposite()
            }.filter { direction ->
                // we can only move 3 steps in a row
                if (this.steps == 3) direction != this.direction
                else true
            }.map { direction ->
                if (direction == this.direction) Pair(direction, this.steps + 1)
                else Pair(direction, 1)
            }.toSet()
        }

        fun possibleDirectionsToMovePart2(): Set<Pair<Direction, Int>> {
            // special case for the first move, when we start I can only go east on this part but if steps is 0 we can go any direction
            if (this.steps == 0) return setOf(
                Pair(Direction.NORTH, 1),
                Pair(Direction.EAST, 1),
                Pair(Direction.SOUTH, 1),
                Pair(Direction.WEST, 1)
            )

            return listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST).filter { direction ->
                direction != this.direction.oposite()
            }.filter { direction ->
                // we can only turn if we have done 4 steps or more
                if (this.steps < 4) direction == this.direction
                else true
            }.filter { direction ->
                // it can move a max of 10 steps facing the same direction
                if (this.steps == 10) direction != this.direction
                else true
            }.map { direction ->
                if (direction == this.direction) Pair(direction, this.steps + 1)
                else Pair(direction, 1)
            }.toSet()
        }
    }
}