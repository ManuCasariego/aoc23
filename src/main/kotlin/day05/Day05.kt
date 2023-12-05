package day05

import Day
import java.time.Instant

class Day05(private val input: String) : Day() {
    fun part1Long(): Long {
        // destinationstart sourcestart rangelength
        // if it isn't mapped then it is the same source and destination, seed 10 goes to soil 10

        val transformations2dArray = getTransformations2dArray()

        var seeds = input.substringBefore("\n\n").substringAfter(":").longs().toMutableList()
        // since they are a chain I will override them

        transformations2dArray.forEach { transformationArray ->

            seeds = seeds.map { seedInt ->
                transformationArray.map { transformation ->
                    val (destinationStart, sourceStart, rangeLength) = transformation
                    if (seedInt >= sourceStart && seedInt < sourceStart + rangeLength) destinationStart + (seedInt - sourceStart) else Long.MIN_VALUE
                }.filterNot { it == Long.MIN_VALUE }.firstOrNull() ?: seedInt

            }.toMutableList()
        }

        // the output is the lowest location
        return seeds.min()
    }

    private fun getRangeThatFits(
        left: Long,
        transformationArray: List<Triple<Long, Long, Long>>,
    ): Triple<Long, Long, Long>? {
        // this function needs to find if left sits under any transformationarray range, if not return null
        return transformationArray.firstOrNull {
            val (_, sourceStart, rangeLength) = it
            left in (sourceStart..<(sourceStart + rangeLength))
        }
    }

    fun part2Long(): Long {
        // destinationstart sourcestart rangelength
        // if it isn't mapped then it is the same source and destination, seed 10 goes to soil 10

        val chunkedSeeds = input.substringBefore("\n\n").substringAfter(":").longs().chunked(2).toMutableList()

        val transformations2dArray = getTransformations2dArray()


        val response = chunkedSeeds.flatMap { chunkSeed ->

            // so we have a chunkseed range
            val chunkSeedRange = Pair(chunkSeed.first(), chunkSeed.last())
            var rangesList = listOf(chunkSeedRange)

            transformations2dArray.forEach { transformationArray ->
                // we need to convert this range in multiple ranges
                val newRanges = mutableListOf<Pair<Long, Long>>()

                rangesList.forEach { range ->
                    val startingRange = range.first
                    val rangeStep = range.second


                    // start the iteration, i have one range and needs to be converted to 1 or more based on the transformationArray we have
                    var left = startingRange

                    while (left != (startingRange + rangeStep)) {
                        var right = left
                        val rangeThatFits = getRangeThatFits(left, transformationArray)
                        if (rangeThatFits == null) {
                            // we didn't find any range that fits
                            right =
                                (startingRange + rangeStep).coerceAtMost(transformationArray.filter { it.second > left }
                                    .minOfOrNull { it.second }?:Long.MAX_VALUE)

                            newRanges.add(Pair(left, right - left - 1))
                        } else {
                            val (destinationStart, sourceStart, rangeLength) = rangeThatFits
                            right = (sourceStart + rangeLength).coerceAtMost(startingRange + rangeStep)
                            // conversion needed
                            val dx = destinationStart - sourceStart
                            newRanges.add(Pair(left + dx, right - left - 1 + dx))
                        }
                        left = right
                    }
                }
                rangesList = newRanges
            }
            rangesList
        }
        return response.minOf { it.first }

    }

    private fun getTransformations2dArray(): List<List<Triple<Long, Long, Long>>> {
        return input.split("\n\n").drop(1).map { transformationChunk ->
            transformationChunk.lines().drop(1).map { transformationLine ->
                val (destinationStart, sourceStart, rangeLength) = transformationLine.longs()
                Triple(destinationStart, sourceStart, rangeLength)
            }
        }
    }

    fun String.longs() = this.split(" ").filterNot { it.isEmpty() }.map { it.toLong() }

    override fun part1(): Int {
        return 0
    }

    override fun part2(): Int {
        return 0
    }
}