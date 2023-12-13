import java.io.File

data class Line(
    var line: String,
    var contiguous: List<Int>,
)

fun main() {
    var counterA = 0
    var counterB = 0

    val LINES = mutableListOf<Line>()
    File("inputs/input12.txt").forEachLine {
        LINES.add(
            Line(
                it.split(" ")[0],
                it.split(" ")[1].split(",").map { it.toInt() },
            )
        )
    }

    for (line in LINES) {
        println(LINES.indexOf(line))
        val numQuestions = line.line.count { it == '?' }
        for (i in 0..(1 shl numQuestions).toInt() - 1) { // For all possible ? arrangements
            var currQuestionIdx = 0
            var formattedString = ""
            val binString = i.toString(radix = 2).padStart(numQuestions, '0')
            for (ch in line.line) {
                if (ch == '?') {
                    formattedString += if (binString[currQuestionIdx] == '1') '#' else '.'
                    currQuestionIdx += 1
                } else {
                    formattedString += ch
                }
            }
            val contigs = formattedString.split(".").filter { it.isNotBlank() }.map {it.length}
            if (contigs.equals(line.contiguous)) {
                counterA += 1
            }
        }
    }
    println(counterA)

    for (line in LINES) {
        line.line = line.line.repeat(5)
        val oldContiguous = line.contiguous.toMutableList()
        line.contiguous = mutableListOf<Int>().apply {
            repeat(5){ this.addAll(oldContiguous) }
        }
    }



}