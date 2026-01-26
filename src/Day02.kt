import aoc.*
import kotlinx.coroutines.runBlocking

fun main() {
    fun part1(input: List<String>): Long {
        var answer = 0L
        for (range in input[0].split(",")) {
            val first = range.split("-")[0].toLong()
            val second = range.split("-")[1].toLong()
            for (idNum in first..second) {
                val idStr = idNum.toString()
                if (idStr.length % 2 == 1) {
                    continue
                }
                val firstHalf = idStr.substring(0, idStr.length / 2)
                val secondHalf = idStr.substring(idStr.length / 2)
                if (firstHalf == secondHalf) {
                    answer += idNum
                }
            }
        }
        return answer
    }

    fun isValid(id: String): Boolean {
        for (length in 1..(id.length/2)) {
            if (id.length % length != 0) continue
            val first = id.substring(0, length)
            var mismatch = false
            for (start in length..<id.length step length) {
                if (id.substring(start, start + length) != first) {
                    mismatch = true
                    break
                }
            }
            if (!mismatch) { return false }
        }
        return true
    }

    fun part2(input: List<String>): Long {
        var answer = 0L
        for (range in input[0].split(",")) {
            val first = range.split("-")[0].toLong()
            val second = range.split("-")[1].toLong()
            for (idNum in first..second) {
                if (!isValid(idNum.toString())) answer += idNum
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



    val inputName = "Day02"
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
