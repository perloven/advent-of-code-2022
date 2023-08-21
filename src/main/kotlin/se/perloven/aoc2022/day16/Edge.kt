package se.perloven.aoc2022.day16

data class Edge(val cost: Int, val toName: String = "", var to: Valve = PLACEHOLDER) {
    companion object {
        private val PLACEHOLDER: Valve =
            Valve(name = "placeholder", flowRate = 0)
    }
    override fun toString(): String {
        return "--$cost--> ${to.name}"
    }
}
