package day23

import Day
import Utils.Point2D
import Utils.Board
import Utils.Direction.NORTH
import Utils.Direction.SOUTH
import Utils.Direction.WEST
import Utils.Direction.EAST


class Day23(private val input: String) : Day() {
    override fun part1(): Long {
        return solveDay(slippery = false)
    }


    override fun part2(): Long {
        return solveDay(slippery = true)
    }

    private fun solveDay(slippery: Boolean):Long{
        val board = Board.fromStringLines(input.lines())
        val start = Point2D(board.board[0].indexOf('.'), 0)
        val end = Point2D(board.board[board.maxY].indexOf('.'), board.maxY)

        val smallGraph = buildSmallGraph(board, start, end, slippery = slippery)

        // now we need to find the shortest path from start to end
        return dfs(start, end, smallGraph, slippery = slippery)
    }

    private fun getPossibleDirections(
        board: Board<Char>, curr: Point2D, visited: MutableSet<Point2D> = mutableSetOf(), slippery: Boolean = false
    ): Sequence<Point2D> {
        var directions = Utils.Direction.entries.asSequence()
        // we filter if it's not slippery
        directions = if (slippery) {
            directions
        } else {
            directions.filter { dir ->
                when (board.get(curr)) {
                    '^' -> dir == NORTH
                    'v' -> dir == SOUTH
                    '<' -> dir == WEST
                    '>' -> dir == EAST
                    else -> true
                }
            }
        }
        return directions
            .map { dir -> curr.move(dir) }
            .filterNot { it in visited }
            .filter { board.inBounds(it) }
            .filterNot { board.get(it) == '#' }
    }

    private fun buildSmallGraph(
        board: Board<Char>,
        start: Point2D,
        end: Point2D,
        slippery: Boolean = false
    ): Map<Point2D, Map<Point2D, Int>> {
        val graph = mutableMapOf<Point2D, MutableMap<Point2D, Int>>()
        val nodes = mutableSetOf<Point2D>()
        board.board.forEachIndexed { y, row ->
            row.forEachIndexed { x, c ->
                if (c != '#') {
                    val curr = Point2D(x, y)
                    val howManyPossibleDirections = getPossibleDirections(board, curr, slippery = slippery).count()
                    if (howManyPossibleDirections > 2) {
                        nodes.add(curr)
                    }
                }
            }
        }

        // I should add the start and end nodes
        nodes.add(start)
        nodes.add(end)

        nodes.forEach { node ->
            // for each node I want to send a BFS to find the distance to all other connected nodes from node
            val distances = mutableMapOf<Point2D, Int>()
            val queue = ArrayDeque<Pair<Point2D, Int>>()
            queue.add(node to 0)
            val visited = mutableSetOf<Point2D>()
            while (queue.isNotEmpty()) {
                val (curr, steps) = queue.removeFirst()
                if (curr in visited) continue
                visited.add(curr)
                if (curr in nodes && curr != node) {
                    // we found another node
                    distances[curr] = steps
                } else {
                    getPossibleDirections(board, curr, slippery = slippery).forEach {
                        queue.add(it to steps + 1)
                    }
                }
            }
            graph[node] = distances
        }
        return graph
    }

    private fun dfs(
        curr: Point2D,
        end: Point2D,
        graph: Map<Point2D, Map<Point2D, Int>>,
        steps: Long = 0L,
        visited: MutableSet<Point2D> = mutableSetOf(),
        slippery: Boolean = false
    ): Long {
        if (curr == end) return steps
        var max = 0L

        visited.add(curr)
        graph[curr]!!.filterNot { it.key in visited }.forEach {
            max = dfs(it.key, end, graph, steps + it.value, visited, slippery).coerceAtLeast(max)
        }
        visited.remove(curr)
        return max
    }
}