package day24

import Day

class Day24(private val input: String) : Day() {
    override fun part1(): Long {
        val hailStones = buildHailStones()
        var solution = 0L
        var hailStonesToCompare = hailStones.toList()
        val range = 200000000000000.0..400000000000000.0
        hailStones.forEach { hailStone ->
            hailStonesToCompare = hailStonesToCompare.drop(1)
            hailStonesToCompare.forEach { otherHailStone ->
                val crossPoint = hailStone.whereWillTheyCross(otherHailStone)
                if (crossPoint != null && crossPoint.first in range && crossPoint.second in range) {
                    solution++
                }
            }
        }
        return solution
    }

    data class HailStone(val position: Utils.Point3D, val velocity: Utils.Point3D) {
        private fun willTheyCross(otherHailStone: HailStone) =
            velocity.y.toDouble() / velocity.x.toDouble() != otherHailStone.velocity.y.toDouble() / otherHailStone.velocity.x.toDouble()


        fun whereWillTheyCross(other: HailStone): Triple<Double, Double, Double>? {
            if (!willTheyCross(other)) return null

            val m1 = velocity.y.toDouble() / velocity.x
            val c1 = position.y - m1 * position.x
            val m2 = other.velocity.y.toDouble() / other.velocity.x
            val c2 = other.position.y - m2 * other.position.x

            val x = (c2 - c1) / (m1 - m2)
            val y = m1 * x + c1

            // t1 is the time taken for this hailstone to reach the cross point
            val t1 = (x - position.x) / velocity.x
            // t2 is the time taken for the other hailstone to reach the cross point
            val t2 = (x - other.position.x) / other.velocity.x

            // if any time is negative, it means they won't cross on positive time, therefore return null
            if (t1 < 0 || t2 < 0) return null
            return Triple(x, y, 0.0)
        }
    }

    private fun buildHailStones() = input.lines().map { line ->
        val (x, y, z) = line.substringBefore('@').split(",").map { it.trim().toLong() }
        val (vx, vy, vz) = line.substringAfter('@').split(",").map { it.trim().toLong() }
        HailStone(Utils.Point3D(x, y, z), Utils.Point3D(vx, vy, vz))
    }

    override fun part2(): Long {
        return 0L
    }

}