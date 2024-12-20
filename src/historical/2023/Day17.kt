package historical.`2023`

import aoc.*
import aoc.collections.SortedList
import aoc.grids.*
import kotlinx.coroutines.runBlocking

class MyGrid(input: List<String>, val ultra: Boolean) : PathfindingGrid(input) {

    private val minMovement = if (ultra) 4 else 1
    private val maxMovement = if (ultra) 10 else 3

    override fun findPath(source: Point, dest: Point): List<Cell> {
        val visited = mutableMapOf<Point, MutableList<Node>>()

        val toVisit = SortedList<Node>(compareBy { it.travelled + it.heuristic })
        toVisit.addAll(startingNodes(source, dest))

        var goal: Node? = null
        while (toVisit.isNotEmpty() && goal == null) {
            val next = toVisit.removeFirst()
            if (!worthConsidering(visited, next, dest)) {
                continue
            }
            val previous = visited[next.state.point] ?: mutableListOf()
            previous.add(next)
            visited[next.state.point] = previous
            if (next.state.point == dest) {
                goal = next
            } else {
                val following = nextNodes(next, dest).filter { worthConsidering(visited, it, dest) }
                toVisit.addAll(following)
            }
        }

        if (goal == null) {
            return emptyList()
        }
        val path = mutableListOf<Point>()
        var current = goal
        while (current != null) {
            path.add(current.state.point)
            val locations = visited[current.cameFrom.point] ?: emptyList()
            current = locations.firstOrNull { it.state == current!!.cameFrom }
        }

        return path.reversed().map { get(it) }
    }

    private fun startingNodes(source: Point, dest: Point): List<Node> {
        val dirs = listOf(NORTH, SOUTH, EAST, WEST)
        return dirs.filter { inBounds(source + it) }
            .map {
                Node(
                    State(source + it, it, 1),
                    State(source, Point(0, 0), 0),
                    distance(source, source + it),
                    heuristic(source + it, dest) + .01
                )
            }
    }

    private fun nextNodes(from: Node, dest: Point): List<Node> {
        val dir = from.state.dir

        val dirs = mutableListOf<Point>()
        // Go straight
        if (from.state.straight < maxMovement) {
            dirs.add(dir)
        }
        // Turn
        if (from.state.straight >= minMovement) {
            dirs.add(Point(dir.y, dir.x))
            dirs.add(Point(-dir.y, -dir.x))
        }

        return dirs
            .map {
                val next = from.state.point + it
                val straight = if (it == from.state.dir) from.state.straight + 1 else 1
                State(next, it, straight)
            }
            .filter { inBounds(it.point) }
            .map { key ->
                val travelled = from.travelled + distance(from.state.point, key.point)
                val heur = heuristic(key.point, dest) + key.straight * .01
                Node(key, from.state, travelled, heur)
            }
    }

    private fun worthConsidering(visited: Map<Point, List<Node>>, node: Node, dest: Point): Boolean {
        if (node.state.straight < minMovement && node.state.point == dest) {
            return false
        }
        val previousVisits = visited[node.state.point] ?: return true
        if (previousVisits
                .filter { it.state.dir == node.state.dir }
                .filter { it.state.straight >= minMovement }
                .filter { it.state.straight <= node.state.straight }
                .any { it.travelled < node.travelled }
        ) {
            return false
        }
        if (previousVisits.any { it.travelled + 30 < node.travelled }) {
            return false
        }
        return true
    }


    private data class State(val point: Point, val dir: Point, val straight: Int)

    private data class Node(
        val state: State,
        val cameFrom: State,
        val travelled: Double,
        val heuristic: Double
    )
}

fun main() {
    fun part1(input: List<String>): Int {
        val grid = MyGrid(input, false)
        grid.diagonalMovement = false
        grid.heuristicFun = { x, y -> x.distanceManhattanTo(y) }

        grid.distanceFun = { _, y -> grid.get(y).value.toDouble() }

        val path = grid.findPath(Point(0, 0), Point(grid.width - 1, grid.height - 1))
        var answer = 0
        for (point in path) {
            answer += grid.get(point.point).value.toInt()
            grid.set(point.point, "X")
        }

        return answer
    }

    fun part2(input: List<String>): Int {
        val grid = MyGrid(input, true)
        grid.diagonalMovement = false
        grid.heuristicFun = { x, y -> x.distanceManhattanTo(y) }

        grid.distanceFun = { _, y -> grid.get(y).value.toDouble() }

        val path = grid.findPath(Point(0, 0), Point(grid.width - 1, grid.height - 1))
        var answer = 0
        for (point in path) {
            answer += grid.get(point.point).value.toInt()
            grid.set(point.point, "X")
        }

        return answer
    }

    fun runParts(inputName: String) {
        inputName.println()
        val input = readInput(inputName)

        val p1 = part1(input)
        "Part 1: $p1".println()
        if (p1 != 0) p1.toClipboard()

        val p2 = part2(input)
        "Part 2: $p2".println()
        if (p2 != 0) p2.toClipboard()

        println()
    }


    val inputName = "Day17"
    if (inputExists(inputName)) {
        if (inputExists(inputName + "_Sample")) {
            runParts(inputName + "_Sample")
        }
        runParts(inputName)
    } else {
        var success: Boolean
        runBlocking {
            success = downloadInput(inputName)
        }
        val status = if (success) "Succeeded" else "Failed"
        "Download $status".println()
    }
}
