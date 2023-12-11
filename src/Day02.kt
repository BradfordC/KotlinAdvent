import aoc.println
import aoc.readInput

fun main() {
    fun part1(input: List<String>): Int {
        var answer = 0
        val colorMax = mapOf(
            "red" to 12,
            "green" to 13,
            "blue" to 14
        )

        val regex = Regex("(\\d+) (red|green|blue)")
        val gameRegex = Regex("Game (\\d+)")
        for (line in input) {
            var valid = true
            for (match in regex.findAll(line)) {
                val num = match.groupValues[1].toInt()
                val color = match.groupValues[2]
                if (num > colorMax[color]!!) {
                    valid = false
                    break
                }
            }
            if (valid) {
                val gameNum = gameRegex.find(line)!!.groups[1]!!.value.toInt()
                answer += gameNum
            }
        }

        return answer
    }

    fun part2(input: List<String>): Int {
        var answer = 0

        val regex = Regex("(\\d+) (red|green|blue)")
        for (line in input) {
            val colorMax = mutableMapOf(
                "red" to 0,
                "green" to 0,
                "blue" to 0
            )
            for (match in regex.findAll(line)) {
                val num = match.groupValues[1].toInt()
                val color = match.groupValues[2]
                if (num > colorMax[color]!!) {
                    colorMax[color] = num
                }
            }
            answer += colorMax.values.reduce { prod, i -> prod * i }
        }

        return answer
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day02")
    part1(input).println()
    part2(input).println()
}
