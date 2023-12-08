package day08

import Day

class Day08(private val input: String) : Day() {
    override fun part1(): Long {
        //AAA = (BBB, CCC)
        val nodesMap: Map<String, Node> = input.lines().drop(2).map { line ->
            val id = line.split(" ")[0]
            val left = line.substringAfter("(").split(",")[0]
            val right = line.substringAfter(", ").substringBefore(")")

            id to Node(id, left, right)
        }.toMap()

        val instructions = input.lines()[0]
        var foundZZZ = false
        var currentNode = nodesMap["AAA"]
        var steps = 0L
        while (!foundZZZ) {
            instructions.forEach { instruction ->
                if (currentNode!!.id == "ZZZ") {
                    foundZZZ = true
                    return steps
                }
                currentNode = when (instruction) {
                    'L' -> nodesMap[currentNode!!.left]
                    'R' -> nodesMap[currentNode!!.right]
                    else -> throw IllegalArgumentException("This shouldn't happen")
                }
                steps++

            }
        }
        return -1
    }


    override fun part2(): Long {

        //AAA = (BBB, CCC)
        val nodesMap: Map<String, Node> = input.lines().drop(2).map { line ->
            val id = line.split(" ")[0]
            val left = line.substringAfter("(").split(",")[0]
            val right = line.substringAfter(", ").substringBefore(")")

            id to Node(id, left, right)
        }.toMap()

        val instructions = input.lines()[0]
        var currentNodes = nodesMap.filter { it.key.endsWith("A") }.map { it.value }

        val statesList = currentNodes.map {
            mutableSetOf<NodeState>()
        }.toList()
        val cycleSizeList = currentNodes.map {
            -1L
        }.toMutableList()


        val stepsList = currentNodes.map { 0L }.toMutableList()
        var steps = 0L
        while (true) {
            instructions.forEachIndexed { instructionIndex, instruction ->
                // also check if we magically found the solution without the cycles
                if (currentNodes.all { it.id.endsWith("Z") } && stepsList.all { it == steps }) {
                    return steps
                }

                // check if we found the cycle for all nodes
                if (cycleSizeList.all { it != -1L }) {
                    // we found the cycle for all nodes

                    while (!stepsList.all { it == stepsList[0] }) {
                        // until they are all the same
                        val indexOfMin = stepsList.mapIndexed { index, stepsValue ->
                            index to stepsValue
                        }.minByOrNull { it.second }!!.first
                        stepsList[indexOfMin] += cycleSizeList[indexOfMin]
                    }
                    return stepsList[0]
                }

                // do the steps
                currentNodes = currentNodes.mapIndexed { nodeIndex, currentNode ->
                    if (stepsList[nodeIndex] > steps) {
                        currentNode
                    } else {

                        if (cycleSizeList[nodeIndex] != -1L) {
                            // we already have the cycle for this node so we increment the steps based on his cycle
                            stepsList[nodeIndex] += cycleSizeList[nodeIndex]
                            currentNode
                        } else {
                            val newNode = when (instruction) {
                                'L' -> nodesMap[currentNode!!.left]
                                else -> nodesMap[currentNode!!.right]
                            }
                            stepsList[nodeIndex]++
                            newNode!!
                        }
                    }
                }
                steps++


                // check if we found a cycle
                currentNodes.mapIndexed { nodeIndex, node -> nodeIndex to node }
                    // we removethe nodes that already have a cycle
                    .filter { cycleSizeList[it.first] == -1L }
                    // we only can find a cycle if the node ends with Z
                    .filter { it.second.id.endsWith("Z") }
                    .forEach { pairIntNode ->
                        var cycleFound = false
                        statesList[pairIntNode.first].filter { it.nodeId == pairIntNode.second.id && it.instructionIndex == instructionIndex }
                            .forEach { nodeState ->
                                // we found a cycle, check if we already found it
                                cycleSizeList[pairIntNode.first] = steps - nodeState.steps
                                cycleFound = true
                            }

                        if (!cycleFound) {
                            // we need to add the state to the set
                            statesList[pairIntNode.first].add(NodeState(pairIntNode.second.id, instructionIndex, steps))
                        }
                    }
            }


        }


    }

    private data class Node(val id: String, val left: String, val right: String)
    private data class NodeState(val nodeId: String, val instructionIndex: Int, val steps: Long)
}


