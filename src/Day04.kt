import aoc.*
import aoc.grids.Grid
import aoc.grids.Point
import kotlinx.coroutines.runBlocking

fun main() {
    fun part1(input: List<String>): Long {
        var answer = 0L
        val grid = Grid(input)
        for (x in grid.findRegions(Regex("X"))) {
            for (dx in -1 .. 1) {
                for (dy in -1 .. 1) {
                    if (grid.get(x.originX + dx, x.originY + dy).value == "M"
                        && grid.get(x.originX + dx * 2, x.originY + dy * 2).value == "A"
                        && grid.get(x.originX + dx * 3, x.originY + dy * 3).value == "S") {
                        answer += 1
                    }
                }
            }
        }
        return answer
    }

    fun part2(input: List<String>): Long {
        var answer = 0L
        val grid = Grid(input)
        for (x in grid.findRegions(Regex("A"))) {
            val xp = Point(x.originX, x.originY)
            val diag1 = listOf(grid.get(xp + Point(-1, -1)).value, grid.get(xp + Point(1, 1)).value)
            val diag2 = listOf(grid.get(xp + Point(1, -1)).value, grid.get(xp + Point(-1, 1)).value)
            if (diag1.contains("M") && diag1.contains("S") && diag2.contains("M") && diag2.contains("S")) {
                answer++
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
            success = downloadInput(inputName)
        }
        val status = if (success) "Succeeded" else "Failed"
        "Download $status".println()
    }
}
