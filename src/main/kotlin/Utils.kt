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
        val rows: List<List<T>> get() = board
        val columns: List<List<T>> get() = board[0].indices.map { col -> board.map { row -> row[col] } }

        val minX: Int get() = 0
        val maxX: Int get() = if (board.isNotEmpty()) board[0].size - 1 else 0
        val minY: Int get() = 0
        val maxY: Int get() = board.size - 1
        fun inBounds(x: Int, y: Int): Boolean {
            return y in board.indices && x in board[y].indices
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


}