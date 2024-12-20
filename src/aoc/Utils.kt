package aoc

import java.math.BigInteger
import java.security.MessageDigest

/**
 * Converts string to md5 historical.`2023`.hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
