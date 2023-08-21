package se.perloven.aoc2022.day16

data class Valve(
    val name: String, val flowRate: Int, var edges: Set<Edge> = emptySet(),
    var visited: Boolean = false, var parent: Valve? = null
) {

    override fun toString(): String {
        return "Valve(name=$name, flowRate=$flowRate, edges=${
            edges.joinToString(prefix = "[", postfix = "]")
        })"
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }

    override fun equals(other: Any?): Boolean {
        return other is Valve && name == other.name && flowRate == other.flowRate
    }

    fun isStart(): Boolean {
        return this.name == "AA"
    }

    fun hasFlow(): Boolean {
        return this.flowRate > 0
    }
}
