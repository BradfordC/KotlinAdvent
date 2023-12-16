import aoc.grids.Cell
import aoc.grids.Grid
import aoc.grids.Point
import aoc.println
import aoc.readInput
import java.lang.Exception

fun main() {

    fun nextPoint(pipe: Cell, from: Point): Point {
        // Assuming the "from" cell actually lines up with the pipe correctly
        val fromAbove = from.y < pipe.y
        val fromLeft = from.x < pipe.x
        val fromRight = from.x > pipe.x

        val goDown = Point(pipe.x, pipe.y + 1)
        val goUp = Point(pipe.x, pipe.y - 1)
        val goLeft = Point(pipe.x - 1, pipe.y)
        val goRight = Point(pipe.x + 1, pipe.y)
        return when (pipe.value) {
            "|" -> if (fromAbove) goDown else goUp
            "-" -> if (fromLeft) goRight else goLeft
            "L" -> if (fromAbove) goRight else goUp
            "J" -> if (fromAbove) goLeft else goUp
            "7" -> if (fromLeft) goDown else goLeft
            "F" -> if (fromRight) goDown else goRight
            else -> throw Exception("Unknown Pipe")
        }
    }

    fun rightSides(pipe: Cell, from: Point): List<Point> {
        // Assuming the "from" cell actually lines up with the pipe correctly
        val fromAbove = from.y < pipe.y
        val fromLeft = from.x < pipe.x
        val fromRight = from.x > pipe.x

        val goDown = Point(pipe.x, pipe.y + 1)
        val goUp = Point(pipe.x, pipe.y - 1)
        val goLeft = Point(pipe.x - 1, pipe.y)
        val goRight = Point(pipe.x + 1, pipe.y)
        // Hard code S since it's easier to manually check where it should go than write a function to detect it
        return when (pipe.value) {
            "|" -> if (fromAbove) listOf(goLeft) else listOf(goRight)
            "-" -> if (fromLeft) listOf(goDown) else listOf(goUp)
            "L" -> if (fromAbove) listOf(goLeft, goDown) else listOf()
            "J" -> if (fromAbove) listOf() else listOf(goDown, goRight)
            "7", "S" -> if (fromLeft) listOf() else listOf(goRight, goUp)
            "F" -> if (fromRight) listOf(goUp, goLeft) else listOf()
            else -> throw Exception("Unknown Pipe")
        }
    }

    fun findPipe(grid: Grid, start: Point): Point {
        // LUL
        return Point(start.x, start.y + 1)
    }

    fun findPath(grid: Grid): List<Cell> {
        val start = grid.findRegions(Regex("S")).first()
        val startCell = grid.get(start.originX, start.originY)

        val path = mutableListOf(startCell)

        var pipe = findPipe(grid, startCell)
        while (grid.get(pipe).value != "S") {
            val prev = path.last()
            val nextCell = grid.get(pipe)
            path.add(nextCell)
            pipe = nextPoint(nextCell, prev)
        }

        return path
    }

    fun findStartIndex(path: List<Cell>): Int {
        // Find the point on the path that is to the leftmost of the highest row
        var min = Point(140, 140)
        var minI = -1
        for ((i, point) in path.withIndex()) {
            if(point.y < min.y || (point.y == min.y && point.x < min.x)) {
                min = point
                minI = i
            }
        }
        return minI
    }

    fun part1(input: List<String>): Int {
        val grid = Grid(input)
        val path = findPath(grid)
        return path.size / 2
    }

    fun part2(input: List<String>): Int {
        val grid = Grid(input)
        val path = findPath(grid)

        // Clear everything that isn't part of the main pipe
        for (x in 0..grid.width) {
            for (y in 0..grid.height) {
                grid.setValue(x, y, " ")
            }
        }
        for (cell in path) {
            grid.setValue(cell, cell.value)
        }

        // Fill the borders
        for (x in 0 until grid.width) {
            for (y in 0 until grid.height) {
                if (x == 0 || x == grid.width - 1 || y == 0 || y == grid.height - 1) {
                    val cell = grid.get(x, y)
                    if (cell.value == " ") {
                        grid.fuzzySelect(cell).forEach { grid.setValue(it, ".") }
                    }
                }
            }
        }

        // Follow the pipes, tracking the cells on the outside of each
        val start = findStartIndex(path)
        var i = start
        val di = if (path[i + 1].x == path[i].x + 1) -1 else 1 // Figure out which direction to go so that the outside is on our right
        while (i + di != start) {
            val current = path[i]
            val nextIndex = (i + di + path.size) % path.size
            val next = path[nextIndex]
            for (outsidePoint in rightSides(next, current)) {
                val cell = grid.get(outsidePoint)
                // Mark any outside cells as outside
                if (cell.value == " ") {
                    grid.fuzzySelect(cell).forEach { grid.setValue(it, ".") }
                }
            }
            i = nextIndex
        }

        return grid.findRegions(Regex(" ")).size
    }

    val input = readInput("Day10")
    part1(input).println()
    part2(input).println()
}
