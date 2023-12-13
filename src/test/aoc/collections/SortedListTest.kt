package aoc.collections

import aoc.grids.Point
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.assertThrows
import java.lang.ClassCastException
import kotlin.math.abs

internal class SortedListTest {

    private var list = SortedList<Int>()

    @BeforeEach
    fun setUp() {
        list = SortedList()
    }

    @Test
    fun add() {
        list.add(5)
        list.add(1)
        list.add(3)
        list.add(-1)

        assertEquals(listOf(-1, 1, 3, 5), list)
    }

    @Test
    fun add_CustomComparator() {
        list = SortedList(compareBy { abs(it) })
        list.add(5)
        list.add(1)
        list.add(3)
        list.add(-5)

        assertEquals(listOf(1, 3, -5, 5), list)
    }

    @Test
    fun add_Incomparable() {
        var pointList = SortedList<Point>()
        pointList.add(Point(1, 1))
        assertThrows<ClassCastException> { pointList.add(Point(2, 2)) }

        pointList = SortedList(compareBy { it.x * 10 + it.y })
        pointList.add(Point(5, 3))
        pointList.add(Point(3, 5))
        pointList.add(Point(4, 4))

        assertEquals(Point(3, 5), pointList[0])
        assertEquals(Point(4, 4), pointList[1])
        assertEquals(Point(5, 3), pointList[2])
    }

    @Test
    fun addAll() {
        list.addAll(listOf(5, 3, 1, 4, 2))

        assertEquals(listOf(1, 2, 3, 4, 5), list)
    }

    @Test
    fun removeAt() {
        list.add(3)
        list.add(1)
        list.add(2)

        list.removeAt(1)

        assertEquals(listOf(1, 3), list)
    }

    @Test
    fun remove() {
        list.add(3)
        list.add(1)
        list.add(2)

        list.remove(2)

        assertEquals(listOf(1, 3), list)
    }

    @Test
    fun iterate() {
        list.add(3)
        list.add(1)
        list.add(2)

        var i = 1
        for (num in list) {
            assertEquals(i, num)
            i++
        }
    }
}