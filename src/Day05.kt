import aoc.*
import aoc.collections.split
import kotlinx.coroutines.runBlocking
import kotlin.math.max
import kotlin.math.min

fun main() {

    fun part1(input: List<String>): Long {
        var answer = 0L
        val rangeVals = input.split { it.isEmpty() }[0].map { str -> str.split("-").map { it.toLong() } }
        val ranges = rangeVals.map { LongRange(it[0], it[1]) }
        val ingredients = input.split { it.isEmpty() }[1].map { it.toLong() }

        for (ingredient in ingredients) {
            for (range in ranges) {
                if (ingredient in range) {
                    answer += 1
                    break
                }
            }
        }

        return answer
    }

    fun overlap(first: LongRange, second: LongRange): Boolean {
        return first.first <= second.last && second.first <= first.last
    }

    fun merge(first: LongRange, second: LongRange): LongRange {
        return LongRange(min(first.first, second.first), max(first.last, second.last))
    }

    fun part2(input: List<String>): Long {
        var answer = 0L
        val rangeVals = input.split { it.isEmpty() }[0].map { str -> str.split("-").map { it.toLong() } }
        val ranges = rangeVals.map { LongRange(it[0], it[1]) }.sortedBy { it.first }.toMutableList()
        val mergedRanges = mutableListOf<LongRange>()
        for (range in ranges) {
            if (mergedRanges.isEmpty()) {
                mergedRanges.add(range)
            }
            else if (overlap(range, mergedRanges.last())) {
                val merged =  merge(range, mergedRanges.last())
                mergedRanges.set(mergedRanges.size - 1, merged)
            }
            else {
                mergedRanges.add(range)
            }
        }
        for (range in mergedRanges) {
            answer += range.last - range.first + 1
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



    val inputName = "Day05"
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
