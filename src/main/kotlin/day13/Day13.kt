package day13

import Day

class Day13(private val input: String) : Day() {

    override fun part1(): Long {
        //ash (.) and rocks (#)
        //dd up the number of columns to the left of each vertical line of reflection; to that, also add 100 multiplied
        // by the number of rows above each horizontal line of reflection.
        val patterns = input.split("\n\n")
        val patternBoards = patterns.map { pattern ->
            val patternBoard = pattern.lines().map { line ->
                line.map { char ->
                    when (char) {
                        '.' -> 0
                        else -> 1
                    }
                }
            }
            patternBoard
        }

        val h = patternBoards.map { patternBoard ->
            // for each pattern board, get all possible solutions
            val solutions = getAllSolutionsForPatternBoard(patternBoard)
            solutions.first()
        }

        return h.sum()

    }

    private fun isSolutionPossible(elements: IntRange, columns: List<List<Int>>): Boolean {
        return if (elements.count() % 2 == 0) {
            // we good
            return elements.all { index ->
                val mirroredIndex = elements.last - (index - elements.first)
                columns[index] == columns[mirroredIndex]
            }
        } else false
    }

    private fun getColumns(patternBoard: List<List<Int>>): List<List<Int>> {
        val columns = mutableListOf<List<Int>>()
        (0..<patternBoard.first().size).forEach { index ->
            columns.add(patternBoard.map { line -> line[index] })
        }
        return columns
    }

    override fun part2(): Long {
        //ash (.) and rocks (#)
        // add up the number of columns to the left of each vertical line of reflection; to that, also add 100 multiplied
        // by the number of rows above each horizontal line of reflection.
        val patterns = input.split("\n\n")
        val patternBoards = patterns.map { pattern ->
            val patternBoard = pattern.lines().map { line ->
                line.map { char ->
                    when (char) {
                        '.' -> 0
                        else -> 1
                    }
                }
            }
            patternBoard
        }

        val h = patternBoards.map { patternBoard ->
            // for each pattern board, get all possible solutions
            val solutions = getAllSolutionsForPatternBoard(patternBoard)
            // now force brute to iterate through every possible value on patternboard until we find another solution
            (0..(patternBoard.size * patternBoard.first().size)).forEach { index ->
                // i got a list of list of ints, i need to mutate the value at index n to be the opposite, from 1 to 0 or 0 to 1
                val mutatedPatternBoard = patternBoard.mapIndexed { lineIndex, line ->
                    line.mapIndexed { columnIndex, value ->
                        if (lineIndex * line.size + columnIndex == index) {
                            if (value == 0) 1 else 0
                        } else value
                    }
                }
                val mutatedSolution =
                    getAllSolutionsForPatternBoard(mutatedPatternBoard).filter { it != solutions.first() }
                if (mutatedSolution.isNotEmpty()) {
                    return@map mutatedSolution.first()
                }
            }
            0L
        }

        return h.sum()
    }

    private fun indicesOfMatchInLine(line: List<Int>, toSearchList: List<List<Int>>): List<Int> {
        return toSearchList.mapIndexed { index, toSearchLine ->
            if (line == toSearchLine) index
            else -1
        }.filter { it != -1 }
    }

    fun getAllSolutionsForPatternBoard(patternBoard: List<List<Int>>): List<Long> {
        val solutions = mutableListOf<Long>()
        // horizontal last line
        val horizontalLastLineRanges =
            indicesOfMatchInLine(patternBoard.last(), patternBoard.dropLast(1)).map { index ->
                val elements = (index..<patternBoard.size)
                elements
            }

        val horizontalFirstLineRanges =
            indicesOfMatchInLine(patternBoard.first(), patternBoard.drop(1)).map { it + 1 }.map { index ->
                val elements = (0..index)
                elements
            }

        val columns = getColumns(patternBoard)

        val verticalFirstLineRanges =
            indicesOfMatchInLine(columns.first(), columns.drop(1)).map { it + 1 }.map { index ->
                val elements = (0..index)
                elements
            }

        val verticalLastLineRanges =
            indicesOfMatchInLine(columns.last(), columns.dropLast(1)).map { index ->
                val elements = (index..<columns.size)
                elements
            }

        val everythingHorizontal =
            (horizontalLastLineRanges + horizontalFirstLineRanges)
                .filter { elements ->
                    val isSolutionPossible = isSolutionPossible(elements, columns = patternBoard)
                    isSolutionPossible
                }.map { elements ->
                    (elements.count() / 2 + elements.first) * 100L
                }

        val everythingVertical = (verticalFirstLineRanges + verticalLastLineRanges)
            .filter { elements ->
                val isSolutionPossible = isSolutionPossible(elements, columns = columns)
                isSolutionPossible
            }.map { elements ->
                (elements.count() / 2 + elements.first).toLong()
            }
        solutions.addAll(everythingHorizontal)
        solutions.addAll(everythingVertical)
        return solutions
    }
}