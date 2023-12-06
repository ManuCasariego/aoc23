package day05

import Day

class Day05(private val input: String) : Day() {
    data class TransformationRange(val destinationStart: Long, val sourceStart: Long, val rangeLength: Long)
    data class Range(val start: Long, val rangeLength: Long) {
        companion object {
            fun getRangeFromStartEnd(start: Long, end: Long): Range {
                return Range(start, end - start - 1)
            }
        }
    }


    override fun part1(): Long {
        // destinationstart sourcestart rangelength
        // if it isn't mapped then it is the same source and destination, seed 10 goes to soil 10

        val transformationsMatrix = getTransformationsMatrix()

        var seeds = input.substringBefore("\n\n").substringAfter(":").longs().toMutableList()
        // since they are a chain I will override them

        transformationsMatrix.forEach { transformationArray ->

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
        transformationArray: List<TransformationRange>,
    ): TransformationRange? {
        // this function needs to find if left sits under any transformationarray range, if not return null
        return transformationArray.firstOrNull {
            left in (it.sourceStart..<(it.sourceStart + it.rangeLength))
        }
    }

    override fun part2(): Long {
        // destinationstart sourcestart rangelength
        // if it isn't mapped then it is the same source and destination, seed 10 goes to soil 10

        val chunkedSeeds = input.substringBefore("\n\n").substringAfter(":").longs().chunked(2).toMutableList()

        val transformationMatrix = getTransformationsMatrix()

        val response = chunkedSeeds.flatMap { chunkSeed ->

            // so we have a chunkseed range
            val chunkSeedRange = Range(chunkSeed.first(), chunkSeed.last())
            var rangesList = listOf(chunkSeedRange)

            transformationMatrix.forEach { transformationArray ->
                // we need to convert this range in multiple ranges
                val newRanges = mutableListOf<Range>()

                rangesList.forEach { range ->
                    val startingRange = range.start
                    val rangeStep = range.rangeLength


                    // start the iteration, i have one range and needs to be converted to 1 or more based on the transformationArray we have
                    var left = startingRange

                    while (left != (startingRange + rangeStep)) {
                        var right = left
                        val rangeThatFits = getRangeThatFits(left, transformationArray)
                        if (rangeThatFits == null) {
                            // we didn't find any range that fits
                            right =
                                (startingRange + rangeStep).coerceAtMost(transformationArray.filter { it.rangeLength > left }
                                    .minOfOrNull { it.rangeLength } ?: Long.MAX_VALUE)
                            newRanges.add(Range.getRangeFromStartEnd(left, right))
                        } else {
                            val (destinationStart, sourceStart, rangeLength) = rangeThatFits
                            right = (sourceStart + rangeLength).coerceAtMost(startingRange + rangeStep)
                            // conversion needed
                            val dx = destinationStart - sourceStart
                            newRanges.add(Range.getRangeFromStartEnd(left + dx, right + dx))
                        }
                        left = right
                    }
                }
                rangesList = newRanges
            }
            rangesList
        }
        return response.minOf { it.start }

    }

    private fun getTransformationsMatrix(): List<List<TransformationRange>> {
        return input.split("\n\n").drop(1).map { transformationChunk ->
            transformationChunk.lines().drop(1).map { transformationLine ->
                val (destinationStart, sourceStart, rangeLength) = transformationLine.longs()
                TransformationRange(destinationStart, sourceStart, rangeLength)
            }
        }
    }

    fun String.longs() = this.split(" ").filterNot { it.isEmpty() }.map { it.toLong() }

}