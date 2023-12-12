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
        if (this % i != 0L) {
            return false
        }
    }
    return true
}

fun Int.isPrime(): Boolean {
    return this.toLong().isPrime()
}

fun Long.primeFactors(): List<Long> {
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

fun Int.clamp(min: Int, max: Int): Int {
    if (this < min) return min
    if (this > max) return max
    return this
}
