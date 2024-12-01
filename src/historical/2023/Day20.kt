package historical.`2023`

import aoc.*
import kotlinx.coroutines.runBlocking
import java.util.LinkedList

data class Pulse(val source: String, val dest: String, val high: Boolean) {
    override fun toString(): String {
        val signal = if(high) "high" else "low"
        return "historical.`2023`.Pulse($source -$signal-> $dest)"
    }
}

abstract class Module(val id: String, val receivers: List<String>) {
    abstract fun receive(pulse: Pulse): List<Pulse>

    open fun addInput(input: String) { /*Intentionally left blank*/
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Module

        if (id != other.id) return false

        return true
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}

class BroadcastModule(id: String, receivers: List<String>) : Module(id, receivers) {
    override fun receive(pulse: Pulse): List<Pulse> {
        return receivers.map { Pulse(id, it, pulse.high) }
    }

    override fun hashCode(): Int {
        return 1
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false
        return true
    }
}

class FlipFlopModule(id: String, receivers: List<String>) : Module(id, receivers) {
    var on = false

    override fun receive(pulse: Pulse): List<Pulse> {
        if (!pulse.high) {
            on = !on
            return receivers.map { Pulse(id, it, on) }
        }
        return emptyList()
    }

    override fun hashCode(): Int {
        return on.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as FlipFlopModule

        if (on != other.on) return false

        return true
    }
}

class ConjunctionModule(id: String, receivers: List<String>) : Module(id, receivers) {
    private val lastReceived = mutableMapOf<String, Boolean>()

    override fun receive(pulse: Pulse): List<Pulse> {
        lastReceived[pulse.source] = pulse.high
        val sendHigh = !allHigh()
        return receivers.map { Pulse(id, it, sendHigh) }
    }

    fun allHigh(): Boolean {
        return lastReceived.values.all { it }
    }

    override fun addInput(input: String) {
        lastReceived[input] = false
    }

    override fun hashCode(): Int {
        return lastReceived.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as ConjunctionModule

        if (lastReceived != other.lastReceived) return false

        return true
    }
}

fun main() {
    fun parseInput(input: List<String>): List<Module> {
        val modules = mutableListOf<Module>()
        for (line in input) {
            val type = line[0]
            val ids = line.parseWords()
            val id = ids.first()
            val receivers = ids.drop(1)
            val module = when (type) {
                '%' -> FlipFlopModule(id, receivers)
                '&' -> ConjunctionModule(id, receivers)
                else -> BroadcastModule(id, receivers)
            }
            modules.add(module)
        }
        return modules
    }

    fun getIdMap(modules: List<Module>): Map<String, Int> {
        return modules.mapIndexed { i, module -> module.id to i }.toMap()
    }

    fun primeInputs(modules: List<Module>) {
        modules.forEach { module ->
            modules.forEach { other ->
                if (module.id in other.receivers) module.addInput(other.id)
            }
        }
    }

    fun part1(input: List<String>): Long {
        val modules = parseInput(input)
        val idMap = getIdMap(modules)
        primeInputs(modules)

        fun List<Module>.get(id: String): Module? {
            val index = idMap[id]
            return if (index != null) this[index] else null
        }

        var lows = 0L
        var highs = 0L

        var i = 0
        repeat(1000) {
            val pulseQueue = LinkedList<Pulse>()
            pulseQueue.add(Pulse("button", "broadcaster", false))

            while(pulseQueue.isNotEmpty()) {
                val nextPulses = LinkedList<Pulse>()
                for (pulse in pulseQueue) {
                    if (pulse.high) highs++ else lows++
                    val receiver = modules.get(pulse.dest)
                    if (receiver != null) {
                        nextPulses.addAll(receiver.receive(pulse))
                    }
                }
                pulseQueue.clear()
                pulseQueue.addAll(nextPulses)
            }
//            "${i++}".println()
        }

        "$lows lows, $highs highs".println()
        return lows * highs
    }

    fun part2(input: List<String>): Long {
        val modules = parseInput(input)
        val idMap = getIdMap(modules)
        primeInputs(modules)

        fun List<Module>.get(id: String): Module? {
            val index = idMap[id]
            return index?.let { this[index] }
        }

        // Each of these contributes to the module that sends data to the final module
        // Once all of them send a high pulse on the same cycle, we're done
        // Evidence shows that each repeats every X cycles, starting on the Xth cycle
        // So, once we've seen the first high signal of each, we can figure out how long it will take until they all send
        // a high signal at the same time
        val contributors = mapOf(
            "ft" to mutableListOf<Long>(),
            "jz" to mutableListOf(),
            "sv" to mutableListOf(),
            "ng" to mutableListOf()
        )

        var i = 0L
        while(contributors.any { it.value.isEmpty() }) {
            i++
            val pulseQueue = LinkedList<Pulse>()
            pulseQueue.add(Pulse("button", "broadcaster", false))

            while(pulseQueue.isNotEmpty()) {
                val nextPulses = LinkedList<Pulse>()
                for (pulse in pulseQueue) {
                    if (pulse.source in contributors.keys && pulse.high) {
                        contributors[pulse.source]?.add(i)
                    }
                    val receiver = modules.get(pulse.dest)
                    if (receiver != null) {
                        nextPulses.addAll(receiver.receive(pulse))
                    }
                }
                pulseQueue.clear()
                pulseQueue.addAll(nextPulses)
            }
        }

        return lcm(contributors.map { it.value.first().toLong() })
    }

    fun runParts(inputName: String) {
        inputName.println()
        val input = readInput(inputName)

        val p1 = part1(input)
        "Part 1: $p1".println()
        if (p1 != 0L) p1.toClipboard()

        val p2 = part2(input)
        "Part 2: $p2".println()
        if (p2 != 0L) p2.toClipboard()

        println()
    }


    val inputName = "Day20"
    if (inputExists(inputName)) {
//        if (inputExists(inputName + "_Sample")) {
//            runParts(inputName + "_Sample")
//        }
        runParts(inputName)
    } else {
        var success: Boolean
        runBlocking {
            success = downloadInput(inputName)
        }
        val status = if (success) "Succeeded" else "Failed"
        "Download $status".println()
    }
}
