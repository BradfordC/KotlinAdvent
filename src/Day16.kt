import aoc.downloadInput
import aoc.grids.Grid
import aoc.grids.Point
import aoc.inputExists
import aoc.println
import aoc.readInput
import kotlinx.coroutines.runBlocking

val RIGHT = Point(1, 0)
val LEFT = Point(-1, 0)
val UP = Point(0, -1)
val DOWN = Point(0, 1)

fun energize(input: List<String>, starting: Pair<Point, Point>): Long {
    var lightBeams = mutableListOf<Pair<Point, Point>>()
    val grid = Grid(input)
    val energized = Grid(input)
    val upGrid = Grid(input)
    val downGrid = Grid(input)
    val leftGrid = Grid(input)
    val rightGrid = Grid(input)
    for (x in 0 until energized.width) {
        for (y in 0 until  energized.height) {
            energized.setValue(x, y, ".")
            upGrid.setValue(x, y, ".")
            downGrid.setValue(x, y, ".")
            leftGrid.setValue(x, y, ".")
            rightGrid.setValue(x, y, ".")
        }
    }
    val dirMap = mapOf(
        UP to upGrid,
        DOWN to downGrid,
        LEFT to leftGrid,
        RIGHT to rightGrid
    )

    lightBeams.add(starting)
    var iter = 0
    while(lightBeams.isNotEmpty() && iter++ < 1000) {
        val nextBeams = mutableListOf<Pair<Point, Point>>()
        for (beam in lightBeams) {
            val next = Point(beam.first.x + beam.second.x, beam.first.y + beam.second.y)
            energized.setValue(next, "#")

            when (grid.get(next).value) {
                "|" -> if (beam.second == RIGHT || beam.second == LEFT) {
                    nextBeams.add(Pair(next, UP))
                    nextBeams.add(Pair(next, DOWN))
                }
                else {
                    nextBeams.add(Pair(next, beam.second))
                }
                "-" -> if (beam.second == UP || beam.second == DOWN) {
                    nextBeams.add(Pair(next, RIGHT))
                    nextBeams.add(Pair(next, LEFT))
                }
                else {
                    nextBeams.add(Pair(next, beam.second))
                }
                "/" -> when(beam.second) {
                    UP -> nextBeams.add(Pair(next, RIGHT))
                    LEFT -> nextBeams.add(Pair(next, DOWN))
                    DOWN -> nextBeams.add(Pair(next, LEFT))
                    RIGHT -> nextBeams.add(Pair(next, UP))
                }
                "\\" -> when(beam.second) {
                    DOWN -> nextBeams.add(Pair(next, RIGHT))
                    LEFT -> nextBeams.add(Pair(next, UP))
                    UP -> nextBeams.add(Pair(next, LEFT))
                    RIGHT -> nextBeams.add(Pair(next, DOWN))
                }
                "." -> nextBeams.add(Pair(next, beam.second))
            }
        }
        lightBeams.clear()
        for (nextBeam in nextBeams) {
            val dirGrid = dirMap[nextBeam.second]!!
            if (dirGrid.get(nextBeam.first).value == ".") {
                dirGrid.setValue(nextBeam.first, "#")
                lightBeams.add(nextBeam)
            }
        }
    }

    return energized.findRegions(Regex("#")).size.toLong()
}

fun main() {
    fun part1(input: List<String>): Long {
        return energize(input, Pair(Point(-1, 0), Point(1, 0)))
    }

    fun part2(input: List<String>): Long {
        val grid = Grid(input)

        val starts = mutableListOf<Pair<Point, Point>>()
        for (x in 0 until grid.width) {
            starts.add(Pair(Point(x, -1), Point(0, 1)))
            starts.add(Pair(Point(x, grid.height), Point(0, -1)))
        }
        for (y in 0 until grid.height) {
            starts.add(Pair(Point(-1, y), Point(1, 0)))
            starts.add(Pair(Point(grid.width, y), Point(-1, 0)))
        }
        return starts.maxOfOrNull { energize(input, it) }!!
    }

    fun runParts(inputName: String) {
        inputName.println()
        val input = readInput(inputName)
        "Part 1: ${part1(input)}".println()
        "Part 2: ${part2(input)}".println()
        println()
    }



    val inputName = "Day16"
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
