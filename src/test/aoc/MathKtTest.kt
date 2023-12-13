package aoc

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

internal class MathKtTest {

    @Test
    fun isPrime() {
        assertFalse(0.isPrime())
        assertTrue(1.isPrime())
        assertTrue(2.isPrime())
        assertTrue(3.isPrime())
        assertFalse(4.isPrime())
        assertFalse(15.isPrime())
        assertTrue(37.isPrime())
        assertFalse(39.isPrime())
        assertTrue(109.isPrime())
        assertFalse(111.isPrime())
    }

    @Test
    fun primeFactors() {
        assertEquals(listOf(1), 1.primeFactors())
        assertEquals(listOf(2, 3), 6.primeFactors())
        assertEquals(listOf(7), 7.primeFactors())
        assertEquals(listOf(3, 7), 21.primeFactors())
        assertEquals(listOf(2, 2, 2, 3), 24.primeFactors())
        assertEquals(listOf(3, 3, 3, 3), 81.primeFactors())
    }

    @Test
    fun lcm() {
        assertEquals(6, lcm(2, 3))
        assertEquals(6, lcm(3, 2))
        assertEquals(10, lcm(5, 10))
        assertEquals(10, lcm(10, 5))
        assertEquals(15, lcm(15))
        assertEquals(60, lcm(20, 12))
        assertEquals(105, lcm(5, 5, 21, 7, 15))
    }

    @Test
    fun clamp() {
        assertEquals(2, (-1).clamp(2, 10))
        assertEquals(2,  0.clamp(2, 10))
        assertEquals(2,  2.clamp(2, 10))
        assertEquals(5,  5.clamp(2, 10))
        assertEquals(10,  10.clamp(2, 10))
        assertEquals(10,  15.clamp(2, 10))
    }

    @Test
    fun minAndMax() {
        assertEquals(Pair(5, 10), minAndMax(5, 10))
        assertEquals(Pair(5, 10), minAndMax(10, 5))
        assertEquals(Pair(5, 5), minAndMax(5, 5))
        assertEquals(Pair(-5, -5), minAndMax(-5, -5))
    }

    @Test
    fun factorial() {
        assertEquals(1, 0.factorial())
        assertEquals(1, 1.factorial())
        assertEquals(2, 2.factorial())
        assertEquals(6, 3.factorial())
        assertEquals(120, 5.factorial())
        assertEquals(479001600, 12.factorial())
        assertThrows<Exception> { 13.factorial() }
    }

    @Test
    fun factDiv() {
        assertEquals(1, factDiv(0, 0))
        assertEquals(1, factDiv(1, 0))
        assertEquals(1, factDiv(1, 1))
        assertEquals(2, factDiv(2, 0))
        assertEquals(2, factDiv(2, 1))
        assertEquals(3, factDiv(3, 2))
        assertEquals(12, factDiv(4, 2))
        assertEquals(90, factDiv(10, 8))
    }

    @Test
    fun choose() {
        assertEquals(1, 1.choose(1))
        assertEquals(1, 2.choose(2))
        assertEquals(2, 2.choose(1))
        assertEquals(3, 3.choose(1))
        assertEquals(3, 3.choose(2))
        assertEquals(6, 4.choose(2))
        assertEquals(10, 5.choose(3))
    }
}