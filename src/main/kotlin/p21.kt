import java.io.File

data class GraphCoord(val x: Int, val y: Int)

data class Walk(
    val current: GraphCoord,
    val dist: Int
) {
    fun getNeighbors(grid: Map<GraphCoord, Char>): List<Walk> {
        if (dist == 64) return emptyList()

        val ret = mutableListOf<Walk>()
        for (offset in mutableListOf(
            GraphCoord(0, 1), GraphCoord(0, -1), GraphCoord(1, 0), GraphCoord(-1, 0)
        )) {
            val potential = GraphCoord(current.x + offset.x, current.y + offset.y)
            if (setOf('.', 'S').contains(grid[potential])) {
                ret.add(this.copy(current = potential,
                    dist = dist + 1))
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
            0
        )
    )

    repeat(64) {
        val nextWalks = mutableSetOf<Walk>()
        for (walk in walks) {
            nextWalks.addAll(walk.getNeighbors(grid))
        }
        walks = nextWalks
    }

    println(walks.size)
}