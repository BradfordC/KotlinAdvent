import aoc.grids.*
import aoc.println
import aoc.readInput
import kotlin.math.*

fun main() {
    fun part1(input: List<String>): Int {
        var answer = 0
        val grid = Grid(input)
        val slowRows = input.withIndex().filter { it.value.none { it == '#' } }.map { it.index }
        val slowCols = (0 until grid.width).filter { grid.getRegion(it..it, 0 until grid.height).gridString().none { it == '#' } }

        val galaxies = grid.findRegions(Regex("#")).map { Point(it.originX, it.originY) }
        for (i in 0 until galaxies.size - 1) {
            for (j in (i+1) until galaxies.size) {
                val start = galaxies[i]
                val dest = galaxies[j]
                var distance = abs(start.x - dest.x) + abs(start.y - dest.y)
                for (row in slowRows) {
                    if (row in min(start.y, dest.y)..max(start.y, dest.y)) {
                        distance += 1
                    }
                }
                for (col in slowCols) {
                    if (col in min(start.x, dest.x)..max(start.x, dest.x)) {
                        distance += 1
                    }
                }
                answer += distance
            }
        }
        return answer
    }

    fun part2(input: List<String>): Long {
        var answer = 0L
        val grid = Grid(input)
        val slowRows = input.withIndex().filter { it.value.none { it == '#' } }.map { it.index }
        val slowCols = (0 until grid.width).filter { grid.getRegion(it..it, 0 until grid.height).gridString().none { it == '#' } }

        val galaxies = grid.findRegions(Regex("#")).map { Point(it.originX, it.originY) }
        for (i in 0 until galaxies.size - 1) {
            for (j in (i+1) until galaxies.size) {
                val start = galaxies[i]
                val dest = galaxies[j]
                var distance = abs(start.x - dest.x) + abs(start.y - dest.y)
                for (row in slowRows) {
                    if (row in min(start.y, dest.y)..max(start.y, dest.y)) {
                        distance += 999999
                    }
                }
                for (col in slowCols) {
                    if (col in min(start.x, dest.x)..max(start.x, dest.x)) {
                        distance += 999999
                    }
                }
                answer += distance
            }
        }
        return answer
    }

    val input = readInput("Day11_Sample")
    part1(input).println()
    part2(input).println()
}
