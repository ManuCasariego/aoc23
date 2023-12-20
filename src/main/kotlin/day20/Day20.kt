package day20

import Day
import Utils.lcm

class Day20(private val input: String) : Day() {
    private fun buildPiecesMap(input: String): MutableMap<String, Piece> {
        val piecesMap = input.lines().associate { line ->
            val id = line.substringBefore("->").trim()
            if (id.startsWith("%")) {
                val name = id.substringAfter("%").trim()
                val output = line.substringAfter("->").trim().split(",").map { it.trim() }
                name to Piece(name, output, on = false, signalsMap = null, type = PieceType.FLIPFLOP)
            } else if (id.startsWith("&")) {
                val name = id.substringAfter("&").trim()
                val output = line.substringAfter("->").trim().split(",").map { it.trim() }
                name to Piece(name, output, on = null, signalsMap = mutableMapOf(), type = PieceType.INVERTER)
            } else {
                val output = line.substringAfter("->").trim().split(",").map { it.trim() }
                "broadcaster" to Piece(
                    "broadcaster",
                    output,
                    on = null,
                    signalsMap = null,
                    type = PieceType.BROADCASTER
                )
            }
        }.toMutableMap()
        val outputPieces = mutableListOf<Piece>()

        // iterate to fill the inverters map
        piecesMap.values.forEach { piece ->
            piece.output.forEach { outputPiece ->
                if (!piecesMap.containsKey(outputPiece)) {
                    // same as output before, it just receives pulses and nothing else
                    outputPieces.add(
                        Piece(
                            outputPiece,
                            emptyList(),
                            on = null,
                            signalsMap = null,
                            type = PieceType.OUTPUT
                        )
                    )
                } else if (piecesMap[outputPiece]!!.type == PieceType.INVERTER) {
                    piecesMap[outputPiece]!!.signalsMap?.set(piece.name, Signal.LOW)
                }
            }
        }
        // add them to the map
        outputPieces.forEach { piecesMap[it.name] = it }
        return piecesMap
    }

    override fun part1(): Long {
        val piecesMap = buildPiecesMap(input)

        // I need a queue to append orders
        val signalsCount = SignalCount(0L, 0L)
        (1..1000).forEach { _ ->
            // push the button
            executeButtonPress(piecesMap, signalsCount)
        }

        return signalsCount.low * signalsCount.high
    }

    private fun executeButtonPress(piecesMap: MutableMap<String, Piece>, signalsCount: SignalCount) {
        val queue = mutableListOf<Order>()
        queue.add(Order(from = "button", to = "broadcaster", signal = Signal.LOW))

        while (queue.isNotEmpty()) {
            val order = queue.removeFirst()
            if (order.signal == Signal.LOW) signalsCount.low++
            else if (order.signal == Signal.HIGH) signalsCount.high++

            val piece = piecesMap[order.to]!!
            if (piece.type == PieceType.OUTPUT) {
                // do nothing
            } else if (piece.type == PieceType.BROADCASTER) {
                // broadcast the signal to every one
                piece.output.forEach { outputPiece ->
                    queue.add(Order(from = piece.name, to = outputPiece, signal = order.signal))
                }
            } else if (piece.type == PieceType.FLIPFLOP) {
                if (order.signal == Signal.HIGH) {
                    // nothing happens
                } else {
                    if (piece.on!!) {
                        piece.output.forEach { outputPiece ->
                            queue.add(Order(from = piece.name, to = outputPiece, signal = Signal.LOW))
                        }
                    } else {
                        piece.output.forEach { outputPiece ->
                            queue.add(Order(from = piece.name, to = outputPiece, signal = Signal.HIGH))
                        }
                    }
                    piece.on = !piece.on!!
                }
            } else if (piece.type == PieceType.INVERTER) {
                // Conjunction modules (prefix &) remember the type of the most recent pulse received from each of their
                // connected input modules; they initially default to remembering a low pulse for each input. When a
                // pulse is received, the conjunction module first updates its memory for that input. Then, if it
                // remembers high pulses for all inputs, it sends a low pulse; otherwise, it sends a high pulse.

                piece.signalsMap!![order.from] = order.signal
                if (piece.signalsMap.values.any { it == Signal.LOW }) {
                    // high pulse
                    piece.output.forEach { outputPiece ->
                        queue.add(Order(from = piece.name, to = outputPiece, signal = Signal.HIGH))
                    }
                } else {
                    // low pulse
                    piece.output.forEach { outputPiece ->
                        queue.add(Order(from = piece.name, to = outputPiece, signal = Signal.LOW))
                    }
                }
            }
        }
    }

