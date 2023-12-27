import java.io.File

data class WirePath(
    val current: String,
    val seen: MutableSet<String> = mutableSetOf(),
    var parent: String = ""
)

fun main() {
    val adj = mutableMapOf<String, MutableSet<String>>()
    File("inputs/input25.txt").forEachLine {
        val source = it.split(":")[0].trim()
        val dests = it.split(":")[1].split(" ").map { it.trim() }.filter { it.isNotEmpty() }
        if (!adj.contains(source)) {
            adj[source] = mutableSetOf()
        }
        adj[source]!!.addAll(dests)
        for (dest in dests) {
            if (!adj.contains(dest)) {
                adj[dest] = mutableSetOf()
            }
            adj[dest]!!.add(source)
        }
    }

    val goodPaths = mutableListOf<MutableSet<String>>()




}