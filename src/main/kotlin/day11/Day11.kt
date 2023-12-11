package day11

import Day
import kotlin.math.max
import kotlin.math.min

class Day11(private val input: String) : Day() {

    private fun getGalaxiesList(): List<Pair<Long, Long>> {
        val galaxiesList = mutableListOf<Pair<Long, Long>>()
        input.lines().mapIndexed { y, line ->
            line.mapIndexed { x, c ->
                if (c == '#') {
                    galaxiesList.add(Pair(x.toLong(), y.toLong()))
                }
            }
        }
        return galaxiesList
    }

    private fun getEmptyYs(): List<Long> {
        return input.lines().mapIndexed { y, line ->
            if (!line.contains("#")) y.toLong() else -1
        }.filter { it != -1L }
    }


    private fun getEmptyXs(): List<Long> {
        return (input.lines()[0].indices).map { x ->
            val numberOfStarsInX = input.lines().count { it[x] == '#' }
            if (numberOfStarsInX == 0) x.toLong() else -1L
        }.filter { it != -1L }
    }

    private fun getDistances(valueOfEmpty: Int): List<Long> {
        val galaxyList = getGalaxiesList()

        val emptyYs = getEmptyYs()
        val emptyXs = getEmptyXs()

        // now we need to get every possible distance between the galaxies
        val galaxiesDiscardSet = mutableSetOf<Pair<Long, Long>>()
        return galaxyList.map { galaxyA ->
            galaxiesDiscardSet.add(galaxyA)
            galaxyList.filterNot { it in galaxiesDiscardSet }.sumOf { galaxyB ->
                val smallX = min(galaxyA.first, galaxyB.first)
                val largeX = max(galaxyA.first, galaxyB.first)
                val xDistance = (LongRange(smallX, largeX - 1).count()) +
                        LongRange(
                            smallX,
                            largeX
                        ).count { it in emptyXs }.toLong() * (valueOfEmpty - 1)
                val smallY = min(galaxyA.second, galaxyB.second)
                val largeY = max(galaxyA.second, galaxyB.second)
                val yDistance = (LongRange(smallY, largeY - 1).count()) +
                        LongRange(
                            smallY,
                            largeY
                        ).count { it in emptyYs }.toLong() * (valueOfEmpty - 1)
                xDistance + yDistance
            }
        }
    }

    override fun part1(): Long {
        return getDistances(2).sum()
    }

    override fun part2(): Long {
        return getDistances(1000000).sum()
    }

}