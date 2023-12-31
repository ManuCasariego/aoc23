package day17

import Day
import Utils.Direction
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
        // I don't think that would work since imagine we are at 10,10 and there's another way that reached 10,10
        // but the right path is to keep going left from there and the one that reached 10,10 can't since they already did 3 steps left

        // there's "only" 140x140 positions,
        // let's try djikstra's algorithm

        return solution(maxStepsSameDirection = 3, turningRestriction = 0)
    }


    private fun solution(
        maxStepsSameDirection: Int,
        turningRestriction: Int = 0,
    ): Long {
        val board = Utils.Board.fromStringLines(input.lines()) { it.digitToInt() }

        // create a min heap map to store min positions
        // we can use a priority queue for this

        val queue = PriorityQueue<Crucible> { a, b -> a.heatLoss.compareTo(b.heatLoss) }
        queue.add(Crucible(0, 0, 0, Direction.EAST, 0))

        val statesSet = mutableSetOf<State>()

        while (!queue.isEmpty()) {
            val crucible = queue.poll()
            if (crucible.x == board.maxX && crucible.y == board.maxY && crucible.steps >= turningRestriction) {
                // turning restriction and max steps same direction restriction are the same
                return crucible.heatLoss.toLong()
            }
            val directions = crucible.possibleDirectionsToMove(turningRestriction, maxStepsSameDirection)
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
        return solution(maxStepsSameDirection = 10, turningRestriction = 4)
    }

    data class State(val x: Int, val y: Int, val direction: Direction, val steps: Int)
    data class Crucible(val x: Int, val y: Int, val heatLoss: Int, val direction: Direction, val steps: Int) {
        fun possibleDirectionsToMove(
            turningRestriction: Int = 0,
            maxStepsSameDirection: Int,
        ): Set<Pair<Direction, Int>> {
            // special case for very first move, if steps is 0 it can turn wherever it wants (NORTH and WEST should hit the wall)
            // but I'm making this function general
            if (this.steps == 0) return setOf(
                Pair(Direction.NORTH, 1),
                Pair(Direction.EAST, 1),
                Pair(Direction.SOUTH, 1),
                Pair(Direction.WEST, 1)
            )

            return listOf(Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST).asSequence()
                .filter { direction ->
                    // no 180 turning
                    direction != this.direction.opposite()
                }.filter { direction ->
                    // turning restriction
                    if (this.steps < turningRestriction) direction == this.direction
                    else true
                }.filter { direction ->
                    // max steps to the same direction restriction
                    if (this.steps == maxStepsSameDirection) direction != this.direction
                    else true
                }.map { direction ->
                    // transforming the result to a pair of direction and steps
                    if (direction == this.direction) Pair(direction, this.steps + 1)
                    else Pair(direction, 1)
                }.toSet()
        }
    }
}