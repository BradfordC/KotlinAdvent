import aoc.downloadInput
import aoc.inputExists
import aoc.println
import aoc.readInput
import kotlinx.coroutines.runBlocking

fun main() {
    fun part1(input: List<String>): Int {
        var answer = 0
        for (line in input) {

        }
        return answer
    }

    fun part2(input: List<String>): Int {
        var answer = 0
        for (line in input) {

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



    val inputName = "Day01"
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
