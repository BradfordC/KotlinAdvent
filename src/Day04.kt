import aoc.*
import aoc.grids.Grid
import kotlinx.coroutines.runBlocking

fun main() {
    fun part1(input: List<String>): Long {
        var answer = 0L
        val grid = Grid(input)
        for (cell in grid.cells()) {
            if (
                cell.value == "@"
                && grid.getNeighbors(cell.point, diagonal = true).count { it.value == "@" } < 4
            ) {
                answer += 1
            }
        }

        return answer
    }

    fun removeRolls(grid: Grid): Grid {
        val newGrid = Grid(grid)
        for (cell in grid.cells().filter { it.value == "@" }) {
            if (grid.getNeighbors(cell.point, diagonal = true).count { it.value == "@" } < 4) {
                newGrid.set(cell.point, ".")
            }
        }
        return newGrid
    }

    fun countRolls(grid: Grid): Long {
        return grid.cells().filter { it.value == "@" }.size.toLong()
    }

    fun part2(input: List<String>): Long {
        val original = Grid(input)
        var before = Grid(input)
        var after = removeRolls(before)
        while(countRolls(before) != countRolls(after)) {
            before = after
            after = removeRolls(after)
        }
        return countRolls(original) - countRolls(after)
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



    val inputName = "Day04"
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
