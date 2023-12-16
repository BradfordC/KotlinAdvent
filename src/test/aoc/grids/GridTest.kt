package aoc.grids

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.assertThrows

internal class GridTest {

    @Test
    fun constructor() {
        val grid = Grid(5, 10)
        assertEquals(5, grid.width)
        assertEquals(10, grid.height)
        assertEquals(0 until 5, grid.xs)
        assertEquals(0 until 10, grid.ys)
        assertEquals( ".", grid.get(0, 0).value)
    }

    @Test
    fun constructor_defaultValue() {
        val grid = Grid(5, 10, "X")
        assertEquals(5, grid.width)
        assertEquals(10, grid.height)
        assertEquals(0 until 5, grid.xs)
        assertEquals(0 until 10, grid.ys)
        assertEquals( "X", grid.get(0, 0).value)
    }

    @Test
    fun constructor_fill() {
        val grid = Grid(5, 10) { x, _ -> "$x" }
        assertEquals(5, grid.width)
        assertEquals(10, grid.height)
        assertEquals(0 until 5, grid.xs)
        assertEquals(0 until 10, grid.ys)
        assertEquals("0", grid.get(0, 0).value)
        assertEquals("0", grid.get(0, 5).value)
        assertEquals("3", grid.get(3, 5).value)
        assertEquals("3", grid.get(3, 5).value)
    }

    @Test
    fun constructor_input() {
        val input = """
            ABCD
            EFGH
            IJKL
        """.trimIndent().split("\n")
        val grid = Grid(input)

        assertEquals(4, grid.width)
        assertEquals(3, grid.height)
        assertEquals(0 until 4, grid.xs)
        assertEquals(0 until 3, grid.ys)
        assertEquals("A", grid.get(0, 0).value)
        assertEquals("D", grid.get(3, 0).value)
        assertEquals("F", grid.get(1, 1).value)
        assertEquals("L", grid.get(3, 2).value)
    }

    @Test
    fun constructor_input_uneven() {
        val input = """
            ABCD
            EFGHXYZ
            IJKL
        """.trimIndent().split("\n")

        assertThrows<IllegalArgumentException> { Grid(input) }
    }

    @Test
    fun get() {
        val grid = Grid(5,10)

        assertEquals(Cell(0, 0, "."), grid.get(0, 0))
        assertEquals(Cell(0, 0, "."), grid.get(Point(0, 0)))
        assertEquals(Cell(0, 0, "."), grid.get(Cell(0, 0, "A"))) // Only care about the coordinates
        assertEquals(Cell(4, 9, "."), grid.get(4, 9))
        assertEquals(Cell(4, 9, "."), grid.get(4, 9))
        assertEquals(Cell(4, 9, "."), grid.get(Cell(4, 9, "A"))) // Only care about the coordinates
    }

    @Test
    fun get_border() {
        val grid = Grid(5,10)

        assertEquals(Cell(-1, 0, ""), grid.get(-1, 0))
        assertEquals(Cell(0, -1, ""), grid.get(0, -1))
        assertEquals(Cell(4, 10, ""), grid.get(4, 10))
        assertEquals(Cell(5, 9, ""), grid.get(5, 9))

        grid.borderValue = "#"
        assertEquals(Cell(-1, 0, "#"), grid.get(-1, 0))
        assertEquals(Cell(0, -1, "#"), grid.get(0, -1))
        assertEquals(Cell(4, 10, "#"), grid.get(4, 10))
        assertEquals(Cell(5, 9, "#"), grid.get(5, 9))
    }

    @Test
    fun get_wrap() {
        val input = """
            ABC
            DEF
            GHI
        """.trimIndent().split("\n")

        val grid = Grid(input)
        grid.wrap = true

        assertEquals(Cell(2, 0, "C"), grid.get(-1, 0))
        assertEquals(Cell(0, 2, "G"), grid.get(0, -1))
        assertEquals(Cell(0, 2, "G"), grid.get(3, 2))
        assertEquals(Cell(2, 0, "C"), grid.get(2, 3))
    }

