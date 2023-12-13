package aoc

import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*

internal class ParsingKtTest {

    @Test
    fun parseInts() {
        assertEquals(listOf(1, 2, 3), "1 2 3".parseInts())
        assertEquals(listOf(1, -2, 3), "1 -2 - 3".parseInts())
        assertEquals(listOf(1, 2, 3), "x1x2x3x".parseInts())
        assertEquals(listOf(11, 22, 33), "11 22 33".parseInts())
        assertEquals(listOf(1, 2, 3), "1.2 3".parseInts())
        assertEquals(listOf(1, 2, 3), "001 02 3".parseInts())
        assertEquals(listOf(10, 0, 0), "10,000,000".parseInts())
    }

    @Test
    fun parseLongs() {
        assertEquals(listOf<Long>(1, 2, 3), "1 2 3".parseLongs())
        assertEquals(listOf<Long>(1, -2, 3), "1 -2 - 3".parseLongs())
        assertEquals(listOf<Long>(1, 2, 3), "x1x2x3x".parseLongs())
        assertEquals(listOf<Long>(11, 22, 33), "11 22 33".parseLongs())
        assertEquals(listOf<Long>(1, 2, 3), "1.2 3".parseLongs())
        assertEquals(listOf<Long>(1, 2, 3), "001 02 3".parseLongs())
        assertEquals(listOf<Long>(10, 0, 0), "10,000,000".parseLongs())
    }

    @Test
    fun parseDoubles() {
        assertEquals(listOf(1.0, 2.0, 3.0), "1 2 3".parseDoubles())
        assertEquals(listOf(1.0, 2.1, 3.23), "1.0 2.1 3.23".parseDoubles())
        assertEquals(listOf(1.0, -2.0, 3.0), "1.0 -2.0 - 3".parseDoubles())
        assertEquals(listOf(1.0, 2.0, 3.0), "x1.0x2x3.0x".parseDoubles())
        assertEquals(listOf(11.1, 22.2, 33.3), "11.1 22.2 33.3".parseDoubles())
        assertEquals(listOf(1.0, 2.0, 30.0), "001 002.0 30".parseDoubles())
        assertEquals(listOf(10.0, 0.0, 0.0), "10,000,000".parseDoubles())
        assertEquals(listOf(10.0, 0.0), "10.000.000".parseDoubles())
    }

    @Test
    fun parseWords() {
        assertEquals(listOf("A", "B", "C"), "A B C".parseWords())
        assertEquals(listOf("A", "B", "C"), "A-B.C".parseWords())
        assertEquals(listOf("AxBxC"), "AxBxC".parseWords())
        assertEquals(listOf("A", "B", "C"), "A1B1C".parseWords())
        assertEquals(listOf("Bad", "Wolf"), "Bad Wolf.".parseWords())
    }

    @Test
    fun parseWords_IncludeNumbers() {
        assertEquals(listOf("A", "B", "C"), "A B C".parseWords(true))
        assertEquals(listOf("A", "B", "C"), "A-B.C".parseWords(true))
        assertEquals(listOf("AxBxC"), "AxBxC".parseWords(true))
        assertEquals(listOf("A1B1C"), "A1B1C".parseWords(true))
        assertEquals(listOf("A", "2", "Z3", "4U"), "A 2 Z3 4U".parseWords(true))
        assertEquals(listOf("B4d", "w0lf"), "B4d w0lf.".parseWords(true))
    }
}