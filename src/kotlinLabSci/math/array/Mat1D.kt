// This is the basic Mat1D class of jShellLab.
// Ιt has a lot of operations covering basic mathematical tasks.
// Also, it offers an extensive range of static operations that help to perform conveniently many things.
// Convenient syntax is offered using Groovy's freatures.
// Also, some native optimized C libraries and NVIDIA CUDA support is offered for faster maths.
package kotlinLabSci.math.array

import edu.emory.mathcs.utils.ConcurrencyUtils
import kotlinLabSci.PrintFormatParams
import kotlinLabGlobal.Interpreter.GlobalValues
import org.bytedeco.javacpp.openblas
import java.text.DecimalFormat
import java.text.DecimalFormatSymbols
import java.util.*
import java.util.concurrent.Future
import java.util.function.UnaryOperator
import java.util.stream.DoubleStream

// keeps the matrix representation as an one-dimensional
// Java double [] array
// the data storage is in column-major order
class Mat1D {
    var Nrows: Int
    var Ncols: Int

    // set the reference to double[][] storage of this matrix object to a different array 	
    // return a reference to the 1-D data representation
    // get the data array by reference
    // The storage of data as double [] array. We can manipulate it directly for efficiency.
    var ref: kotlin.DoubleArray?
        get() = field

    constructor(v: kotlin.DoubleArray?, r: Int, c: Int) {
        ref = v
        Nrows = r
        Ncols = c
    }

    constructor(n: Int, m: Int) {  // creates a zero Mat1D 
        ref = DoubleArray(n * m)
        Nrows = n
        Ncols = m
    }

    fun stream(): DoubleStream {
        return Arrays.stream(ref)
    }

    fun parallelstream(): DoubleStream {
        return Arrays.stream(ref).parallel()
    }

    operator  fun times(that: Mat1D): Mat1D {
     return    multiply(that)
    }

    operator  fun times(that: Double): Mat1D {
        return    multiply(that)
    }
      // multiply using openBLAS
     fun multiply(that: Mat1D): Mat1D {
        val Ccolumns = that.Ncols
        val result = DoubleArray(Nrows * that.Ncols)
        val alpha = 1.0
        val beta = 0.0
        val lda = Nrows
        val ldb = Ncols
        val ldc = Nrows
        // perform the multiplication using openblas  
        openblas.cblas_dgemm(
            openblas.CblasColMajor,
            openblas.CblasNoTrans,
            openblas.CblasNoTrans,
            Nrows,
            that.Ncols,
            Ncols,
            alpha,
            ref,
            lda,
            that.ref,
            ldb,
            beta,
            result,
            ldc
        )

        // return the resulted matrix
        return Mat1D(result, Nrows, that.Ncols)
    }

    /*
// multiply using openBLAS
 final public Mat1D test( Mat1D that)  {
     
   int Ccolumns = that.Ncols;
   double [] result = new double[Nrows*that.Ncols];
   double alpha=1.0;
   double beta=0.0;
   int lda = Nrows;
   int ldb = Ncols;
   int ldc = Nrows;
    // perform the multiplication using openblas  
   org.bytedeco.javacpp.openblas.LAPACKE_dgetrf_
           
           //cblas_dgemm(openblas.CblasRowMajor, CblasNoTrans, CblasNoTrans, Nrows, that.Ncols,  Ncols, alpha, d, lda, that.d, ldb, beta, 
    //    result, ldc);
 
       // return the resulted matrix
    return new Mat1D(result, Nrows, that.Ncols);
}
*/
    fun numRows(): Int {
        return Nrows
    }

    fun numCols(): Int {
        return Ncols
    }

    fun numColumns(): Int {
        return Ncols
    }

    // returns size as an array of two ints, i.e. int[2]
    fun size(): IntArray {
        val siz = IntArray(2)
        siz[0] = Nrows
        siz[1] = Ncols
        return siz
    }

    // transform  the data array to a 2-D double[][] Java array representation
    fun toDoubleArray(): Array<kotlin.DoubleArray> {
        val data = Array(Nrows) { DoubleArray(Ncols) }
        var cnt = 0
        for (c in 0 until Ncols) for (r in 0 until Nrows) data[r][c] = ref!![cnt++]
        return data
    }

