package aoc


fun String.parseInts(): List<Int> {
    val regex = Regex("-?\\d+")
    return regex.findAll(this).map { it.value.toInt() }.toList()
}

fun String.parseLongs(): List<Long> {
    val regex = Regex("-?\\d+")
    return regex.findAll(this).map { it.value.toLong() }.toList()
}

fun String.parseDoubles(): List<Double> {
    val regex = Regex("-?\\d+\\.?\\d*")
    return regex.findAll(this).map { it.value.toDouble() }.toList()
}