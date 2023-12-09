package aoc.grids

class Region(val originX: Int, val originY: Int, input: List<String>): Grid(input) {
    fun toParent(x: Int, y: Int): Point {
        return Point(originX + x, originY + y)
    }

    fun toParent(point: Point): Point {
        return toParent(point.x, point.y)
    }

    override fun toString(): String {
        return "Region(originX=$originX, originY=$originY) ${super.toString()}"
    }
}