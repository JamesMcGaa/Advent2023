import java.io.File

data class Node(
    val name: String,
    val left: String,
    val right: String
)

data class Ghost(
    var current: String,
    val seen: HashMap<Pair<String, Long>, Long> // Node, Move -> Index
)

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

    var MATH = mutableListOf<Pair<Long, Long>>()
    var counterB = 0L
    var currentGhosts = GRAPH.keys.filter { it.endsWith('A') }.map { Ghost(it, hashMapOf(Pair(it, 0L) to 0L)) }.toMutableSet()
    while (currentGhosts.isNotEmpty()) {
        val moveIdx = (counterB % MOVES.length).toLong()
        val move = MOVES[moveIdx.toInt()]
        val newGhosts = mutableSetOf<Ghost>()
        currentGhosts.forEach { ghost ->
            val newCurrent = if (move == 'L') GRAPH[ghost.current]!!.left else GRAPH[ghost.current]!!.right
            val newSeenEntry = Pair(newCurrent, moveIdx)
            if (ghost.seen.contains(newSeenEntry)) { // Loop detected
                MATH.add(Pair(counterB, counterB - ghost.seen[newSeenEntry]!!))
                println("${counterB}, ${counterB - ghost.seen[newSeenEntry]!!}")
            } else { // Update the ghost and keep looking for a loop
                ghost.seen[newSeenEntry] = counterB
                ghost.current = newCurrent
                newGhosts.add(ghost)
            }
        }
        currentGhosts = newGhosts
        counterB += 1
    }
    println(MATH)

//    var MATH = mutableListOf(
//        Pair(12086L, 12083L),
//        Pair(14894L, 14893L),
//        Pair(17142L, 17141L),
//        Pair(19952L, 19951L),
//        Pair(20520L, 20513L),
//        Pair(22201L, 22199L)
//    )
    MATH = MATH.map {
        Pair(it.first % it.second, it.second)
    }.toMutableList()
    println(MATH)

    var current = MATH.first().first
    var lcm = MATH.first().second
    MATH.remove(MATH.first())
    while (MATH.isNotEmpty()) {
        current += lcm
//        println(current)
        for (pair in MATH) {
            if (current % pair.second == (pair.first % pair.second)) {
                MATH.remove(pair)
                println(MATH.size)
                lcm *= pair.second
            }
        }
    }
    println(current)


}
