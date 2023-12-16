package aoc.grids

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

data class Point(val x: Int, val y: Int) {
    fun distanceTo(other: Point): Double {
        val dx = (x - other.x).toDouble()
        val dy = (y - other.y).toDouble()
        return sqrt(dx.pow(2) + dy.pow(2))
    }

    fun distanceChessTo(other: Point): Double {
        val dx = (x - other.x).absoluteValue.toDouble()
        val dy = (y - other.y).absoluteValue.toDouble()
        return max(dx, dy)
    }

    fun distanceManhattanTo(other: Point): Double {
        val dx = (x - other.x).absoluteValue.toDouble()
        val dy = (y - other.y).absoluteValue.toDouble()
        return dx + dy;
    }

    operator fun plus(other: Point): Point {
        return Point(x + other.x, y + other.y)
    }

    operator fun times(v: Int): Point {
        return Point(x * v, y * v)
    }
}