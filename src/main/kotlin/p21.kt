import java.io.File

data class GraphCoord(var x: Int, var y: Int) {
    fun getNeighborsUnfiltered(): List<GraphCoord> {
        val ret = mutableListOf<GraphCoord>()

        val offsets = mutableListOf(
                GraphCoord(0, 1), GraphCoord(0, -1), GraphCoord(1, 0), GraphCoord(-1, 0)
            )

        for (offset in offsets) {
            ret.add(GraphCoord(x + offset.x, y + offset.y))

        }
        return ret
    }
}

data class ReflectedCoord(var x: Int, var y: Int, var xr: Int, var yr: Int)

data class Walk(
    val current: GraphCoord,
    val dist: Int,
    val xReflection: Int,
    val yReflection: Int
) {


    val reflected = ReflectedCoord(current.x, current.y, xReflection, yReflection)

//    val hashCode: String = current.toString() + xReflection.toString() + ":" + yReflection.toString()

    fun getNeighbors(grid: Map<GraphCoord, Char>): List<Walk> {

        val ret = mutableListOf<Walk>()
        for (offset in mutableListOf(
            GraphCoord(0, 1), GraphCoord(0, -1), GraphCoord(1, 0), GraphCoord(-1, 0)
        )) {
            var potential = GraphCoord(current.x + offset.x, current.y + offset.y)
            var xr = xReflection
            var yr = yReflection
            if (grid[potential] == null) {
                if (offset.x == 1) {
                    potential = potential.copy(x = 0)
                    xr += 1
                }
                if (offset.x == -1) {
                    potential = potential.copy(x = grid.keys.map {it.x}.max())
                    xr -= 1
                }
                if (offset.y == 1) {
                    potential = potential.copy(y = 0)
                    yr += 1
                }
                if (offset.y == -1) {
                    potential = potential.copy(y = grid.keys.map {it.y}.max())
                    yr -= 1
                }
            }
            if (setOf('.', 'S').contains(grid[potential])) {
                ret.add(this.copy(current = potential,
                    dist = dist + 1, xReflection = xr, yReflection = yr))
            }
        }
        return ret
    }
}

fun main() {
    lateinit var start: GraphCoord
    val grid = mutableMapOf<GraphCoord, Char>()
    val input = File("inputs/input21.txt").readLines()
    for (x in input.indices) {
        for (y in input[x].indices) {
            grid[GraphCoord(x, y)] = input[x][y]
            if (input[x][y] == 'S') {
                start = GraphCoord(x, y)
            }
        }
    }

    var walks = mutableSetOf<Walk>(
        Walk(
            start,
            0,
            0,
            0
        )
    )

    var safelyVisited = mutableSetOf<ReflectedCoord>(ReflectedCoord(start.x, start.y, 0, 0))

    repeat(10000) {
        val nextWalks = mutableSetOf<Walk>()
        for (walk in walks) {
            safelyVisited.add(walk.reflected)
            for (neighbor in walk.getNeighbors(grid)) {
                if (!safelyVisited.contains(neighbor.reflected)) {
                    nextWalks.add(neighbor)
                }
            }
        }
        walks = nextWalks
    }

    println(safelyVisited.size)
}