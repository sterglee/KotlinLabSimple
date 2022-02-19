package kotlinLabSci.math.array

import javax.swing.JOptionPane

object benchMatrix {
    @JvmStatic
    fun main(args: Array<String>) {
        val start = System.currentTimeMillis()
        val NLoop = 1000
        val N = 200
        val m = Array(N) { DoubleArray(N) }
        for (reps in 0 until NLoop) for (r in 0 until N) for (c in 0 until N) m[r][c] = (NLoop * r * c).toDouble()
        val end = System.currentTimeMillis()
        val delay = (end - start).toDouble() / 1000.0
        JOptionPane.showMessageDialog(null, "delay = $delay")
    }
}