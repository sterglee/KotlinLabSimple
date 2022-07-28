package kotlinLabSci.math.array

import kotlin.DoubleArray

class BasicDSP {
    private val signal: DoubleArray by lazy { TODO() }

    companion object {
        // f1=9; f2 = 0.3; t = inc(0, 0.01, 10); x = sin(f1*t)+4*cos(f2*t); plot(t,x); fx = fft(x);
        @JvmStatic
        fun sfft(sig: DoubleArray): DoubleArray {
            var sig = sig
            val N = sig.size
            val M = (Math.log(N.toDouble()) / Math.log(2.0)).toInt()
            val NpowerTwo = Math.pow(2.0, M.toDouble()).toInt()
            if (NpowerTwo != N) {   // truncate the signal to a power of 2
                val truncSig = DoubleArray(NpowerTwo)
                for (k in 0 until NpowerTwo) truncSig[k] = sig[k]
                sig = truncSig
            }
            val result: DoubleArray
            result = fftMag(sig)
            return result
        }
        @JvmStatic
        fun sfft(sigMat: Matrix): Matrix {
            val matVals = sigMat.array
            var sig = matVals[0]
            val N = sig.size
            val M = (Math.log(N.toDouble()) / Math.log(2.0)).toInt()
            val NpowerTwo = Math.pow(2.0, M.toDouble()).toInt()
            if (NpowerTwo != N) {   // truncate the signal to a power of 2
                val truncSig = DoubleArray(NpowerTwo)
                for (k in 0 until NpowerTwo) truncSig[k] = sig[k]
                sig = truncSig
            }
            val result: DoubleArray
            result = fftMag(sig)
            return Matrix(result)
        }

        private var n = 0
        private var nu = 0
        private fun bitrev(j: Int): Int {
            var j2: Int
            var j1 = j
            var k = 0
            for (i in 1..nu) {
                j2 = j1 / 2
                k = 2 * k + j1 - 2 * j2
                j1 = j2
            }
            return k
        }

        @JvmStatic
        fun fftMag(x: DoubleArray): DoubleArray {
            // assume n is a power of 2
            n = x.size
            nu = (Math.log(n.toDouble()) / Math.log(2.0)).toInt()
            var n2 = n / 2
            var nu1 = nu - 1
            val xre = DoubleArray(n)
            val xim = DoubleArray(n)
            val mag = DoubleArray(n2)
            var tr: Double
            var ti: Double
            var p: Double
            var arg: Double
            var c: Double
            var s: Double
            for (i in 0 until n) {
                xre[i] = x[i]
                xim[i] = 0.0
            }
            var k = 0
            for (l in 1..nu) {
                while (k < n) {
                    for (i in 1..n2) {
                        p = bitrev(k shr nu1).toDouble()
                        arg = 2 * Math.PI * p / n
                        c = Math.cos(arg)
                        s = Math.sin(arg)
                        tr = xre[k + n2] * c + xim[k + n2] * s
                        ti = xim[k + n2] * c - xre[k + n2] * s
                        xre[k + n2] = xre[k] - tr
                        xim[k + n2] = xim[k] - ti
                        xre[k] += tr
                        xim[k] += ti
                        k++
                    }
                    k += n2
                }
                k = 0
                nu1--
                n2 = n2 / 2
            }
            k = 0
            var r: Int
            while (k < n) {
                r = bitrev(k)
                if (r > k) {
                    tr = xre[k]
                    ti = xim[k]
                    xre[k] = xre[r]
                    xim[k] = xim[r]
                    xre[r] = tr
                    xim[r] = ti
                }
                k++
            }
            mag[0] = Math.sqrt(xre[0] * xre[0] + xim[0] * xim[0]) / n
            for (i in 1 until n / 2) mag[i] = 2 * Math.sqrt(xre[i] * xre[i] + xim[i] * xim[i]) / n
            return mag
        }
    }
}