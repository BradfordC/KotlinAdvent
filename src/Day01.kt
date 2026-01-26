import aoc.*
import kotlinx.coroutines.runBlocking

fun main() {
    fun part1(input: List<String>): Long {
        var answer = 0L
        var direction = 50L
        for (line in input) {
            val rotation = line.parseInts()[0]
            if (line.startsWith('R')) {
                direction += rotation
            }
            else {
                direction -= rotation
            }
            if (direction % 100 == 0L) {
                answer += 1
            }
        }
        return answer
    }

    fun part2(input: List<String>): Long {
        var answer = 0L
        var direction = 50L
        for (line in input) {
            val fullRotations = line.parseInts()[0] / 100
            val absRotation = line.parseInts()[0] % 100
            val newDirection = direction + if (line.startsWith('R')) absRotation else -absRotation
            if (newDirection >= 100 || (newDirection <= 0 && direction != 0L)) {
                answer += 1
            }
            direction = Math.floorMod(newDirection, 100L)
            answer += fullRotations
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
            success = downloadInput(inputName, 2025)
        }
        val status = if (success) "Succeeded" else "Failed"
        "Download $status".println()
    }
}
