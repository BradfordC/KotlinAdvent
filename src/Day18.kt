import aoc.*
import aoc.grids.*
import kotlinx.coroutines.runBlocking
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Int {
        var answer = 0
        val grid = Grid(50, 50)
        var current = Point(25, 25)
        for (line in input) {
            val parts = line.parseWords(true)
            val dir = parts[0]
            val quant = parts[1].toInt()
            repeat(quant) {
                val move = when(dir) {
                    "R" -> EAST
                    "D" -> SOUTH
                    "L" -> WEST
                    "U" -> NORTH
                    else -> throw Exception()
                }
                current += move
                grid.set(current, "#")
            }
        }

        val fill = grid.fuzzySelect(Point(300, 307))
        val edges = grid.fuzzySelect(Point(300, 306))

        return fill.size + edges.size
    }

    fun getDistance(hex: String): Int {
        return hex.map {
            when(it) {
                '0' -> 0
                '1' -> 1
                '2' -> 2
                '3' -> 3
                '4' -> 4
                '5' -> 5
                '6' -> 6
                '7' -> 7
                '8' -> 8
                '9' -> 9
                'a' -> 10
                'b' -> 11
                'c' -> 12
                'd' -> 13
                'e' -> 14
                'f' -> 15
                else -> throw Exception()
            }
        }.reduce { acc, v -> acc * 16 + v }
    }

    fun shoelace(points: List<Point>): Long {
        var outline = 0L

        var area = 0L
        for (i in points.indices) {
            var a = points[i]
            var b = points[(i + 1) % points.size]

            outline += a.distanceTo(b).toLong()

            val pos = a.x.toLong() * b.y.toLong()
            val neg = a.y.toLong() * b.x.toLong()

            area += pos - neg
        }

        return (abs(area) + outline) / 2 + 1
    }

    fun part2(input: List<String>): Long {
        var answer = 0

        var topLeft = Point(0, 0)
        var botRight = Point(0, 0)
        var current = Point(0, 0)

        val points = mutableListOf<Point>()

        for (line in input) {
            val parts = line.parseWords(true)
            val inst = parts.last()
            val dir = inst.last()
            val distance = getDistance(inst.dropLast(1))

            when (dir) {
                '0' -> current += Point(distance, 0)
                '1' -> current += Point(0, distance)
                '2' -> current += Point(-distance, 0)
                '3' -> current += Point(0, -distance)
            }

            if (current.x > botRight.x) {
                botRight = Point(current.x, botRight.y)
            }
            if (current.x < topLeft.x) {
                topLeft = Point(current.x, topLeft.y)
            }
            if (current.y > botRight.y) {
                botRight = Point(botRight.x, current.y)
            }
            if (current.y < topLeft.y) {
                topLeft = Point(topLeft.x, current.y)
            }

            points.add(current)
        }

        return shoelace(points)
    }

    fun runParts(inputName: String) {
        inputName.println()
        val input = readInput(inputName)

        val p1 = part1(input)
        "Part 1: $p1".println()
        if (p1 != 0) p1.toClipboard()

        val p2 = part2(input)
        "Part 2: $p2".println()
        if (p2 != 0L) p2.toClipboard()

        println()
    }



    val inputName = "Day18"
    if (inputExists(inputName)) {
        if (inputExists(inputName + "_Sample")) {
            runParts(inputName + "_Sample")
        }
        runParts(inputName)
    }
    else {
        var success: Boolean
        runBlocking {
            success = downloadInput(inputName)
        }
        val status = if (success) "Succeeded" else "Failed"
        "Download $status".println()
    }
}
