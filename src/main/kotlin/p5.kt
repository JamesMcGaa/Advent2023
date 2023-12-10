import java.io.File

data class ConversionRange(
    val dest: Long,
    val source: Long,
    val length: Long,
)

data class Mapping(
    val sourceType: String,
    val destType: String,
    val ranges: MutableList<ConversionRange>
) {
    fun convert(source: Long): Long {
        for (range in ranges) {
            if (source >= range.source && source <= range.source + range.length - 1) {
                return range.dest + (source - range.source)
            }
        }
        return source
    }
}

fun main() {
    val MAPPINGS = hashMapOf<String, Mapping>()
    var SEEDS = listOf<Long>()
    var currentSource = ""
    File("inputs/input5.txt").forEachLine { line ->
        if (SEEDS.isEmpty()) {
            SEEDS = line.split(":")[1].split(" ").filter { it.isNotBlank() }.map { it.toLong() }
        } else {
            if (line.contains("map")) {
                val dashed = line.split(" ")[0]
                val source = dashed.split("-")[0]
                val dest = dashed.split("-")[2]
                currentSource = source
                MAPPINGS[source] = Mapping(source, dest, mutableListOf())
            } else if (line.isNotBlank()) {
                val nums = line.split(" ").map { it.toLong() }
                MAPPINGS[currentSource]!!.ranges.add(
                    ConversionRange(
                        nums[0], nums[1], nums[2]
                    )
                )
            }
        }
    }

    var currentVals = SEEDS.toList()
    var currentType = "seed"
    while (currentType != "location") {
        val mapping = MAPPINGS[currentType]!!
        currentType = mapping.destType
        currentVals = currentVals.map { mapping.convert(it) }
    }

    println(currentVals.min())

}
