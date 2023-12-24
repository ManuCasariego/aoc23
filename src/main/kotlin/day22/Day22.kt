package day22

import Day
import Utils.Point3D
import kotlin.math.min

class Day22(private val input: String) : Day() {
    private val bricks = buildBricks(input)
    private val fallenBricks = makeBricksFall(bricks, speedyExit = false).first

    override fun part1(): Long {
        return fallenBricks.count { fallenBrick ->
            val remainingBricks = fallenBricks.toMutableSet()
            remainingBricks.remove(fallenBrick)
            makeBricksFall(remainingBricks, speedyExit = true).second == 0L
        }.toLong()
    }

    private fun buildBricks(input: String): List<Brick> {
        return input.lines().mapIndexed { idx, line ->
            val (start, end) = line.split("~")
            val (x1, y1, z1) = start.split(",").map { it.toLong() }
            val (x2, y2, z2) = end.split(",").map { it.toLong() }
            Brick(idx, Point3D(x1, y1, z1), Point3D(x2, y2, z2))
        }
    }

    private fun makeBricksFall(bricks: Collection<Brick>, speedyExit: Boolean): Pair<Set<Brick>, Long> {
        val fallenBricks = mutableSetOf<Brick>()
        var numberOfMovedBricks = 0L
        bricks.sortedBy { min(it.start.z, it.end.z) }.forEach { brick ->
            var fallingBrick = brick
            while (min(fallingBrick.start.z, fallingBrick.end.z) > 1L) {
                // we move it down until it hits something
                val tryBrick = fallingBrick.moveDown()
                if (fallenBricks.any { it.intersects(tryBrick) }) {
                    break
                } else {
                    if (speedyExit) return setOf<Brick>() to 1L
                    fallingBrick = tryBrick
                }
            }
            if (fallingBrick != brick) numberOfMovedBricks++
            fallenBricks.add(fallingBrick)
        }
        return fallenBricks to numberOfMovedBricks
    }

    data class Brick(val index: Int, val start: Point3D, val end: Point3D) {
        fun intersects(other: Brick): Boolean {
            // putting the z the first one as it's the most likely to be different, moving the check here goes from
            // 14s to 6s
            if (!doesItIntersect(this.start.z, this.end.z, other.start.z, other.end.z)) return false
            if (!doesItIntersect(this.start.x, this.end.x, other.start.x, other.end.x)) return false
            if (!doesItIntersect(this.start.y, this.end.y, other.start.y, other.end.y)) return false
            return true
        }

        private fun doesItIntersect(aStart: Long, aEnd: Long, bStart: Long, bEnd: Long): Boolean {
            val (minA, maxA) = if (min(aStart, aEnd) == aStart) aStart to aEnd else aEnd to aStart
            val (minB, maxB) = if (min(bStart, bEnd) == bStart) bStart to bEnd else bEnd to bStart
            return !(minA > maxB || maxA < minB)
        }

        fun moveDown(): Brick {
            return Brick(
                this.index,
                Point3D(this.start.x, this.start.y, this.start.z - 1),
                Point3D(this.end.x, this.end.y, this.end.z - 1)
            )
        }
    }

    override fun part2(): Long {
        val solution = fallenBricks.map { fallenBrick ->
            val remainingBricks = fallenBricks.toMutableSet()
            remainingBricks.remove(fallenBrick)
            makeBricksFall(remainingBricks, speedyExit = false).second
        }
        return solution.sum()
    }
}