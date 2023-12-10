package aoc.grids


class Cell(x: Int, y: Int, val value: String): Point(x, y) {
    override fun toString(): String {
        return "Cell(x=$x, y=$y, value='$value')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false
        if (!super.equals(other)) return false

        other as Cell

        if (value != other.value) return false

        return true
    }

    override fun hashCode(): Int {
        var result = super.hashCode()
        result = 31 * result + value.hashCode()
        return result
    }
}