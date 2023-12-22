package day22

import Day
import Utils.Point3D
import kotlin.math.min

class Day22(private val input: String) : Day() {
    override fun part1(): Long {
        val bricks = buildBricks(input)
        val fallenBricks = makeBricksFall(bricks)
        return fallenBricks.count { fallenBrick ->
            // we need to check if we remove fallen brick, will any other brick fall?
            val remainingBricks = fallenBricks.filter { it != fallenBrick }
            val makeThemFallAgain = makeBricksFall(remainingBricks)
            makeThemFallAgain.containsAll(remainingBricks) && remainingBricks.containsAll(makeThemFallAgain)
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

    private fun makeBricksFall(bricks: List<Brick>): List<Brick> {
        val fallenBricks = mutableListOf<Brick>()
        bricks.sortedBy { min(it.start.z, it.end.z) }.forEach { brick ->
            var fallingBrick = brick
            var brickPlaced = false
            // let's move if down as much as possible
            while (fallingBrick.start.z > 1L && fallingBrick.end.z > 1L) {
                val tryBrick = Brick(
                    fallingBrick.index,
                    Point3D(fallingBrick.start.x, fallingBrick.start.y, fallingBrick.start.z - 1),
                    Point3D(fallingBrick.end.x, fallingBrick.end.y, fallingBrick.end.z - 1)
                )
                if (fallenBricks.any { it.intersects(tryBrick) }) {
                    // it clashes, thereforw we need to add current falling brick to fallen bricks
                    fallenBricks.add(fallingBrick)
                    brickPlaced = true
                    // we can't move down anymore
                    break
                } else {
                    // we can move down
                    fallingBrick = tryBrick
                }
            }
            if (!brickPlaced) {
                // we can place it at the bottom
                fallenBricks.add(fallingBrick)
            }
        }
        // move them to free fall
        return fallenBricks
    }

    data class Brick(val index: Int, val start: Point3D, val end: Point3D) {
        fun intersects(other: Brick): Boolean {
            val thisMiddlePoints = this.start.getListFromOneToAnother(this.end)
            val otherMiddlePoints = other.start.getListFromOneToAnother(other.end)
            return thisMiddlePoints.any { otherMiddlePoints.contains(it) }
        }
    }

    override fun part2(): Long {
        val bricks = buildBricks(input)

        val fallenBricks = makeBricksFall(bricks)
        val solution = fallenBricks.map { fallenBrick ->
            val remainingBricks = fallenBricks.filter { it != fallenBrick }
            val makeThemFallAgain = makeBricksFall(remainingBricks)
            remainingBricks.count { !makeThemFallAgain.contains(it) }.toLong()
        }
        return solution.sum()
    }

}