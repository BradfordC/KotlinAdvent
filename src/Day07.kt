val CARDS = listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A')
val CARDS_JOKER_LOW = listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')

data class Hand(val cards: List<Char>, val bid: Int, val wildJokers: Boolean) : Comparable<Hand> {
    private fun handStrength(): Int {
        val cardCount = mutableMapOf<Char, Int>()
        for (card in cards) {
            if (card in cardCount) {
                cardCount[card] = cardCount[card]!! + 1
            } else {
                cardCount[card] = 1
            }
        }

        if (wildJokers) {
            cardCount.adjustJokers()
        }

        if (cardCount.keys.size == 1) {
            return 7
        }
        if (cardCount.keys.size == 2) {
            return if (cardCount.values.contains(1)) 6 else 5
        }
        if (cardCount.keys.size == 3) {
            return if (cardCount.values.contains(3)) 4 else 3
        }
        if (cardCount.keys.size == 4) {
            return 2
        }
        return 1
    }

    private fun MutableMap<Char, Int>.adjustJokers() {
        val jokerCount = this.remove('J') ?: 0
        if (jokerCount == 5) {
            this['A'] = 5
        } else {
            val maxCount = this.values.maxOrNull() ?: 0
            for (card in this.keys) {
                if (this[card]!! == maxCount) {
                    this[card] = maxCount + jokerCount
                    break
                }
            }
        }
    }

    override fun compareTo(other: Hand): Int {
        val cardRanks = if (wildJokers) CARDS_JOKER_LOW else CARDS
        if (this.handStrength() != other.handStrength()) {
            return this.handStrength() compareTo other.handStrength()
        }
        for (i in this.cards.indices) {
            if (this.cards[i] != other.cards[i]) {
                return cardRanks.indexOf(this.cards[i]) compareTo cardRanks.indexOf(other.cards[i])
            }
        }
        return 0
    }
}

fun main() {
    fun part1(input: List<String>): Int {
        var answer = 0
        val hands = mutableListOf<Hand>()
        for (line in input) {
            val handStr = line.substring(0..4)
            val bid = line.substring(6)
            hands.add(Hand(handStr.toList(), bid.toInt(), false))
        }
        hands.sort()
        for ((i, hand) in hands.withIndex()) {
            answer += (i + 1) * hand.bid
        }
        return answer
    }

    fun part2(input: List<String>): Int {
        var answer = 0
        val hands = mutableListOf<Hand>()
        for (line in input) {
            val handStr = line.substring(0..4)
            val bid = line.substring(6)
            hands.add(Hand(handStr.toList(), bid.toInt(), true))
        }
        hands.sort()
        for ((i, hand) in hands.withIndex()) {
            answer += (i + 1) * hand.bid
        }
        return answer
    }

    // test if implementation meets criteria from the description, like:
//    val testInput = readInput("Day01_test")
//    check(part1(testInput) == 1)

    val input = readInput("Day07")
    part1(input).println()
    part2(input).println()
}
