package aoc.grids

import aoc.collections.SortedList

open class PathfindingGrid(input: List<String>) : Grid(input) {
    var walls = Regex("")
    var diagonalMovement = true

    var heuristicFun = { source: Point, dest: Point -> source.distanceTo(dest) }

    fun heuristic(source: Point, dest: Point): Double {
        return wrappedPoints(dest).minOf { heuristicFun(source, it) }
    }

    fun findPath(source: Point, dest: Point): List<Cell> {
        val nodeMap = List(height) { y ->
            MutableList(width) { x ->
                PathfindingNode(
                    Point(x, y), null, Double.MAX_VALUE, Double.MAX_VALUE, false
                )
            }
        }
        val startingNode = PathfindingNode(source, null, 0.0, distance(source, dest), true)
        nodeMap[source.y][source.x] = startingNode
        val toVisit = SortedList<PathfindingNode>(compareBy { it.travelled + it.heuristic })
        toVisit.add(startingNode)
        while (toVisit.isNotEmpty()) {
            val current = toVisit.removeFirst()
            current.visited = true
            if (current.point.x == dest.x && current.point.y == dest.y) {
                break
            }
            for (neighbor in this.getNeighbors(current.point, diagonal = diagonalMovement)) {
                val neighborNode = nodeMap[neighbor.y][neighbor.x]
                if (neighborNode.visited) continue
                if (walls.matches(get(neighborNode.point).value)) continue
                val neighborTravelled = current.travelled + distance(current.point, neighbor.point)
                if (neighborNode.cameFrom == null) {
                    neighborNode.heuristic = heuristic(neighbor.point, dest)
                    toVisit.add(neighborNode)
                }
                if (neighborTravelled < neighborNode.travelled) {
                    neighborNode.cameFrom = current.point
                    neighborNode.travelled = neighborTravelled
                }
            }
        }

        val path = mutableListOf<Cell>()
        var toAdd = nodeMap[dest.y][dest.x]
        while (toAdd.visited) {
            path.add(get(toAdd.point))
            if (toAdd.cameFrom == null) {
                break
            }
            toAdd = nodeMap[toAdd.cameFrom!!.y][toAdd.cameFrom!!.x]
        }
        if (toAdd.point != source) {
            return mutableListOf()
        }
        path.add(get(toAdd.point))
        return path.reversed()
    }

    private data class PathfindingNode(
        val point: Point, var cameFrom: Point?, var travelled: Double, var heuristic: Double, var visited: Boolean
    )
}