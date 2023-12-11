package aoc

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import java.io.File
import java.math.BigInteger
import java.security.MessageDigest
import java.time.LocalDate
import kotlin.io.path.Path
import kotlin.io.path.exists
import kotlin.io.path.readLines

/**
 * Reads lines from the given input txt file.
 */
fun readInput(name: String) = Path("resources/$name.txt").readLines()

fun inputExists(name: String) = Path("resources/$name.txt").exists()

suspend fun downloadInput(name: String, year: Int = LocalDate.now().year): Boolean {
    val tokenFile = Path("resources/token.txt")
    val token = if (tokenFile.exists()) tokenFile.readLines().firstOrNull() else null
    if (token.isNullOrEmpty()) {
        println("""
            No Token Found
            To Download:
            1. Visit https://adventofcode.com/
            2. Press F12 to open developer tools
            3. Log into your account
            4. In the network tab, find the "callback" request
            5. In Headers -> Response Headers -> Set-Cookie, copy the "session" value (between = and ;)
            6. Create "resources/token.txt" file
            7. Paste the value in that file
        """.trimIndent())
        return false
    }
    val day = name.substring(3, 5).toInt()

    val url = "https://adventofcode.com/$year/day/$day/input"

    val client = HttpClient()
    val response = client.get(url) {
        headers {
            append("cookie", "session=$token")
        }
    }
    client.close()

    if(response.status != HttpStatusCode.OK) {
        response.status.println()
        response.body<String>().println()
        return false
    }

    File("resources/$name.txt").writeText(response.body())

    return true
}

/**
 * Converts string to md5 hash.
 */
fun String.md5() = BigInteger(1, MessageDigest.getInstance("MD5").digest(toByteArray()))
    .toString(16)
    .padStart(32, '0')

/**
 * The cleaner shorthand for printing output.
 */
fun Any?.println() = println(this)
