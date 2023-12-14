package day14

import Day

class Day14(private val input: String) : Day() {
    private val maxX = input.lines().first().length
    private val maxY = input.lines().size
    override fun part1(): Long {
        //he rounded rocks (O) will roll when the platform is tilted,
        // while the cube-shaped rocks (#) will stay in place.
        // You note the positions of all of the empty spaces (.)
        val (roundRocks, cubeRocks) = parseRocks()
        val newRoundRocks = moveRoundRocksNorth(roundRocks, cubeRocks)
        return calculateValue(newRoundRocks)
    }

    private fun parseRocks(): Pair<MutableSet<Rock>, MutableSet<Rock>> {
        val roundRocks = mutableSetOf<Rock>()
        val cubeRocks = mutableSetOf<Rock>()

        input.lines().forEachIndexed { dy, line ->
            line.forEachIndexed { dx, c ->
                when (c) {
                    '.' -> null
                    'O' -> roundRocks.add(Rock(dx, dy))
                    '#' -> cubeRocks.add(Rock(dx, dy))
                    else -> throw IllegalArgumentException("Unknown char $c")
                }
            }
        }
        return Pair(roundRocks, cubeRocks)
    }

    data class Rock(val x: Int, val y: Int)

    override fun part2(): Long {

        var (roundRocks, cubeRocks) = parseRocks()
        val statesMap = mutableMapOf<Set<Rock>, Int>()

        val total = 1_000_000_000
        var cycleLength = 0
        var currentIteration = 0
        while (currentIteration < total) {
            if (roundRocks in statesMap) {
                cycleLength = currentIteration - statesMap[roundRocks]!!
                break
            }
            statesMap[roundRocks] = currentIteration
            roundRocks = do1Cycle(roundRocks, cubeRocks).toMutableSet()
            currentIteration++
        }
        if (cycleLength > 0) {
            val remainingCycles = (total - currentIteration) % cycleLength
            (1..remainingCycles).forEach { _ ->
                roundRocks = do1Cycle(roundRocks, cubeRocks).toMutableSet()
            }

        }
        return calculateValue(roundRocks)
    }

    private fun calculateValue(roundRocks: Set<Rock>): Long {
        return roundRocks.sumOf { rock -> maxY - rock.y }.toLong()
    }

    private fun moveRoundRocksNorth(roundRocks: Set<Rock>, cubeRocks: Set<Rock>): Set<Rock> {
        val newRoundRocks = mutableSetOf<Rock>()
        roundRocks.sortedBy { it.y }.forEach { roundRock ->
            var newY = roundRock.y
            var canContinue = true
            while (canContinue) {
                newY--
                val potentialRock = Rock(roundRock.x, newY)
                if (newY < 0 || cubeRocks.contains(potentialRock) || newRoundRocks.contains(potentialRock)) {
                    canContinue = false
                    newRoundRocks.add(Rock(roundRock.x, newY + 1))
                }
            }
        }
        return newRoundRocks.toSet()
    }

    private fun moveRoundRocksSouth(roundRocks: Set<Rock>, cubeRocks: Set<Rock>): Set<Rock> {
        val newRoundRocks = mutableSetOf<Rock>()
        roundRocks.sortedByDescending { it.y }.forEach { roundRock ->
            var newY = roundRock.y
            var canContinue = true
            while (canContinue) {
                newY++
                val potentialRock = Rock(roundRock.x, newY)
                if (newY >= maxY || cubeRocks.contains(potentialRock) || newRoundRocks.contains(
                        potentialRock
                    )
                ) {
                    canContinue = false
                    newRoundRocks.add(Rock(roundRock.x, newY - 1))
                }
            }
        }
        return newRoundRocks.toSet()
    }

    private fun moveRoundRocksEast(roundRocks: Set<Rock>, cubeRocks: Set<Rock>): Set<Rock> {
        val newRoundRocks = mutableSetOf<Rock>()
        roundRocks.sortedByDescending { it.x }.forEach { roundRock ->
            var newX = roundRock.x
            var canContinue = true
            while (canContinue) {
                newX++
                val potentialRock = Rock(newX, roundRock.y)
                if (newX >= maxX || cubeRocks.contains(potentialRock) || newRoundRocks.contains(
                        potentialRock
                    )
                ) {
                    canContinue = false
                    newRoundRocks.add(Rock(newX - 1, roundRock.y))
                }
            }
        }
        return newRoundRocks.toSet()
    }

    private fun moveRoundRocksWest(roundRocks: Set<Rock>, cubeRocks: Set<Rock>): Set<Rock> {
        val newRoundRocks = mutableSetOf<Rock>()
        roundRocks.sortedBy { it.x }.forEach { roundRock ->
            var newX = roundRock.x
            var canContinue = true
            while (canContinue) {
                newX--
                val potentialRock = Rock(newX, roundRock.y)
                if (newX < 0 || cubeRocks.contains(potentialRock) || newRoundRocks.contains(
                        potentialRock
                    )
                ) {
                    canContinue = false
                    newRoundRocks.add(Rock(newX + 1, roundRock.y))
                }
            }
        }
        return newRoundRocks.toSet()
    }

    private fun do1Cycle(roundRocks: Set<Rock>, cubeRocks: Set<Rock>): Set<Rock> {
        return moveRoundRocksEast(
            moveRoundRocksSouth(
                moveRoundRocksWest(
                    moveRoundRocksNorth(
                        roundRocks,
                        cubeRocks
                    ), cubeRocks
                ), cubeRocks
            ), cubeRocks
        )
    }
}


