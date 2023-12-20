package day20

import Day
import Utils.lcm

class Day20(private val input: String) : Day() {
    override fun part1(): Long {
        //broadcaster -> a, b, c
        //%a -> b
        //%b -> c
        //%c -> inv
        //&inv -> a
        val pieceMap = input.lines().associate { line ->
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
                // this should be only for broadcaster, but it's not
                val name = id.trim()
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
        pieceMap.values.forEach { piece ->
            piece.output.forEach { outputPiece ->
                if (!pieceMap.containsKey(outputPiece)) {
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
                } else if (pieceMap[outputPiece]!!.type == PieceType.INVERTER) {
                    pieceMap[outputPiece]!!.signalsMap?.set(piece.name, Signal.LOW)
                }
            }
        }
        // add them to the map
        outputPieces.forEach { pieceMap[it.name] = it }


        // I need a queue to append orders
        val queue = mutableListOf<Order>()
        // first is low and second is high
        val signalsCount = SignalCount(0L, 0L)
        (1..1000).forEach { _ ->
            // push the button
            queue.add(Order(from = "button", to = "broadcaster", signal = Signal.LOW))

            while (queue.isNotEmpty()) {
                val order = queue.removeFirst()
                if (order.signal == Signal.LOW) signalsCount.low++
                else if (order.signal == Signal.HIGH) signalsCount.high++

                val piece = pieceMap[order.to]!!
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

        return signalsCount.low * signalsCount.high
    }

    data class SignalCount(var high: Long, var low: Long)

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
        //broadcaster -> a, b, c
        //%a -> b
        //%b -> c
        //%c -> inv
        //&inv -> a
        val pieceMap = input.lines().associate { line ->
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
                // this should be only for broadcaster, but it's not
                val name = id.trim()
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
        pieceMap.values.forEach { piece ->
            piece.output.forEach { outputPiece ->
                if (!pieceMap.containsKey(outputPiece)) {
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
                } else if (pieceMap[outputPiece]!!.type == PieceType.INVERTER) {
                    pieceMap[outputPiece]!!.signalsMap?.set(piece.name, Signal.LOW)
                }
            }
        }
        // add them to the map
        outputPieces.forEach { pieceMap[it.name] = it }


        // I need a queue to append orders
        val queue = mutableListOf<Order>()
        // first is low and second is high
        val signalsCount = SignalCount(0L, 0L)
        var rxLowPulses = 0L
        var i = 0L

        // in my case, for the rx piece to receive a low pulse we need 4 high pulses from dl, vd, bh and ns
        // TODO : Make it generic, not only for my input
        var firstHighPulseFromDL = 0L
        var firstHighPulseFromVD = 0L
        var firstHighPulseFromBH = 0L
        var firstHighPulseFromNS = 0L
        while (true) {
            i++
            // push the button
            queue.add(Order(from = "button", to = "broadcaster", signal = Signal.LOW))

            while (queue.isNotEmpty()) {
                val order = queue.removeFirst()
                if (order.signal == Signal.LOW) signalsCount.low++
                else if (order.signal == Signal.HIGH) signalsCount.high++

                val piece = pieceMap[order.to]!!


                if (firstHighPulseFromNS != 0L && firstHighPulseFromVD != 0L && firstHighPulseFromBH != 0L && firstHighPulseFromDL != 0L) {
                    return lcm(lcm(lcm(firstHighPulseFromNS, firstHighPulseFromVD), firstHighPulseFromBH), firstHighPulseFromDL)
                }
                // check when vd sends a high pulse to zh
                if (piece.name == "zh" && order.signal == Signal.HIGH && order.from == "dl") {
                    println("vd sends a high pulse to zh, buttons pressed: $i")
                    if (firstHighPulseFromDL == 0L) firstHighPulseFromDL = i
                }
                if (piece.name == "zh" && order.signal == Signal.HIGH && order.from == "vd") {
                    println("vd sends a high pulse to zh, buttons pressed: $i")
                    if (firstHighPulseFromVD == 0L) firstHighPulseFromVD = i
                }
                if (piece.name == "zh" && order.signal == Signal.HIGH && order.from == "bh") {
                    println("vd sends a high pulse to zh, buttons pressed: $i")
                    if (firstHighPulseFromBH == 0L) firstHighPulseFromBH = i
                }
                if (piece.name == "zh" && order.signal == Signal.HIGH && order.from == "ns") {
                    println("vd sends a high pulse to zh, buttons pressed: $i")
                    if (firstHighPulseFromNS == 0L) firstHighPulseFromNS = i
                }
                // check when bh sends a high pulse to zh
                // check when ns sends a high pulse to zh
                // check when dl sends a high pulse to zh


                if (piece.type == PieceType.OUTPUT) {
                    // do nothing
                    // it never gets a low pulse
                    if (piece.name == "rx" && order.signal == Signal.LOW) rxLowPulses++
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
            if (rxLowPulses == 1L) return i
            if (i % 1000000 == 0L) println(i)
        }
        return -1
//        return signalsCount.low * signalsCount.high
    }
}