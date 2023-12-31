package day07

import Day

class Day07(private val input: String) : Day() {
    private val cardValues = listOf(
        // empty to align the parts with the index
        emptyList(),
        // part 1
        listOf('2', '3', '4', '5', '6', '7', '8', '9', 'T', 'J', 'Q', 'K', 'A'),
        // part 2
        listOf('J', '2', '3', '4', '5', '6', '7', '8', '9', 'T', 'Q', 'K', 'A')
    )


    override fun part1(): Long {
        // in case of the same play you need to compare the value of the first card,
        return parseInputTriplets(input, 1).sortedBy { (hand, highestPlay, _) ->
            getHandValue(hand, highestPlay, 1)
        }.mapIndexed { index, (_, _, bid) -> (index + 1) * bid }.sum()
    }

    private fun parseInputTriplets(input: String, part: Int): List<Triple<String, Long, Long>> {
        return input.lines().map {
            val hand = it.split(" ").first() // length 5
            val bid = it.split(" ").last().toLong()
            // triple format hand - highest play - bid
            Triple(hand, hand.highestPlay(part), bid)
        }
    }

    private fun getHandValue(hand: String, highestPlay: Long, part: Int = 1): Long {
        var points = highestPlay * 15L
        hand.forEach { char ->
            points += cardValues[part].indexOf(char)
            points *= 15
        }
        return points
    }

    override fun part2(): Long {
        //A, K, Q, J, T, 9, 8, 7, 6, 5, 4, 3, or 2
        // in case of the same play you need to compare the value of the first card,

        return parseInputTriplets(input, 2).sortedBy { (hand, highestPlay, _) ->
            getHandValue(hand, highestPlay, 2)
        }.mapIndexed { index, (_, _, bid) -> (index + 1) * bid }.sum()
    }

    private fun String.highestPlay(part: Int = 1): Long {
        // this function will return a value from 1 to 7 depending on the play
        //7 Five of a kind, where all five cards have the same label: AAAAA
        //6 Four of a kind, where four cards have the same label and one card has a different label: AA8AA
        //5 Full house, where three cards have the same label, and the remaining two cards share a different label: 23332
        //4 Three of a kind, where three cards have the same label, and the remaining two cards are each different from any other card in the hand: TTT98
        //3 Two pair, where two cards share one label, two other cards share a second label, and the remaining card has a third label: 23432
        //2 One pair, where two cards share one label, and the other three cards have a different label from the pair and each other: A23A4
        //1 High card, where all cards' labels are distinct: 23456

        var charCount = this.charCountList()

        if (part == 2) {
            // now we need to pay attention to the 'J's
            val numberOfJokers = charCount.firstOrNull { it.first == 'J' }?.second ?: 0
            if (numberOfJokers == 5) {
                // edge case
                return 7
            }
            // remove the Js
            charCount = charCount.filter { it.first != 'J' }.sortedByDescending { it.second }.toMutableList()
            // now I add the jokers as the card with the biggest amount
            charCount[0] = charCount[0].first to charCount[0].second + numberOfJokers
        }

        return if (charCount.map { it.second }.contains(5)) 7
        else if (charCount.map { it.second }.contains(4)) 6
        else if (charCount.map { it.second }.contains(3) && charCount.map { it.second }.contains(2)) 5
        else if (charCount.map { it.second }.contains(3)) 4
        else if (charCount.map { it.second }.contains(2) && charCount.map { it.second }.size == 3) 3
        else if (charCount.map { it.second }.contains(2)) 2
        else 1
    }

    private fun String.charCountList(): List<Pair<Char, Int>> {
        return this.groupingBy { it }.eachCount().toList()
    }
}