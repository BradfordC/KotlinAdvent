package aoc.collections

fun <T> List<T>.split(delimiter: (T) -> Boolean): List<List<T>> {
    val indices = this.withIndex().filter { t -> delimiter(t.value) }.map { it.index }
    if (indices.isEmpty()) {
        return listOf(this)
    }
    val sections = mutableListOf<List<T>>()
    var prev = -1
    for (i in indices) {
        val section = this.subList(prev + 1, i)
        sections.add(section)
        prev = i
    }
    sections.add(this.subList(indices.last() + 1, this.size))
    return sections
}