import java.io.File

data class Node(
    val name: String,
    val left: String,
    val right: String
) {

}

fun main() {
    val input = File("inputs/input8.txt").readLines()
    val MOVES = input[0]
    val GRAPH = hashMapOf<String, Node>()
    input.subList(2, input.lastIndex + 1).forEach {
        val start = it.substring(0, 2 + 1)
        val left = it.substring(7, 9 + 1)
        val right = it.substring(12, 14 + 1)
        GRAPH[start] = Node(start, left, right)
    }
    println(GRAPH)

    var counterB = 0
    var currentSet = GRAPH.keys.filter { it.endsWith('A') }
    while (currentSet.filter { !it.endsWith('Z') }.isNotEmpty()) {
        val move = MOVES[counterB % MOVES.length]
        if (move == 'L') {
            currentSet = currentSet.map { GRAPH[it]!!.left }
        } else {
            currentSet = currentSet.map { GRAPH[it]!!.right }
        }
        counterB += 1
    }
    println(counterB)
}
