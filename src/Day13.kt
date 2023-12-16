import aoc.downloadInput
import aoc.grids.Grid
import aoc.inputExists
import aoc.println
import aoc.readInput
import kotlinx.coroutines.runBlocking

fun main() {

    fun Grid.getCol(x: Int): String {
        return this.getRegion(x..x, 0 until this.height).gridString()
    }

    fun Grid.getRow(y: Int): String {
        return this.getRegion(0 until this.width, y..y).gridString()
    }

    fun getXReflection(grid: Grid, ignore: Int? = null): Int {
        for (x in 0 until grid.width - 1) {
            if (x == ignore) {
                continue
            }
            var isReflection = true
            for (z in 0..x) {
                val refl = 2 * x - z + 1
                if (refl in 0 until grid.width && grid.getCol(z) != grid.getCol(refl)) {
                    isReflection = false
                }
            }
            if(isReflection) {
                return x + 1
            }
        }
        return -1
    }

    fun getYReflection(grid: Grid, ignore: Int? = null): Int {
        for (y in 0 until grid.height - 1) {
            if (y == ignore) {
                continue
            }
            var isReflection = true
            for (z in 0..y) {
                val refl = 2 * y - z + 1
                if (refl in 0 until grid.height && grid.getRow(z) != grid.getRow(refl)) {
                    isReflection = false
                }
            }
            if(isReflection) {
                return (y + 1)
            }
        }
        return -1
    }

    fun part1(input: List<String>): Int {
        var answer = 0
        val blankLines = input.withIndex().partition { it.value.isNotEmpty() }.second.map { it.index }
        val indices = listOf(-1) + blankLines + listOf(input.size)

        for (window in indices.windowed(2)) {
            val grid = Grid(input.subList(window[0] + 1, window[1]))

            val xReflection = getXReflection(grid)
            if (xReflection >= 0) {
                answer += xReflection
            }

            val yReflection = getYReflection(grid)
            if (yReflection >= 0) {
                answer += yReflection * 100
            }
        }
        return answer
    }

    fun part2(input: List<String>): Int {
        var answer = 0
        val blankLines = input.withIndex().partition { it.value.isNotEmpty() }.second.map { it.index }
        val indices = listOf(-1) + blankLines + listOf(input.size)

        for (window in indices.windowed(2)) {
            val grid = Grid(input.subList(window[0] + 1, window[1]))

            val xReflection = getXReflection(grid)
            val yReflection = getYReflection(grid)

            var addedValue = -1
            for (x in 0 until grid.width) {
                for (y in 0 until grid.height) {
                    val newGrid = Grid(input.subList(window[0] + 1, window[1]))
                    val oldValue = newGrid.get(x, y).value
                    newGrid.set(x, y, if (oldValue == ".") "#" else ".")

                    val newX = getXReflection(newGrid, xReflection - 1)
                    if (newX >= 0) {
                        addedValue = newX
                    }

                    val newY = getYReflection(newGrid, yReflection - 1)
                    if (newY >= 0) {
                        addedValue = newY * 100
                    }
                }
            }
            answer += addedValue
        }
        return answer
    }

    fun runParts(inputName: String) {
        inputName.println()
        val input = readInput(inputName)
        "Part 1: ${part1(input)}".println()
        "Part 2: ${part2(input)}".println()
        println()
    }



    val inputName = "Day13"
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
