package historical.`2023`

import aoc.*
import kotlinx.coroutines.runBlocking
import kotlin.math.pow

fun main() {

    fun checkString(string: String, groups: List<Int>): Boolean {
        val lavaRegex = Regex("#+")
        val matches = lavaRegex.findAll(string).toList()
        if (matches.size != groups.size) {
            return false
        }
        for ( (i, match) in matches.withIndex()) {
            if(groups[i] != match.value.length) {
                return false
            }
        }
        return true
    }

    fun checkInverse(string: String, stack: List<IntRange>): Boolean {
        for (i in string.withIndex().filter { it.value == '#' }.map { it.index }) {
            if (stack.none { it.contains(i) }) {
                return false
            }
        }
        return true
    }

    fun permuteString(string: String, indices: List<Int>, num: Int): String {
        var runningString = StringBuilder(string)
        for (i in indices.indices) {
            val lava = ((num / 2.0.pow(i)).toInt() % 2) == 1
            runningString.setCharAt(indices[i], if(lava) '#' else '.')
        }
        return runningString.toString()
    }

    fun oldWay(map: String, groups: List<Int>): Int {
        var answer = 0
        val unknownIndices = map.withIndex().filter { it.value == '?' }.map { it.index }
        for (i in 0 until 2.0.pow(unknownIndices.size).toInt()) {
            val permuted = permuteString(map, unknownIndices, i)
            if(checkString(permuted, groups)) {
                answer += 1
            }
        }
        return answer
    }

    fun part1(input: List<String>): Int {
        var answer = 0
        for (line in input) {
            val halves = line.split(" ")
            val map = halves[0]
            val groups = halves[1].parseInts()
            answer += oldWay(map, groups)
        }
        return answer
    }

    val permutationsMap = mutableMapOf<Pair<String, List<Int>>, Long>()

    fun String.strip(char: Char): String {
        if (this.isEmpty()) {
            return this
        }
        val start = this.indexOfFirst { it != char }
        val last = this.indexOfLast { it != char }
        return this.substring(start..last)
    }

    fun groupFits(springs: String, size: Int): Boolean {
        if (size > springs.length) {
            return false
        }
        if (springs.take(size).any { it == '.' }) {
            return false
        }
        if (size < springs.length && springs[size] == '#') {
            return false
        }
        return true
    }

    fun getPermutations(springs: String, groups: List<Int>): Long {
        val key = Pair(springs, groups)
        return permutationsMap.getOrPut(key) {
            var answer = 0L

            if (springs.isEmpty()) {
                return if (groups.isEmpty()) 1 else 0
            }
            if (groups.isEmpty()) {
                return if (springs.none { it == '#'}) 1 else 0
            }

            if (springs[0] != '#') {
                answer += getPermutations(springs.drop(1), groups)
            }

            if (springs[0] != '.') {
                if (groupFits(springs, groups[0])) {
                    answer += getPermutations(springs.drop(groups[0] + 1), groups.drop(1))
                }
            }

            answer
        }
    }

    fun part2(input: List<String>): Long {
        var answer = 0L

        for ((i, line) in input.withIndex()) {
            val halves = line.split(" ")
//            val halves = "???????????????????# 1,2,1,2,3,1".split(" ")
            val original = halves[0]
            val originalGroups = halves[1].split(",").map { it.toInt() }.toList()

            val map = List(5) { original }.reduce { acc, s -> "$acc?$s" }
            val groupSizes = mutableListOf<Int>()
            repeat(5) { groupSizes.addAll(originalGroups) }

            val permutations = getPermutations(map.strip('.'), groupSizes)
//            val permutations = getPermutations(original, originalGroups)
            "${i + 1} $permutations".println()
            answer += permutations
        }
        return answer
    }

    fun runParts(inputName: String) {
        inputName.println()
        val input = readInput(inputName)
        "Part 1: ${part1(input)}".println()
        "Part 2: ${part2(input)}".println()
        println()
    }



    val inputName = "Day12"
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