    @Test
    fun set() {
        val grid = Grid(5, 10)
        grid.set(1, 1, "A")
        grid.set(3, 1, "B")
        grid.set(-1, -1, "X") // Out of bounds

        assertEquals("A", grid.get(1, 1).value)
        assertEquals("B", grid.get(3, 1).value)
        assertEquals("", grid.get(-1, -1).value) // Out of bounds
    }

    @Test
    fun set_wrap() {
        val grid = Grid(5, 10)
        grid.wrap = true
        grid.set(-1, -1, "X")

        assertEquals(Cell(4, 9, "X"), grid.get(-1, -1))
        assertEquals(Cell(4, 9, "X"), grid.get(4, 9))
    }

    @Test
    fun cells() {
        val grid = Grid(5, 10)

        val cells = grid.cells()

        assertEquals(50, cells.size)
        assertEquals(Cell(0, 0, "."), cells[0])
        assertEquals(Cell(1, 0, "."), cells[1])
        assertEquals(Cell(0, 1, "."), cells[5])
        assertEquals(Cell(1, 1, "."), cells[6])
    }

    @Test
    fun getNeighbors() {
        val input = """
            ABC
            DEF
            GHI
        """.trimIndent().split("\n")

        val grid = Grid(input)
        var neighbors = grid.getNeighbors(Point(0, 0))
        assertEquals(2, neighbors.size)
        assertEquals("B", neighbors[0].value)
        assertEquals("D", neighbors[1].value)

        neighbors = grid.getNeighbors(Point(1, 1))
        assertEquals(4, neighbors.size)
        assertEquals("B", neighbors[0].value)
        assertEquals("D", neighbors[1].value)
        assertEquals("F", neighbors[2].value)
        assertEquals("H", neighbors[3].value)

        neighbors = grid.getNeighbors(Point(2, 3))
        assertEquals(1, neighbors.size)
        assertEquals("I", neighbors[0].value)
    }

    @Test
    fun getNeighbors_diagonal() {
        val input = """
            ABC
            DEF
            GHI
        """.trimIndent().split("\n")

        val grid = Grid(input)
        var neighbors = grid.getNeighbors(Point(0, 0), diagonal = true)
        assertEquals(3, neighbors.size)
        assertEquals("B", neighbors[0].value)
        assertEquals("D", neighbors[1].value)
        assertEquals("E", neighbors[2].value)

        neighbors = grid.getNeighbors(Point(1, 1), diagonal = true)
        assertEquals(8, neighbors.size)
        assertEquals("A", neighbors[0].value)
        assertEquals("B", neighbors[1].value)
        assertEquals("C", neighbors[2].value)
        assertEquals("D", neighbors[3].value)
        assertEquals("F", neighbors[4].value)
        assertEquals("G", neighbors[5].value)
        assertEquals("H", neighbors[6].value)
        assertEquals("I", neighbors[7].value)

        neighbors = grid.getNeighbors(Point(2, 3), diagonal = true)
        assertEquals(2, neighbors.size)
        assertEquals("H", neighbors[0].value)
        assertEquals("I", neighbors[1].value)
    }

    @Test
    fun getNeighbors_includeOob() {
        val input = """
            ABC
            DEF
            GHI
        """.trimIndent().split("\n")

        val grid = Grid(input)

        val neighbors = grid.getNeighbors(Point(0, 0), includeOob = true)
        assertEquals(4, neighbors.size)
        assertEquals(Cell(0, -1, ""), neighbors[0])
        assertEquals(Cell(-1, 0, ""), neighbors[1])
        assertEquals(Cell(1, 0, "B"), neighbors[2])
        assertEquals(Cell(0, 1, "D"), neighbors[3])
    }

    @Test
    fun getNeighbors_wrap() {
        val input = """
            ABC
            DEF
            GHI
        """.trimIndent().split("\n")

        val grid = Grid(input)
        grid.wrap = true

        val neighbors = grid.getNeighbors(Point(0, 0), diagonal = true)
        assertEquals(8, neighbors.size)
        assertEquals(Cell(2, 2, "I"), neighbors[0])
        assertEquals(Cell(0, 2, "G"), neighbors[1])
        assertEquals(Cell(1, 2, "H"), neighbors[2])
        assertEquals(Cell(2, 0, "C"), neighbors[3])
        assertEquals(Cell(1, 0, "B"), neighbors[4])
        assertEquals(Cell(2, 1, "F"), neighbors[5])
        assertEquals(Cell(0, 1, "D"), neighbors[6])
        assertEquals(Cell(1, 1, "E"), neighbors[7])
    }

