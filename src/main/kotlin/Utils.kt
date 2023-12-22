import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

object Utils {
    fun List<String>.splitByEmpty(): List<List<String>> {
        val groups = mutableListOf<List<String>>()
        var currentGroup = mutableListOf<String>()
        this.forEach { line ->
            if (line.isEmpty()) {
                groups.add(currentGroup)
                currentGroup = mutableListOf()
            } else {
                currentGroup.add(line)
            }
        }
        // Add the last group if it's not empty
        if (currentGroup.isNotEmpty()) {
            groups.add(currentGroup)
        }
        return groups
    }


    fun String.ints(delimiter: String): List<Int> {
        return this.split(delimiter).filter { it.isNotEmpty() }.map { it.toInt() }
    }

    fun String.longs(delimiter: String): List<Long> {
        return this.split(delimiter).filter { it.isNotEmpty() }.map { it.toLong() }
    }

    class Board<T>(val board: List<List<T>>) {
        val rows: List<List<T>> = board
        val columns: List<List<T>> =
            if (board.isNotEmpty()) board[0].indices.map { col -> board.map { row -> row[col] } } else emptyList()

        val minX: Int = 0
        val maxX: Int = if (board.isNotEmpty()) board[0].size - 1 else 0
        val minY: Int = 0
        val maxY: Int = board.size - 1
        val height: Int = board.size
        val width: Int = if (board.isNotEmpty()) board[0].size else 0

        fun get(x: Int, y: Int): T {
            return board[y][x]
        }

        fun get(point2D: Point2D): T {
            return get(point2D.x.toInt(), point2D.y.toInt())
        }

        fun inBounds(x: Int, y: Int): Boolean {
            return y in 0 until height && x in 0 until width
        }

        fun inBounds(point2D: Point2D): Boolean {
            return inBounds(point2D.x.toInt(), point2D.y.toInt())
        }

        fun getPosition(t: T): Point2D {
            val y = rows.indexOfFirst { it.contains(t) }
            val x = rows[y].indexOf(t)
            return Point2D(x, y)
        }

        companion object {
            fun <T> fromStringLines(lines: List<String>, converter: (Char) -> T): Board<T> {
                val board = lines.map { line ->
                    line.map { char -> converter(char) }
                }
                return Board(board)
            }

            fun fromStringLines(lines: List<String>): Board<Char> {
                return fromStringLines(lines) { it }
            }
        }
    }

    fun gcd(a: Long, b: Long): Long {
        if (b == 0L) return a
        return gcd(b, a % b)
    }

    fun lcm(a: Long, b: Long): Long {
        return a * (b / gcd(a, b))
    }

    /**
     * Takes a list of base/modulo combinations and returns the lowest number for which the states coincide such that:
     *
     * for all i: state(i) == base_state(i).
     *
     * E.g. chineseRemainder((3,4), (5,6), (2,5)) == 47
     */
    fun chineseRemainder(values: List<Pair<Long, Long>>): Long {
        if (values.isEmpty()) {
            return 0L
        }
        var result = values[0].first
        var lcm = values[0].second
        for (i in 1 until values.size) {
            val (base, modulo) = values[i]
            val target = base % modulo
            while (result % modulo != target) {
                result += lcm
            }
            lcm = lcm(lcm, modulo)
        }
        return result
    }

    fun List<String>.transpose(): List<String> {
        return this.first().indices.map { index ->
            this.map { line -> line[index] }.joinToString("")
        }
    }

    enum class Direction {
        NORTH, EAST, SOUTH, WEST;

        fun opposite(): Direction {
            return when (this) {
                NORTH -> SOUTH
                EAST -> WEST
                SOUTH -> NORTH
                WEST -> EAST
            }
        }

        fun left(): Direction {
            return when (this) {
                NORTH -> WEST
                EAST -> NORTH
                SOUTH -> EAST
                WEST -> SOUTH
            }
        }

        fun right(): Direction {
            // it looks funny
            return left().opposite()
        }
    }

    data class Point2D(val x: Long, val y: Long) {
        // create a constructor that takes two ints
        constructor(x: Int, y: Int) : this(x.toLong(), y.toLong())
        fun manhattanDistance(other: Point2D): Long {
            return abs(this.x - other.x) + abs(this.y - other.y)
        }

        // note that NORTH removes from y and SOUTH adds to y
        fun move(direction: Direction, n: Int = 1): Point2D {
            return when (direction) {
                Direction.NORTH -> Point2D(x, y - n)
                Direction.EAST -> Point2D(x + n, y)
                Direction.SOUTH -> Point2D(x, y + n)
                Direction.WEST -> Point2D(x - n, y)
            }
        }
    }

    data class Point3D(val x: Long, val y: Long, val z: Long) {
        fun manhattanDistance(other: Point3D): Long {
            return abs(this.x - other.x) + abs(this.y - other.y) + abs(this.z - other.z)
        }

        fun getListFromOneToAnother(other: Point3D): List<Point3D>{
            val middlePoints = mutableListOf<Point3D>()
            // they should be the same on two dimensions
            if (this.x == other.x && this.y == other.y) {
                val minZ = min(this.z, other.z)
                val maxZ = max(this.z, other.z)
                for (z in minZ..maxZ){
                    middlePoints.add(Point3D(x, y, z))
                }
            } else if (this.x == other.x && this.z == other.z){
                val minY = min(this.y, other.y)
                val maxY = max(this.y, other.y)
                val minZ = min(this.z, other.z)
                val maxZ = max(this.z, other.z)
                for (y in minY..maxY){
                    for (z in minZ..maxZ){
                        middlePoints.add(Point3D(x, y, z))
                    }
                }
            } else if (this.y == other.y && this.z == other.z){
                val minX = min(this.x, other.x)
                val maxX = max(this.x, other.x)
                val minZ = min(this.z, other.z)
                val maxZ = max(this.z, other.z)
                for (x in minX..maxX){
                    for (z in minZ..maxZ){
                        middlePoints.add(Point3D(x, y, z))
                    }
                }
            }
            return middlePoints
        }
    }

}