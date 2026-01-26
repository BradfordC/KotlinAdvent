package historical.`2024`

import aoc.*
import kotlinx.coroutines.runBlocking
import kotlin.math.abs

fun main() {
    fun part1(input: List<String>): Long {
        var answer = 0L
        for (line in input) {
            val difs = line.parseLongs()
                .windowed(2)
                .map { i -> i[1] - i[0] }
            val sameDir = difs.filter { x -> x < 0 }.size == difs.size || difs.filter { x -> x > 0 }.size == difs.size
            val smallNums = difs.map { x -> abs(x) }.filter { x -> x > 3 }.isEmpty()
            if (sameDir && smallNums) {
                answer++;
            }
        }
        return answer
    }

    fun isSafe(nums: List<Long>): Boolean {
        val difs = nums.windowed(2)
            .map { i -> i[1] - i[0] }
        val sameDir = difs.filter { x -> x < 0 }.size == difs.size || difs.filter { x -> x > 0 }.size == difs.size
        val smallNums = difs.map { x -> abs(x) }.none { x -> x > 3 }
        return sameDir && smallNums
    }

    fun part2(input: List<String>): Long {
        var answer = 0L
        for (line in input) {
            val nums = line.parseLongs()
            for ( i in nums.indices) {
                val dampedNums = nums.toMutableList()
                dampedNums.removeAt(i)
                if (isSafe(dampedNums)) {
                    answer++
                    break
                }
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
            success = downloadInput(inputName)
        }
        val status = if (success) "Succeeded" else "Failed"
        "Download $status".println()
    }
}
