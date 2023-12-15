package day15

import Day

class Day15(private val input: String) : Day() {
    override fun part1(): Long {
        val a = input.split(',').map { word ->
            hash(word)
        }
        return a.sum().toLong()
    }

    private fun hash(s: String): Int {
        var value = 0
        for (c in s) {
            value += c.code
            value *= 17
            value %= 256
        }
        return value
    }

    override fun part2(): Long {
        val instructions = input.split(",")
        val boxes = List<MutableList<Lens>>(256) { mutableListOf() }

        instructions.forEach { instruction ->
            val label = if (instruction.contains("=")) instruction.substringBefore("=") else instruction.substringBefore("-")
            val boxIndex = hash(label)
            val operation = instruction.substringAfterLast(label)
            if (operation.startsWith("-")) {
                // operation to remove
                boxes[boxIndex].removeIf { it.label == label }
            } else if (operation.startsWith("=")) {
                // operation to set
                val focalLength = operation.substringAfter("=").toInt()
                val lensIndex = boxes[boxIndex].indexOfFirst { it.label == label }
                if (lensIndex == -1) {
                    // not in the box yet
                    boxes[boxIndex].add(Lens(label, focalLength))
                } else {
                    // already in the box
                    boxes[boxIndex][lensIndex].focalLength = focalLength
                }
            }
        }

        var totalFocusingPower = 0
        for (i in boxes.indices) {
            for (j in boxes[i].indices) {
                totalFocusingPower += (i + 1) * (j + 1) * boxes[i][j].focalLength
            }
        }
        return totalFocusingPower.toLong()
    }


    data class Lens(val label: String, var focalLength: Int)

}