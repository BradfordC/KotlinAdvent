package historical.`2023`

import aoc.parseInts
import aoc.println
import aoc.readInput

fun main() {
    fun getNext(values: List<Int>): List<Int> {
        val next = mutableListOf<Int>()
        var prev = values[0]
        for (value in values.subList(1, values.size)) {
            next.add(value - prev)
            prev = value
        }
        return next
    }

    fun part1(input: List<String>): Int {
        var answer = 0
        for (line in input) {
            val series = mutableListOf(line.parseInts())
            while (series.last().any { it != 0 }) {
                series.add(getNext(series.last()))
            }
            answer += series.sumOf { it.last() }
        }
        return answer
    }

    fun part2(input: List<String>): Int {
        var answer = 0
        for (line in input) {
            val series = mutableListOf(line.parseInts())
            while (series.last().any { it != 0 }) {
                series.add(getNext(series.last()))
            }
            answer += series.map { it.first() }.reduceRight { acc, v -> acc - v}
        }
        return answer
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day09")
    part1(input).println()
    part2(input).println()
}
