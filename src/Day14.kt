import aoc.downloadInput
import aoc.grids.Grid
import aoc.grids.Point
import aoc.inputExists
import aoc.println
import aoc.readInput
import kotlinx.coroutines.runBlocking

fun Grid.deposit(cell: Point, rocks: Int, dx: Int, dy: Int) {
    for (i in 0 until rocks) {
        setValue(cell.x + dx * (i + 1), cell.y + dy * (i + 1), "O")
    }
}

fun Grid.tiltNorth() {
    for (x in 0 until width) {
        var rocks = 0
        for (y in height - 1 downTo 0) {
            val cell = getCell(x, y)
            if (cell.value == "O") {
                setValue(cell, ".")
                rocks ++
            }
            if (cell.value == "#") {
                deposit(cell, rocks, 0, 1)
                rocks = 0
            }
        }
        deposit(Point(x, -1), rocks, 0, 1)
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

    fun part2(input: List<String>): Long {
        val grid = Grid(input)

        for (i in 0 until 1000000000) {
            for (dir in listOf(Pair(0,-1), Pair(-1, 0), Pair(0, 1), Pair(1, 0))) {
                grid.tiltNorth() // TODO
            }
//            "$i -> ${grid.load()}".println()
        }

        return grid.load()
    }

    fun runParts(inputName: String) {
        inputName.println()
        val input = readInput(inputName)
        "Part 1: ${part1(input)}".println()
//        "Part 2: ${part2(input)}".println()
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
