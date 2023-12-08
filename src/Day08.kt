fun main() {
    fun part1(input: List<String>): Int {
        var answer = 0

        val inputs = input[0].toList().map { if (it == 'L') 0 else 1 }.toList()
        val map = mutableMapOf<String, List<String>>()
        for (line in input.subList(2, input.size)) {
            val key = line.substring(0, 3)
            val left = line.substring(7, 10)
            val right = line.substring(12, 15)
            map[key] = listOf(left, right)
        }

        var loc = "AAA"
        while (loc != "ZZZ") {
            loc = map[loc]!![inputs.get(answer % inputs.size)]
            answer++
        }

        return answer
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

        val locs = map.keys.filter { it.endsWith("A") }.toMutableList()
        var index = 0
        while (locs.find { !it.endsWith("Z") } != null) {
            for (i in locs.indices) {
                locs[i] = map[locs[i]]!![inputs.get(index)]
            }
            index++
            index %= inputs.size
            answer++
        }

        return answer
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day08")
    part1(input).println()
//    part2(input).println()
}
