import aoc.downloadInput
import aoc.inputExists
import aoc.println
import aoc.readInput
import kotlinx.coroutines.runBlocking

fun String.hash(): Int {
    return this.toList().fold(0) { acc, char -> ((acc + char.code) * 17) % 256 }
}

fun main() {
    fun part1(input: List<String>): Int {
        return input[0].split(",").map { it.hash() }.sum()
    }

    fun part2(input: List<String>): Int {
        val steps = input[0].split(",")
        val boxes = mutableListOf<MutableList<Pair<String, Int>>>()
        repeat(256) {
            boxes.add(mutableListOf())
        }
        steps.forEach {
            val (key, value) = it.split("-", "=")
            val remove = value.isEmpty()
            val focal = if (value.isNotEmpty()) value.toInt() else 0

            val box = boxes[key.hash()]
            if (remove) {
                box.removeIf { entry -> entry.first == key }
            }
            else {
                val entry = Pair(key, focal)
                val index = box.indexOfFirst { entry -> entry.first == key }
                if (index >= 0 ) {
                    box.removeAt(index)
                    box.add(index, entry)
                }
                else {
                    box.add(entry)
                }
            }
        }

        var answer = 0
        for ((boxNum, box) in boxes.withIndex()) {
            for ((entryNum, entry) in box.withIndex()) {
                answer += (boxNum + 1) * (entryNum + 1) * entry.second
            }
        }

        return answer
    }

    fun runParts(inputName: String) {
        inputName.println()
        val input = readInput(inputName)
        "Part 1: ${part1(input)}".println()
        "Part 2: ${part2(input)}".println()
        println()
    }



    val inputName = "Day15"
    if (inputExists(inputName)) {
        if (inputExists(inputName + "_Sample")) {
            runParts(inputName + "_Sample")
        }
        runParts(inputName)
    }
    else {
        var success: Boolean
        runBlocking {
            success = downloadInput(inputName)
        }
        val status = if (success) "Succeeded" else "Failed"
        "Download $status".println()
    }
}
