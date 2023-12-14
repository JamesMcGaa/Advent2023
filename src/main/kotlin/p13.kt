import java.io.File

fun main() {
    val pictures = mutableListOf(Picture(mutableListOf()))
    val lines = File("inputs/input13.txt").forEachLine {
        if (it.isEmpty()) {
            pictures.add(Picture(mutableListOf()))
        } else {
            pictures.last().lines.add(it)
        }
    }

    var counterA = 0
    pictures.forEach {
        counterA += it.getReflectionIndex()
    }
    println(counterA)
}

data class Picture(
    val lines: MutableList<String>,
) {
    fun getReflectionIndex(): Int {
        findCenterRow@for (centerRowIdx in 0 .. lines.size - 2) {
            var upperIdx = centerRowIdx
            var lowerIdx = centerRowIdx + 1
            while (upperIdx >= 0 && lowerIdx <= lines.lastIndex) {
                if (getRow(upperIdx) != getRow(lowerIdx)) {
                    continue@findCenterRow
                }
                upperIdx -= 1
                lowerIdx += 1
            }
            return 100 * (centerRowIdx+1)
        }

        findCenterCol@for (centerColIdx in 0 .. lines[0].length - 2) {
            var leftIdx = centerColIdx
            var rightIdx = centerColIdx + 1
            while (leftIdx >= 0 && rightIdx <= lines[0].lastIndex) {
                if (getCol(leftIdx) != getCol(rightIdx)) {
                    continue@findCenterCol
                }
                leftIdx -= 1
                rightIdx += 1
            }
            return centerColIdx+1
        }

        return -1
    }

    fun getCol(idx: Int): String {
        var col = ""
        for (line in lines) {
            col += line[idx]
        }
        return col
    }

    fun getRow(idx: Int): String {
        return lines[idx]
    }
}
