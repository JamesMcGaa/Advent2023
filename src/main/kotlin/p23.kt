import java.io.File
import kotlin.math.max

data class GraphNode(
    var adj: List<Node>
)
data class TrailExplorer(
    val current: GraphCoord,
    val seen: Set<GraphCoord>,
) {
    fun getNeighbors(
        grid: Map<GraphCoord, Char>,
        isPartB: Boolean = false,
        setGrid: Set<GraphCoord> = setOf()
    ): List<TrailExplorer> {
        val ret = mutableListOf<TrailExplorer>()

        val offsets =
            if (isPartB) {
                mutableListOf(
                    GraphCoord(0, 1), GraphCoord(0, -1), GraphCoord(1, 0), GraphCoord(-1, 0)
                )
            } else {
                when (grid[current]) {
                    '>' -> mutableListOf(GraphCoord(0, 1))
                    '<' -> mutableListOf(GraphCoord(0, -1))
                    '^' -> mutableListOf(GraphCoord(-1, 0))
                    'v' -> mutableListOf(GraphCoord(1, 0))
                    else -> mutableListOf(
                        GraphCoord(0, 1), GraphCoord(0, -1), GraphCoord(1, 0), GraphCoord(-1, 0)
                    )
                }
            }

        for (offset in offsets) {
            var potential = GraphCoord(current.x + offset.x, current.y + offset.y)
            if (!isPartB) {
                if (setOf('.', '^', 'v', '<', '>').contains(grid[potential]) && !seen.contains(potential)) {
                    ret.add(this.copy(current = potential, seen = seen.toMutableSet().apply { add(potential) }))
                }
            } else {
                if (setGrid.contains(potential) && !seen.contains(potential)) {
                    ret.add(this.copy(current = potential, seen = seen.toMutableSet().apply { add(potential) }))
                }
            }
        }
        return ret
    }
}

fun main() {
    val bridgeFinder = BridgeFinder()
//    bridgeFinder.solveA()

}

class BridgeFinder {
    lateinit var start: GraphCoord
    lateinit var end: GraphCoord
    val gridA = mutableMapOf<GraphCoord, Char>()
    val gridB = mutableSetOf<GraphCoord>()

    lateinit var bannedNode: GraphCoord

    init {
        val input = File("inputs/input23.txt").readLines()
        for (x in input.indices) {
            for (y in input[x].indices) {
                gridA[GraphCoord(x, y)] = input[x][y]

                if (input[x][y] != '#') {
                    gridB.add(GraphCoord(x, y))
                }

                if (x == 0 && input[x][y] == '.') {
                    start = GraphCoord(x, y)
                }
                if (x == input.lastIndex && input[x][y] == '.') {
                    end = GraphCoord(x, y)
                }
            }
        }

        val dominators = mutableSetOf<GraphCoord>()
        for (v in gridB) {
            bannedNode = v
            if (!dfsWithBanned()) {
                dominators.add(v)
            }
        }

        val trueDominators = mutableListOf<GraphCoord>()
        for (v in dominators) {
            val neighbors = v.getNeighborsUnfiltered().filter { gridB.contains(it) }
            if (!(neighbors.size == 2 && neighbors.filter { dominators.contains(it) }.size == 2)) {
                trueDominators.add(v)
            }
        }

        val ordered = bfs()
        trueDominators.sortedBy { ordered.indexOf(it) }
        println(trueDominators)

        println(gridB.size)
        println(dominators.size)
        println(trueDominators.size)

        val vertexToDominator = mutableMapOf<GraphCoord, Set<GraphCoord>>()
        for (v in gridB) {
            vertexToDominator[v] = findDominators(trueDominators.toSet(), v)
        }
        val dominatorToVertex = mutableMapOf<GraphCoord, Set<GraphCoord>>()
        for (trueDominator in trueDominators) {
            dominatorToVertex[trueDominator] =
                vertexToDominator.keys.filter { vertexToDominator[it]!!.contains(trueDominator) }.toSet()
        }
        println(vertexToDominator)
        println(dominatorToVertex)

        var counterB = 0
        for (i in 0..trueDominators.lastIndex - 1) {
            counterB += generalizedDist(
                trueDominators[i]!!,
                trueDominators[i + 1]!!,
                dominatorToVertex[trueDominators[i]!!]!! union dominatorToVertex[trueDominators[i + 1]!!]!!
            )
        }

        println(counterB - (trueDominators.size - 2))

        println(generalizedDist(start, end, gridB) - 1)
    }

    fun generalizedDist(a: GraphCoord, b: GraphCoord, grid: Set<GraphCoord>): Int {
        val stack = mutableListOf(TrailExplorer(a, setOf<GraphCoord>(a)))
        var longestSoFar = 0
        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            if (current.current == b) {
                longestSoFar = max(longestSoFar, current.seen.size)
                continue
            }
            stack.addAll(current.getNeighbors(mutableMapOf<GraphCoord, Char>(), true, grid))
        }
        return longestSoFar
    }

    fun findDominators(dominators: Set<GraphCoord>, begin: GraphCoord): Set<GraphCoord> {
        val queue = mutableListOf(begin)
        val seen = mutableListOf<GraphCoord>()
        val adj = mutableSetOf<GraphCoord>()
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (dominators.contains(current)) {
                adj.add(current)
                continue
            } else if (!gridB.contains(current) || seen.contains(current)) {
                continue
            } else {
                seen.add(current)
                queue.addAll(current.getNeighborsUnfiltered().filter { gridB.contains(it) })
            }
        }
        return adj
    }

    fun bfs(): List<GraphCoord> {
        val queue = mutableListOf(start)
        val seen = mutableListOf<GraphCoord>()
        while (queue.isNotEmpty()) {
            val current = queue.removeFirst()
            if (current == end) {
                return seen
            } else if (!gridB.contains(current) || seen.contains(current)) {
                continue
            } else {
                seen.add(current)
                queue.addAll(current.getNeighborsUnfiltered().filter { gridB.contains(it) })
            }

        }
        throw Exception("Didnt find end in bfs")
    }

    fun dfsWithBanned(): Boolean {
        val stack = mutableListOf(start)
        val seen = mutableSetOf<GraphCoord>()
        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            if (current == end) {
                return true
            } else if (current == bannedNode || !gridB.contains(current) || seen.contains(current)) {
                continue
            } else {
                seen.add(current)
                stack.addAll(current.getNeighborsUnfiltered().filter { gridB.contains(it) })
            }

        }
        return false
    }


    fun solveA() {
        val stack = mutableListOf(TrailExplorer(start, setOf<GraphCoord>()))
        var longestSoFar = 0
        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            if (current.current == end) {
                longestSoFar = max(longestSoFar, current.seen.size)
                continue
            }
            stack.addAll(current.getNeighbors(gridA))
        }
        println(longestSoFar)
    }
}