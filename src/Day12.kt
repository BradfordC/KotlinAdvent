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

    fun String.supportsBlock(block: IntRange): Boolean {
        if (block.last >= this.length) {
            return false
        }
        if (this.substring(block).contains('.')) {
            return false
        }
        val leftEdge = block.first - 1
        if (leftEdge >= 0 && this[leftEdge] == '#') {
            return false
        }
        val rightEdge = block.last + 1
        if (rightEdge < length && this[rightEdge] == '#') {
            return false
        }
        return true
    }

    fun MutableList<IntRange>.advanceTopBlock(startingPoints: Map<Int, List<Int>>): Boolean {
        val block = this.removeLast()
        val length = block.last - block.first + 1
        val starts = startingPoints[length]!!
        for (start in starts.filter { it > block.first }) {
            val newBlock = start until (start + length)
            if (starts.contains(start)) {
                this.add(newBlock)
                return true
            }
        }
        return false
    }

    fun MutableList<IntRange>.makeProgress(startingPoints: Map<Int, List<Int>>) {
        while (this.isNotEmpty()) {
            if (advanceTopBlock(startingPoints)) {
                break
            }
        }
    }

    fun MutableList<IntRange>.placeNextBlock(startingPoints: List<Int>, size: Int): Boolean {
        val end = lastOrNull()?.last ?: -2
        for (start in startingPoints.filter { it > end + 1 }) {
            add(start until start + size)
            return true
        }
        return false
    }

    fun MutableList<IntRange>.visualize(map: String) {
        val blocks = StringBuilder()
        for (i in 0 until map.length) {
            if (this.any { it.contains(i) }) {
                blocks.append("_")
            }
            else {
                blocks.append(" ")
            }
        }
        "$blocks".println()
        map.println()
    }

    fun newWay(map: String, groupSizes: List<Int>): Long {
        var answer = 0L

        val sizeToStarts = { size: Int ->
            map
                .withIndex()
                .filter { it.value != '.' }
                .map { it.index }
                .filter { map.supportsBlock(it until it + size) }
                .toList()
        }

        val starts = groupSizes
            .distinct()
            .associateWith { sizeToStarts(it) }

        val stack = mutableListOf<IntRange>()
        stack.placeNextBlock(starts[groupSizes[0]]!!, groupSizes[0])
        var i = 0
        while (stack.isNotEmpty()) {
            if(stack.size < groupSizes.size) {
                val nextSize = groupSizes[stack.size]
                if(!stack.placeNextBlock(starts[nextSize]!!, nextSize)) {
                    stack.makeProgress(starts)
                }
            }
            else {
                stack.makeProgress(starts)
            }

            if(stack.size == groupSizes.size) {
                if(checkInverse(map, stack)) {
                    stack.visualize(map)
                    answer++
                }
            }
        }
        return answer
    }

    fun part2(input: List<String>): Long {
        var answer = 0L
        for ((i, line) in input.withIndex()) {
            val halves = line.split(" ")
            val original = halves[0]
            val originalSizes = halves[1].parseInts()

            val map = List(5) { original }.reduce { acc, s -> "$acc?$s" }
            val groupSizes = mutableListOf<Int>()
            repeat(5) { groupSizes.addAll(halves[1].parseInts()) }

            val myAnswer = newWay(map, groupSizes)
            "${i + 1}: $myAnswer".println()
            answer += myAnswer
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
