/*
 * Copyright (c) 2009-2020, Peter Abeles. All Rights Reserved.
 *
 * This file is part of Efficient Java Matrix Library (EJML).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.ejml.dense.row.decomposition.qr;

import javax.annotation.Generated;
import org.ejml.data.FMatrixRMaj;

import org.ejml.concurrency.EjmlConcurrency;

/**
 * <p>
 * Contains different functions that are useful for computing the QR decomposition of a matrix.
 * </p>
 *
 * <p>
 * Two different families of functions are provided for help in computing reflectors.  Internally
 * both of these functions switch between normalization by division or multiplication.  Multiplication
 * is most often significantly faster than division (2 or 3 times) but produces less accurate results
 * on very small numbers.  It checks to see if round off error is significant and decides which
 * one it should do.
 * </p>
 *
 * <p>
 * Tests were done using the stability benchmark in jmatbench and there doesn't seem to be
 * any advantage to always dividing by the max instead of checking and deciding.  The most
 * noticeable difference between the two methods is with very small numbers.
 * </p>
 *
 * @author Peter Abeles
 */
@Generated("org.ejml.dense.row.decomposition.qr.QrHelperFunctions_FDRM")
public class QrHelperFunctions_MT_FDRM {

    /**
     * <p>
     * Performs a rank-1 update operation on the submatrix specified by w with the multiply on the right.<br>
     * <br>
     * A = (I - &gamma;*u*u<sup>T</sup>)*A<br>
     * </p>
     * <p>
     * The order that matrix multiplies are performed has been carefully selected
     * to minimize the number of operations.
     * </p>
     *
     * <p>
     * Before this can become a truly generic operation the submatrix specification needs
     * to be made more generic.
     * </p>
     */
    public static void rank1UpdateMultR( FMatrixRMaj A, float[] u, float gamma,
                                         int colA0,
                                         int w0, int w1,
                                         float[] _temp ) {
//        for( int i = colA0; i < A.numCols; i++ ) {
//            float val = 0;
//
//            for( int k = w0; k < w1; k++ ) {
//                val += u[k]*A.data[k*A.numCols +i];
//            }
//            _temp[i] = gamma*val;
//        }

        // reordered to reduce cpu cache issues
        for (int i = colA0; i < A.numCols; i++) {
            _temp[i] = u[w0]*A.data[w0*A.numCols + i];
        }

        for (int k = w0 + 1; k < w1; k++) {
            int indexA = k*A.numCols + colA0;
            float valU = u[k];
            for (int i = colA0; i < A.numCols; i++) {
                _temp[i] += valU*A.data[indexA++];
            }
        }

        for (int i = colA0; i < A.numCols; i++) {
            _temp[i] *= gamma;
        }

        // end of reorder

        EjmlConcurrency.loopFor(w0, w1, i->{
            float valU = u[i];

            int indexA = i*A.numCols + colA0;
            for (int j = colA0; j < A.numCols; j++) {
                A.data[indexA++] -= valU*_temp[j];
            }
        });
    }

    // Useful for concurrent implementations where you don't want to modify u[0] to set it to 1.0f
    public static void rank1UpdateMultR_u0( FMatrixRMaj A, float[] u, final float u_0,
                                            final float gamma,
                                            final int colA0,
                                            final int w0, final int w1,
                                            final float[] _temp ) {
//        for( int i = colA0; i < A.numCols; i++ ) {
//            float val = 0;
//
//            for( int k = w0; k < w1; k++ ) {
//                val += u[k]*A.data[k*A.numCols +i];
//            }
//            _temp[i] = gamma*val;
//        }

        // reordered to reduce cpu cache issues
        for (int i = colA0; i < A.numCols; i++) {
            _temp[i] = u_0*A.data[w0*A.numCols + i];
        }

        for (int k = w0 + 1; k < w1; k++) {
            int indexA = k*A.numCols + colA0;
            float valU = u[k];
            for (int i = colA0; i < A.numCols; i++) {
                _temp[i] += valU*A.data[indexA++];
            }
        }

        for (int i = colA0; i < A.numCols; i++) {
            _temp[i] *= gamma;
        }

        // end of reorder
        {
            int indexA = w0*A.numCols + colA0;
            for (int j = colA0; j < A.numCols; j++) {
                A.data[indexA++] -= u_0*_temp[j];
            }
        }

        EjmlConcurrency.loopFor(w0+1, w1, i->{
            final float valU = u[i];

            int indexA = i*A.numCols + colA0;
            for (int j = colA0; j < A.numCols; j++) {
                A.data[indexA++] -= valU*_temp[j];
            }
        });
    }

    public static void rank1UpdateMultR( FMatrixRMaj A,
                                         float[] u, int offsetU,
                                         float gamma,
                                         int colA0,
                                         int w0, int w1,
                                         float[] _temp ) {
//        for( int i = colA0; i < A.numCols; i++ ) {
//            float val = 0;
//
//            for( int k = w0; k < w1; k++ ) {
//                val += u[k+offsetU]*A.data[k*A.numCols +i];
//            }
//            _temp[i] = gamma*val;
//        }

        // reordered to reduce cpu cache issues
        for (int i = colA0; i < A.numCols; i++) {
            _temp[i] = u[w0 + offsetU]*A.data[w0*A.numCols + i];
        }

        for (int k = w0 + 1; k < w1; k++) {
            int indexA = k*A.numCols + colA0;
            float valU = u[k + offsetU];
            for (int i = colA0; i < A.numCols; i++) {
                _temp[i] += valU*A.data[indexA++];
            }
        }
        for (int i = colA0; i < A.numCols; i++) {
            _temp[i] *= gamma;
        }

        // end of reorder

        EjmlConcurrency.loopFor(w0, w1, i->{
            float valU = u[i + offsetU];

            int indexA = i*A.numCols + colA0;
            for (int j = colA0; j < A.numCols; j++) {
                A.data[indexA++] -= valU*_temp[j];
            }
        });
    }

    /**
     * <p>
     * Performs a rank-1 update operation on the submatrix specified by w with the multiply on the left.<br>
     * <br>
     * A = A(I - &gamma;*u*u<sup>T</sup>)<br>
     * </p>
     * <p>
     * The order that matrix multiplies are performed has been carefully selected
     * to minimize the number of operations.
     * </p>
     *
     * <p>
     * Before this can become a truly generic operation the submatrix specification needs
     * to be made more generic.
     * </p>
     */
    public static void rank1UpdateMultL( FMatrixRMaj A, float[] u,
                                         float gamma,
                                         int colA0,
                                         int w0, int w1 ) {
        EjmlConcurrency.loopFor(colA0, A.numRows, i->{
            int startIndex = i*A.numCols + w0;
            float sum = 0;
            int rowIndex = startIndex;
            for (int j = w0; j < w1; j++) {
                sum += A.data[rowIndex++]*u[j];
            }
            sum = -gamma*sum;

            rowIndex = startIndex;
            for (int j = w0; j < w1; j++) {
                A.data[rowIndex++] += sum*u[j];
            }
        });
    }
}
