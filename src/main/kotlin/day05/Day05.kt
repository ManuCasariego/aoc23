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

    fun part2Long(): Long {
        // destinationstart sourcestart rangelength
        // if it isn't mapped then it is the same source and destination, seed 10 goes to soil 10

        val chunkedSeeds = input.substringBefore("\n\n").substringAfter(":").longs().chunked(2).toMutableList()

        val transformations2dArray = getTransformations2dArray()

        val response = chunkedSeeds.map { chunkSeed ->
            // chunkSeed contains 2 numbers, the start and the range
            println("starting chunk")
            println(Instant.now())
            (chunkSeed.first()..<(chunkSeed.first() + chunkSeed.last())).minOf { seedInt ->
                // seedInt
                var seedIntAux = seedInt

                transformations2dArray.forEach { transformationArray ->
                    seedIntAux = transformationArray.firstNotNullOfOrNull { transformation ->
                        val (destinationStart, sourceStart, rangeLength) = transformation
                        if (seedIntAux in (sourceStart..<sourceStart + rangeLength)) {
                            destinationStart + (seedIntAux - sourceStart)

                        } else null
                    } ?: seedIntAux
                }
                seedIntAux
            }
        }
        return response.min()

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