package kotlinLabSci.math.array

object ExtensionsMat1D {

    operator fun Double.plus(x: Mat1D):Mat1D = x.plus(this)
    operator fun Int.plus(x: Mat1D):Mat1D = x.plus(this.toDouble())
    operator fun Double.minus(x: Mat1D):Mat1D  = x.minus(this)
    operator fun Int.minus(x: Mat1D): Mat1D = x.minus(this.toDouble())
    operator fun Double.times(x: Mat1D): Mat1D = x.times(this)
    operator fun Int.times(x: Mat1D): Mat1D = x.times(this.toDouble())


}