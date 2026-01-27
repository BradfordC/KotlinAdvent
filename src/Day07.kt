import aoc.*
import aoc.grids.Grid
import kotlinx.coroutines.runBlocking

fun main() {
    fun part1(input: List<String>): Long {
        var answer = 0L
        val grid = Grid(input)
        var beams = setOf(input.first().indexOf('S'))
        for (row in 1..<grid.height) {
            var nextBeams = mutableSetOf<Int>()
            for (beam in beams) {
                if (grid.get(beam, row).value == "^") {
                    answer+=1
                    nextBeams.add(beam - 1)
                    nextBeams.add(beam + 1)
                }
                else {
                    nextBeams.add(beam)
                }
            }
            beams = nextBeams
        }
        return answer
    }

    fun part2(input: List<String>): Long {
        val grid = Grid(input)
        var beams = mapOf(input.first().indexOf('S') to 1L)
        for (row in 1..<grid.height) {
            var nextBeams = mutableMapOf<Int, Long>()
            for (beam in beams) {
                val col = beam.key
                val count = beam.value
                if (grid.get(beam.key, row).value == "^") {
                    nextBeams[col + 1] = nextBeams.getOrPut(col + 1) { 0 } + count
                    nextBeams[col - 1] = nextBeams.getOrPut(col - 1) { 0 } + count
                }
                else {
                    nextBeams[col] = nextBeams.getOrPut(col) { 0 } + count
                }
            }
            beams = nextBeams
        }
        return beams.values.sum()
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



    val inputName = "Day07"
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
