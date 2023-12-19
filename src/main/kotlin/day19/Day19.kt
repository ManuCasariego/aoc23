package day19

import Day
import Utils.splitByEmpty
import java.util.Stack

class Day19(private val input: String) : Day() {
    override fun part1(): Long {
        //ex{x>10:one,m<20:two,a>30:R,A}
        // R rejected
        // A approved
        val twoPartsInput = input.lines().splitByEmpty()
        val rulesMap = mutableMapOf<String, List<Rule>>()
        twoPartsInput.first().forEach { line ->
            val id = line.substringBefore('{')
            val rules = line.substringAfter('{').substringBefore('}').split(',')
            val parsedRules = rules.map { string ->
                //s>2236:A,s<1879:A,x<3217:R,R
                if (string.contains(':')) {
                    if (string.contains('<')) {
                        // smaller than
                        val variable = string.substringBefore('<')
                        val operation = '<'
                        val value = string.substringAfter('<').substringBefore(':').toInt()
                        val destination = string.substringAfter(':')
                        Rule(variable, operation, value, destination)
                    } else {
                        val variable = string.substringBefore('>')
                        val operation = '>'
                        val value = string.substringAfter('>').substringBefore(':').toInt()
                        val destination = string.substringAfter(':')
                        Rule(variable, operation, value, destination)
                    }

                } else {
                    // easy rule
                    Rule(null, null, null, string)
                }
            }
            rulesMap[id] = parsedRules
            InputLine(id, parsedRules)

        }


        val b = twoPartsInput.last().map { line ->
            //{x=787,m=2655,a=1222,s=2876}
            val valuesMap = mutableMapOf<String, Int>()
            line.substringAfter('{').substringBefore('}').split(',').forEach { string ->
                valuesMap[string.substringBefore('=')] = string.substringAfter('=').toInt()
            }
            valuesMap
        }

        val acceptedParts = mutableListOf<List<Int>>()
        b.forEach { valueMap ->
            var currentSetOfRules = "in"
            var state = State.ON_GOING
            while (state == State.ON_GOING) {
                val currentInputLine = rulesMap[currentSetOfRules]!!
                val manu = solveEverything(currentInputLine, valueMap)
                state = manu.first
                if (state == State.ON_GOING) {
                    currentSetOfRules = manu.second
                } else if (state == State.ACCEPTED) {
                    acceptedParts.add(valueMap.values.toList())
                } else {
                    println("rejected")
                }
            }


        }

        return acceptedParts.sumOf { it.sum() }.toLong()
    }

    data class InputLine(val id: String, val rules: List<Rule>)
    data class Rule(val variable: String?, val operation: Char?, val value: Int?, val destination: String)
    enum class State { ACCEPTED, REJECTED, ON_GOING }


    fun solveEverything(rulesList: List<Rule>, valueMap: MutableMap<String, Int>): Pair<State, String> {


        rulesList.forEach { rule ->
            if (rule.variable == null) {
                // it's an easy redirect rule
                return when (rule.destination) {
                    "R" -> {
                        State.REJECTED to ""
                    }

                    "A" -> {
                        State.ACCEPTED to ""
                    }

                    else -> {
                        State.ON_GOING to rule.destination
                    }
                }
            } else {
                // it's a value rule
                if (rule.operation == '<') {
                    if (valueMap.containsKey(rule.variable)) {
                        if (valueMap[rule.variable]!! < rule.value!!) {
                            if (rule.destination == "R") {
                                return State.REJECTED to ""
                            } else if (rule.destination == "A") {
                                return State.ACCEPTED to ""
                            } else {
                                return State.ON_GOING to rule.destination
                            }
                        }
                    }
                    return@forEach

                } else {
                    if (valueMap[rule.variable]!! > rule.value!!) {
                        if (rule.destination == "R") {
                            return State.REJECTED to ""
                        } else if (rule.destination == "A") {
                            return State.ACCEPTED to ""
                        } else {
                            return State.ON_GOING to rule.destination
                        }
                    }
                }
            }
        }

        return State.REJECTED to ""
    }

