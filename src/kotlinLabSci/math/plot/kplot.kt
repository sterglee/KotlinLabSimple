package kotlinLabSci.math.plot

import kotlinLabSci.math.plot.plot.*

class kplot {

    companion object {
        @JvmStatic
        fun kplot(x: DoubleArray, y: DoubleArray): PlotPanel {
            println("hi kplot")

            return plot(x, y)

        }
    }
}