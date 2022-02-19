package kotlinLabSci.FFT

import kotlinLabSci.math.array.Vec
import org.apache.commons.math3.complex.Complex
import org.apache.commons.math3.transform.DftNormalization
import org.apache.commons.math3.transform.FastFourierTransformer
import org.apache.commons.math3.transform.TransformType

class ApacheFFT {
    private val signal: DoubleArray = TODO()

    companion object {
        /*
    var f1=9; var f2 = 0.3; var t = inc(0, 0.01, 10);  var x = sin(f1*t)+4*cos(f2*t); subplot(2,1,1); plot(t,x);
    var fx = fft(x); 
    var fxre = getReParts(fx)
    subplot(2,1,2); plot(fxre)
     */
        fun fft(sig: DoubleArray): Array<Complex> {
            var sig = sig
            val N = sig.size
            val M = (Math.log(N.toDouble()) / Math.log(2.0)).toInt()
            var NpowerTwo = Math.pow(2.0, M.toDouble()).toInt()
            if (NpowerTwo != N) {   // padd the signal with zeros in order to be a power of 2
                NpowerTwo = Math.pow(2.0, (M + 1).toDouble()).toInt()
                val paddSig = DoubleArray(NpowerTwo)
                for (k in 0 until N) paddSig[k] = sig[k]
                for (k in N until NpowerTwo) paddSig[k] = 0.0
                sig = paddSig
            }
            val result: Array<Complex>
            val fftj = FastFourierTransformer(DftNormalization.STANDARD)
            result = fftj.transform(sig, TransformType.FORWARD)
            return result
        }

        fun absfft(sig: DoubleArray): DoubleArray {
            val fftSig = fft(sig)
            val N = fftSig.size
            val absSig = DoubleArray(N)
            for (k in 0 until N) absSig[k] = fftSig[k].abs()
            return absSig
        }

        fun realsfft(sig: DoubleArray): DoubleArray {
            val fftSig = fft(sig)
            val N = fftSig.size
            val realsSig = DoubleArray(N)
            for (k in 0 until N) realsSig[k] = fftSig[k].real
            return realsSig
        }

        fun imagsfft(sig: DoubleArray): DoubleArray {
            val fftSig = fft(sig)
            val N = fftSig.size
            val imagsSig = DoubleArray(N)
            for (k in 0 until N) imagsSig[k] = fftSig[k].imaginary
            return imagsSig
        }

        fun absfft(vsig: Vec): DoubleArray {
            return absfft(vsig.getv())
        }

        fun realsfft(vsig: Vec): DoubleArray {
            return realsfft(vsig.getv())
        }

        fun imagsfft(vsig: Vec): DoubleArray {
            return imagsfft(vsig.getv())
        }

        fun ifft(sig: Array<Complex?>?): Array<Complex> {
            val fftj = FastFourierTransformer(DftNormalization.STANDARD)
            return fftj.transform(sig, TransformType.INVERSE)
        }

        fun fft(sigVec: Vec): Array<Complex> {
            return fft(sigVec.getv())
        } //static public powsp()
    }
}