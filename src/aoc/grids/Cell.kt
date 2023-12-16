package aoc.grids


data class Cell(val point: Point, val value: String) {
    val x = point.x
    val y = point.y

    constructor(x: Int, y: Int, value: String): this(Point(x, y), value)
}