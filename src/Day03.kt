import aoc.*
import kotlinx.coroutines.runBlocking

fun main() {
    fun part1(input: List<String>): Long {
        var answer = 0L
        for (line in input) {
            val batteries = line.map { it.digitToInt() }
            val max = batteries.subList(0, batteries.size - 1).max()
            val index = batteries.indexOf(max)
            val maxSecond = batteries.subList(index + 1, batteries.size).max()
            answer += max * 10 + maxSecond
        }
        return answer
    }

    fun part2(input: List<String>): Long {
        var answer = 0L
        for (line in input) {
            val batteries = line.map { it.digitToInt() }
            var startPoint = 0
            var joltage = 0L
            for (i in 11 downTo 0 ) {
                val candidates = batteries.subList(startPoint, batteries.size - i)
                val max = candidates.max()
                startPoint = startPoint + candidates.indexOf(max) + 1
                joltage += max * Math.powExact(10L, i)
            }
            answer += joltage
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
            success = downloadInput(inputName, 2025)
        }
        val status = if (success) "Succeeded" else "Failed"
        "Download $status".println()
    }
}