    override fun part2(): Long {
        //Each of the four ratings (x, m, a, s) can have an integer value ranging from a minimum
        // of 1 to a maximum of 4000. Of all possible distinct combinations of ratings,
        // your job is to figure out which ones will be accepted.
        val twoPartsInput = input.lines().splitByEmpty()
        val rulesMap = mutableMapOf<String, List<Rule>>()
        val a = twoPartsInput.first().map { line ->
            val id = line.substringBefore('{')
            val rules = line.substringAfter('{').substringBefore('}').split(',')
            val parsedRules = rules.map { string ->
                //s>2236:A,s<1879:A,x<3217:R,R
                if (string.contains(':')) {
                    if (string.contains('<')) {
                        // smaller than
                        val variable = string.substringBefore('<')
                        val operation = '<'
                        val value = string.substringAfter('<').substringBefore(':').toInt()
                        val destination = string.substringAfter(':')
                        Rule(variable, operation, value, destination)
                    } else {
                        val variable = string.substringBefore('>')
                        val operation = '>'
                        val value = string.substringAfter('>').substringBefore(':').toInt()
                        val destination = string.substringAfter(':')
                        Rule(variable, operation, value, destination)
                    }

                } else {
                    // easy rule
                    Rule(null, null, null, string)
                }
            }
            rulesMap[id] = parsedRules
            InputLine(id, parsedRules)

        }

        // we start with xrange = 0, 4001 and so on
        // we get here in{s<1351:px,qqz}
        // first rule in there s < 1351
        // then we need to create two stacks from there
        // one for px and one to continue the party
        //RuleForPart2("in", Pair(0, 4001), Pair(0, 4001), Pair(0, 4001), Pair(0, 4001)) turns to
        // RuleForPart2("px", Pair(0, 1351), Pair(0, 4001), Pair(0, 4001), Pair(0, 4001)) and
        // RuleForPart2("continue", Pair(1351, 4001), Pair(0, 4001), Pair(0, 4001), Pair(0, 4001))

        val stack = Stack<RuleForPart2>()
        stack.add(RuleForPart2("in", Pair(0, 4001), Pair(0, 4001), Pair(0, 4001), Pair(0, 4001)))
        while (stack.isNotEmpty()) {
            var currentRule = stack.pop()
            val currentInputLine = rulesMap[currentRule.id]!!
            currentInputLine.forEach { rule ->
                if (currentRule == null) {
                    return@forEach
                }
                var manu = doStuffPart2(currentRule, rule)
                if (manu.isNotEmpty() && manu.first().id == "A") {
                    addToAcceptedStuffPart2(manu.first())
                    manu = manu.drop(1)
                }
                if (manu.isNotEmpty() && manu.first().id == "R") {
                    manu = manu.drop(1)
                }
                if (manu.isNotEmpty() && manu.first().id == "A") {
                    addToAcceptedStuffPart2(manu.first())
                    manu = manu.drop(1)
                }
                if (manu.isNotEmpty() && manu.first().id == "R") {
                    manu = manu.drop(1)
                }
                if (manu.size == 2) {
                    stack.add(manu.first())
                    currentRule = manu.last()
                }
                if (manu.size == 1) {
                    if (manu.first().id == "continue") {
                        currentRule = manu.first()
                    } else {
                        stack.add(manu.first())
                        currentRule = null
                    }
                }
            }
        }
        return acceptedStuffPart2
    }

    fun addToAcceptedStuffPart2(rule: RuleForPart2) {
        val possibleXs = IntRange(rule.xRange.first + 1, rule.xRange.second - 1).count().toLong()
        val possibleMs = IntRange(rule.mRange.first + 1, rule.mRange.second - 1).count().toLong()
        val possibleAs = IntRange(rule.aRange.first + 1, rule.aRange.second - 1).count().toLong()
        val possibleSs = IntRange(rule.sRange.first + 1, rule.sRange.second - 1).count().toLong()
        acceptedStuffPart2 += possibleXs * possibleMs * possibleAs * possibleSs
    }

    data class RuleForPart2(
        val id: String,
        val xRange: Pair<Int, Int>,
        val mRange: Pair<Int, Int>,
        val aRange: Pair<Int, Int>,
        val sRange: Pair<Int, Int>,
    )

    var acceptedStuffPart2 = 0L

