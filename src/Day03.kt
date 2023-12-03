import kotlin.math.*


fun String.checkForSymbol(range: IntRange): Boolean {
    val subStr = this.substring(range)
    return subStr.contains(Regex("[^\\d\\.]"))
}

fun main() {
    fun part1(input: List<String>): Int {
        var sum = 0
        val regex = Regex("\\d+")
        for (i in input.indices) {
            for (match in regex.findAll(input[i])) {
                val range = max(0, match.range.first - 1) .. min(match.range.last + 1, input[i].length - 1)
                for (j in i-1..i+1) {
                    if (j in input.indices && input[j].checkForSymbol(range)) {
                        sum += match.value.toInt()
                        break
                    }
                }
            }
        }
        return sum
    }

    fun part2(input: List<String>): Int {
        var answer = 0
        val gearCounts = mutableMapOf<Pair<Int, Int>, MutableList<Int>>()
        val partRegex = Regex("\\d+")
        val gearRegex = Regex("\\*")
        for ((i, line) in input.withIndex()) {
            for (match in partRegex.findAll(line)) {
                val range = max(0, match.range.first - 1) .. min(match.range.last + 1, line.length - 1)
                val partValue = match.value.toInt()
                for (j in i-1..i+1) {
                    if (j in input.indices) {
                        val subStr = input[j].substring(range)
                        for (gear in gearRegex.findAll(input[j].substring(range))) {
                            val gearLocation = Pair(j, gear.range.first + range.first)
                            val neighboringParts = gearCounts.getOrDefault(gearLocation, mutableListOf())
                            neighboringParts.add(partValue)
                            gearCounts[gearLocation] = neighboringParts
                        }
                    }
                }
            }
        }
        return gearCounts.values.filter { l -> l.size == 2 }.map { l -> l[0] * l[1] }.reduce { sum, value -> sum + value}
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day03")
    part1(input).println()
    part2(input).println()
}
