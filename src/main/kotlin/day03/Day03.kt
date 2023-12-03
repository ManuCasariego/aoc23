package day03

import Day

class Day03(private val input: String) : Day() {

    override fun part1(): Int {
        val board: List<List<Char>> = input.lines().map { line ->
            // transform line to list of chars
            line.toCharArray().toList()
        }
        var sum = 0
        // iterate and get numbers
        input.lines().forEachIndexed { y, line ->
            Regex("\\d+").findAll(line).forEach { matchResult ->
                val number = matchResult.value.toInt()
                // now we have to check if the number is adjacent to any symbol other than period (.)
                // we can check the 8 adjacent squares per digit

                val isValid = matchResult.range.any { range ->
                    isSymbolAdjacentToPosition(board, range, y)
                }
                if (isValid) sum += number
            }


        }
        return sum

    }

    override fun part2(): Int {

        val board: List<List<Char>> = input.lines().map { line ->
            // transform line to list of chars
            line.toCharArray().toList()
        }
        // iterate and get numbers

        val auxMap = mutableMapOf<Pair<Int, Int>, List<Triple<Int, Int, Int>>>()
        // the triple in auxmap will include x, y and the actual number
        input.lines().forEachIndexed { y, line ->
            Regex("\\d+").findAll(line).forEach { matchResult ->
                val number = matchResult.value.toInt()
                // now we have to check if the number is adjacent to any symbol other than period (.)
                // we can check the 8 adjacent squares per digit

                matchResult.range.forEach { rangeX ->
                    if (isSymbolAdjacentToPosition(board, rangeX, y)) {
                        whatSymbolIsAdjacentToPosition(board, rangeX, y).forEach { triple ->
                            // tre triples are the symbol, x and y, x and y need to be accessed the other way around
                            if (triple.first == '*') auxMap[Pair(triple.second, triple.third)] =
                                auxMap[Pair(triple.second, triple.third)]?.plus(
                                    Triple(
                                        matchResult.range.first,
                                        y,
                                        number
                                    )
                                ) ?: listOf(Triple(matchResult.range.first, y, number))

                        }
                    }
                }


            }


        }
        // to check
        val a = auxMap.map { (_, list) ->
            if (list.distinct().size == 2) {
                list.distinct()[0].third * list.distinct()[1].third
            } else {
                0
            }
        }
        return a.sum()

    }

    private fun isSymbolAdjacentToPosition(board: List<List<Char>>, x: Int, y: Int): Boolean {
        // calculate all 8 adjacent positions
        val positions = (-1..1).flatMap { dy ->
            (-1..1).map { dx ->
                Pair(
                    (x + dx).coerceAtLeast(0).coerceAtMost(board[0].size - 1),
                    (y + dy).coerceAtLeast(0).coerceAtMost(board.size - 1)
                )
            }
        }.distinct()
        return positions.map { (board[it.second][it.first].isSymbol()) }.any { it }
    }

    private fun whatSymbolIsAdjacentToPosition(board: List<List<Char>>, x: Int, y: Int): List<Triple<Char, Int, Int>> {
        // calculate all 8 adjacent positions
        val positions = (-1..1).flatMap { dy ->
            (-1..1).map { dx ->
                Pair(
                    (x + dx).coerceAtLeast(0).coerceAtMost(board[0].size - 1),
                    (y + dy).coerceAtLeast(0).coerceAtMost(board.size - 1)
                )
            }
        }.distinct()
        return positions.filter { (board[it.second][it.first].isSymbol()) }
            .map { Triple(board[it.second][it.first], it.first, it.second) }
    }


    private fun Char.isSymbol(): Boolean {
        return !this.isDigit() && this != '.'
    }

}