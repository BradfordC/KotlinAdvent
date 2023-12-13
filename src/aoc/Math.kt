package aoc

import kotlin.math.*

fun Long.isPrime(): Boolean {
    if (this == 2L) {
        return true
    }
    if (this % 2 == 0L) {
        return false
    }
    val maxCheck = sqrt(this.toDouble()).toInt()
    for (i in 3..maxCheck step 2) {
        if (this % i == 0L) {
            return false
        }
    }
    return true
}

fun Int.isPrime(): Boolean {
    return this.toLong().isPrime()
}

fun Long.primeFactors(): List<Long> {
    if (this == 1L) return listOf(1)
    val primes = mutableListOf<Long>()
    var remaining = this
    while(remaining != 1L) {
        val maxCheck = sqrt(remaining.toDouble()).toLong()
        var foundFactor = false
        for (i in 2..maxCheck) {
            if (remaining % i == 0L) {
                primes.add(i)
                remaining /= i
                foundFactor = true
                break
            }
        }
        if (!foundFactor) {
            primes.add(remaining)
            remaining = 1
        }
    }
    return primes
}

fun Int.primeFactors(): List<Int> {
    return this.toLong().primeFactors().map { it.toInt() }.toList()
}

fun lcm(vararg values: Long): Long {
    return lcm(values.toList())
}

fun lcm(values: Iterable<Long>): Long {
    val factorCounts = values.map { it.primeFactors().groupingBy { it }.eachCount() }
    val combined = mutableMapOf<Long, Int>()
    for (factors in factorCounts) {
        for ((k, v) in factors) {
            combined[k] = max((combined[k] ?: 0), v)
        }
    }
    return combined.entries.fold(1L) { acc, v -> acc * v.key.toDouble().pow(v.value).toLong() }
}

fun lcm(vararg values: Int): Int {
    return lcm(values.map { it.toLong() }).toInt()
}

fun lcm(values: Iterable<Int>): Int {
    return lcm(values.map { it.toLong() }).toInt()
}

fun Long.clamp(min: Long, max: Long): Long {
    if (this < min) return min
    if (this > max) return max
    return this
}

fun Int.clamp(min: Int, max: Int): Int {
    if (this < min) return min
    if (this > max) return max
    return this
}

fun minAndMax(x: Long, y: Long): Pair<Long, Long> {
    if (x < y) {
        return Pair(x, y)
    }
    return Pair(y, x)
}

fun minAndMax(x: Int, y: Int): Pair<Int, Int> {
    if (x < y) {
        return Pair(x, y)
    }
    return Pair(y, x)
}

fun Long.factorial(): Long {
    if (this > 20) throw Exception("$this! will overflow Long")
    if (this == 0L) return 1
    return (1..this).reduce { a, b -> a * b }
}

fun Int.factorial(): Int {
    if (this > 12) throw Exception("$this! will overflow Int")
    return this.toLong().factorial().toInt()
}

fun factDiv(dividend: Long, divisor: Long): Long {
    if (dividend < divisor) throw Exception("Dividend must be greater")
    if (dividend == divisor) return 1
    if (dividend > 20) throw Exception("$dividend! will overflow Long")
    return (divisor + 1..dividend).reduce { a, b -> a * b }
}

fun factDiv(dividend: Int, divisor: Int): Int {
    if (dividend > 15) throw Exception("$dividend! will overflow Int")
    return factDiv(dividend.toLong(), divisor.toLong()).toInt()
}

fun Long.choose(k: Long): Long {
    val (min, max) = minAndMax(k, this - k)
    return factDiv(this, max) / min.factorial()
}

fun Int.choose(k: Int): Int {
    return this.toLong().choose(k.toLong()).toInt()
}