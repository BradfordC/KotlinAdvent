import aoc.*

fun main() {

    fun timeToZ(start: String, map: Map<String, List<String>>, inputs: List<Int>): Int {
        var answer = 0
        var loc = start
        // Only 1 XXZ entry per cycle
        while (!loc.endsWith("Z")) {
            loc = map[loc]!![inputs[answer % inputs.size]]
            answer++
        }
        return answer
    }

    fun part1(input: List<String>): Int {
        val inputs = input[0].toList().map { if (it == 'L') 0 else 1 }.toList()
        val map = mutableMapOf<String, List<String>>()
        for (line in input.subList(2, input.size)) {
            val key = line.substring(0, 3)
            val left = line.substring(7, 10)
            val right = line.substring(12, 15)
            map[key] = listOf(left, right)
        }

        return timeToZ("AAA", map, inputs)
    }

    fun part2(input: List<String>): Long {
        var answer = 0L

        val inputs = input[0].toList().map { if (it == 'L') 0 else 1 }.toList()
        val map = mutableMapOf<String, List<String>>()
        for (line in input.subList(2, input.size)) {
            val key = line.substring(0, 3)
            val left = line.substring(7, 10)
            val right = line.substring(12, 15)
            map[key] = listOf(left, right)
        }

        val starts = map.keys.filter { it.endsWith("A") }
        /**
         * Found through experimentation that:
         * 1. Each start point leads to an independent cycle
         * 2. Each cycle only contains one XXZ entry
         * 3. The time it takes to reach XXZ the first time is equal to the cycle length
         * Those three factors mean it's OK to take the least common multiple of timeToZ() for each input,
         * even though that wouldn't work for a general solution
          */
        val times = starts.map { timeToZ(it, map, inputs).toLong() }
        return lcm(times)
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day08")
    part1(input).println()
    part2(input).println()
}
