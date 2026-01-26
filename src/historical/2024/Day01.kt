package historical.`2024`

import aoc.*
import kotlinx.coroutines.runBlocking
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long {
        var answer = 0L
        val leftList = mutableListOf<Int>()
        val rightList = mutableListOf<Int>()

        for (line in input) {
            val nums = line.parseInts()
            leftList.add(nums[0])
            rightList.add(nums[1])
        }
        leftList.sort()
        rightList.sort()

        for (i in 0 until leftList.size) {
            answer += abs(leftList[i] - rightList[i])
        }

        return answer
    }

    fun part2(input: List<String>): Long {
        var answer = 0L
        val leftList = mutableListOf<Long>()
        val frequency = mutableMapOf<Long, Long>()
        for (line in input) {
            val nums = line.parseLongs()
            leftList.add(nums[0])
            frequency.put(nums[1], frequency.getOrDefault(nums[1], 0) + 1)
        }

        for (i in leftList) {
            answer += i * frequency.getOrDefault(i, 0)
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
            success = downloadInput(inputName)
        }
        val status = if (success) "Succeeded" else "Failed"
        "Download $status".println()
    }
}