    fun doStuffPart2(rule: RuleForPart2, dividerRule: Rule): List<RuleForPart2> {
        // dividerRule is x < 1351: px
        // m<471:A,a<2572:rzf,s>2871:A,R
        //RuleForPart2("in", Pair(0, 4001), Pair(0, 4001), Pair(0, 4001), Pair(0, 4001)) turns to
        //RuleForPart2("px", Pair(0, 1351), Pair(0, 4001), Pair(0, 4001), Pair(0, 4001)) and
        //RuleForPart2("continue", Pair(1351, 4001), Pair(0, 4001), Pair(0, 4001), Pair(0, 4001))

        if (dividerRule.variable == null) {
            // it's an easy redirect rule
            when (dividerRule.destination) {
                "R" -> {
                    return listOf(RuleForPart2("R", rule.xRange, rule.mRange, rule.aRange, rule.sRange))
                }

                "A" -> {
                    return listOf(RuleForPart2("A", rule.xRange, rule.mRange, rule.aRange, rule.sRange))
                }

                else -> {
                    // go to another rule
                    return listOf(
                        RuleForPart2(
                            dividerRule.destination,
                            rule.xRange,
                            rule.mRange,
                            rule.aRange,
                            rule.sRange
                        )
                    )
                }
            }
        } else {
            // it's a value rule
            if (dividerRule.operation == '<') {
                if (dividerRule.variable == "x") {
                    val newRule = RuleForPart2(
                        dividerRule.destination,
                        Pair(rule.xRange.first, dividerRule.value!!),
                        rule.mRange,
                        rule.aRange,
                        rule.sRange
                    )
                    val newRuleContinue = RuleForPart2(
                        "continue",
                        Pair(dividerRule.value - 1, rule.xRange.second),
                        rule.mRange,
                        rule.aRange,
                        rule.sRange
                    )
                    return listOf(newRule, newRuleContinue)
                } else if (dividerRule.variable == "m") {
                    val newRule = RuleForPart2(
                        dividerRule.destination,
                        rule.xRange,
                        Pair(rule.mRange.first, dividerRule.value!!),
                        rule.aRange,
                        rule.sRange
                    )
                    val newRuleContinue = RuleForPart2(
                        "continue",
                        rule.xRange,
                        Pair(dividerRule.value - 1, rule.mRange.second),
                        rule.aRange,
                        rule.sRange
                    )
                    return listOf(newRule, newRuleContinue)
                } else if (dividerRule.variable == "a") {
                    val newRule = RuleForPart2(
                        dividerRule.destination,
                        rule.xRange,
                        rule.mRange,
                        Pair(rule.aRange.first, dividerRule.value!!),
                        rule.sRange
                    )
                    val newRuleContinue = RuleForPart2(
                        "continue",
                        rule.xRange,
                        rule.mRange,
                        Pair(dividerRule.value - 1, rule.aRange.second),
                        rule.sRange
                    )
                    return listOf(newRule, newRuleContinue)
                } else if (dividerRule.variable == "s") {
                    val newRule = RuleForPart2(
                        dividerRule.destination,
                        rule.xRange,
                        rule.mRange,
                        rule.aRange,
                        Pair(rule.sRange.first, dividerRule.value!!)
                    )
                    val newRuleContinue = RuleForPart2(
                        "continue",
                        rule.xRange,
                        rule.mRange,
                        rule.aRange,
                        Pair(dividerRule.value - 1, rule.sRange.second)
                    )
                    return listOf(newRule, newRuleContinue)
                }


            } else if (dividerRule.operation == '>') {
                if (dividerRule.variable == "x") {
                    val newRule = RuleForPart2(
                        dividerRule.destination,
                        Pair(dividerRule.value!!, rule.xRange.second),
                        rule.mRange,
                        rule.aRange,
                        rule.sRange
                    )
                    val newRuleContinue = RuleForPart2(
                        "continue",
                        Pair(rule.xRange.first, dividerRule.value + 1),
                        rule.mRange,
                        rule.aRange,
                        rule.sRange
                    )
                    return listOf(newRule, newRuleContinue)
                } else if (dividerRule.variable == "m") {
                    val newRule = RuleForPart2(
                        dividerRule.destination,
                        rule.xRange,
                        Pair(dividerRule.value!!, rule.mRange.second),
                        rule.aRange,
                        rule.sRange
                    )
                    val newRuleContinue = RuleForPart2(
                        "continue",
                        rule.xRange,
                        Pair(rule.mRange.first, dividerRule.value + 1),
                        rule.aRange,
                        rule.sRange
                    )
                    return listOf(newRule, newRuleContinue)
                } else if (dividerRule.variable == "a") {
                    val newRule = RuleForPart2(
                        dividerRule.destination,
                        rule.xRange,
                        rule.mRange,
                        Pair(dividerRule.value!!, rule.aRange.second),
                        rule.sRange
                    )
                    val newRuleContinue = RuleForPart2(
                        "continue",
                        rule.xRange,
                        rule.mRange,
                        Pair(rule.aRange.first, dividerRule.value + 1),
                        rule.sRange
                    )
                    return listOf(newRule, newRuleContinue)
                } else if (dividerRule.variable == "s") {
                    val newRule = RuleForPart2(
                        dividerRule.destination,
                        rule.xRange,
                        rule.mRange,
                        rule.aRange,
                        Pair(dividerRule.value!!, rule.sRange.second)
                    )
                    val newRuleContinue = RuleForPart2(
                        "continue",
                        rule.xRange,
                        rule.mRange,
                        rule.aRange,
                        Pair(rule.sRange.first, dividerRule.value + 1)
                    )
                    return listOf(newRule, newRuleContinue)
                }
            }
        }
        return emptyList()
    }
}