    constructor(n: Int, m: Int, v: Double) {  // creates a Mat1D filled with the value
        ref = DoubleArray(n * m)
        Nrows = n
        Ncols = m
        for (k in 0 until n * m) ref!![k] = v
    }

    // a copy constructor
    constructor(m: Mat1D) {    // matrix to copy
        ref = DoubleArray.copy(m.ref)
        Nrows = m.Nrows
        Ncols = m.Ncols
    }

    // column major data storage, thus
    // (row, col) element is at position: col*Nrows+row
    operator fun get(row: Int, col: Int): Double {
        return ref!![col * Nrows + row]
    }

    // column major data storage, thus
    // (row, col) element is at position: col*Nrows+row
    operator fun set(row: Int, col: Int, v: Number) {
        ref!![col * Nrows + row] = v.toDouble()
    }

    operator fun set(row: Int, col: Int, v: Int) {
        ref!![col * Nrows + row] = v.toDouble()
    }

    // this method is used to overload the indexing operator e.g.,
    // A = rand(8, 9);   a23 = A[2,3]
    fun getAt(row: Int, col: Int): Double {
        return ref!![col * Nrows + row]
    }

    // assuming a column major storage of values
    fun putAt(row: Int, col: Int, value: Double) {
        ref!![col * Nrows + row] = value
    }

    fun getAt(r: Int): Double {
        return ref!![r]
    }

    // IntRanges we use the following routine for handling lists of Integers
    private fun putAtWithInt(a1: Int, a2: Int, value: Double) {
        ref!![a2 * Ncols + a1] = value
    }

    /* e,g,
 x = rand(12, 15)
 x[2..3, 1..2] = 12.22  // subrange 2 to 3 tows, 1 to 2 cols to 12.22
 x[-1..-1, 3..5] = 35  // cols  3 to 5  to 35
 x[0..1, -1..-1] = -11.11 // rows 0 to 1 to -11.11

 */
    fun sr(rowS: Int, rowE: Int, value: Double) {
        for (rows in rowS..rowE) for (cols in 0 until Ncols) ref!![cols * Nrows + rows] = value
    }

    fun sc(colsS: Int, colsE: Int, value: Double) {
        for (rows in 0 until Nrows) for (cols in colsS..colsE) ref!![cols * Nrows + rows] = value
    }

    fun src(rowS: Int, rowE: Int, colsS: Int, colsE: Int, value: Double) {
        for (rows in rowS..rowE) for (cols in colsS..colsE) ref!![cols * Nrows + rows] = value
    }

    fun src(rowS: Int, rowInc: Int, rowE: Int, colsS: Int, colInc: Int, colsE: Int, value: Double) {
        var rows = rowS
        while (rows <= rowE) {
            var cols = colsS
            while (cols <= colsE) {
                ref!![cols * Nrows + rows] = value
                cols += colInc
            }
            rows += rowInc
        }
    }

    fun s(rng: IntArray, value: Double) {
        val rowsS = rng[0]
        val rowsE = rng[1]
        val colsS = rng[2]
        val colsE = rng[3]
        for (rows in rowsS..rowsE) for (cols in colsS..colsE) ref!![cols * Nrows + rows] = value
    }

