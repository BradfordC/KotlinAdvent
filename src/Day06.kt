import aoc.*
import kotlinx.coroutines.runBlocking

fun main() {
    fun part1(input: List<String>): Long {
        var answer = 0L
        val inputs = mutableListOf<List<Long>>()
        var ops = listOf<String>()
        for (line in input) {
            if (!line.contains('+')) {
                inputs.add(line.parseLongs())
            }
            else {
                ops = line.split("\\s+".toRegex()).filter { it.isNotEmpty() }
            }
        }
        for (i in ops.indices) {
            answer += inputs.map { it[i] }
                .reduce { a, b -> if (ops[i] == "*") a * b else a + b }
        }
        return answer
    }

    fun part2(input: List<String>): Long {
        var answer = 0L
        val opIndices = input.last().withIndex().filter { !it.value.isWhitespace() }.map { it.index }
        val problemRanges = opIndices.plus(input.last().length+1).windowed(2).map { IntRange(it[0], it[1] - 2) }
        for (range in problemRanges) {
            val operation = input.last()[range.first]
            val values = mutableListOf<Long>()
            for (col in range.last downTo range.first) {
                var value = ""
                for (row in 0..<input.size-1) {
                    if (!input[row][col].isWhitespace()) {
                        value += input[row][col]
                    }
                }
                values.add(value.toLong())
            }
            val result = values.reduce { a, b -> if (operation == '*') a * b else a + b }
            answer += result
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



    val inputName = "Day06"
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
