fun main() {
    data class SeedMap(val sourceRange: LongRange, val dif: Long)

    fun getSeeds(line: String): List<Long> {
        val seedRegex = Regex("\\d+")
        return seedRegex.findAll(line)
            .map { seed -> seed.value.toLong() }
            .toList()
    }

    fun getSeedsTwo(line: String): List<LongRange> {
        val seedRegex = Regex("(\\d+) (\\d+)")
        val seeds = mutableListOf<LongRange>()
        for (match in seedRegex.findAll(line)) {
            val rangeStart = match.groupValues[1].toLong()
            val rangeSize = match.groupValues[2].toLong()
            seeds.add(rangeStart until rangeStart + rangeSize)
        }
        return seeds
    }

    fun createMaps(input: List<String>): List<MutableList<SeedMap>> {
        val maps = mutableListOf<MutableList<SeedMap>>()
        var currentMap: MutableList<SeedMap> = mutableListOf()
        val mapRegex = Regex("\\d+")
        for (line in input) {
            if (line.contains("map")) {
                if(currentMap.isNotEmpty()) {
                    maps.add(currentMap)
                }
                currentMap = mutableListOf()
            }
            else if (mapRegex.containsMatchIn(line)){
                val matches = mapRegex.findAll(line).toList()
                val sourceStart = matches[1].value.toLong()
                val destStart = matches[0].value.toLong()
                val rangeSize = matches[2].value.toLong()
                val sourceEnd = sourceStart + rangeSize
                currentMap.add(SeedMap(sourceStart until sourceEnd, destStart - sourceStart))
            }
        }
        maps.add(currentMap)
        return maps
    }


    fun runMap(value: Long, soilMap: MutableList<SeedMap>): Pair<Long, Long> {
        val range = soilMap.firstOrNull { it.sourceRange.contains(value) }
        return if (range == null) {
            Pair(value, 0)
        } else {
            Pair(value + range.dif, range.sourceRange.last - value)
        }
    }

    fun part1(input: List<String>): Long {
        val seeds = getSeeds(input[0])
        val maps = createMaps(input.subList(1, input.size))
        val locations = mutableListOf<Long>()
        for (seed in seeds) {
            var currentValue = seed
            for (soilMap in maps) {
                currentValue = runMap(currentValue, soilMap).first
            }
            locations.add(currentValue)
        }
        return locations.minOf { it }
    }

    fun part2(input: List<String>): Long {
        val seeds = getSeedsTwo(input[0])
        val maps = createMaps(input.subList(1, input.size))
        var minLocation = Long.MAX_VALUE
        for (seedRange in seeds) {
            var seed = seedRange.first
            while ( seed <= seedRange.last) {
                var currentValue = seed
                var space = Long.MAX_VALUE
                for (soilMap in maps) {
                    val returnValue = runMap(currentValue, soilMap)
                    currentValue = returnValue.first
                    space = space.coerceAtMost(returnValue.second)
                }
                seed += space + 1
                minLocation = minLocation.coerceAtMost(currentValue)
            }
        }
        return minLocation
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day05")
    part1(input).println()
    part2(input).println()
}
