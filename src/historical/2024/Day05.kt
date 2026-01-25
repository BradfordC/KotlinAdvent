import aoc.*
import kotlinx.coroutines.runBlocking

fun main() {
    // This code is what happens when I program when half-asleep
    fun part1(input: List<String>): Long {
        var answer = 0L
        val good = mutableMapOf<Long, MutableList<Long>>()
        val bad = mutableMapOf<Long, MutableList<Long>>()
        var inInputs = true
        for (line in input) {
            if (inInputs) {
                if (line.isEmpty()) {
                    inInputs = false
                }
                else {
                    val nums = line.parseLongs()
                    good.getOrPut(nums[0]) { mutableListOf() }.add(nums[1])
                    bad.getOrPut(nums[1]) { mutableListOf() }.add(nums[0])
                }
            }
            else {
                val rules = line.parseLongs()
                val illegals = mutableSetOf<Long>()
                var good = true
                for (page in rules) {
                    if (illegals.contains(page)) {
                        good = false
                    }
                    if (bad.contains(page)) {
                        illegals.addAll(bad[page]!!)
                    }
                }
                if (good) {
                    answer += rules[rules.size / 2]
                }
            }
        }
        return answer
    }

    fun part2(input: List<String>): Long {
        var answer = 0L
        val good = mutableMapOf<Long, MutableList<Long>>()
        val bad = mutableMapOf<Long, MutableList<Long>>()
        var inInputs = true
        for (line in input) {
            if (inInputs) {
                if (line.isEmpty()) {
                    inInputs = false
                }
                else {
                    val nums = line.parseLongs()
                    good.getOrPut(nums[0]) { mutableListOf() }.add(nums[1])
                    bad.getOrPut(nums[1]) { mutableListOf() }.add(nums[0])
                }
            }
            else {
                val rules = mutableListOf<Long>()
                rules.addAll(line.parseLongs())
                val orderedRules = mutableListOf<Long>()
                while(rules.isNotEmpty()) {
                    for (page in rules) {
                        val bads = bad.getOrDefault(page, emptyList())
                        var first = true
                        for (other in rules) {
                            if (bads.contains(other)) {
                                first = false
                            }
                        }
                        if (first) {
                            orderedRules.add(page)
                            rules.remove(page)
                            break
                        }
                    }
                }
                answer += orderedRules[orderedRules.size / 2]
            }
        }
        // Subtract part 1 answer
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
            success = downloadInput(inputName)
        }
        val status = if (success) "Succeeded" else "Failed"
        "Download $status".println()
    }
}
