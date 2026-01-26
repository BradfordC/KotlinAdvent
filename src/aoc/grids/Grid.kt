package aoc.grids

open class Grid(val width: Int, val height: Int, defaultValue: String = ".") {

    private var data: List<MutableList<Cell>> = initData(width, height, defaultValue)

    val xs = 0 until width
    val ys = 0 until height
    var wrap = false
    var borderValue = ""
    var distanceFun = { source: Point, dest: Point -> source.distanceTo(dest) }

    constructor(input: List<String>) : this(input.minOf { it.length }, input.size) {
        require(input.minOf { it.length } == input.maxOf { it.length }) { "Grid is uneven" }
        data = input.mapIndexed { y, line -> line.mapIndexed { x, char -> Cell(x, y, char.toString()) }.toMutableList() }
    }

    constructor(other: Grid) : this(other.width, other.height) {
        for (cell in other.cells()) {
            set(cell.point.x, cell.point.y, cell.value)
        }
    }

    constructor(width: Int, height: Int, fill: (Int, Int) -> String) : this(width, height) {
        for (x in xs) {
            for (y in ys) {
                set(x, y, fill(x, y))
            }
        }
    }

    private fun initData(width: Int, height: Int, value: String): List<MutableList<Cell>> {
        return (0 until height).map { y ->
            (0 until width).map { x -> Cell(x, y, value) }.toMutableList()
        }
    }


    fun get(point: Point): Cell {
        return get(point.x, point.y)
    }

    fun get(x: Int, y: Int): Cell {
        val realX = if (wrap) x.mod(width) else x
        val realY = if (wrap) y.mod(height) else y
        return if (inBounds(realX, realY)) {
            data[realY][realX]
        } else {
            Cell(realX, realY, borderValue)
        }
    }

    fun set(point: Point, value: String) {
        set(point.x, point.y, value)
    }

    fun set(x: Int, y: Int, value: String) {
        val realX = if (wrap) x.mod(width) else x
        val realY = if (wrap) y.mod(height) else y
        if (inBounds(realX, realY)) {
            data[realY][realX] = Cell(realX, realY, value)
        }
    }

    fun inBounds(point: Point): Boolean {
        return inBounds(point.x, point.y)
    }

    fun inBounds(x: Int, y: Int): Boolean {
        return x in 0 until width && y in 0 until height
    }

    protected fun wrappedPoints(point: Point): List<Point> {
        val points = mutableListOf(point)
        if (wrap) {
            for (dy in -1..1) {
                for (dx in -1..1) {
                    if (dx == 0 && dy == 0) continue
                    points.add(Point(point.x + dx * this.width, point.y + dy * this.height))
                }
            }
        }
        return points
    }

    fun distance(source: Point, dest: Point): Double {
        return wrappedPoints(dest).minOf { distanceFun(source, it) }
    }

    fun cells(): List<Cell> {
        val cells = mutableListOf<Cell>()
        for (y in 0 until height) {
            for (x in 0 until width) {
                    cells.add(get(x, y))
            }
        }
        return cells.toList()
    }

    fun getNeighbors(point: Point, diagonal: Boolean = false, includeOob: Boolean = false): List<Cell> {
        val neighbors = mutableListOf<Cell>()
        for (dy in -1 .. 1) {
            for (dx in -1..1) {
                if (dx == 0 && dy == 0) continue
                if (!diagonal && dx != 0 && dy != 0) continue
                if (includeOob || wrap || inBounds(point.x + dx, point.y + dy)) {
                    neighbors.add(get(point.x + dx, point.y + dy))
                }
            }
        }
        return neighbors
    }

    fun fuzzySelect(point: Point, diagonal: Boolean = false): List<Cell> {
        val cell = get(point)
        return fuzzySelect(point, diagonal) { it.value == cell.value }
    }

    fun fuzzySelect(point: Point, diagonal: Boolean = false, match: (Cell) -> Boolean): List<Cell> {
        val start = get(point)
        val selected = mutableSetOf(start)
        val toAssess = mutableListOf(start)
        while(toAssess.isNotEmpty()) {
            val next = toAssess.removeLast()
            for (neighbor in getNeighbors(next.point, diagonal)) {
                if (selected.contains(neighbor) || !match(neighbor)) {
                    continue
                }
                selected.add(neighbor)
                toAssess.add(neighbor)
            }
        }
        return selected.toList()
    }

    fun getRegion(xRange: IntRange, yRange: IntRange): Region {
        val regionStrings = data.subList(yRange.first, yRange.last + 1).map { rowString(it).substring(xRange) }
        return Region(xRange.first, yRange.first, regionStrings)
    }

    fun findRegions(regex: Regex): List<Region> {
        val regions = mutableListOf<Region>()
        for ((i, row) in data.map { rowString(it) }.withIndex()) {
            for (match in regex.findAll(row)) {
                regions.add(getRegion(match.range, i..i))
            }
        }
        return regions
    }

    private fun rowString(row: List<Cell>): String {
        return row.fold("") { acc, v -> acc + v.value }
    }

    fun gridString(): String {
        return data.map { rowString(it) }.reduce { acc, v -> acc + "\n" + v }
    }

    override fun toString(): String {
        val header = "Grid(height=$height, width=$width, wrap=$wrap, borderValue='$borderValue')\n"
        return header + gridString()
    }
}