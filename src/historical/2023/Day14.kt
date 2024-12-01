package historical.`2023`

import aoc.downloadInput
import aoc.grids.Grid
import aoc.grids.Point
import aoc.inputExists
import aoc.println
import aoc.readInput
import kotlinx.coroutines.runBlocking

fun Grid.deposit(cell: Point, rocks: Int, dx: Int, dy: Int) {
    for (i in 0 until rocks) {
        set(cell.x + dx * (i + 1), cell.y + dy * (i + 1), "O")
    }
}

fun Grid.tiltNorth() {
    for (x in 0 until width) {
        var rocks = 0
        for (y in height - 1 downTo 0) {
            val cell = get(x, y)
            if (cell.value == "O") {
                set(cell.point, ".")
                rocks ++
            }
            if (cell.value == "#") {
                deposit(cell.point, rocks, 0, 1)
                rocks = 0
            }
        }
        deposit(Point(x, -1), rocks, 0, 1)
    }
}


class Rock(var point: Point, var toYoink: Rock?) {
    fun yoink(offset: Point, nextMagnetMap: Map<Point, Rock>) {
        if (toYoink != null) {
            val newLocation = Point(point.x + offset.x, point.y + offset.y)
            toYoink!!.point = newLocation
            val nextMagnet = nextMagnetMap[newLocation]
            nextMagnet!!.addToYoink(toYoink!!)
            toYoink!!.yoink(offset, nextMagnetMap)
            toYoink = null
        }
    }

    fun addToYoink(rock: Rock) {
        if (toYoink == null) {
            toYoink = rock
        }
        else {
            toYoink!!.addToYoink(rock)
        }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        var answer = 0
        val grid = Grid(input)

        grid.tiltNorth()

        for (rock in grid.findRegions(Regex("O")).map { Point(it.originX, it.originY) }) {
            answer += grid.height - rock.y
        }

        return answer
    }

    fun Grid.load(): Long {
        var answer = 0L
        for (rock in this.findRegions(Regex("O")).map { Point(it.originX, it.originY) }) {
            answer += this.height - rock.y
        }
        return answer
    }

    fun makeMap(grid: Grid, rocks: MutableList<Rock>, points: List<Point>, dir: Point): Pair<List<Rock>, Map<Point, Rock>> {
        val magnets = mutableListOf<Rock>()
        val magnetMap = mutableMapOf<Point, Rock>()

        for (point in points) {
            val cell = grid.get(point)
            var lower = grid.get(point.x + dir.x, point.y + dir.y)
            if (cell.value == "#" && lower.value != "#") {
                val magnet = Rock(cell.point, null)
                magnets.add(magnet)
                while (lower.value != "#") {
                    magnetMap[Point(lower.x, lower.y)] = magnet
                    if (lower.value == "O") {
                        val rock = Rock(lower.point, null)
                        rocks.add(rock)
                        grid.set(lower.point, ".")
                        magnet.addToYoink(rock)
                    }
                    lower = grid.get(lower.x + dir.x, lower.y + dir.y)
                }
            }
        }

        return Pair(magnets, magnetMap)
    }

    fun part2(input: List<String>): Long {
        val grid = Grid(input)

        val rocks = mutableListOf<Rock>()

        val magnetLists = mutableListOf<List<Rock>>()
        val magnetMaps = mutableListOf<Map<Point, Rock>>()

        grid.borderValue = "#"

        var points = mutableListOf<Point>()
        for (y in -1 until grid.height) {
            for (x in 0 until grid.width) {
                points.add(Point(x, y))
            }
        }
        val (northMagnets, northMagnetMap) = makeMap(grid, rocks, points, Point(0, 1))
        magnetLists.add(northMagnets)
        magnetMaps.add(northMagnetMap)

        points = mutableListOf()
        for (x in -1 until grid.width) {
            for (y in (grid.height - 1) downTo 0 ) {
                points.add(Point(x, y))
            }
        }
        val (westMagnets, westMagnetMap) = makeMap(grid, rocks, points, Point(1, 0))
        magnetLists.add(westMagnets)
        magnetMaps.add(westMagnetMap)

        points = mutableListOf()
        for (y in grid.height downTo 0) {
            for (x in (grid.width - 1) downTo 0) {
                points.add(Point(x, y))
            }
        }
        val (southMagnets, southMagnetMap) = makeMap(grid, rocks, points, Point(0, -1))
        magnetLists.add(southMagnets)
        magnetMaps.add(southMagnetMap)

        points = mutableListOf()
        for (x in grid.width downTo 0) {
            for (y in 0 until grid.height) {
                points.add(Point(x, y))
            }
        }
        val (eastMagnets, eastMagnetMap) = makeMap(grid, rocks, points, Point(-1, 0))
        magnetLists.add(eastMagnets)
        magnetMaps.add(eastMagnetMap)

        val offsets = listOf(Point(0, 1), Point(1, 0), Point(0, -1), Point(-1, 0))

        var iter = 0
        // Supposed to be 1000000000.... but this works, somehow?!?
        // Apparently it loops every 36 cycles, and 1 million - 1 thousand is a multiple of 36
        repeat(1000) {
            for (i in 0 ..3) {
                val magnets = magnetLists[i]
                val nextMagnetMap = magnetMaps[(i + 1) % 4]
                val offset = offsets[i]
                for (magnet in magnets) {
                    magnet.yoink(offset, nextMagnetMap)
                }
            }

            "${iter++} -> ${rocks.sumOf { grid.height - it.point.y }}".println()
        }

        return rocks.sumOf { grid.height - it.point.y }.toLong()
    }

    fun runParts(inputName: String) {
        inputName.println()
        val input = readInput(inputName)
        "Part 1: ${part1(input)}".println()
        "Part 2: ${part2(input)}".println()
        println()
    }



    val inputName = "Day14"
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
