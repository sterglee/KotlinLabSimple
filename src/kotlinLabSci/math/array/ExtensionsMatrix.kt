package kotlinLabSci.math.array

object ExtensionsMatrix {

    operator fun Double.plus(x: Matrix):Matrix = x.plus(this)
    operator fun Int.plus(x: Matrix):Matrix = x.plus(this)
    operator fun Double.minus(x: Matrix):Matrix  = x.minus(this)
    operator fun Int.minus(x: Matrix): Matrix = x.minus(this)
    operator fun Double.times(x: Matrix): Matrix = x.times(this)
    operator fun Int.times(x: Matrix): Matrix = x.times(this.toDouble())

 
}