package kotlinLabSci.math.array

object ExtensionsVec {

    operator fun Double.plus(x: Vec):Vec = x.plus(this)
    operator fun Int.plus(x: Vec):Vec = x.plus(this)
    operator fun Double.minus(x: Vec):Vec  = x.minus(this)
    operator fun Int.minus(x: Vec): Vec = x.minus(this)
    operator fun Double.times(x: Vec): Vec = x.times(this)
    operator fun Int.times(x: Vec): Vec = x.times(this)


}