    // extracts a submatrix, e.g. m.gc( 2,  12 ) corresponds to Matlab's m(:, 2:12)'
    fun gc(colLow: Int, colHigh: Int): Mat1D {
        val rowStart = 0
        val rowEnd = Nrows - 1 // all rows
        var colEnd = colHigh
        val rowInc = 1
        val colInc = 1
        val rowNum = Nrows // take all the rows
        return if (colLow <= colEnd) {    // positive increment
            if (colEnd == -1) {
                colEnd = Ncols - 1
            } // if -1 is specified take all the columns
            val colNum = colEnd - colLow + 1
            val subMatr = Mat1D(rowNum, colNum) // create a Matrix to keep the extracted range
            // fill the created matrix with values
            var crow = rowStart // indexes current row
            var ccol = colLow // indexes current column
            var rowIdx = 0
            var colIdx = 0 // indexes at the new Matrix
            while (crow <= rowEnd) {
                ccol = colLow
                colIdx = 0
                while (ccol <= colEnd) {
                    subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol))
                    colIdx++
                    ccol += colInc
                }
                rowIdx += rowInc
                crow += rowInc
            } // crow <= rowEnd
            subMatr
        } // positive increment
        else {  // negative increment
            val colNum = colEnd - colLow + 1
            val subMatr = Mat1D(rowNum, colNum) // create a Matrix to keep the extracted range
            // fill the created matrix with values
            var crow = rowStart // indexes current row
            var ccol = colLow // indexes current column
            var rowIdx = 0
            var colIdx = 0 // indexes at the new Matrix
            while (crow <= rowEnd) {
                ccol = colLow
                colIdx = 0
                while (ccol >= colEnd) {
                    subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol))
                    colIdx++
                    ccol += colInc
                }
                rowIdx++
                crow += rowInc
            } // crow <= rowEnd
            subMatr // return the submatrix
        }
    }

    // column select 
    fun gc(colL: Int, inc: Int, colH: Int): Mat1D {
        return grc(0, 1, Nrows - 1, colL, inc, colH)
    }

    // row select 
    fun gr(rowL: Int, inc: Int, rowH: Int): Mat1D {
        return grc(rowL, inc, rowH, 0, 1, Ncols - 1)
    }

    /* extract the columns specified with indices specified with  the array colIndices.
 The new matrix is formed by using all the rows of the original matrix 
 but with using only the specified columns.
 The columns at the new matrix are arranged in the order specified with the array colIndices
 e.g. 
  testMat = M(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12")
  colIndices  = [3, 1] as int []
   extract3_1cols = testMat.gc( colIndices)
   */
    fun gc(colIndices: IntArray): Mat1D {
        val lv = colIndices.size
        return if (lv > Ncols) // do nothing
        {
            println("array indices length = $lv is greater than the number of columns of the matrix = $Ncols")
            this
        } else {  // dimension of array with column indices to use is correct
            // allocate array
            val colFiltered = Mat1D(Nrows, lv)
            for (col in 0 until lv) {
                val currentColumn = colIndices[col] // the specified column
                for (row in 0 until Nrows)  // copy the corresponding row
                    colFiltered.putAt(row, col, this.getAt(row, currentColumn))
            }
            colFiltered // return the column filtered array
        } // dimension of array with column indices to use is correct
    }

    /* extract the rows specified with indices specified with  the array rowIndices.
 The new matrix is formed by using all the columns of the original matrix 
 but with using only the specified rows.
 The rows at the new matrix are arranged in the order specified with the array rowIndices
 e.g. 
 testMat = M(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12; 13 14 15 16; 17 18 19 20")
 rowIndices = [3, 1] as int []
 extract3_1rows = testMat.gr(rowIndices)
   */
    fun gr(rowIndices: IntArray): Mat1D {
        val lv = rowIndices.size
        return if (lv > Nrows) // do nothing
        {
            println("array indices length = $lv is greater than the number of rows of the matrix = $Nrows")
            this
        } else {  // dimension of array with column indices to use is correct
            // allocate array
            val rowFiltered = Mat1D(lv, Ncols)
            for (row in 0 until lv) {
                val currentRow = rowIndices[row] // the specified row
                for (col in 0 until Ncols)  // copy the corresponding row
                    rowFiltered.putAt(row, col, this.getAt(currentRow, col))
            }
            rowFiltered // return the column filtered array
        } // dimension of array with column indices to use is correct
    }

    /* extract the columns specified with true values at the array  colIndices.
 The new matrix is formed by using all the rows of the original matrix 
 but with using only the specified columns.
 e.g. 
 testMat = M(" 1.0 2.0 3.0 4.0; 5.0 6.0 7.0 8.0; 9 10 11 12")
 colIndices = [true, false, true, false] as boolean []
 extract0_2cols = testMat.gc(colIndices)
   */
    fun gc(colIndices: BooleanArray): Mat1D {
        val lv = colIndices.size
        return if (lv != Ncols) // do nothing
        {
            println("array indices length = $lv is not the number of columns of the matrix = $Ncols")
            this
        } else {  // dimension of array with column indices to use is correct
            // count the number of trues
            var ntrues = 0
            for (k in 0 until Ncols) if (colIndices[k] == true) ntrues++

            // allocate array
            val colFiltered = Mat1D(Nrows, ntrues)
            var currentColumn = 0
            for (col in 0 until Ncols) {
                if (colIndices[col]) { // copy the corresponding column
                    for (row in 0 until Nrows) colFiltered.putAt(row, currentColumn, this.getAt(row, col))
                    currentColumn++
                } // copy the corresponding column
            }
            colFiltered // return the column filtered array
        } // dimension of array with column indices to use is correct
    }

    /* extract the rows specified with true values at the array rowIndices.
 The new matrix is formed by using all the columns of the original matrix 
 but with using only the specified rows.
 e.g. 
 testMat = M(" 1.0 2.0 3.0 ; 5.0 6.0 7.0 ; 8 9 10 ; 11 12 13")
 rowIndices = [false, true, false, true] as boolean[] 
 extract1_3rows = testMat.gr(rowIndices)
   */
    fun gr(rowIndices: BooleanArray): Mat1D {
        val lv = rowIndices.size
        return if (lv != Nrows) // do nothing
        {
            println("array indices length = $lv is not the number of rows of the matrix = $Nrows")
            this
        } else {  // dimension of array with row indices to use is correct
            // count the number of trues
            var ntrues = 0
            for (k in 0 until Nrows) if (rowIndices[k]) ntrues++

            // allocate array
            val rowFiltered = Mat1D(ntrues, Ncols)
            var currentRow = 0
            for (row in 0 until Nrows) if (rowIndices[row]) {  // copy the corresponding row
                for (col in 0 until Ncols) rowFiltered.putAt(currentRow, col, this.getAt(row, col))
                currentRow++
            }
            rowFiltered
        } // dimension of array with row indices to use is correct 
    }

    fun gr(rowL: Int, rowH: Int): Mat1D {
        var rowEnd = rowH
        val colStart = 0
        val colEnd = Ncols - 1 // all columns
        val colNum = Ncols
        val colInc = 1
        return if (rowL <= rowEnd) {   // positive increment
            val rowInc = 1
            if (rowEnd == -1) {
                rowEnd = Nrows - 1
            } // if -1 is specified take all the rows
            val rowNum = rowEnd - rowL + 1
            val subMatr = Mat1D(rowNum, colNum) // create a Mat to keep the extracted range
            // fill the created matrix with values
            var crow = rowL // indexes current row
            var ccol = colStart
            var rowIdx = 0
            var colIdx = 0 // indexes at the new Mat1D
            while (crow <= rowEnd) {
                ccol = colStart
                colIdx = 0
                while (ccol <= colEnd) {
                    subMatr.putAt(rowIdx, colIdx, getAt(crow, ccol))
                    colIdx++
                    ccol += colInc
                }
                rowIdx++
                crow += rowInc
            } // crow <= rowEnd
            subMatr // return the submatrix
        } // rowStart <= rowEnd
        else { // rowStart > rowEnd
            val rowInc = -1
            val rowNum = rowL - rowEnd + 1
            val subMatr = Mat1D(rowNum, colNum) // create a Mat to keep the extracted range
            // fill the created matrix with values
            var crow = rowL // indexes current row at the source matrix
            var ccol = colStart
            var rowIdx = 0
            var colIdx = 0 // indexes at the new Mat
            while (crow >= rowEnd) {
                ccol = colStart
                colIdx = 0
                while (ccol <= colEnd) {
                    subMatr.putAt(rowIdx, colIdx, getAt(crow, ccol))
                    colIdx++
                    ccol += colInc
                }
                rowIdx++
                crow += rowInc
            }
            subMatr // return the submatrix
        } // rowStart > rowEnd
    }

    // extracts a submatrix, e.g. m.grc( 2,  12, 4,   8 )  corresponds to Matlab's m(2:12, 4:8)'
    fun grc(rowLow: Int, rowHigh: Int, colLow: Int, colHigh: Int): Mat1D {
        var rowInc = 1
        if (rowHigh < rowLow) rowInc = -1
        var colInc = 1
        if (colHigh < colLow) colInc = -1
        val rowNum = Math.floor(((rowHigh - rowLow) / rowInc).toDouble()).toInt() + 1
        val colNum = Math.floor(((colHigh - colLow) / colInc).toDouble()).toInt() + 1
        val subMatr = Mat1D(rowNum, colNum) // create a Mat1D to keep the extracted range
        return if (rowLow <= rowHigh && colLow <= colHigh) {    // positive increment at rows and columns
            var crow = rowLow // indexes current row
            var ccol = colLow // indexes current column
            var rowIdx = 0
            var colIdx = 0 // indexes at the new Mat1D
            while (crow <= rowHigh) {
                ccol = colLow
                colIdx = 0
                while (ccol <= colHigh) {
                    subMatr.putAt(rowIdx, colIdx, getAt(crow, ccol))
                    colIdx++
                    ccol += colInc
                }
                rowIdx++
                crow += rowInc
            } // crow <= rowEnd
            subMatr
        } // positive increment
        else if (rowLow >= rowHigh && colLow <= colHigh) {
            // fill the created matrix with values
            var crow = rowLow // indexes current row
            var ccol = colLow // indexes current column
            var rowIdx = 0
            var colIdx = 0 // indexes at the new Mat1D
            while (crow >= rowHigh) {
                ccol = colLow
                colIdx = 0
                while (ccol <= colHigh) {
                    subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol))
                    colIdx++
                    ccol += colInc
                }
                rowIdx++
                crow += rowInc
            } // crow <= rowEnd
            subMatr // return the submatrix
        } else if (rowLow <= rowHigh && colLow >= colHigh) {
            // fill the created matrix with values
            var crow = rowLow // indexes current row
            var ccol = colLow // indexes current column
            var rowIdx = 0
            var colIdx = 0 // indexes at the new Mat1D
            while (crow <= rowHigh) {
                ccol = colLow
                colIdx = 0
                while (ccol >= colHigh) {
                    subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol))
                    colIdx++
                    ccol += colInc
                }
                rowIdx++
                crow += rowInc
            } // crow <= rowEnd
            subMatr // return the submatrix
        } else {
            // fill the created matrix with values
            var crow = rowLow // indexes current row
            var ccol = colLow // indexes current column
            var rowIdx = 0
            var colIdx = 0 // indexes at the new Mat1D
            while (crow >= rowHigh) {
                ccol = colLow
                colIdx = 0
                while (ccol >= colHigh) {
                    subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol))
                    colIdx++
                    ccol += colInc
                }
                rowIdx++
                crow += rowInc
            } // crow > rowEnd
            subMatr // return the submatrix
        }
    }

    // extracts a submatrix, e.g. m.grc( 2, 3, 12, 4, 2,  8 )  corresponds to Matlab's m(2:3:12, 4:2:8)'
    fun grc(rowLow: Int, rowInc: Int, rowHigh: Int, colLow: Int, colInc: Int, colHigh: Int): Mat1D {
        val rowNum = Math.floor(((rowHigh - rowLow) / rowInc).toDouble()).toInt() + 1
        val colNum = Math.floor(((colHigh - colLow) / colInc).toDouble()).toInt() + 1
        val subMatr = Mat1D(rowNum, colNum) // create a Mat1D to keep the extracted range
        return if (rowLow <= rowHigh && colLow <= colHigh) {    // positive increment at rows and columns
            var crow = rowLow // indexes current row
            var ccol = colLow // indexes current column
            var rowIdx = 0
            var colIdx = 0 // indexes at the new Mat1D
            while (crow <= rowHigh) {
                ccol = colLow
                colIdx = 0
                while (ccol <= colHigh) {
                    subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol))
                    colIdx++
                    ccol += colInc
                }
                rowIdx++
                crow += rowInc
            } // crow <= rowEnd
            subMatr
        } // positive increment
        else if (rowLow >= rowHigh && colLow <= colHigh) {
            // fill the created matrix with values
            var crow = rowLow // indexes current row
            var ccol = colLow // indexes current column
            var rowIdx = 0
            var colIdx = 0 // indexes at the new Mat1D
            while (crow >= rowHigh) {
                ccol = colLow
                colIdx = 0
                while (ccol <= colHigh) {
                    subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol))
                    colIdx++
                    ccol += colInc
                }
                rowIdx++
                crow += rowInc
            } // crow <= rowEnd
            subMatr // return the submatrix
        } else if (rowLow <= rowHigh && colLow >= colHigh) {
            // fill the created matrix with values
            var crow = rowLow // indexes current row
            var ccol = colLow // indexes current column
            var rowIdx = 0
            var colIdx = 0 // indexes at the new Mat1D
            while (crow <= rowHigh) {
                ccol = colLow
                colIdx = 0
                while (ccol >= colHigh) {
                    subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol))
                    colIdx++
                    ccol += colInc
                }
                rowIdx++
                crow += rowInc
            } // crow <= rowEnd
            subMatr // return the submatrix
        } else {
            // fill the created matrix with values
            var crow = rowLow // indexes current row
            var ccol = colLow // indexes current column
            var rowIdx = 0
            var colIdx = 0 // indexes at the new Mat1D
            while (crow >= rowHigh) {
                ccol = colLow
                colIdx = 0
                while (ccol >= colHigh) {
                    subMatr.putAt(rowIdx, colIdx, this.getAt(crow, ccol))
                    colIdx++
                    ccol += colInc
                }
                rowIdx++
                crow += rowInc
            } // crow > rowEnd
            subMatr // return the submatrix
        }
    }

    // extracts a specific row, take all columns, e.g. m.gr(2) corresponds to Matlab's m(2, :)'
    fun gr(row: Int): Mat1D {
        val colStart = 0
        val colEnd = Ncols - 1 // all columns
        val rowNum = 1
        val colNum = colEnd - colStart + 1
        val subMatr = Mat1D(rowNum, colNum) // create a Mat1D to keep the extracted range
        // fill the created matrix with values
        var ccol = colStart
        while (ccol <= colEnd) {
            subMatr.putAt(0, ccol, this.getAt(row, ccol))
            ccol++
        }
        return subMatr
    }

    // extracts a specific column, take all rows, e.g. m.gc( 2) corresponds to Matlab's m(:,2:)'  
    fun gc(col: Int): Mat1D {
        val rowStart = 0
        val rowEnd = Nrows - 1 // all rows
        val colNum = 1
        val rowNum = rowEnd - rowStart + 1
        val subMatr = Mat1D(rowNum, colNum) // create a Mat1D to keep the extracted range
        // fill the created matrix with values
        var crow = rowStart
        while (crow <= rowEnd) {
            subMatr.putAt(crow - rowStart, 0, this.getAt(crow, col))
            crow++
        }
        return subMatr
    }

    operator fun plus(v2: Mat1D): Mat1D {
        val r = Mat1D(Nrows, Ncols)
        for (k in 0 until Nrows * Ncols) r.ref!![k] = ref!![k] + v2.ref!![k]
        return r
    }

    operator fun plus(x: Double): Mat1D {
        val r = Mat1D(Nrows, Ncols)
        for (k in 0 until Nrows * Ncols) r.ref!![k] = ref!![k] + x
        return r
    }

    fun sum(): Double {
        var sm = 0.0
        for (k in 0 until Nrows * Ncols) sm += ref!![k]
        return sm
    }

    operator fun minus(x: Double): Mat1D {
        val r = Mat1D(Nrows, Ncols)
        for (k in 0 until Nrows * Ncols) r.ref!![k] = ref!![k] - x
        return r
    }

    fun multiply(x: Double): Mat1D {
        val r = Mat1D(Nrows, Ncols)
        for (k in 0 until Nrows * Ncols) r.ref!![k] = ref!![k] * x
        return r
    }

    operator fun plus(v2: kotlin.DoubleArray): Mat1D {
        val r = Mat1D(Nrows, Ncols)
        for (k in 0 until Nrows * Ncols) r.ref!![k] = ref!![k] + v2[k]
        return r
    }

    operator fun minus(v2: Mat1D): Mat1D {
        val r = Mat1D(Nrows, Ncols)
        for (k in 0 until Nrows * Ncols) r.ref!![k] = ref!![k] - v2.ref!![k]
        return r
    }

    operator fun minus(v2: kotlin.DoubleArray): Mat1D {
        val r = Mat1D(Nrows, Ncols)
        for (k in 0 until Nrows * Ncols) r.ref!![k] = ref!![k] - v2[k]
        return r
    }

    fun sin(): Mat1D {
        val ra = DoubleArray(Nrows * Ncols)
        for (k in 0 until Nrows * Ncols) ra[k] = Math.sin(ref!![k])
        return Mat1D(ra, Nrows, Ncols)
    }

    fun cos(): Mat1D {
        val ra = DoubleArray(Nrows * Ncols)
        for (k in 0 until Nrows * Ncols) ra[k] = Math.cos(ref!![k])
        return Mat1D(ra, Nrows, Ncols)
    }

    fun ceil(): Mat1D {
        val ra = DoubleArray(Nrows * Ncols)
        for (k in 0 until Nrows * Ncols) ra[k] = Math.ceil(ref!![k])
        return Mat1D(ra, Nrows, Ncols)
    }

    fun floor(): Mat1D {
        val ra = DoubleArray(Nrows * Ncols)
        for (k in 0 until Nrows * Ncols) ra[k] = Math.floor(ref!![k])
        return Mat1D(ra, Nrows, Ncols)
    }

    fun round(): Mat1D {
        val ra = DoubleArray(Nrows * Ncols)
        for (k in 0 until Nrows * Ncols) ra[k] = Math.round(ref!![k]).toDouble()
        return Mat1D(ra, Nrows, Ncols)
    }

    fun tan(): Mat1D {
        val ra = DoubleArray(Nrows * Ncols)
        for (k in 0 until Nrows * Ncols) ra[k] = Math.tan(ref!![k])
        return Mat1D(ra, Nrows, Ncols)
    }

    fun sqrt(): Mat1D {
        val ra = DoubleArray(Nrows * Ncols)
        for (k in 0 until Nrows * Ncols) ra[k] = Math.sqrt(ref!![k])
        return Mat1D(ra, Nrows, Ncols)
    }

    fun atan(): Mat1D {
        val ra = DoubleArray(Nrows * Ncols)
        for (k in 0 until Nrows * Ncols) ra[k] = Math.atan(ref!![k])
        return Mat1D(ra, Nrows, Ncols)
    }

    fun sinh(): Mat1D {
        val ra = DoubleArray(Nrows * Ncols)
        for (k in 0 until Nrows * Ncols) ra[k] = Math.sinh(ref!![k])
        return Mat1D(ra, Nrows, Ncols)
    }

    fun cosh(): Mat1D {
        val ra = DoubleArray(Nrows * Ncols)
        for (k in 0 until Nrows * Ncols) ra[k] = Math.cosh(ref!![k])
        return Mat1D(ra, Nrows, Ncols)
    }

    fun tanh(): Mat1D {
        val ra = DoubleArray(Nrows * Ncols)
        for (k in 0 until Nrows * Ncols) ra[k] = Math.tanh(ref!![k])
        return Mat1D(ra, Nrows, Ncols)
    }

    // in place routines
    fun isin(): Mat1D {
        for (k in 0 until Nrows * Ncols) ref!![k] = Math.sin(ref!![k])
        return this
    }

    fun icos(): Mat1D {
        for (k in 0 until Nrows * Ncols) ref!![k] = Math.cos(ref!![k])
        return this
    }

    fun iceil(): Mat1D {
        for (k in 0 until Nrows * Ncols) ref!![k] = Math.ceil(ref!![k])
        return this
    }

    fun ifloor(): Mat1D {
        for (k in 0 until Nrows * Ncols) ref!![k] = Math.floor(ref!![k])
        return this
    }

    fun iround(): Mat1D {
        for (k in 0 until Nrows * Ncols) ref!![k] = Math.cos(ref!![k])
        return this
    }

    fun itan(): Mat1D {
        for (k in 0 until Nrows * Ncols) ref!![k] = Math.cos(ref!![k])
        return this
    }

    fun isqrt(): Mat1D {
        for (k in 0 until Nrows * Ncols) ref!![k] = Math.sqrt(ref!![k])
        return this
    }

    fun iatan(): Mat1D {
        for (k in 0 until Nrows * Ncols) ref!![k] = Math.atan(ref!![k])
        return this
    }

    fun isinh(): Mat1D {
        for (k in 0 until Nrows * Ncols) ref!![k] = Math.sinh(ref!![k])
        return this
    }

    fun itanh(): Mat1D {
        for (k in 0 until Nrows * Ncols) ref!![k] = Math.tanh(ref!![k])
        return this
    }

    fun irand(): Mat1D {
        for (k in 0 until Nrows * Ncols) ref!![k] = Math.random()
        return this
    }

    // map Java 8 lambda function
    fun map(myfun: UnaryOperator<Double>): Mat1D {
        val dm = DoubleArray(Nrows * Ncols)
        for (elem in 0 until Nrows * Ncols) dm[elem] = myfun.apply(ref!![elem])
        return Mat1D(dm, Nrows, Ncols)
    }

    //SOS
    fun mapf(cf: computeFunction) {
        var idx = 0
        for (i in 0 until Nrows) for (j in 0 until Ncols) ref!![idx] =
            cf.f(ref!![idx++]) // apply the compute function to the map
    }

    fun pmapf(cf: computeFunction): Mat1D {
        val dm = DoubleArray(Nrows * Ncols)
        val rN = Ncols
        var nthreads = ConcurrencyUtils.getNumberOfThreads()

        // the matrix is split by row regions and the mutliplication in each such region
        // will be performed in parallel by a seperate thread
        nthreads = Math.min(nthreads, rN)
        val futures: Array<Future<*>?> = arrayOfNulls(nthreads)
        val colsPerThread = (rN / nthreads) + 1 // how many rows the thread processes
        var threadId = 0 // the current threadId
        while (threadId < nthreads) {  // for all threads 
            // define the column region for the current thread
            val firstCol = threadId * colsPerThread // start of columns range 
            val lastCol = if (threadId == nthreads - 1) rN else firstCol + colsPerThread // end of columns range
            futures[threadId] = GlobalValues.execService.submit {
                var a = firstCol // the first column of the matrix that this thread processes
                var idx = a * Nrows
                while (a < lastCol) {  // the last column of the matrix that this thread processes
                    for (j in 0 until Nrows) ref!![idx] = cf.f(ref!![idx++])
                    a++
                }
            } // run

            threadId++
        } // for all threads
        ConcurrencyUtils.waitForCompletion(futures)
        return Mat1D(dm, Nrows, Ncols)
    }

    fun abs(): Mat1D {
        val ra = DoubleArray(Nrows * Ncols)
        for (k in 0 until Nrows * Ncols) ra[k] = Math.abs(ref!![k])
        return Mat1D(ra, Nrows, Ncols)
    }

    override fun toString(): String {
        return if (ref != null) {
            val sb = StringBuilder()
            var formatString = "0."
            for (k in 0 until PrintFormatParams.vecDigitsPrecision) formatString += "0"
            val digitFormat = DecimalFormat(formatString)
            digitFormat.decimalFormatSymbols = DecimalFormatSymbols(Locale("us"))
            var mxElems = ref!!.size
            var moreElems = ""
            if (mxElems > PrintFormatParams.vecMxElemsToDisplay) {
                // vector has more elements than we can display
                mxElems = PrintFormatParams.vecMxElemsToDisplay
                moreElems = " .... "
            }
            var i = 0
            var colnum = 0
            var rownum = 0
            while (i < mxElems) {
                sb.append(digitFormat.format(getAt(rownum, colnum)) + "  ")
                i++
                colnum++
                if (colnum == Ncols) {
                    colnum = 0
                    rownum++
                    sb.append("\n")
                }
            }
            sb.append(
                """
    $moreElems
    
    """.trimIndent()
            )
            sb.toString()
        } else ""
    }

    companion object {
        fun size(M: Mat1D): IntArray {
            return M.size()
        }

        // assuming column major storage
        @JvmStatic
        fun fill1d(nrows: Int, ncols: Int, value: Double): Mat1D {
            val o = DoubleArray(nrows * ncols)
            var pos = 0
            for (c in 0 until ncols) for (r in 0 until nrows) o[pos++] = value
            return Mat1D(o, nrows, ncols)
        }
        @JvmStatic
        fun rand1d(nrows: Int, ncols: Int): Mat1D {
            val ra = DoubleArray(nrows * ncols)
            for (k in 0 until nrows * ncols) ra[k] = Math.random()
            return Mat1D(ra, nrows, ncols)
        }
        @JvmStatic
        fun ones1d(nrows: Int, ncols: Int): Mat1D {
            val ra = DoubleArray(nrows * ncols)
            for (k in 0 until nrows * ncols) ra[k] = 1.0
            return Mat1D(ra, nrows, ncols)
        }
        @JvmStatic
        fun zeros1d(nrows: Int, ncols: Int): Mat1D {
            val ra = DoubleArray(nrows * ncols)
            for (k in 0 until nrows * ncols) ra[k] = Math.random()
            return Mat1D(ra, nrows, ncols)
        }
    }
}