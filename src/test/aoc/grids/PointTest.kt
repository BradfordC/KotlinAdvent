package aoc.grids

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class PointTest {

    private val origin = Point(0, 0)

    @Test
    fun distanceTo() {
        assertEquals(1.0, origin.distanceTo(Point(1, 0)))
        assertEquals(1.0, origin.distanceTo(Point(0, 1)))
        assertEquals(1.0, origin.distanceTo(Point(-1, 0)))
        assertEquals(1.0, origin.distanceTo(Point(0, -1)))

        assertEquals(1.41, origin.distanceTo(Point(1, 1)), .01)
        assertEquals(5.83, origin.distanceTo(Point(5, 3)), .01)
        assertEquals(7.61, origin.distanceTo(Point(-7, 3)), .01)

    }

    @Test
    fun distanceChessTo() {
        assertEquals(1.0, origin.distanceChessTo(Point(1, 0)))
        assertEquals(1.0, origin.distanceChessTo(Point(0, 1)))
        assertEquals(1.0, origin.distanceChessTo(Point(-1, 0)))
        assertEquals(1.0, origin.distanceChessTo(Point(0, -1)))

        assertEquals(1.0, origin.distanceChessTo(Point(1, 1)))
        assertEquals(5.0, origin.distanceChessTo(Point(5, 3)))
        assertEquals(7.0, origin.distanceChessTo(Point(-7, 3)))
    }

    @Test
    fun distanceManhattanTo() {
        assertEquals(1.0, origin.distanceManhattanTo(Point(1, 0)))
        assertEquals(1.0, origin.distanceManhattanTo(Point(0, 1)))
        assertEquals(1.0, origin.distanceManhattanTo(Point(-1, 0)))
        assertEquals(1.0, origin.distanceManhattanTo(Point(0, -1)))

        assertEquals(2.0, origin.distanceManhattanTo(Point(1, 1)))
        assertEquals(8.0, origin.distanceManhattanTo(Point(5, 3)))
        assertEquals(10.0, origin.distanceManhattanTo(Point(-7, 3)))
    }

    @Test
    fun plus() {
        val start = Point(2, 1)

        assertEquals(Point(2, 1), start + Point(0, -0))
        assertEquals(Point(3, 2), start + Point(1, 1))
        assertEquals(Point(0, 0), start + Point(-2, -1))
        assertEquals(Point(-3, 6), start + Point(-5, 5))
    }

    @Test
    fun times() {
        val start = Point(2, 1)

        assertEquals(Point(-2, -1), start * -1)
        assertEquals(Point(0, 0), start * 0)
        assertEquals(Point(2, 1), start * 1)
        assertEquals(Point(10, 5), start * 5)
    }

    @Test
    fun directions() {
        assertEquals(Point(0, -1), origin + NORTH)
        assertEquals(Point(1, 0), origin + EAST)
        assertEquals(Point(0, 1), origin + SOUTH)
        assertEquals(Point(-1, 0), origin + WEST)
        assertEquals(Point(1, -1), origin + NE)
        assertEquals(Point(1, 1), origin + SE)
        assertEquals(Point(-1, 1), origin + SW)
        assertEquals(Point(-1, -1), origin + NW)

        var current = Point(5, 5)
        current += NORTH * 5
        assertEquals(Point(5, 0), current)
    }
}