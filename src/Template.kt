import aoc.*
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
        return answer + 1
    }

    fun runParts(inputName: String) {
        inputName.println()
        val input = readInput(inputName)

        val p1 = part1(input)
        "Part 1: $p1".println()
        if (p1 != 0) p1.toClipboard()

        val p2 = part2(input)
        "Part 2: $p2".println()
        if (p2 != 0) p2.toClipboard()

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
