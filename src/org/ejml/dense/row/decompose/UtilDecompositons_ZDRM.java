/*
 * Copyright (c) 2020, Peter Abeles. All Rights Reserved.
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

package org.ejml.dense.row.decompose;

import org.ejml.data.ZMatrixRMaj;
import org.ejml.dense.row.CommonOps_ZDRM;
import org.jetbrains.annotations.Nullable;

/**
 * Helper functions for generic decompsotions.
 *
 * @author Peter Abeles
 */
public class UtilDecompositons_ZDRM {

    public static ZMatrixRMaj checkIdentity( @Nullable ZMatrixRMaj A, int numRows, int numCols ) {
        if (A == null) {
            return CommonOps_ZDRM.identity(numRows, numCols);
        } else if (numRows != A.numRows || numCols != A.numCols)
            throw new IllegalArgumentException("Input is not " + numRows + " x " + numCols + " matrix");
        else
            CommonOps_ZDRM.setIdentity(A);
        return A;
    }

    public static ZMatrixRMaj checkZeros( @Nullable ZMatrixRMaj A, int numRows, int numCols ) {
        if (A == null) {
            return new ZMatrixRMaj(numRows, numCols);
        } else if (numRows != A.numRows || numCols != A.numCols)
            throw new IllegalArgumentException("Input is not " + numRows + " x " + numCols + " matrix");
        else
            A.zero();
        return A;
    }

    /**
     * Creates a zeros matrix only if A does not already exist.  If it does exist it will fill
     * the lower triangular portion with zeros.
     */
    public static ZMatrixRMaj checkZerosLT( @Nullable ZMatrixRMaj A, int numRows, int numCols ) {
        if (A == null) {
            return new ZMatrixRMaj(numRows, numCols);
        } else if (numRows != A.numRows || numCols != A.numCols)
            throw new IllegalArgumentException("Input is not " + numRows + " x " + numCols + " matrix");
        else {
            for (int i = 0; i < A.numRows; i++) {
                int index = i*A.numCols*2;
                int end = index + Math.min(i, A.numCols)*2;
                while (index < end) {
                    A.data[index++] = 0;
                }
            }
        }
        return A;
    }

    /**
     * Creates a zeros matrix only if A does not already exist.  If it does exist it will fill
     * the upper triangular portion with zeros.
     */
    public static ZMatrixRMaj checkZerosUT( @Nullable ZMatrixRMaj A, int numRows, int numCols ) {
        if (A == null) {
            return new ZMatrixRMaj(numRows, numCols);
        } else if (numRows != A.numRows || numCols != A.numCols)
            throw new IllegalArgumentException("Input is not " + numRows + " x " + numCols + " matrix");
        else {
            int maxRows = Math.min(A.numRows, A.numCols);
            for (int i = 0; i < maxRows; i++) {
                int index = (i*A.numCols + i + 1)*2;
                int end = (i*A.numCols + A.numCols)*2;
                while (index < end) {
                    A.data[index++] = 0;
                }
            }
        }
        return A;
    }
}
