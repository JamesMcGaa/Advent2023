import java.io.File
import java.util.HashMap


fun main() {
    val BOARD = hashMapOf<Pair<Int, Int>, Char>()
    val lines = File("inputs/input10.txt").readLines()
    val POSSIBLE_PIPES = mutableListOf('|', '-', 'L', 'J', '7', 'F')

    lateinit var START_COORDS: Pair<Int, Int>

    for (y in lines.indices) {
        for (x in lines[0].indices) {
            BOARD[Pair(x, y)] = lines[y][x]
            if (lines[y][x] == 'S') {
                START_COORDS = Pair(x, y)
            }
        }
    }

    for (pipeCast in POSSIBLE_PIPES) {
        BOARD[START_COORDS] = pipeCast
        val stack = mutableListOf(START_COORDS)
        val seen: HashMap<Pair<Int, Int>, Int> = hashMapOf(START_COORDS to -1)
        while (stack.isNotEmpty()) {
            val current = stack.removeLast()
            if (seen.containsKey(current) && seen[current] != -1) {
                seen[current] = seen.getOrDefault(current, 0) + 1
                continue
            } else {
                seen[current] = seen.getOrDefault(current, 0) + 1
                when (BOARD[current]) {
                    '|' -> {
                        stack.add(Pair(current.first, current.second - 1))
                        stack.add(Pair(current.first, current.second + 1))
                    }

                    '-' -> {
                        stack.add(Pair(current.first - 1, current.second))
                        stack.add(Pair(current.first + 1, current.second))
                    }

                    'L' -> {
                        stack.add(Pair(current.first, current.second - 1))
                        stack.add(Pair(current.first + 1, current.second))
                    }

                    'J' -> {
                        stack.add(Pair(current.first, current.second - 1))
                        stack.add(Pair(current.first - 1, current.second))
                    }

                    '7' -> {
                        stack.add(Pair(current.first - 1, current.second))
                        stack.add(Pair(current.first, current.second + 1))
                    }

                    'F' -> {
                        stack.add(Pair(current.first + 1, current.second))
                        stack.add(Pair(current.first, current.second + 1))
                    }

                    else -> Unit // Out of bounds, '.' ground pieces
                }
            }
        }
        if (seen.values.toSet().size == 1 && seen.values.toSet().contains(2)) {
            println(seen.keys.size / 2)

            var counterB = 0
            val trapped = hashSetOf<Pair<Int, Int>>()


            for (key in BOARD.keys) {
                if (!seen.containsKey(key)) {
                    if (!canEscape(key, seen, lines[0].lastIndex, lines.lastIndex)) {
                        trapped.add(key)
                        counterB += 1
                    }
                }
            }

            // For visualizing surrounded nodes
            for (y in lines.indices) {
                for (x in lines[y].indices) {
                    val key = Pair(x,y)
                    if (trapped.contains(key)) {
                        print('*')
                    }
                    else if(seen.contains(key)) {
                        print('█')
                    }
                    else {
                        print(BOARD[key])
                    }
                }
                print('\n')
            }
            println(counterB)
        }
    }
}

fun canEscape(
    current: Pair<Int, Int>,
    walls: HashMap<Pair<Int, Int>, Int>,
    maxX: Int,
    maxY: Int
): Boolean {
    val seen = hashSetOf<Pair<Int, Int>>()
    val stack = mutableListOf(current)
    while (stack.isNotEmpty()) {
        val current = stack.removeLast()
        if (seen.contains(current)) {
            continue
        }

        seen.add(current)
        if (current.first < 0 || current.second < 0 || current.first > maxX || current.second > maxY) {
            return true
        }

        val newNeighbors = mutableListOf(
            Pair(current.first - 1, current.second),
            Pair(current.first + 1, current.second),
            Pair(current.first, current.second - 1),
            Pair(current.first, current.second + 1)
        )
        stack.addAll(newNeighbors.filter { !walls.containsKey(it) })
    }
    return false
}