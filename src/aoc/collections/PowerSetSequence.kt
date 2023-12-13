package aoc.collections

class PowerSetSequence<T>(private val list: List<T>): Sequence<List<T>> {
    override fun iterator(): Iterator<List<T>> {
        return PowerSetIterator(list)
    }

    private class PowerSetIterator<T>(private val list: List<T>): Iterator<List<T>> {

        private var nextIndices = mutableListOf<Int>()

        override fun hasNext(): Boolean {
            return nextIndices.size <= list.size
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