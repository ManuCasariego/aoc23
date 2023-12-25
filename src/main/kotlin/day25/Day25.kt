package day25

import Day

class Day25(private val input: String) : Day() {
    override fun part1(): Long {

        val componentsMap = input.lines().associate { line ->
            // jqt: rhn xhk nvd
            val componentId = line.substringBefore(": ")
            val connections = line.substringAfter(": ").split(" ").toSet()
            componentId to connections
        }.toMutableMap()
        // making the connections go both ways
        componentsMap.keys.toList().forEach { component ->
            componentsMap[component]!!.forEach { connection ->
                if (!componentsMap.contains(connection)) {
                    componentsMap[connection] = emptySet()
                } else {
                    componentsMap[connection] = componentsMap[connection]!!.plus(component)
                }
            }
        }

        // Testing Woldsom's approach
        // I mean my "algorithm" wasn't very hard either, Sample paths between 1000 random pairs of nodes,
        // sort encountered edges by frequency, top three were the answer

        val mutableCountMap = mutableMapOf<String, Int>()
        (1..1000).forEach { _ ->
            val start = componentsMap.keys.random()
            // don't care if they are the same
            val end = componentsMap.keys.random()

            bfs(componentsMap, start, end).forEach { component ->
                mutableCountMap[component] = (mutableCountMap[component] ?: 0) + 1
            }
        }

        val top3connections = mutableCountMap.entries.sortedByDescending { it.value }.take(3)
        val newComponentsMap = removeConnections(componentsMap, top3connections)
        val groups = groups(newComponentsMap.keys.toMutableList(), newComponentsMap)
        // groups.size should be two, we could check and rerun the 1000 random pairs if it's not
        return (groups.first().size * groups.last().size).toLong()
    }

    private fun removeConnections(
        componentsMap: MutableMap<String, Set<String>>,
        top3connections: List<MutableMap.MutableEntry<String, Int>>
    ): MutableMap<String, Set<String>> {
        // it needs to return a copy of the componentMap without the top3connections
        top3connections.forEach { connection ->
            val (first, second) = connection.key.split("/")
            componentsMap[first] = componentsMap[first]!!.minus(second)
            componentsMap[second] = componentsMap[second]!!.minus(first)
        }
        return componentsMap
    }

    // this function gets the graph a.k.a. the componentsMap, a start node and an end node
    // and returns the shortest path from start to end
    private fun bfs(
        componentsMap: MutableMap<String, Set<String>>,
        start: String,
        end: String
    ): List<String> {
        // queue has the current node and the path to get to it
        val queue = mutableListOf(start to emptyList<String>())
        val visited = mutableSetOf<String>()
        while (queue.isNotEmpty()) {
            val (currentNode, currentPath) = queue.removeFirst()
            if (currentNode == end) {
                return currentPath
            }
            visited.add(currentNode)
            componentsMap[currentNode]!!.filterNot { it in visited }.forEach {
                val values = listOf(currentNode, it).sorted()
                val tag = "${values.first()}/${values.last()}"
                queue.add(it to currentPath + tag)
            }
        }
        return emptyList()
    }


    private fun groups(
        componentsIds: MutableList<String>,
        componentsMap: MutableMap<String, Set<String>>
    ): MutableList<Set<String>> {
        val groups = mutableListOf<Set<String>>()
        while (componentsIds.isNotEmpty()) {
            val groupOfFirst = buildGroupOfComponents(componentsIds.first(), componentsMap)
            groups.add(groupOfFirst)
            componentsIds.removeAll(groupOfFirst)
        }
        return groups
    }

    private fun buildGroupOfComponents(first: String, componentsMap: MutableMap<String, Set<String>>): Set<String> {
        val queue = mutableListOf(first)
        val visited = mutableSetOf<String>()

        while (queue.isNotEmpty()) {
            val curr = queue.removeFirst()
            if (curr in visited) continue
            visited.add(curr)
            queue.addAll(componentsMap[curr]!!)
        }
        return visited.toSet()
    }

    override fun part2(): Long {
        return 0
    }
}