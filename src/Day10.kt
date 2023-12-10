import aoc.grids.Cell
import aoc.grids.Grid
import aoc.grids.Point
import java.lang.Exception

fun main() {

    fun nextPoint(pipe: Cell, from: Point): Point {
        // Assuming the from cell actually lines up with the pipe correctly
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
        // Assuming the from cell actually lines up with the pipe correctly
        val fromAbove = from.y < pipe.y
        val fromLeft = from.x < pipe.x
        val fromRight = from.x > pipe.x

        val goDown = Point(pipe.x, pipe.y + 1)
        val goUp = Point(pipe.x, pipe.y - 1)
        val goLeft = Point(pipe.x - 1, pipe.y)
        val goRight = Point(pipe.x + 1, pipe.y)
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
        val startCell = grid.getCell(start.originX, start.originY)

        val path = mutableListOf(startCell)

        var pipe = findPipe(grid, startCell)
        while (grid.getCell(pipe).value != "S") {
            val prev = path.last()
            val nextCell = grid.getCell(pipe)
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

    fun bucketFill(grid: Grid, point: Point, match: (Cell) -> Boolean = {true}) {
        val start = grid.getCell(point)
        val visited = mutableSetOf(start)
        val toFill = mutableListOf(start)
        while(toFill.isNotEmpty()) {
            val cell = toFill.removeLast()
            grid.setValue(cell, ".")
            val neighbors = grid.getNeighboringCells(cell, false)
            for (cell in neighbors) {
                if (visited.contains(cell) || !match(cell)) {
                    continue
                }
                toFill.add(cell)
                visited.add(cell)
            }
        }
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
                    val cell = grid.getCell(x, y)
                    if (cell.value == " ") bucketFill(grid, cell) { it.value == " "}
                }
            }
        }



//         Follow the pipes, tracking the cells on the outside of each
        val start = findStartIndex(path)
        var i = start
        val di = if (path[i + 1].x == path[i].x + 1) -1 else 1 // Figure out which direction to go so that the outside is on our right
        while (i + di != start) {
            var current = path[i]
            var nextIndex = (i + di + path.size) % path.size
            var next = path[nextIndex]
            for (outsidePoint in rightSides(next, current)) {
                if (grid.getCell(outsidePoint).value == " ") {
                    bucketFill(grid, outsidePoint) { it.value == " "}
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
