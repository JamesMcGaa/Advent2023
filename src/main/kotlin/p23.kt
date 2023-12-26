import java.io.File
import kotlin.math.max
import kotlin.math.min

data class TrailExplorer(
    val current: GraphCoord,
    val seen: Set<GraphCoord>,
) {
    fun getNeighbors(grid: Map<GraphCoord, Char>): List<TrailExplorer> {
        val ret = mutableListOf<TrailExplorer>()

        val offsets = when (grid[current]) {
            '>' -> mutableListOf(GraphCoord(0, 1))
            '<' -> mutableListOf(GraphCoord(0, -1))
            '^' -> mutableListOf(GraphCoord(-1, 0))
            'v' -> mutableListOf(GraphCoord(1, 0))
            else -> mutableListOf(
                GraphCoord(0, 1), GraphCoord(0, -1), GraphCoord(1, 0), GraphCoord(-1, 0)
            )
        }

        for (offset in offsets) {
            var potential = GraphCoord(current.x + offset.x, current.y + offset.y)
            if (setOf('.', '^', 'v', '<', '>').contains(grid[potential]) && !seen.contains(potential)) {
                ret.add(this.copy(current = potential, seen = seen.toMutableSet().apply { add(potential) }))
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

//    val visited = mutableSetOf<GraphCoord>()
//    val tin = mutableMapOf<GraphCoord, Int>()
//    val low = mutableMapOf<GraphCoord, Int>()
//    var timer = 0

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
            if (dfsWithBanned()) {
                dominators.add(v)
            }
        }

        println(gridB.size)
        println(dominators.size)

//        for (v in gridB) {
//            tin[v] = -1
//            low[v] = -1
//        }
//        println(gridB.size)
//        dfs(start, null)
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

//    fun dfs(v: GraphCoord, p: GraphCoord?) {
//        if (visited.contains(v)) {
//            return
//        }
//        visited.add(v)
////        println(visited.size)
//        tin[v] = timer
//        low[v] = timer
//        timer++
//
//        val neighbors =  v.getNeighborsUnfiltered().filter { gridB.contains(it) }
//        for (to in neighbors) {
//            if (to == p) {
//                continue
//            }
//            if (visited.contains(to)) {
//                low[v] = min(low[v]!!, tin[to]!!)
//            } else {
//                dfs(to, v)
//                low[v] = min(low[v]!!, tin[to]!!)
//                if(low[to]!! > tin[v]!!) {
//                    println(v)
//                }
//            }
//        }
//    }

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