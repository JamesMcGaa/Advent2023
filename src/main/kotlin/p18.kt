import java.io.File


data class Coord(var x: Int, var y: Int)

fun main() {

    var current = Coord(0,0)
    val board = hashMapOf<Coord, Int>(Coord(0,0) to 1)
    File("inputs/input18.txt").forEachLine {
        val orientation = it.split(" ")[0]
        val amount = it.split(" ")[1].toInt()
        val color = it.split(" ")[2]

        when (orientation) {
            "R" -> {
                for (i in current.y + 1 .. current.y + amount) {
                    board[current.copy(y = i)] = 1
                }
                current = current.copy(y = current.y + amount)
            }
            "L" -> {
                for (i in current.y - amount .. current.y - 1) {
                    board[current.copy(y = i)] = 1
                }
                current = current.copy(y = current.y - amount)
            }
            "U" -> {
                for (i in current.x - amount .. current.x - 1) {
                    board[current.copy(x = i)] = 1
                }
                current = current.copy(x = current.x - amount)
            }
            "D" -> {
                for (i in current.x + 1 .. current.x + amount) {
                    board[current.copy(x = i)] = 1
                }
                current = current.copy(x = current.x + amount)
            }
            else -> throw Exception("Bad orientation")
        }
    }

    val xMin = board.keys.map {it.x}.min()
    val xMax = board.keys.map {it.x}.max()
    val yMin = board.keys.map {it.y}.min()
    val yMax = board.keys.map {it.y}.max()

    current = Coord(xMin- 1, yMin - 1)
    val stack = mutableListOf(current)
    val flooded = mutableSetOf<Coord>()
    while (stack.isNotEmpty()) {
        current = stack.removeLast()
        if (current in flooded) {
            continue
        }
        if (current.x < xMin - 1 || current.x > xMax + 1 || current.y < yMin - 1 || current.y > yMax + 1) { // Truly out of bounds
            continue
        }
        if (board[current] != null)  { // Hit a trench
            continue
        }
        flooded.add(current)
        stack.add(current.copy(x = current.x+1))
        stack.add(current.copy(x = current.x-1))
        stack.add(current.copy(y = current.y+1))
        stack.add(current.copy(y = current.y-1))
    }

    println("${xMin}, ${xMax}, ${yMin}, ${yMax}, ${flooded.size}")
    println((xMax - xMin + 3) * (yMax - yMin + 3) - flooded.size)

}