package day03

import Day

class Day03(private val input: String) : Day() {

    override fun part1(): Int {
        val board: List<List<Char>> = input.lines().map { it.toCharArray().toList() }
        // iterate and get numbers
        return input.lines().mapIndexed { y, line ->
            Regex("\\d+").findAll(line).map { matchResult ->
                val number = matchResult.value.toInt()
                // now we have to check if the number is adjacent to any symbol other than period (.)
                // we can check the 8 adjacent squares per digit

                val isValid = matchResult.range.any { range ->
                    isSymbolAdjacentToPosition(board, range, y)
                }
                if (isValid) number else 0
            }.sum()
        }.sum()
    }

    override fun part2(): Int {

        val board: List<List<Char>> = input.lines().map { line ->
            // transform line to list of chars
            line.toCharArray().toList()
        }
        // iterate and get numbers

        val auxMap = mutableMapOf<Pair<Int, Int>, Set<Triple<Int, Int, Int>>>()
        // the triple in auxmap will include the actual number, x and y
        input.lines().forEachIndexed { y, line ->
            Regex("\\d+").findAll(line).forEach { matchResult ->
                val number = matchResult.value.toInt()
                // now we have to check if the number is adjacent to any symbol other than period (.)
                // we can check the 8 adjacent squares per digit

                matchResult.range.forEach { rangeX ->
                    if (isSymbolAdjacentToPosition(board, rangeX, y)) {
                        whatSymbolIsAdjacentToPosition(board, rangeX, y).forEach { triple ->
                            // tre triples are the symbol, x and y
                            if (triple.first == '*') auxMap[Pair(triple.second, triple.third)] =
                                auxMap[Pair(triple.second, triple.third)]?.plus(
                                    Triple(
                                        number,
                                        matchResult.range.first,
                                        y
                                    )
                                ) ?: setOf(Triple(number, matchResult.range.first, y))
                        }
                    }
                }
            }
        }
        // to check
        return auxMap.values.map { set ->
            if (set.size == 2) set.fold(1) { a, b -> a * b.first } else 0
        }.sum()
    }

    private fun isSymbolAdjacentToPosition(board: List<List<Char>>, x: Int, y: Int): Boolean {
        val positions = getAllAdjacentPositions(board, x, y)
        return positions.map { (board[it.second][it.first].isSymbol()) }.any { it }
    }

    private fun getAllAdjacentPositions(board: List<List<Char>>, x: Int, y: Int): List<Pair<Int, Int>> {
        // calculate all 8 adjacent positions
        return (-1..1).flatMap { dy ->
            (-1..1).map { dx ->
                Pair(
                    (x + dx).coerceAtLeast(0).coerceAtMost(board[0].size - 1),
                    (y + dy).coerceAtLeast(0).coerceAtMost(board.size - 1)
                )
            }
        }.distinct()
    }

    private fun whatSymbolIsAdjacentToPosition(board: List<List<Char>>, x: Int, y: Int): List<Triple<Char, Int, Int>> {
        // I'm returning a triplet with the first being the adjacent char, and then the coordinates of it
        val positions = getAllAdjacentPositions(board, x, y)
        return positions.filter { (board[it.second][it.first].isSymbol()) }
            .map { Triple(board[it.second][it.first], it.first, it.second) }
    }


    private fun Char.isSymbol(): Boolean {
        return !this.isDigit() && this != '.'
    }

}