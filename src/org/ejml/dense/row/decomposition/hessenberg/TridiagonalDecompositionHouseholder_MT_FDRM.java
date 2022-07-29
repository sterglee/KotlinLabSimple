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

package org.ejml.dense.row.decomposition.hessenberg;

import javax.annotation.Generated;
import org.ejml.concurrency.EjmlConcurrency;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.decomposition.qr.QrHelperFunctions_MT_FDRM;

/**
 * Concurrent implementation of {@link TridiagonalDecompositionHouseholder_FDRM}
 *
 * @author Peter Abeles
 */
@Generated("org.ejml.dense.row.decomposition.hessenberg.TridiagonalDecompositionHouseholder_MT_DDRM")
public class TridiagonalDecompositionHouseholder_MT_FDRM
        extends TridiagonalDecompositionHouseholder_FDRM {
    /**
     * Performs the householder operations on left and right and side of the matrix.  Q<sup>T</sup>AQ
     *
     * @param row Specifies the submatrix.
     * @param gamma The gamma for the householder operation
     */
    @Override
    public void householderSymmetric( int row, float gamma ) {
        int startU = (row - 1)*N;

        // compute v = -gamma*A*u
        EjmlConcurrency.loopFor(row, N, i -> {
            float total = 0;
            // the lower triangle is not written to so it needs to traverse upwards
            // to get the information.  Reduces the number of matrix writes need
            // improving large matrix performance
            for (int j = row; j < i; j++) {
                total += QT.data[j*N + i]*QT.data[startU + j];
            }
            for (int j = i; j < N; j++) {
                total += QT.data[i*N + j]*QT.data[startU + j];
            }
            w[i] = -gamma*total;
        });

        // alpha = -0.5f*gamma*u^T*v
        float alpha = 0;

        for (int i = row; i < N; i++) {
            alpha += QT.data[startU + i]*w[i];
        }
        alpha *= -0.5f*gamma;

        // w = v + alpha*u
        for (int i = row; i < N; i++) {
            w[i] += alpha*QT.data[startU + i];
        }
        // A = A + w*u^T + u*w^T
        EjmlConcurrency.loopFor(row, N, i -> {
            float ww = w[i];
            float uu = QT.data[startU + i];

            int rowA = i*N;
            for (int j = i; j < N; j++) {
                // only write to the upper portion of the matrix
                // this reduces the number of cache misses
                QT.data[rowA + j] += ww*QT.data[startU + j] + w[j]*uu;
            }
        });
    }

    @Override
    protected void rank1UpdateMultL( FMatrixRMaj A, float gamma, int colA0, int w0, int w1 ) {
        QrHelperFunctions_MT_FDRM.rank1UpdateMultL(A, w, gamma, colA0, w0, w1);
    }

    @Override
    protected void rank1UpdateMultR( FMatrixRMaj A, float gamma, int colA0, int w0, int w1 ) {
        QrHelperFunctions_MT_FDRM.rank1UpdateMultR(A, w, gamma, colA0, w0, w1, this.b);
    }
}
