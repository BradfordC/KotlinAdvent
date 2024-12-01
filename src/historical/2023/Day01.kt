package historical.`2023`

import aoc.println
import aoc.readInput

fun main() {
    fun part1(input: List<String>): Int {
        val regex = Regex("(\\d)")

        var sum = 0
        for (line in input) {
            val matches = regex.findAll(line)
            val first = matches.first().value.toInt()
            val last = matches.last().value.toInt()
            sum += first * 10 + last
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        val valueMap = mutableMapOf(
                "one" to 1,
                "two" to 2,
                "three" to 3,
                "four" to 4,
                "five" to 5,
                "six" to 6,
                "seven" to 7,
                "eight" to 8,
                "nine" to 9
        )

        for (i in 1..9) {
            valueMap[i.toString()] = i
        }

        val valuesStr = valueMap.keys.reduce { str, value -> "$str|$value" }
        val regex = Regex("(?=($valuesStr))")

        var sum = 0
        for (line in input) {
            val matches = regex.findAll(line)
            val first = valueMap[matches.first().groupValues[1]]!!
            val last = valueMap[matches.last().groupValues[1]]!!
            sum += first * 10 + last
        }
        return sum
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day01")
    part1(input).println()
    part2(input).println()
}
