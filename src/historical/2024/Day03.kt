package historical.`2024`

import aoc.*
import kotlinx.coroutines.runBlocking

fun main() {
    fun part1(input: List<String>): Long {
        var answer = 0L
        val regex = Regex("mul\\((\\d+),(\\d+)\\)")
        for (line in input) {
            for (match in regex.findAll(line)) {
                val left = match.groupValues[1].toLong()
                val right = match.groupValues[2].toLong()
                answer += left * right
            }
        }
        return answer
    }

    fun sumMuls(line: String): Long {
        val regex = Regex("mul\\((\\d+),(\\d+)\\)")
        var sum = 0L
        for (match in regex.findAll(line)) {
            val left = match.groupValues[1].toLong()
            val right = match.groupValues[2].toLong()
            sum += left * right
        }
        return sum
    }

    fun part2(input: List<String>): Long {
        var answer = 0L
        val singleLine = input.joinToString("")
        for (doSection in singleLine.split(Regex("do\\(\\)"))) {
            answer += sumMuls(doSection.split(Regex("don't\\(\\)"))[0])
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



    val inputName = "Day03"
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
