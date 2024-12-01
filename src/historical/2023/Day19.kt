package historical.`2023`

import aoc.*
import aoc.collections.split
import kotlinx.coroutines.runBlocking

data class Input(val x: Int, val m: Int, val a: Int, val s: Int) {
    fun value(): Int {
        return x + m + a + s
    }
}

data class InputRange(val x: IntRange, val m: IntRange, val a: IntRange, val s: IntRange) {
    fun IntRange.size(): Long {
        return this.last - this.first + 1L
    }

    fun value(): Long {
        return x.size() * m.size() * a.size() * s.size()
    }
}

data class Rule(val v: String, val check: String, val q: Int, val res: String) {
    fun evaluate(input: Input): String? {
        if (v == "GOTO") {
            return res
        }
        val value = when (v) {
            "x" -> input.x
            "m" -> input.m
            "a" -> input.a
            "s" -> input.s
            else -> throw Exception(v)
        }
        val result = when (check) {
            ">" -> value > q
            "<" -> value < q
            else -> throw Exception(check)
        }
        return if (result) res else null
    }

    fun split(input: InputRange): List<Pair<String?, InputRange>> {
        if (v == "GOTO") {
            return listOf(Pair(res, input))
        }

        val range = when (v) {
            "x" -> input.x
            "m" -> input.m
            "a" -> input.a
            "s" -> input.s
            else -> throw Exception(v)
        }

        // True, False
        val splits = when (check) {
            ">" -> if (range.first > q) {
                listOf(range, null)
            } else if (range.last <= q) {
                listOf(null, range)
            } else {
                listOf(q + 1..range.last, range.first..q)
            }

            "<" -> if (range.last < q) {
                listOf(range, null)
            } else if (range.first >= q) {
                listOf(null, range)
            } else {
                listOf(range.first until q, q..range.last)
            }

            else -> throw Exception(check)
        }



        return splits.withIndex()
            .filter { it.value != null }
            .map { Pair(if (it.index == 0) res else null, it.value) }
            .map { when(v) {
                "x" -> Pair(it.first, InputRange(it.second!!, input.m, input.a, input.s))
                "m" -> Pair(it.first, InputRange(input.x, it.second!!, input.a, input.s))
                "a" -> Pair(it.first, InputRange(input.x, input.m, it.second!!, input.s))
                "s" -> Pair(it.first, InputRange(input.x, input.m, input.a, it.second!!))
                else -> throw Exception(v)
            } }
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        val (rules, inputStrs) = input.split { it.isEmpty() }

        var ruleMap = mutableMapOf<String, List<Rule>>()
        rules.forEach {
            val regex = Regex("(\\w+)\\{(.*)\\}")
            val matches = regex.find(it)!!
            val key = matches.groups[1]!!.value
            val ruleStr = matches.groups[2]!!.value
            val ruleList = ruleStr.split(",").map {
                val ruleRegex = Regex("(\\w)([><])(\\d+):(\\w+)")
                val match = ruleRegex.find(it)
                if (match == null) {
                    Rule("GOTO", "GOTO", 0, it)
                } else {
                    Rule(
                        match.groupValues[1],
                        match.groupValues[2],
                        match.groupValues[3].toInt(),
                        match.groupValues[4]
                    )
                }
            }
            ruleMap[key] = ruleList
        }

        val inputs = inputStrs.map { it.parseInts() }
            .map { Input(it[0], it[1], it[2], it[3]) }

        var answer = 0

        for (input in inputs) {
            var currentRule: String? = "in"
            var exit = false
            while (currentRule != null && !exit) {
                var rules = ruleMap[currentRule]!!
                for (rule in rules) {
                    when (val result = rule.evaluate(input)) {
                        "R" -> {
                            exit = true
                            break
                        }

                        "A" -> {
                            exit = true
                            answer += input.value()
                            break
                        }

                        null -> continue
                        else -> {
                            currentRule = result
                            break
                        }
                    }
                }
            }
        }

        return answer
    }

    fun part2(input: List<String>): Long {
        val (rules, inputStrs) = input.split { it.isEmpty() }

        var ruleMap = mutableMapOf<String, List<Rule>>()
        rules.forEach {
            val regex = Regex("(\\w+)\\{(.*)\\}")
            val matches = regex.find(it)!!
            val key = matches.groups[1]!!.value
            val ruleStr = matches.groups[2]!!.value
            val ruleList = ruleStr.split(",").map {
                val ruleRegex = Regex("(\\w)([><])(\\d+):(\\w+)")
                val match = ruleRegex.find(it)
                if (match == null) {
                    Rule("GOTO", "GOTO", 0, it)
                } else {
                    Rule(
                        match.groupValues[1],
                        match.groupValues[2],
                        match.groupValues[3].toInt(),
                        match.groupValues[4]
                    )
                }
            }
            ruleMap[key] = ruleList
        }

        var answer = 0L
        val start = Pair("in", InputRange(1..4000, 1..4000, 1..4000, 1..4000))
        val stack = mutableListOf<Pair<String?, InputRange>>(start)

        while (stack.isNotEmpty()) {
            var next: Pair<String?, InputRange>? = stack.removeLast()
            val rules = ruleMap[next?.first]!!
            for (rule in rules) {
                val splits = rule.split(next!!.second)
                next = null
                for (split in splits) {
                    when (split.first) {
                        "A" -> answer += split.second.value()
                        "R" -> continue
                        null -> next = split
                        else -> stack.add(split)
                    }
                }
                if (next == null) {
                    break
                }
            }
        }

        return answer
    }

    fun runParts(inputName: String) {
        inputName.println()
        val input = readInput(inputName)

        val p1 = part1(input)
        "Part 1: $p1".println()
        if (p1 != 0) p1.toClipboard()

        val p2 = part2(input)
        "Part 2: $p2".println()
        if (p2 != 0L) p2.toClipboard()

        println()
    }


    val inputName = "Day19"
    if (inputExists(inputName)) {
        if (inputExists(inputName + "_Sample")) {
            runParts(inputName + "_Sample")
        }
        runParts(inputName)
    } else {
        var success: Boolean
        runBlocking {
            success = downloadInput(inputName)
        }
        val status = if (success) "Succeeded" else "Failed"
        "Download $status".println()
    }
}
