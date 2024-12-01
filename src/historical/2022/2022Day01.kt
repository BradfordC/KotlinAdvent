package historical.`2022`

import aoc.println
import aoc.readInput
import kotlin.math.*

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        var max = 0
        for (line in input) {
            try {
                sum += line.toInt()
            } catch (e: NumberFormatException) {
                max = max(sum, max)
                sum = 0
            }
        }
        return max
    }

    fun part2(input: List<String>): Int {
        val biggest = mutableListOf<Int>()
        var elfSum = 0
        for (line in input) {
            try {
                elfSum += line.toInt()
            } catch (e: NumberFormatException) {
                biggest.add(elfSum)
                if (biggest.size > 3) {
                    biggest.sort()
                    biggest.removeAt(0)
                }
                elfSum = 0
            }
        }
        return biggest.reduce { sum, value -> sum + value }
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("2022Day01")
    part1(input).println()
    part2(input).println()
}