    data class SignalCount(var low: Long, var high: Long)

    data class Order(val from: String, val to: String, val signal: Signal)

    data class Piece(
        val name: String,
        val output: List<String>,
        var on: Boolean? = null,
        val signalsMap: MutableMap<String, Signal>? = null,
        val type: PieceType,
    )

    enum class Signal {
        HIGH, LOW;

        fun reverse(): Signal {
            return if (this == HIGH) LOW
            else HIGH
        }
    }

    enum class PieceType { FLIPFLOP, INVERTER, BROADCASTER, OUTPUT }

    override fun part2(): Long {
        val piecesMap = buildPiecesMap(input)

        // I need a queue to append orders
        val queue = mutableListOf<Order>()
        // first is low and second is high
        val signalsCount = SignalCount(0L, 0L)
        var i = 0L

        // in my case, for the rx piece to receive a low pulse we need 4 high pulses from dl, vd, bh and ns
        val pieceThatPointsToRx = piecesMap.values.filter { it.output.contains("rx") }
        if (pieceThatPointsToRx.size != 1) throw Exception("There should be only one piece that points to rx")

        val piecesThatShouldEmmitHighPulse =
            piecesMap.values.filter { it.output.contains(pieceThatPointsToRx.first().name) }
                .associate { it.name to -1L }.toMutableMap()


        while (true) {
            i++
            // push the button
            queue.add(Order(from = "button", to = "broadcaster", signal = Signal.LOW))

            while (queue.isNotEmpty()) {
                val order = queue.removeFirst()
                if (order.signal == Signal.LOW) signalsCount.low++
                else if (order.signal == Signal.HIGH) signalsCount.high++

                val piece = piecesMap[order.to]!!

                if (piecesThatShouldEmmitHighPulse.values.all { it != -1L }) {
                    // we have all the high pulses
                    // we can stop the execution
                    return piecesThatShouldEmmitHighPulse.values.reduce { acc, l -> lcm(acc, l) }
                }
                if (piecesThatShouldEmmitHighPulse.containsKey(order.from) &&
                    piecesThatShouldEmmitHighPulse[order.from] == -1L &&
                    order.signal == Signal.HIGH &&
                    order.to == pieceThatPointsToRx.first().name
                ) {
                    piecesThatShouldEmmitHighPulse[order.from] = i
                }

                if (piece.type == PieceType.OUTPUT) {
                    // do nothing
                } else if (piece.type == PieceType.BROADCASTER) {
                    // broadcast the signal to every one
                    piece.output.forEach { outputPiece ->
                        queue.add(Order(from = piece.name, to = outputPiece, signal = order.signal))
                    }
                } else if (piece.type == PieceType.FLIPFLOP) {
                    if (order.signal == Signal.HIGH) {
                        // nothing happens
                    } else {
                        if (piece.on!!) {
                            piece.output.forEach { outputPiece ->
                                queue.add(Order(from = piece.name, to = outputPiece, signal = Signal.LOW))
                            }
                        } else {
                            piece.output.forEach { outputPiece ->
                                queue.add(Order(from = piece.name, to = outputPiece, signal = Signal.HIGH))
                            }
                        }
                        piece.on = !piece.on!!
                    }
                } else if (piece.type == PieceType.INVERTER) {
                    // Conjunction modules (prefix &) remember the type of the most recent pulse received from each of their
                    // connected input modules; they initially default to remembering a low pulse for each input. When a
                    // pulse is received, the conjunction module first updates its memory for that input. Then, if it
                    // remembers high pulses for all inputs, it sends a low pulse; otherwise, it sends a high pulse.

                    piece.signalsMap!![order.from] = order.signal
                    if (piece.signalsMap.values.any { it == Signal.LOW }) {
                        // high pulse
                        piece.output.forEach { outputPiece ->
                            queue.add(Order(from = piece.name, to = outputPiece, signal = Signal.HIGH))
                        }
                    } else {
                        // all the signals are high then we send a low pulse
                        piece.output.forEach { outputPiece ->
                            queue.add(Order(from = piece.name, to = outputPiece, signal = Signal.LOW))
                        }
                    }
                }
            }
        }
    }
}