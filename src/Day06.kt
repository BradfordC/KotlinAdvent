fun main() {
    fun part1(input: List<String>): Int {
        val regex = Regex("\\d+")
        val times = regex.findAll(input[0]).toList()
        val distances = regex.findAll(input[1]).toList()
        var answer = 1
        for (i in times.indices) {
            val time = times[i].value.toInt()
            val distance = distances[i].value.toInt()
            var winning = 0
            for (charge in 1 until time) {
                val remaining = time - charge
                if (distance < charge * remaining) {
                    winning += 1
                }
            }
            answer *= winning
        }

        return answer
    }

    fun part2(input: List<String>): Int {
        val time = input[0].replace(" ", "").removePrefix("Time:").toLong()
        val distance = input[1].replace(" ", "").removePrefix("Distance:").toLong()
        var winning = 0
        for (charge in 1 until time) {
            val remaining = time - charge
            if (distance < charge * remaining) {
                winning += 1
            }
        }
        return winning
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day06")
    part1(input).println()
    part2(input).println()
}
