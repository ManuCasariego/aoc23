package day11

import Day
import kotlin.math.max
import kotlin.math.min

class Day11(private val input: String) : Day() {
    override fun part1(): Long {
        val galaxyList = getGalaxiesList()

        val emptyYs = getEmptyYs()
        val emptyXs = getEmptyXs()

        // now we need to get every possible distance between the galaxies
        val galaxiesDiscardSet = mutableSetOf<Pair<Long, Long>>()
        val distances = galaxyList.map { galaxyA ->
            galaxiesDiscardSet.add(galaxyA)
            galaxyList.filter { galaxiesDiscardSet.contains(it) }.sumOf { galaxyB ->
                val smallX = min(galaxyA.first, galaxyB.first)
                val largeX = max(galaxyA.first, galaxyB.first)
                val xDistance = (LongRange(smallX, largeX - 1).count()) +
                        LongRange(
                            smallX,
                            largeX
                        ).count { it in emptyXs }.toLong()
                val smallY = min(galaxyA.second, galaxyB.second)
                val largeY = max(galaxyA.second, galaxyB.second)
                val yDistance = (LongRange(smallY, largeY - 1).count()) +
                        LongRange(
                            smallY,
                            largeY
                        ).count { it in emptyYs }.toLong()
                xDistance + yDistance
            }
        }

        return distances.sum()
    }

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


    override fun part2(): Long {
        val galaxyList = getGalaxiesList()
        // now let's get the empty Ys
        val emptyYs = getEmptyYs()
        val emptyXs = getEmptyXs()

        // now we need to get every possible distance between the galaxies
        val galaxiesDiscardSet = mutableSetOf<Pair<Long, Long>>()
        val distances = galaxyList.map { galaxyA ->
            galaxiesDiscardSet.add(galaxyA)
            galaxyList.filter { galaxiesDiscardSet.contains(it) }.sumOf { galaxyB ->
                val smallX = min(galaxyA.first, galaxyB.first)
                val largeX = max(galaxyA.first, galaxyB.first)
                val xDistance = (LongRange(smallX, largeX - 1).count()) +
                        LongRange(
                            smallX,
                            largeX
                        ).count { it in emptyXs }.toLong() * 999999
                val smallY = min(galaxyA.second, galaxyB.second)
                val largeY = max(galaxyA.second, galaxyB.second)
                val yDistance = (LongRange(smallY, largeY - 1).count()) +
                        LongRange(
                            smallY,
                            largeY
                        ).count { it in emptyYs }.toLong() * 999999
                xDistance + yDistance
            }
        }
        return distances.sum()
    }

    private fun getEmptyYs(): List<Long> {
        return input.lines().mapIndexed { y, line ->
            if (!line.contains("#")) y.toLong() else -1
        }.filter { it != -1L }
    }


    private fun getEmptyXs(): List<Long> {
        return (0..<input.lines()[0].length).map { x ->
            val numberOfStartsInX = input.lines().map { it[x] }.count { it == '#' }
            if (numberOfStartsInX == 0) x.toLong() else -1L
        }.filter { it != -1L }
    }
}