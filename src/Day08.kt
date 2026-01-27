import aoc.*
import kotlinx.coroutines.runBlocking
import kotlin.math.abs
import kotlin.math.pow

fun main() {

    data class Point3D(var x: Long, var y: Long, var z: Long) {
        fun distanceTo(other: Point3D): Double {
            val dx = abs(this.x - other.x).toDouble()
            val dy = abs(this.y - other.y).toDouble()
            val dz = abs(this.z - other.z).toDouble()
            return listOf(dx, dy, dz).sumOf { it.pow(2) }.pow(0.5)
        }
    }

    class CircuitTracker() {
        val circuits = mutableListOf<MutableSet<Point3D>>()

        fun addConnection(x: Point3D, y: Point3D) {
            val indexX = circuits.indexOfFirst { it.contains(x) }
            val indexY = circuits.indexOfFirst { it.contains(y) }
            if (indexX == -1 && indexY == -1) {
                // New circuit
                circuits.add(mutableSetOf(x, y))
            }
            else if (indexX == -1) {
                // Y is already in a circuit, add X to that circuit
                circuits[indexY].add(x)
            }
            else if (indexY == -1) {
                // X is already in a circuit, add Y to that circuit
                circuits[indexX].add(y)
            }
            else if (indexX != indexY){
                // Both already in different circuits, merge them together
                circuits[indexX].addAll(circuits[indexY])
                circuits.removeAt(indexY)
            }
            // Else both are in the same circuit, do nothing
        }
    }

    fun part1(input: List<String>): Long {
        val points = input
            .map { it.parseLongs() }
            .map { Point3D(it[0], it[1], it[2]) }
        val circuits = CircuitTracker()
        // Naive approach, surely it'll work >_>
        var threshold = 0.0
        for (i in 0..<1000) {
            var smallDistance = Double.MAX_VALUE
            var smallX: Point3D? = null
            var smallY: Point3D? = null
            for (x in points.indices) {
                for (y in x+1..<points.size) {
                    val distance = points[x].distanceTo(points[y])
                    if (distance < smallDistance && distance > threshold) {
                        smallDistance = distance
                        smallX = points[x]
                        smallY = points[y]
                    }
                }
            }
            if (smallX == null || smallY == null) {
                println("No connection found over the threshold.")
                break
            }
            // Doesn't work if there are two pairs the same distance apart
            // Surely there aren't, though, right? Right?
            threshold = smallDistance
            circuits.addConnection(smallX, smallY)
//            println(i.toString())
        }
        return circuits.circuits
            .map { it.size }
            .sortedDescending()
            .take(3)
            .reduce { a, b -> a * b }
            .toLong()
    }

    fun part2(input: List<String>): Long {
        var answer = -1L
        val points = input
            .map { it.parseLongs() }
            .map { Point3D(it[0], it[1], it[2]) }
        val circuits = CircuitTracker()
        var threshold = 0.0
        var i = 0
        while (answer < 0) {
            var smallDistance = Double.MAX_VALUE
            var smallX: Point3D? = null
            var smallY: Point3D? = null
            for (x in points.indices) {
                for (y in x+1..<points.size) {
                    val distance = points[x].distanceTo(points[y])
                    if (distance < smallDistance && distance > threshold) {
                        smallDistance = distance
                        smallX = points[x]
                        smallY = points[y]
                    }
                }
            }
            if (smallX == null || smallY == null) {
                println("No connection found over the threshold.")
                break
            }
            // It didn't break before, maybe it won't break now?
            threshold = smallDistance
            circuits.addConnection(smallX, smallY)
            if (circuits.circuits.size == 1 && i > 10 && circuits.circuits[0].size == points.size) {
                answer = smallX.x * smallY.x
                break
            }
//            println(i++.toString() + "\t${circuits.circuits.size}")
        }
        return answer
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



    val inputName = "Day08"
    if (inputExists(inputName)) {
        if (inputExists(inputName + "_Sample")) {
            runParts(inputName + "_Sample")
        }
        runParts(inputName)
    }
    else {
        var success: Boolean
        runBlocking {
            success = downloadInput(inputName, 2025)
        }
        val status = if (success) "Succeeded" else "Failed"
        "Download $status".println()
    }
}
