package day12

import Day

class Day12(private val input: String) : Day() {
    override fun part1(): Long {
        // ????.#...#... 4,1,1
        // reused part2 for part1 since it's more efficient
        return input.lines().sumOf { line ->
            val wantedInstructions = line.split(" ").last().ints()
            val blueprint = line.split(" ").first()

            val numberOfWays = solvePart2(blueprint, wantedInstructions, listOf(), mutableMapOf())
            numberOfWays
        }
    }

    // . -> working
    // # -> not working
    data class CustomElement(val count: Int, val spring: Spring)
    enum class Spring { WORKING, NOT_WORKING }

    //currentStructure contains the history of the currentBlueprint, for example 1 WORKING, 1 NOT WORKING, 2 WORKING, 1 NOT WORKING...
    private fun solvePart1(
        currentBlueprint: String,
        wantedInstructions: List<Int>,
        currentStructure: List<CustomElement>,
    ): Long {
        // end conditions
        if (currentBlueprint.isEmpty()) {
            return if (checkIfWeAreDone(currentStructure, wantedInstructions)) 1 else 0
        }
        if (!checkIfWeAreGoingOk(currentStructure, wantedInstructions)) {
            return 0
        }

        val currentElement = currentBlueprint.first()

        var count = 0L

        // now it depends on the current element (?), (.) working, (#) not working
        // for the . and # it's pretty straight forward, we just need to check if the last element is working or not in
        // order to append the currentElement to the right place and continue the recursion
        if (currentElement == '#') {
            // not working
            count += if (currentStructure.isEmpty() || currentStructure.last().spring == Spring.WORKING) {
                solvePart1(
                    currentBlueprint.drop(1),
                    wantedInstructions,
                    currentStructure + CustomElement(1, Spring.NOT_WORKING)
                )
            } else {
                solvePart1(
                    currentBlueprint.drop(1),
                    wantedInstructions,
                    currentStructure.dropLast(1) + CustomElement(currentStructure.last().count + 1, Spring.NOT_WORKING)
                )
            }
        } else if (currentElement == '.') {
            // working
            if (currentStructure.isEmpty() || currentStructure.last().spring == Spring.NOT_WORKING) {
                count += solvePart1(
                    currentBlueprint.drop(1),
                    wantedInstructions,
                    currentStructure + CustomElement(1, Spring.WORKING)
                )
            } else {
                count += solvePart1(
                    currentBlueprint.drop(1),
                    wantedInstructions,
                    // we don't care about how many working springs we have in a row, that's irrelevant for the solution
                    currentStructure
                )
            }
        } else if (currentElement == '?') {
            // in this case we need to try both things, to make it work and to make it not work
            // working
            count += if (currentStructure.isEmpty() || currentStructure.last().spring == Spring.NOT_WORKING) {
                solvePart1(
                    currentBlueprint.drop(1),
                    wantedInstructions,
                    currentStructure + CustomElement(1, Spring.WORKING)
                )
            } else {
                solvePart1(
                    currentBlueprint.drop(1),
                    wantedInstructions,
                    currentStructure
                )
            }

            // not working
            count += if (currentStructure.isEmpty() || currentStructure.last().spring == Spring.WORKING) {
                solvePart1(
                    currentBlueprint.drop(1),
                    wantedInstructions,
                    currentStructure + CustomElement(1, Spring.NOT_WORKING)
                )
            } else {
                solvePart1(
                    currentBlueprint.drop(1),
                    wantedInstructions,
                    currentStructure.dropLast(1) + CustomElement(currentStructure.last().count + 1, Spring.NOT_WORKING)
                )
            }
        }
        return count
    }

    private fun solvePart2(
        currentBlueprint: String,
        wantedInstructions: List<Int>,
        currentStructure: List<CustomElement>,
        cacheMap: MutableMap<State, Long>,
    ): Long {
        // end conditions
        if (currentBlueprint.isEmpty()) {
            return if (checkIfWeAreDone(currentStructure, wantedInstructions)) 1 else 0
        }
        if (!checkIfWeAreGoingOk(currentStructure, wantedInstructions)) {
            return 0
        }
        // caching
        val state = State(currentBlueprint.length, currentStructure)
        if (cacheMap.containsKey(state)) {
            return cacheMap[state]!!
        }

        val currentElement = currentBlueprint.first()

        var count = 0L

        // now it depends on the current element (?), (.) working, (#) not working
        if (currentElement == '#' || currentElement == '?') {
            // not working
            if (currentStructure.isEmpty() || currentStructure.last().spring == Spring.WORKING) {
                count += solvePart2(
                    currentBlueprint.drop(1),
                    wantedInstructions,
                    currentStructure + CustomElement(1, Spring.NOT_WORKING),
                    cacheMap
                )
            } else {
                count += solvePart2(
                    currentBlueprint.drop(1),
                    wantedInstructions,
                    currentStructure.dropLast(1) + CustomElement(currentStructure.last().count + 1, Spring.NOT_WORKING),
                    cacheMap
                )
            }
        }
        if (currentElement == '.' || currentElement == '?') {
            // working
            if (currentStructure.isEmpty() || currentStructure.last().spring == Spring.NOT_WORKING) {
                count += solvePart2(
                    currentBlueprint.drop(1),
                    wantedInstructions,
                    currentStructure + CustomElement(1, Spring.WORKING),
                    cacheMap
                )
            } else {
                count += solvePart2(
                    currentBlueprint.drop(1),
                    wantedInstructions,
                    currentStructure,
                    cacheMap
                )
            }
        }
        cacheMap[state] = count
        return count
    }

    private fun checkIfWeAreGoingOk(
        currentStructure: List<CustomElement>,
        wantedInstructions: List<Int>,
    ): Boolean {
        val filteredCurrent = currentStructure.filter { customElement -> customElement.spring == Spring.NOT_WORKING }
        if (filteredCurrent.size > wantedInstructions.size) return false
        filteredCurrent
            .forEachIndexed { index, customElement ->
                if (index == filteredCurrent.size - 1) {
                    // last element
                    if (customElement.count > wantedInstructions[index]) {
                        //this means we passed the wantedInstructions
                        return false
                    }
                } else {
                    if (customElement.count != wantedInstructions[index]) {
                        //this means we passed the wantedInstructions
                        return false
                    }
                }
            }
        return true

    }

    private fun checkIfWeAreDone(
        currentStructure: List<CustomElement>,
        wantedInstructions: List<Int>,
    ): Boolean {
        val filteredCurrent = currentStructure.filter { customElement -> customElement.spring == Spring.NOT_WORKING }
        if (filteredCurrent.size != wantedInstructions.size) return false
        filteredCurrent
            .forEachIndexed { index, customElement ->
                if (customElement.count != wantedInstructions[index]) {
                    //this means we passed the wantedInstructions
                    return false
                }
            }
        return true

    }

    private fun String.ints() = this.split(",").map { it.toInt() }
    override fun part2(): Long {

        // ????.#...#... 4,1,1
        val a = input.lines().map { line ->
            // ugly way to get the wanted instructions and blueprint * 5
            val wantedInstructions = (0..<5).flatMap { line.split(" ").last().ints() }
            var blueprint = ""
            (0..<5).forEach {
                blueprint += if (it == 4) line.split(" ").first()
                else line.split(" ").first() + "?"
            }

            val cacheMap: MutableMap<State, Long> = mutableMapOf()
            val numberOfWays = solvePart2(blueprint, wantedInstructions, listOf(), cacheMap)
            numberOfWays
        }
        return a.sum()
    }

    data class State(val blueprintSize: Int, val currentStructure: List<CustomElement>)
}