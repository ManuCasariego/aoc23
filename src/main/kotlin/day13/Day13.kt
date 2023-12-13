package day13

import Day

class Day13(private val input: String) : Day() {

    override fun part1(): Long {
        val patternBoards = buildPatternBoards()
        return patternBoards.sumOf { patternBoard ->
            getAllSolutionsForPatternBoard(patternBoard).first()
        }
    }

    private fun isSolutionPossible(elements: IntRange, columns: List<List<Int>>): Boolean {
        return if (elements.count() % 2 == 0) {
            return elements.all { index ->
                val mirroredIndex = elements.last - (index - elements.first)
                columns[index] == columns[mirroredIndex]
            }
        } else false
    }

    private fun transpose(patternBoard: List<List<Int>>): List<List<Int>> {
        val columns = mutableListOf<List<Int>>()
        (0..<patternBoard.first().size).forEach { index ->
            columns.add(patternBoard.map { line -> line[index] })
        }
        return columns
    }

    override fun part2(): Long {
        val patternBoards = buildPatternBoards()

        return patternBoards.sumOf { patternBoard ->
            val solutions = getAllSolutionsForPatternBoard(patternBoard)
            (0..(patternBoard.size * patternBoard.first().size)).forEach { index ->
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
                    return@sumOf mutatedSolution.first()
                }
            }
            0L
        }
    }

    private fun indicesOfMatchInLine(line: List<Int>, toSearchList: List<List<Int>>): List<Int> {
        return toSearchList.mapIndexed { index, toSearchLine ->
            if (line == toSearchLine) index
            else -1
        }.filter { it != -1 }
    }

    private fun getAllSolutionsForPatternBoard(patternBoard: List<List<Int>>): List<Long> {
        return (getTotalHorizontal(patternBoard).map { it * 100L } + getTotalHorizontal(transpose(patternBoard)))
    }

    private fun getTotalHorizontal(patternBoard: List<List<Int>>): List<Long> {
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
        return (horizontalLastLineRanges + horizontalFirstLineRanges).filter { elements ->
            isSolutionPossible(elements, columns = patternBoard)
        }.map { elements ->
            (elements.count() / 2 + elements.first).toLong()
        }
    }

    private fun buildPatternBoards(): List<List<List<Int>>> {
        val patterns = input.split("\n\n")
        return patterns.map { pattern ->
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
    }
}