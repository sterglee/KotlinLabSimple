package kotlinLabSci.math.array

import kotlinLabSci.PrintFormatParams
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import java.util.stream.DoubleStream

// Vectors are dynamically resizable while double[] arrays are not
class Vec {
    @JvmField
    var v: kotlin.DoubleArray
    var len = 0

    constructor(n: Int) {
        v = DoubleArray(n)
        len = n
    }

    // initialize by copy
    constructor(vin: kotlin.DoubleArray) {
        val N = vin.size
        len = N
        v = DoubleArray(N)
        for (n in 0 until N) v[n] = vin[n]
    }

    // initialize by reference
    constructor(vin: kotlin.DoubleArray, isRef: Boolean) {
        len = vin.size
        v = vin
    }

    fun clone(): Vec {
        val vc = DoubleArray(length())
        return Vec(vc)
    }

    fun copy(): Vec {
        val vc = DoubleArray(length())
        for (k in 0 until length()) vc[k] = v[k]
        return Vec(vc)
    }

    fun getv(): kotlin.DoubleArray {
        return v
    }

    fun size(): Int {
        return len
    }

    fun length(): Int {
        return len
    }

    fun stream(): DoubleStream {
        return Arrays.stream(v)
    }

    fun parallelstream(): DoubleStream {
        return Arrays.stream(v).parallel()
    }

    fun apply(k: Int): Double {
        return v[k]
    }

    fun getAt(k: Int): Double {
        return v[k]
    }

    fun putAt(k: Int, value: Double) {
        v[k] = value
    }

    // assign a whole double [] array at k th position
    fun putAt(k: Int, value: kotlin.DoubleArray) {
        val vl = value.size
        if (k + vl > v.size) return  // out of range
        for (n in 0 until vl) v[k + n] = value[n]
    }

    // assign a whole Vec at k th position
    fun putAt(k: Int, value: Vec) {
        val vl = value.v.size
        if (k + vl > v.size) return  // out of range
        for (n in 0 until vl) v[k + n] = value.v[n]
    }

    operator fun set(i: Int, vn: Number) {
        v[i] = vn.toDouble()
    }

    operator fun get(i: Int): Double {
        return v[i]
    }

    fun sum(): Double {
        var smAll = 0.0
        for (k in 0 until len) smAll += v[k]
        return smAll
    }

    fun ssum(): Double {
        return DoubleStream.of(*v).parallel().reduce { x: Double, y: Double -> x + y }.asDouble
    }

    fun sprod(): Double {
        return DoubleStream.of(*v).parallel().reduce(1.0) { x: Double, y: Double -> x * y }
    }

    fun prod(): Double {
        var prodAll = 1.0
        for (k in 0 until len) prodAll *= v[k]
        return prodAll
    }

    fun mean(): Double {
        var mnAll = 0.0
        for (k in 0 until len) mnAll += v[k]
        return mnAll / len
    }

    fun norm1(): Double {
        var sm = 0.0
        var cv = 0.0
        for (k in 0 until length()) {
            cv = v[k]
            if (cv < 0) cv = -cv
            sm += cv
        }
        return sm
    }

    fun norm2(): Double {
        var nrm = 0.0
        var cv = 0.0
        for (k in 0 until length()) {
            cv = v[k]
            nrm += cv * cv
        }
        return Math.sqrt(nrm)
    }

    fun norm2_robust(): Double {
        var scale = 0.0
        var ssq = 1.0
        for (i in 0 until length()) if (v[i] != 0.0) {
            val absxi = Math.abs(v[i])
            if (scale < absxi) {
                ssq = 1 + ssq * (scale / absxi) * (scale / absxi)
                scale = absxi
            } else ssq += absxi / scale * (absxi / scale)
        }
        return scale * Math.sqrt(ssq)
    }

    fun normInf(): Double {
        var max = 0.0
        for (i in 0 until length()) max = Math.max(Math.abs(v[i]), max)
        return max
    }

