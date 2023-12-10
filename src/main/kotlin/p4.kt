import java.io.File
fun main() {
    var counterA = 0
    var counterB = 0
    File("inputs/input4.txt").forEachLine {
        line ->
        val nums = line.split(":")[1].trim()
        val left = nums.split("|")[0].trim().split(" ").filter {it.isNotBlank()}.map {it.toInt()}.toSet()
        val right = nums.split("|")[1].trim().split(" ").filter {it.isNotBlank()}.map {it.toInt()}.toSet()
        counterA += (1L shl (left intersect right).size - 1).toInt()
    }

    println(counterA)
    println(counterB)
}
