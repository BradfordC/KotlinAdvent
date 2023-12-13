package aoc.collections

import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class CollectionUtilsKtTest {

    var list = mutableListOf<String>()

    @BeforeEach
    fun setUp() {
        list = mutableListOf()
    }

    @Test
    fun split() {
        list = mutableListOf("AAA", "ABC", "BBB", "BAC", "CCC")

        // Regular behavior
        val regular = list.split { it == "BBB" }
        assertEquals(2, regular.size)
        assertEquals(listOf("AAA", "ABC"), regular[0])
        assertEquals(listOf("BAC", "CCC"), regular[1])

        // Split on sequential items
        val sequential = list.split { it.contains("B") }
        assertEquals(4, sequential.size)
        assertEquals(listOf("AAA"), sequential[0])
        assertEquals(emptyList<String>(), sequential[1])
        assertEquals(emptyList<String>(), sequential[2])
        assertEquals(listOf("CCC"), sequential[3])

        // Split on the first item
        val start = list.split { it == "AAA" }
        assertEquals(2, start.size)
        assertEquals(emptyList<String>(), start[0])
        assertEquals(listOf("ABC", "BBB", "BAC", "CCC"), start[1])

        // Split on the last item
        val end = list.split { it == "CCC" }
        assertEquals(2, end.size)
        assertEquals(listOf("AAA", "ABC", "BBB", "BAC"), end[0])
        assertEquals(emptyList<String>(), end[1])

        // Split on no items
        val none = list.split { it == "DDD" }
        assertEquals(1, none.size)
        assertEquals(listOf("AAA", "ABC", "BBB", "BAC", "CCC"), none[0])

        // Split on all items
        val all = list.split { true }
        assertEquals(6, all.size)
        assertTrue(all.all { it.isEmpty() })
    }
}