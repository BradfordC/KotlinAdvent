package aoc.collections

import java.util.function.UnaryOperator

/**
 * An ArrayList that preserves sorting order
 * It accomplishes this by inserting new elements in order
 * Thus, functions which can insert at specific locations are not implemented
 * Less efficient than PriorityQueue, but preserves iteration order
 *
 * If no Comparator is passed in the constructor, type must be Comparable
 * Otherwise, add() will throw a ClassCastException
 */
class SortedList<T>(comparator: Comparator<in T>? = null): ArrayList<T>() {

    private val usedComparator: Comparator<in T> = comparator
        ?: Comparator { a: T, b: T -> (a as Comparable<T>).compareTo(b) }

    override fun add(element: T): Boolean {
        var i = binarySearch(element, usedComparator)
        if (i < 0) {
            i = -i - 1
        }
        super.add(i, element)
        return true
    }

    override fun add(index: Int, element: T) {
        throw NotImplementedError()
    }

    override fun addAll(elements: Collection<T>): Boolean {
        elements.forEach { add(it) }
        return elements.isNotEmpty()
    }

    override fun addAll(index: Int, elements: Collection<T>): Boolean {
        throw NotImplementedError()
    }

    override fun set(index: Int, element: T): T {
        throw NotImplementedError()
    }

    override fun replaceAll(operator: UnaryOperator<T>) {
        throw NotImplementedError()
    }
}