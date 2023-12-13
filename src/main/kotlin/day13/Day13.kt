package day13

import Day
import Utils.splitByEmpty
import Utils.transpose

class Day13(private val input: String) : Day() {

    override fun part1(): Long {
        val patternBoards = buildPatternBoards()
        return patternBoards.sumOf { patternBoard ->
            getAllSolutionsForPatternBoard(patternBoard).first()
        }
    }

    private fun isSolutionPossible(elements: IntRange, columns: List<String>): Boolean {
        return if (elements.count() % 2 == 0) {
            return elements.all { index ->
                val mirroredIndex = elements.last - (index - elements.first)
                columns[index] == columns[mirroredIndex]
            }
        } else false
    }

    override fun part2(): Long {
        val patternBoards = buildPatternBoards()

        return patternBoards.sumOf { patternBoard ->
            val solutions = getAllSolutionsForPatternBoard(patternBoard)
            (0..(patternBoard.size * patternBoard.first().length)).forEach { index ->
                val mutatedPatternBoard = patternBoard.mapIndexed { lineIndex, line ->
                    line.mapIndexed { columnIndex, value ->
                        if (lineIndex * line.length + columnIndex == index) {
                            if (value == '.') '#' else '.'
                        } else value
                    }
                }.map { it.joinToString("") }

                val mutatedSolution =
                    getAllSolutionsForPatternBoard(mutatedPatternBoard).filter { it != solutions.first() }
                if (mutatedSolution.isNotEmpty()) {
                    return@sumOf mutatedSolution.first()
                }
            }
            0L
        }
    }

    private fun indicesOfMatchInLine(line: String, toSearchList: List<String>): List<Int> {
        return toSearchList.mapIndexed { index, toSearchLine ->
            if (line == toSearchLine) index
            else -1
        }.filter { it != -1 }
    }

    private fun getAllSolutionsForPatternBoard(patternBoard: List<String>): List<Long> {
        return (getTotalHorizontal(patternBoard).map { it * 100L } + getTotalHorizontal(patternBoard.transpose()))
    }

    private fun getTotalHorizontal(patternBoard: List<String>): List<Long> {
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

    private fun buildPatternBoards(): List<List<String>> {
        return input.lines().splitByEmpty()
    }
}