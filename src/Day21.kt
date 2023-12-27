import aoc.*
import aoc.collections.SortedList
import aoc.grids.*
import kotlinx.coroutines.runBlocking
import kotlin.math.abs
import kotlin.math.floor
import kotlin.math.min

fun main() {
    fun part1(input: List<String>): Long {
        val grid = PathfindingGrid(input)
        grid.diagonalMovement = false

        val display = PathfindingGrid(input)

        grid.walls = Regex("#")
        val startReg = grid.findRegions(Regex("S")).first()
        val start = Point(startReg.originX, startReg.originY)
        grid.set(start, "0")

        for (step in 1 .. 64) {
            for (x in 0 until grid.width) {
                for (y in 0 until grid.height) {
                    val cell = grid.get(x, y)
                    val prevPaint = (1 - (step % 2)).toString()
                    val paint = (step % 2).toString()
                    if (cell.value == prevPaint) {
                        grid.getNeighbors(cell.point, false, false)
                            .filter { it.value == "." }
                            .forEach {
                                grid.set(it.point, paint)
                                display.set(it.point, paint)
                            }
                    }
                }
            }
        }

//        display.gridString().println()

        return grid.findRegions(Regex("0")).size.toLong()
    }

    fun tile(input: List<String>, size: Int): List<String> {
        val output = mutableListOf<String>()
        repeat(size) {
            for (line in input) {
                var newLine = ""
                repeat(size) {
                    newLine += line
                }
                output.add(newLine.replace('S', '.'))
            }
        }
        val mid = output.size / 2

        val updated = StringBuilder(output[mid])
        updated.setCharAt(mid, 'S')
        output[mid] = updated.toString()

        return output
    }

    data class EntryPoint(val entry: Point, val onStep: Int)

    data class RegionMeta(val entries: MutableList<EntryPoint>, val firstStep: Int, val diagonal: Boolean)

    fun getRegion(point: Point, grid: Grid): Point {
        val regX = floor(point.x.toDouble() / grid.width).toInt()
        val regY = floor(point.y.toDouble() / grid.height).toInt()
        return Point(regX, regY)
    }

    fun findRegionMeta(grid: Grid, start: Point): List<RegionMeta> {
        val cardinals = listOf(NORTH, EAST, SOUTH, WEST)

        val regions = mutableMapOf<Point, RegionMeta>()
        val visited = mutableSetOf(start)
        var nextSteps = listOf(start)

        var step = 1
        repeat (grid.height * 2) {
            val newSteps = mutableListOf<Point>()
            for (point in nextSteps) {
                for (dir in cardinals) {
                    val next = point + dir
                    if (visited.add(next)) {
                        if (grid.get(next).value == "#") {
                            continue
                        }
                        val fromRegion = getRegion(point, grid)
                        val region = getRegion(next, grid)
                        if (abs(region.x) > 1 || abs(region.y) > 1) {
                            continue
                        }
                        if (fromRegion != region) {
                            val diagonal = region.x != 0 && region.y != 0
                            val entry = EntryPoint(grid.get(next).point, step)
                            val regionMeta = regions.getOrPut(region) { RegionMeta(mutableListOf(), step, diagonal) }
                            regionMeta.entries.add(entry)
                        }
                        newSteps.add(next)
                    }
                }
            }
            nextSteps = newSteps
            step++
        }

        return regions.values.toList()
    }

    fun countPlots(grid: Grid, entries: List<EntryPoint>, startStep: Int, endStep: Int): Long {
        val visited = entries.filter { it.onStep == startStep }.map { it.entry }.toMutableSet()
        var nextSteps = entries.filter { it.onStep == startStep }.map { it.entry }


        var answer = if (startStep % 2 == 0) visited.size.toLong() else 0
        for (i in (startStep + 1)..endStep) {
            val newSteps = mutableListOf<Point>()
            for (point in nextSteps) {
                for (next in grid.getNeighbors(point).map { it.point }) {
                    if (visited.add(next)) {
                        if (grid.get(next).value == "#") {
                            continue
                        }
                        if (i % 2 == 0) {
                            answer++
//                            grid.set(next, "O")
                        }
                        newSteps.add(next)
                    }
                }
            }
            if (newSteps.isEmpty()) {
                break
            }

            entries.filter { it.onStep == i + 1 }.forEach {
                if (visited.add(it.entry)) {
                    newSteps.add(it.entry)
                }
            }

            nextSteps = newSteps
        }

//        grid.gridString().println()

        return answer
    }

    fun part2(input: List<String>): Long {
        val grid = Grid(input)
        val start = grid.findRegions(Regex("S")).first().let { Point(it.originX, it.originY) }

        grid.wrap = true
        val regions = findRegionMeta(grid, start)
        grid.wrap = false

        val totalSteps = 10
        val cycleLength = grid.width

        val cache = mutableMapOf<Pair<RegionMeta, Int>, Long>()

        var answer = countPlots(grid, listOf(EntryPoint(start, 0)), 0, totalSteps)
        for (region in regions) {
            var count = 1
            for (step in region.firstStep..totalSteps step cycleLength) {
                val stepsInRegion = min(totalSteps - step, cycleLength * 2)
                val key = Pair(region, stepsInRegion)
                val display = Grid(input)
                val plots = cache.getOrPut(key) { countPlots(display, region.entries, step, totalSteps) }
                answer += plots * count
                if (region.diagonal) {
                    count++
                }
            }
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


    val inputName = "Day21"
    if (inputExists(inputName)) {
        if (inputExists(inputName + "_Sample")) {
            runParts(inputName + "_Sample")
        }
//        runParts(inputName)
    } else {
        var success: Boolean
        runBlocking {
            success = downloadInput(inputName)
        }
        val status = if (success) "Succeeded" else "Failed"
        "Download $status".println()
    }
}
