import aoc.*
import aoc.collections.powerSet
import aoc.grids.Point
import kotlinx.coroutines.runBlocking
import kotlin.math.abs
import kotlin.math.max
import kotlin.math.min

enum class Relation {
    CROSS,
    TOUCH,
    OVERLAP,
    NO_INTERSECTION
}

class Line(val a: Point, val b: Point) {
    fun isVertical(): Boolean {
        return a.x == b.x
    }

    fun getRelationTo(other: Line): Relation {
        if (isVertical() == other.isVertical()) {
            assert(false) { "You were supposed to avoid this" }
        }
        val vert = if(isVertical()) this else other
        val hori = if(isVertical()) other else this
        if (vert.a.x > min(hori.a.x, hori.b.x) &&
                vert.a.x < max(hori.a.x, hori.b.x) &&
                hori.a.y > min(vert.a.y, vert.b.y) &&
                hori.a.y < max(vert.a.y, vert.b.y)) {
            return Relation.CROSS
        }
        if (vert.a.x >= min(hori.a.x, hori.b.x) &&
            vert.a.x <= max(hori.a.x, hori.b.x) &&
            hori.a.y >= min(vert.a.y, vert.b.y) &&
            hori.a.y <= max(vert.a.y, vert.b.y)) {
            return Relation.TOUCH
        }
        return Relation.NO_INTERSECTION
    }
}

fun main() {
    fun part1(input: List<String>): Long {
        var answer = 0L
        val points = input
            .map { it.parseInts() }
            .map { Point(it[0], it[1]) }
        for (pair in points.powerSet(2)) {
            val dx = abs(pair[0].x - pair[1].x) + 1
            val dy = abs(pair[0].y - pair[1].y) + 1
            answer = maxOf(answer, dx * dy.toLong())
        }
        return answer
    }

    fun part2(input: List<String>): Long {
        var answer = 0L
        val points = input
            .map { it.parseInts() }
            .map { Point(it[0], it[1]) }
        val lines = points.plus(points[0])
            .windowed(2)
            .map { Line(it[0], it[1]) }
        val verticalLines = lines.filter { it.isVertical() }
        val horizontalLines = lines.filter { !it.isVertical() }

        var i = 0
        for (pair in points.powerSet(2)) {
            i++
            val dx = abs(pair[0].x - pair[1].x) + 1
            val dy = abs(pair[0].y - pair[1].y) + 1
            val area = dx * dy.toLong()
            if (area < answer) {
                continue
            }
            val corner3 = Point(pair[0].x, pair[1].y)
            val corner4 = Point(pair[1].x, pair[0].y)
            val rectSides = listOf(
                Line(pair[0], corner3),
                Line(pair[0], corner4),
                Line(pair[1], corner3),
                Line(pair[1], corner4)
            )
            var valid = true
            for (side in rectSides) {
                if (side.isVertical()) {
                    // If any of the input polygon's edges fully cross this side, this rectangle is invalid
                    if (horizontalLines.any() { it.getRelationTo(side) == Relation.CROSS } ) {
                        valid = false
                        break
                    }
                    // If any of the input polygon's edges touch, but do not cross this side,
                    // check to see if that edge goes into the rectangle. If so, this rectangle is invalid.
                    if (horizontalLines
                        .filter { it.getRelationTo(side) == Relation.TOUCH }
                        .flatMap { listOf(it.a.x, it.b.x) }
                        .any { it > min(pair[0].x, pair[1].x) && it < max(pair[0].x, pair[1].x)}
                        ) {
                        valid = false
                        break
                    }
                }
                else {
                    // If any of the input polygon's edges fully cross this side, this rectangle is invalid
                    if (verticalLines.any() { it.getRelationTo(side) == Relation.CROSS } ) {
                        valid = false
                        break
                    }
                    // If any of the input polygon's edges touch, but do not cross this side,
                    // check to see if that edge goes into the rectangle. If so, this rectangle is invalid.
                    if (verticalLines
                            .filter { it.getRelationTo(side) == Relation.TOUCH }
                            .flatMap { listOf(it.a.y, it.b.y) }
                            .any { it > min(pair[0].y, pair[1].y) && it < max(pair[0].y, pair[1].y)}
                    ) {
                        valid = false
                        break
                    }
                }
            }
            if (valid) {
                answer = area
                println("${i}: $area [${pair[0]} -> ${pair[1]}]")
            }
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



    val inputName = "Day09"
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
