package historical.`2024`

import aoc.*
import aoc.grids.*
import kotlinx.coroutines.runBlocking

fun main() {
    fun part1(input: List<String>): Long {
        val directions = listOf(NORTH, EAST, SOUTH, WEST)
        val grid = Grid(input)
        grid.borderValue = "E"
        var location = grid.findRegions(Regex("\\^")).first().let { Point(it.originX, it.originY) }
        var currentDir = 0

        val visited = mutableSetOf<Pair<Point, Int>>()

        while (visited.add(Pair(location, currentDir))) {
            val destination = location + directions[currentDir]
            if (grid.get(destination).value == "#") {
                currentDir = (currentDir + 1) % 4
            }
            else if (grid.get(destination).value == "E") {
                break
            }
            else {
                grid.set(location, "X")
                location = destination
            }
        }

        return grid.findRegions(Regex("X")).size + 1L
    }

    fun loops(grid: Grid): Boolean {
        val directions = listOf(NORTH, EAST, SOUTH, WEST)
        var location = grid.findRegions(Regex("\\^")).first().let { Point(it.originX, it.originY) }
        var currentDir = 0

        val visited = mutableSetOf<Pair<Point, Int>>()

        var loopDetected = true
        while (visited.add(Pair(location, currentDir))) {
            val destination = location + directions[currentDir]
            if (grid.get(destination).value == "#") {
                currentDir = (currentDir + 1) % 4
            }
            else if (grid.get(destination).value == "E") {
                loopDetected = false
                break
            }
            else {
                location = destination
            }
        }
        return loopDetected
    }

    fun part2(input: List<String>): Long {
        val grid = Grid(input)
        grid.borderValue = "E"
        var answer = 0L
        for (cell in grid.cells()) {
            if (cell.value == ".") {
                grid.set(cell.point, "#")
                if (loops(grid)) {
                    answer++
                }
                grid.set(cell.point, ".")
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



    val inputName = "Day06"
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
