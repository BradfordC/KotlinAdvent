import kotlin.math.*

fun main() {
    fun part1(input: List<String>): Int {
        var answer = 0
        for (line in input) {
            val cardStr = line.split(":")[1].split("|")[0]
            val ourNumsStr = line.split("|")[1]

            val cardNums = cardStr
                .split(" ")
                .filter { str -> str.isNotEmpty() }
                .map { str -> str.toInt() }
                .toSet()
            val ourNums = ourNumsStr
                .split(" ")
                .filter { str -> str.isNotEmpty() }
                .map { str -> str.toInt() }
                .toSet()
            val shared = cardNums.intersect(ourNums)
            if (shared.isNotEmpty()) {
                shared.size.println()
                answer += 2.0.pow(shared.size - 1).toInt()
            }
        }
        return answer
    }

    fun part2(input: List<String>): Int {
        var answer = 0
        val copies = input.map { _ -> 0 }.toMutableList()
        for ((i, line) in input.withIndex()) {
            val cardStr = line.split(":")[1].split("|")[0]
            val ourNumsStr = line.split("|")[1]

            val cardNums = cardStr
                .split(" ")
                .filter { str -> str.isNotEmpty() }
                .map { str -> str.toInt() }
                .toSet()
            val ourNums = ourNumsStr
                .split(" ")
                .filter { str -> str.isNotEmpty() }
                .map { str -> str.toInt() }
                .toSet()
            val winningNums = cardNums.intersect(ourNums).size
            answer += 1 + copies[i]
            for (j in ((i + 1)..(i + winningNums))) {
                copies[j] += 1 + copies[i]
            }
        }
        return answer
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day04")
    part1(input).println()
    part2(input).println()
}
