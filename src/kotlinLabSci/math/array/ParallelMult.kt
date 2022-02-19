package kotlinLabSci.math.array

import edu.emory.mathcs.utils.ConcurrencyUtils
import kotlinLabGlobal.Interpreter.GlobalValues
import org.jblas.NativeBlas
import java.util.concurrent.Future
import kotlin.DoubleArray

object ParallelMult {
    @JvmStatic
    fun pmul(v1: Array<DoubleArray>, v2: Array<DoubleArray>): Array<DoubleArray> {
        return if (GlobalValues.hostIsLinux64 || GlobalValues.hostIsMac) // fast BLAS works only for Linux and Mac
            pmulblas(v1, v2) else  // Java multithreaded based multiplication
            pmulJavaMulti(v1, v2)
    }

    // parallel multiplication using Java multithreading
    fun pmulJavaMulti(v1: Array<DoubleArray>, v2: Array<DoubleArray>): Array<DoubleArray> {
        // Java multithreaded based multiplication
        val rN = v1.size
        val rM: Int = v1[0].size
        val sN = v2.size
        val sM: Int = v2[0].size


        // transpose first matrix that. This operation is very important in order to exploit cache locality
        val thatTrans = Array(sM) { DoubleArray(sN) }
        for (r in 0 until sN) for (c in 0 until sM) thatTrans[c][r] = v2[r][c]
        val vr = Array(rN) { DoubleArray(sM) } // for computing the return Matrix
        var nthreads = ConcurrencyUtils.getNumberOfThreads()
        nthreads = Math.min(
            nthreads,
            rN
        ) // larger number of threads than the number of cores of the system deteriorate performance
        val futures = arrayOfNulls<Future<*>>(nthreads)
        val rowsPerThread = (sM / nthreads) + 1 // how many rows the thread processes
        var threadId = 0 // the current threadId
        while (threadId < nthreads) {  // for all threads 
            val firstRow = threadId * rowsPerThread
            val lastRow = if (threadId == nthreads - 1) sM else firstRow + rowsPerThread
            futures[threadId] = GlobalValues.execService.submit {
                var a = firstRow // the first row of the matrix that this thread processes
                while (a < lastRow) {  // the last row of the matrix that this thread processes
                    var b = 0
                    while (b < rN) {
                        var s = 0.0
                        var c = 0
                        while (c < rM) {
                            s += v1[b][c] * thatTrans[a][c]
                            c++
                        }
                        vr[b][a] = s
                        b++
                    }
                    a++
                }
            }
            threadId++
        } // for all threads

        // wait for all the multiplication worker threads to complete
        ConcurrencyUtils.waitForCompletion(futures)
        return vr
    }

    // fast multiply using native BLAS and Java multithreading
    fun pmulblas(v1: Array<DoubleArray>, v2: Array<DoubleArray>): Array<DoubleArray> {
        val Arows = v1.size
        val Acols: Int = v1[0].size
        val Ccols: Int = v2[0].size
        val C = Array(Arows) { DoubleArray(Ccols) } // the result matrix
        var nthreads = ConcurrencyUtils.getNumberOfThreads()
        nthreads = Math.min(nthreads, Arows)
        val futures: Array<Future<*>?> = arrayOfNulls(nthreads)
        val rowsPerThread = (Arows / nthreads) // how many rows the thread processes
        var threadId = 0 // the current threadId
        while (threadId < nthreads) {  // for all threads 
            val firstRow = threadId * rowsPerThread
            var lastRowVar = firstRow + rowsPerThread
            if (threadId == nthreads - 1) lastRowVar = Arows
            val lastRow = lastRowVar
            val numRowsForThread = lastRow - firstRow
            futures[threadId] = GlobalValues.execService.submit {
                val a = DoubleArray(numRowsForThread * Acols)
                val b = DoubleArray(Acols * Ccols)
                val c = DoubleArray(numRowsForThread * Ccols)

                //  copy the part of the v1 matrix to a linear 1-D array
                var cnt = 0
                var col = 0
                while (col < Acols) {
                    var row = firstRow
                    while (row < lastRow) {
                        a[cnt] = v1[row][col]
                        row++ // column major
                        cnt++
                    }
                    col++
                }

                // copy the v2 matrix to a linear 1-D array
                cnt = 0
                col = 0
                while (col < Ccols) {
                    var row = 0
                    while (row < Acols) {
                        b[cnt] = v2[row][col]
                        row++ // column major
                        cnt++
                    }
                    col++
                }

                //NativeBlas.dgemm('N', 'N', c.rows, c.columns, a.columns,
                //               alpha, a.data, 0,
                //		a.rows, b.data, 0, b.rows,
                //beta, c.data, 0, c.rows);
                NativeBlas.dgemm(
                    'N', 'N', numRowsForThread, Ccols, Acols,
                    1.0, a, 0,
                    numRowsForThread, b, 0, Acols,
                    0.0, c, 0, numRowsForThread
                )


                //_root_.jeigen.JeigenJna.Jeigen.dense_multiply(numRowsForThread,  Acols , Ccols, a, b, c)
                cnt = 0
                col = 0
                while (col < Ccols) {
                    var row = firstRow
                    while (row < lastRow) {
                        C[row][col] = c[cnt]
                        row++
                        cnt++
                    }
                    col++
                }
            } // run
            // Runnable
            threadId++
        } // for all threads
        ConcurrencyUtils.waitForCompletion(futures)
        return C
    }
}