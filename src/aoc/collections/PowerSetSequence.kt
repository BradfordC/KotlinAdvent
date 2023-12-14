package aoc.collections

class PowerSetSequence<T>(private val list: List<T>, private val size: Int? = null): Sequence<List<T>> {
    init {
        if (size != null && size > list.size) throw IllegalArgumentException("Requested size ($size) > size of list (${list.size})")
    }

    override fun iterator(): Iterator<List<T>> {
        return PowerSetIterator(list, size)
    }

    private class PowerSetIterator<T>(private val list: List<T>, private val size: Int? = null): Iterator<List<T>> {

        private var nextIndices = if (size == null) mutableListOf() else (0 until size).toMutableList()

        override fun hasNext(): Boolean {
            return nextIndices.size <= (size ?: list.size)
        }

        override fun next(): List<T> {
            val sublist = nextIndices.map { list[it] }.toList()
            advanceIndices()
            return sublist
        }

        private fun advanceIndices()  {
            if (nextIndices.isEmpty()) {
                nextIndices.add(0)
                return
            }
            val currentSize = nextIndices.size
            var toIncrement = nextIndices.indices.filter { canIncrement(it) }.maxOrNull()
            if (toIncrement == null) {
                nextIndices = (0..currentSize).toMutableList()
            }
            else {
                val incremented = nextIndices[toIncrement] + 1
                for (i in toIncrement until nextIndices.size) {
                    nextIndices[i] = incremented + (i - toIncrement)
                }
            }
        }

        private fun canIncrement(i: Int): Boolean {
            val incremented = nextIndices[i] + 1
            val nextVal = if (i == nextIndices.size - 1) list.size else nextIndices[i + 1]
            return incremented < nextVal
        }
    }
}