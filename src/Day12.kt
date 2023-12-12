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

    fun canFit(springs: String, at: Int, size: Int): Boolean {
        if (at + size > springs.length) {
            return false
        }
        val spring = at until at + size
        if (springs.substring(spring).contains('.')) {
            return false
        }
        val next = at + size
        return next == springs.length || springs[next] != '#'
    }

    fun shortCircuit(springs: String, groups: List<Int>): Long {
        val key = Pair(springs, groups)
        if (groups.isEmpty()) {
            return permutationsMap.getOrPut(key) {
                if (springs.any { it == '#' }) 0 else 1
            }
        }
        val minLength = groups.sum() + groups.size - 1
        if (minLength > springs.length) {
            return permutationsMap.getOrPut(key) { 0 }
        }
        if (springs.all { it == '?' } && springs.length < 20) { // Max size to prevent Long overflow (!)
            return permutationsMap.getOrPut(key) {
                val space = springs.length - minLength
                val count = (space + groups.size).toLong().choose(space.toLong())
                count
            }
        }
        return -1
    }

    fun String.strip(char: Char): String {
        if (this.isEmpty()) {
            return this
        }
        val start = this.indexOfFirst { it != char }
        val last = this.indexOfLast { it != char }
        return this.substring(start..last)
    }

    fun getPermutations(springs: String, groups: List<Int>): Long {
        val key = Pair(springs, groups)
        return permutationsMap.getOrPut(key) {
            val easy = shortCircuit(springs, groups)
            if (easy >= 0) {
                return easy
            }
            var answer = 0L
            val firstKnown = springs.indexOf('#')
            val range = if (firstKnown >= 0) 0..firstKnown else springs.indices
            for (i in range) {
                if (springs[i] != '.') {
                    if (canFit(springs, i, groups[0])) {
                        val remainingStart = i + groups[0] + 1
                        val remaining = if (remainingStart < springs.length) springs.substring(remainingStart) else ""
                        val remainingGroups = groups.subList(1, groups.size)
                        answer += getPermutations(remaining.strip('.'), remainingGroups)
                    }
                }
            }
            return answer
        }
    }

    fun part2(input: List<String>): Long {
        var answer = 0L

        for ((i, line) in input.withIndex()) {
            val halves = line.split(" ")
            val original = halves[0]
            val originalGroups = halves[1].split(",").map { it.toInt() }.toList()

            val map = List(5) { original }.reduce { acc, s -> "$acc?$s" }
            val groupSizes = mutableListOf<Int>()
            repeat(5) { groupSizes.addAll(originalGroups) }

            val permutations = getPermutations(map.strip('.'), groupSizes)
            "${i + 1} $permutations".println()
            answer += permutations
        }
        return answer
    }

    fun runParts(inputName: String) {
        inputName.println()
        val input = readInput(inputName)
//        "Part 1: ${part1(input)}".println()
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
