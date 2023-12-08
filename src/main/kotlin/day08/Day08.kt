package day08

import Day

class Day08(private val input: String) : Day() {
    override fun part1(): Long {
        val nodesMap = parseNodesMap(input)
        val instructions = input.lines()[0]
        return minDistanceToReachFromToNode(instructions, nodesMap, "AAA", listOf("ZZZ"))
    }

    override fun part2(): Long {
        val nodesMap = parseNodesMap(input)
        val instructions = input.lines()[0]

        val startingNodes = nodesMap.filter { it.key.endsWith("A") }.map { it.key }
        val endNodes = nodesMap.filter { it.key.endsWith("Z") }.map { it.key }
        // this graph has the following properties:
        // 1.   if you find a solution on x steps, you will get to the same state in x steps again,
        //      so you only need to find the distance from every start to a valid end, and do the lcm of them all
        //      and that will be the answer
        // 2.   the distance x will always be a denominator of instructions.length
        return startingNodes.map { minDistanceToReachFromToNode(instructions, nodesMap, it, endNodes) }
            .reduce { acc, l -> lcm(acc, l) }
    }

    private fun parseNodesMap(input: String): Map<String, Node> {
        //AAA = (BBB, CCC)
        return input.lines().drop(2).map { line ->
            val id = line.split(" ")[0]
            val left = line.substringAfter("(").split(",")[0]
            val right = line.substringAfter(", ").substringBefore(")")
            id to Node(id, left, right)
        }.toMap()
    }

    private fun minDistanceToReachFromToNode(
        instructions: String,
        nodesMap: Map<String, Node>,
        fromNode: String,
        toNode: List<String>,
    ): Long {
        var currentNode = nodesMap[fromNode]
        var steps = 0L
        while (true) {
            instructions.forEach { instruction ->
                if (toNode.contains(currentNode!!.id)) {
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
    }

    private fun gcd(a: Long, b: Long): Long {
        if (b == 0L) return a
        return gcd(b, a % b)
    }

    private fun lcm(a: Long, b: Long): Long {
        return a * (b / gcd(a, b))
    }

    data class Node(val id: String, val left: String, val right: String)
}


