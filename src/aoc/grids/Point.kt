package aoc.grids

import kotlin.math.absoluteValue
import kotlin.math.max
import kotlin.math.pow
import kotlin.math.sqrt

open class Point(val x: Int, val y: Int) {
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

    override fun toString(): String {
        return "Point($x,$y)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Point

        if (x != other.x) return false
        if (y != other.y) return false

        return true
    }

    override fun hashCode(): Int {
        var result = x
        result = 31 * result + y
        return result
    }
}