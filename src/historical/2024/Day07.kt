import aoc.*
import kotlinx.coroutines.runBlocking

fun main() {

    fun canEqual(answer: Long, constants: List<Long>): Boolean {
        val ops = listOf("*", "+")
        val operators = mutableListOf<Int>()
        var result = constants[0]
        var index = 1
        var prev = 0

        while (true) {
            if (index == constants.size) {
            }
            var newResult = result
            if (index > prev) {
                operators.add(0)
                newResult = newResult * constants[index]
            }
            if (index == prev) {
                val prevOp = operators.removeLast()
                val 
            }
        }

    }

    fun part1(input: List<String>): Long {
        var answer = 0L
        canEqual(100L, listOf(10L, 9L, 5L, 5L))
        for (line in input) {
        }
        return answer
    }

    fun part2(input: List<String>): Long {
        var answer = 0L
        for (line in input) {

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
            success = downloadInput(inputName)
        }
        val status = if (success) "Succeeded" else "Failed"
        "Download $status".println()
    }
}
