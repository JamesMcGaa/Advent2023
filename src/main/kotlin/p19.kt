import java.io.File

data class Part(
    val x: Int,
    val m: Int,
    val a: Int,
    val s: Int,
) {
    val ratingNumber = x + m + a + s
}

data class Workflow(
    val identifier: String,
    val conditions: List<Conditional>,
    val default: String
) {
    fun runForPart(part: Part): String {
        for (condition in conditions) {
            if (condition.matchesPart(part)) {
                return condition.dest
            }
        }
        return default
    }
}

data class Conditional(
    val category: Char,
    val ineq: Char,
    val comparison: Int,
    val dest: String,
) {
    fun matchesPart(part: Part): Boolean {
        when(category) {
            'x' -> {
                return if (ineq == '>') part.x > comparison else part.x < comparison
            }
            'm' -> {
                return if (ineq == '>') part.m > comparison else part.m < comparison
            }
            'a' -> {
                return if (ineq == '>') part.a > comparison else part.a < comparison
            }
            's' -> {
                return if (ineq == '>') part.s > comparison else part.s < comparison
            }
            else -> throw Exception("Bad Category")
        }
    }
}

fun strToConditional(input: String): Conditional {
    val dest = input.split(":")[1]
    val content = input.split(":")[0]
    val ineq = content[1]
    val category = content[0].toChar()
    val comparison = content.substring(2).toInt()
    return Conditional(category, ineq, comparison, dest)
}

fun main() {
    val workFlows = mutableMapOf<String, Workflow>()
    val partsList = mutableMapOf<Part, String>()

    var loadingConditions = true
    File("inputs/input19.txt").forEachLine {
        if (it.isBlank()) {
            loadingConditions = false
            return@forEachLine
        }

        if (loadingConditions) {
            val begin = it.indexOf('{')
            val identifier = it.substring(0, begin)
            val content = it.substring(begin + 1, it.lastIndex).split(",").toMutableList()
            val default = content.removeLast()
            workFlows[identifier] = Workflow(
                identifier, content.map { strToConditional(it) }, default
            )
        } else {
            val tokens = it.removePrefix("{").removeSuffix("}").split(",")
            partsList[
                Part(
                    tokens[0].substring(2).toInt(),
                    tokens[1].substring(2).toInt(),
                    tokens[2].substring(2).toInt(),
                    tokens[3].substring(2).toInt(),
                )
            ] = "in"
        }
    }

    for (part in partsList.keys) {
        var currentWorkflow = partsList[part]!!
        while (!listOf("A", "R").contains(currentWorkflow)) {
            currentWorkflow = workFlows[currentWorkflow]!!.runForPart(part)
        }
        partsList[part] = currentWorkflow
    }

    var counterA = 0
    for (part in partsList.keys) {
        if (partsList[part]!! == "A") {
            counterA += part.ratingNumber
        }
    }
    println(counterA)

    return
}