    @Test
    fun fuzzySelect() {
        val input = """
            O.O
            O..
            .O.
        """.trimIndent().split("\n")

        val grid = Grid(input)

        val selected = grid.fuzzySelect(Point(0, 0))
        assertEquals(2, selected.size)
        assertTrue(selected.any { it == Cell(0, 0, "O") })
        assertTrue(selected.any { it == Cell(0, 1, "O") })
    }

    @Test
    fun fuzzySelect_diagonal() {
        val input = """
            O.O
            O..
            .O.
        """.trimIndent().split("\n")

        val grid = Grid(input)

        val selected = grid.fuzzySelect(Point(0, 0), true)
        assertEquals(3, selected.size)
        assertTrue(selected.any { it == Cell(0, 0, "O") })
        assertTrue(selected.any { it == Cell(0, 1, "O") })
        assertTrue(selected.any { it == Cell(1, 2, "O") })
    }

    @Test
    fun fuzzySelect_wrap() {
        val input = """
            O.O
            O..
            .O.
        """.trimIndent().split("\n")

        val grid = Grid(input)
        grid.wrap = true

        val selected = grid.fuzzySelect(Point(0, 0))
        assertEquals(3, selected.size)
        assertTrue(selected.any { it == Cell(0, 0, "O") })
        assertTrue(selected.any { it == Cell(0, 1, "O") })
        assertTrue(selected.any { it == Cell(2, 0, "O") })
    }

    @Test
    fun fuzzySelect_custom() {
        val input = """
            a1b
            3dg
            4e5
        """.trimIndent().split("\n")

        val grid = Grid(input)

        val selected = grid.fuzzySelect(Point(0, 0)) { it.value.toIntOrNull() != null }
        assertEquals(4, selected.size)
        assertTrue(selected.any { it == Cell(0, 0, "a") }) // Original cell is always chosen
        assertTrue(selected.any { it == Cell(1, 0, "1") })
        assertTrue(selected.any { it == Cell(0, 1, "3") })
        assertTrue(selected.any { it == Cell(0, 2, "4") })
    }

    @Test
    fun getRegion() {
        val input = """
            abc123
            suv456
            xyz789
        """.trimIndent().split("\n")

        val grid = Grid(input)
        val region = grid.getRegion(1..3, 1..2)

        assertEquals(1, region.originX)
        assertEquals(1, region.originY)
        assertEquals(3, region.width)
        assertEquals(2, region.height)
        assertEquals("u", region.get(0, 0).value)
        assertEquals("4", region.get(2, 0).value)
        assertEquals("z", region.get(1, 1).value)
    }

    @Test
    fun findRegions() {
        val input = """
            abc123
            suv456
            xyz789
        """.trimIndent().split("\n")

        val grid = Grid(input)
        val regions = grid.findRegions(Regex("\\d+"))

        assertEquals(3, regions.size)
        assertTrue(regions.all { it.width == 3 && it.height == 1 })
        assertEquals("1", regions[0].get(0, 0).value)
        assertEquals("4", regions[1].get(0, 0).value)
        assertEquals("7", regions[2].get(0, 0).value)
    }

    @Test
    fun inBounds() {
        val grid = Grid(3, 5)

        assertTrue(grid.inBounds(0, 0))
        assertTrue(grid.inBounds(2, 0))
        assertTrue(grid.inBounds(0, 4))
        assertTrue(grid.inBounds(2, 4))

        assertFalse(grid.inBounds(-1, 0))
        assertFalse(grid.inBounds(0, -1))
        assertFalse(grid.inBounds(3, 4))
        assertFalse(grid.inBounds(2, 5))

        grid.wrap = false
        assertFalse(grid.inBounds(-1, 0))
        assertFalse(grid.inBounds(0, -1))
        assertFalse(grid.inBounds(3, 4))
        assertFalse(grid.inBounds(2, 5))
    }
}