    operator fun plus(that: Vec): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        val vthat = that.getv()
        for (k in 0 until thislen) vp[k] = v[k] + vthat[k]
        return Vec(vp, true)
    }

    operator fun plus(that: kotlin.DoubleArray): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        for (k in 0 until thislen) vp[k] = v[k] + that[k]
        return Vec(vp, true)
    }

    operator fun plus(that: Double): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        for (k in 0 until thislen) vp[k] = v[k] + that
        return Vec(vp, true)
    }

    operator fun plus(that: Int): Vec {
        return plus(that.toDouble())
    }

    operator fun minus(that: Double): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        for (k in 0 until thislen) vp[k] = v[k] - that
        return Vec(vp, true)
    }

    operator fun minus(that: Int): Vec {
        return minus(that.toDouble())
    }
    fun negative(): Vec {
        val vr = Vec(length())
        for (k in 0 until length()) vr[k] = -getAt(k)
        return vr
    }

    operator fun minus(that: Vec): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        val vthat = that.getv()
        for (k in 0 until thislen) vp[k] = v[k] - vthat[k]
        return Vec(vp, true)
    }

    operator fun minus(that: kotlin.DoubleArray): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        for (k in 0 until thislen) vp[k] = v[k] - that[k]
        return Vec(vp, true)
    }

    /*
public Vec  multiply(Matrix  that ) {
    return that.multiply(this);
}
*/
    operator  fun times(that: Vec): Vec {
        return this.multiply(that)
    }


    fun multiply(that: Vec): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        val vthat = that.getv()
        for (k in 0 until thislen) vp[k] = v[k] * vthat[k]
        return Vec(vp, true)
    }

    operator fun times(that: Int): Vec {
        return times(that.toDouble())
    }

    operator fun times(that: kotlin.DoubleArray): Vec {
        return this.multiply(that)
    }

    fun multiply(that: kotlin.DoubleArray): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        for (k in 0 until thislen) vp[k] = v[k] * that[k]
        return Vec(vp, true)
    }


    operator fun times(that: Double): Vec {
        return this.multiply(that)
    }

    fun multiply(that: Double): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        for (k in 0 until thislen) vp[k] = v[k] * that
        return Vec(vp, true)
    }

    operator fun div(that: Double): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        for (k in 0 until thislen) vp[k] = v[k] / that
        return Vec(vp, true)
    }

    operator fun div(that: Vec): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        val vthat = that.getv()
        for (k in 0 until thislen) vp[k] = v[k] / vthat[k]
        return Vec(vp, true)
    }

    fun power(that: Vec): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        val vthat = that.getv()
        for (k in 0 until thislen) vp[k] = Math.pow(v[k], vthat[k])
        return Vec(vp, true)
    }

    fun or(that: Vec): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        val vthat = that.getv()
        for (k in 0 until thislen) vp[k] = (v[k].toInt() or vthat[k].toInt()).toDouble()
        return Vec(vp, true)
    }

    fun and(that: Vec): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        val vthat = that.getv()
        for (k in 0 until thislen) vp[k] = (v[k].toInt() and vthat[k].toInt()).toDouble()
        return Vec(vp, true)
    }

    fun xor(that: Vec): Vec {
        val thislen = length()
        val vp = DoubleArray(thislen)
        val vthat = that.getv()
        for (k in 0 until thislen) vp[k] = (v[k].toInt() xor vthat[k].toInt()).toDouble()
        return Vec(vp, true)
    }

    fun Vinc(begin: Double, pitch: Double, end: Double): kotlin.DoubleArray {
        if (begin > end && pitch > 0) return DoubleArray.increment(end, pitch, begin)
        return if (begin < end && pitch < 0) DoubleArray.increment(end, pitch, begin) else DoubleArray.increment(
            begin,
            pitch,
            end
        )
    }

    override fun toString(): String {
        val sb = StringBuilder()
        if (PrintFormatParams.getVerbose() == true) {
            var formatString = "0."
            for (k in 0 until PrintFormatParams.vecDigitsPrecision) formatString += "0"
            val digitFormat = DecimalFormat(formatString)
            digitFormat.decimalFormatSymbols = DecimalFormatSymbols(Locale("us"))
            var mxElems = length()
            var moreElems = ""
            if (mxElems > PrintFormatParams.vecMxElemsToDisplay) {
                // vector has more elements than we can display
                mxElems = PrintFormatParams.vecMxElemsToDisplay
                moreElems = " .... "
            }
            var i = 0
            while (i < mxElems) {
                sb.append(digitFormat.format(v[i]) + "  ")
                i++
            }
        }
        return sb.toString()
    } /*
public String toString() {
    StringBuilder cb = new StringBuilder("Vec("+this.length()+") =  [");
    int mx = mxVecLenToString;
    if (mx > this.length())
        mx = this.length();
    for (int k=0; k < mx-1; k++)
        cb.append(GlobalValues.fmtString.format(v[k])+", ");
    cb.append(GlobalValues.fmtString.format(v[mx-1])+"]");
    
    return cb.toString();
}
*/

    companion public object VecOps {
        const val mxVecLenToString = 100

        // construct a zero-indexed Vec from values, e.g. 
        //  v = V(3.4, -6.7, -1.2, 5.6)

        @JvmStatic
        fun V(vararg values: Double): Vec {
            val nl = values.size // number of elements
            val sv = Vec(nl) // create a Vec
            for (k in 0 until nl) sv.v[k] = values[k] // copy value
            return sv // return the constructed matrix
        }

        // construct a zero-indexed Vec from a String, e.g.
        //  v = V("3.4 -6.7 -1.2 5.6")

        @JvmStatic
        fun V(s: String?): Vec {
            var nelems = 0

// count how many numbers each row has. Assuming that each line has the same number of elements
            var strtok = StringTokenizer(s, ", ") // elements are separated by ',' or ' '
            while (strtok.hasMoreTokens()) {
                val tok = strtok.nextToken()
                nelems++
            }
            val data = DoubleArray(nelems)
            strtok = StringTokenizer(s, ", ") // elements are separated by ',' or ' '
            var k = 0
            while (strtok.hasMoreTokens()) {
                val tok = strtok.nextToken()
                data[k++] = tok.toDouble()
            }
            return Vec(data, true)
        }

        @JvmStatic
        fun vones(n: Int): Vec {
            val data = DoubleArray(n)
            for (i in 0 until n) data[i] = 1.0
            return Vec(data, true)
        }

        @JvmStatic
        fun vzeros(n: Int): Vec {
            val data = DoubleArray(n)
            for (i in 0 until n) data[i] = 0.0
            return Vec(data, true)
        }
@JvmStatic
        fun vrand(n: Int): Vec {
            val data = DoubleArray(n)
            for (i in 0 until n) data[i] = Math.random()
            return Vec(data, true)
        }

        @JvmStatic
        fun vfill(n: Int, value: Double): Vec {
            val data = DoubleArray(n)
            for (i in 0 until n) data[i] = value
            return Vec(data, true)
        }


        @JvmStatic
        fun vlinspace(startv: Double, endv: Double): Vec {
            val nP = 100 // use 100 as default number of points
            val v = DoubleArray(nP)
            val dx = (startv - endv) / (nP - 1)
            for (i in 0 until nP) v[i] = startv + i * dx
            return Vec(v, true)
        }


        @JvmStatic
        fun vlinspace(startv: Double, endv: Double, nP: Int): Vec {
            val v = DoubleArray(nP)
            val dx = (startv - endv) / (nP - 1)
            for (i in 0 until nP) v[i] = startv + i * dx
            return Vec(v, true)
        }

        // use by default logspace=10
       @JvmStatic
        fun vlogspace(startOrig: Double, endOrig: Double): Vec {
            val nP = 100
            return vlogspace(startOrig, endOrig, nP, 10.0)
        }

        // use by default logspace=10
        @JvmOverloads

        @JvmStatic
        fun vlogspace(startOrig: Double, endOrig: Double, nP: Int, logBase: Double = 10.0): Vec {
            var positiveTransformed = false
            var transformToPositive = 0.0
            var start = startOrig
            var end = endOrig // use these values to handle negative values
            var axisReversed = false
            if (start > end) {   // reverse axis
                start = endOrig
                end = startOrig
                axisReversed = true
            }
            if (start <= 0) {  // make values positive
                transformToPositive = -start + 1
                start = 1.0
                end = end + transformToPositive
                positiveTransformed = true
            }
            val logBaseFactor = 1.0 / Math.log10(logBase)
            val start_tmp = Math.log10(start) * logBaseFactor
            val end_tmp = Math.log10(end) * logBaseFactor
            //println("logBaseFactor = "+logBaseFactor+"  start_tmp = "+start_tmp+"  end_tmp = "+end_tmp)
            val values = DoubleArray(nP)
            val dx = (end_tmp - start_tmp) / (nP - 1)
            for (i in 0 until nP) values[i] = Math.pow(logBase, start_tmp + i * dx)
            if (positiveTransformed) // return to the original range of values
            {
                for (i in 0 until nP) values[i] -= -transformToPositive
                start -= transformToPositive
            }
            if (axisReversed) {
                val valuesNew = DoubleArray(nP)
                valuesNew[0] = values[nP - 1]
                for (i in 1 until nP) {
                    valuesNew[i] = valuesNew[i - 1] - (values[i] - values[i - 1])
                }
                return Vec(valuesNew, true)
            }
            return Vec(values, true)
        }

        @JvmStatic
        fun vinc(begin: Double, pitch: Double, end: Double): Vec {
            if (begin > end && pitch > 0) return Vec(DoubleArray.increment(end, pitch, begin))
            return if (begin < end && pitch < 0) Vec(
                DoubleArray.increment(
                    end,
                    pitch,
                    begin
                )
            ) else Vec(DoubleArray.increment(begin, pitch, end))
        }

        // operations returning double []

        @JvmStatic
        fun Vones(n: Int): kotlin.DoubleArray {
            val data = DoubleArray(n)
            for (i in 0 until n) data[i] = 1.0
            return data
        }

        @JvmStatic
        fun Vzeros(n: Int): kotlin.DoubleArray {
            val data = DoubleArray(n)
            for (i in 0 until n) data[i] = 0.0
            return data
        }

        @JvmStatic
        fun Vrand(n: Int): kotlin.DoubleArray {
            val data = DoubleArray(n)
            for (i in 0 until n) data[i] = Math.random()
            return data
        }

        @JvmStatic
        fun Vfill(n: Int, value: Double): kotlin.DoubleArray {
            val data = DoubleArray(n)
            for (i in 0 until n) data[i] = value
            return data
        }

        @JvmStatic
        fun Vlinspace(startv: Double, endv: Double): kotlin.DoubleArray {
            val nP = 100 // use 100 as default number of points
            val v = DoubleArray(nP)
            val dx = (startv - endv) / (nP - 1)
            for (i in 0 until nP) v[i] = startv + i * dx
            return v
        }

        @JvmStatic
        fun Vlinspace(startv: Double, endv: Double, nP: Int): kotlin.DoubleArray {
            val v = DoubleArray(nP)
            val dx = (startv - endv) / (nP - 1)
            for (i in 0 until nP) v[i] = startv + i * dx
            return v
        }

        // use by default logspace=10

        @JvmStatic
        fun Vlogspace(startOrig: Double, endOrig: Double): kotlin.DoubleArray {
            val nP = 100
            return Vlogspace(startOrig, endOrig, nP, 10.0)
        }

        // use by default logspace=10
        @JvmOverloads

        @JvmStatic
        fun Vlogspace(startOrig: Double, endOrig: Double, nP: Int, logBase: Double = 10.0): kotlin.DoubleArray {
            var positiveTransformed = false
            var transformToPositive = 0.0
            var start = startOrig
            var end = endOrig // use these values to handle negative values
            var axisReversed = false
            if (start > end) {   // reverse axis
                start = endOrig
                end = startOrig
                axisReversed = true
            }
            if (start <= 0) {  // make values positive
                transformToPositive = -start + 1
                start = 1.0
                end = end + transformToPositive
                positiveTransformed = true
            }
            val logBaseFactor = 1.0 / Math.log10(logBase)
            val start_tmp = Math.log10(start) * logBaseFactor
            val end_tmp = Math.log10(end) * logBaseFactor
            //println("logBaseFactor = "+logBaseFactor+"  start_tmp = "+start_tmp+"  end_tmp = "+end_tmp)
            val values = DoubleArray(nP)
            val dx = (end_tmp - start_tmp) / (nP - 1)
            for (i in 0 until nP) values[i] = Math.pow(logBase, start_tmp + i * dx)
            if (positiveTransformed) // return to the original range of values
            {
                for (i in 0 until nP) values[i] -= -transformToPositive
                start -= transformToPositive
            }
            if (axisReversed) {
                val valuesNew = DoubleArray(nP)
                valuesNew[0] = values[nP - 1]
                for (i in 1 until nP) {
                    valuesNew[i] = valuesNew[i - 1] - (values[i] - values[i - 1])
                }
                return valuesNew
            }
            return values
        }
    }
}