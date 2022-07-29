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

package org.ejml.dense.block;

import javax.annotation.Generated;
import org.ejml.EjmlParameters;
import org.ejml.UtilEjml;
import org.ejml.data.DGrowArray;
import org.ejml.data.DMatrixRBlock;
import org.ejml.data.DMatrixRMaj;
import org.ejml.data.DSubmatrixD1;
import org.ejml.dense.row.CommonOps_DDRM;
import org.ejml.dense.row.MatrixFeatures_DDRM;
import org.ejml.dense.row.RandomMatrices_DDRM;
import org.ejml.ops.DConvertMatrixStruct;
import org.jetbrains.annotations.Nullable;

import java.util.Random;


/**
 * Various operations on {@link DMatrixRBlock}.
 *
 * @author Peter Abeles
 */
@Generated("org.ejml.dense.block.MatrixOps_DDRB")
public class MatrixOps_MT_DDRB {


    // This can be speed up by inlining the multBlock* calls, reducing number of multiplications
    // and other stuff. doesn't seem to have any speed advantage over mult_reorder()
    public static void mult( DMatrixRBlock A, DMatrixRBlock B, DMatrixRBlock C ) {
        if (A.numCols != B.numRows)
            throw new IllegalArgumentException("Columns in A are incompatible with rows in B");
        if (A.numRows != C.numRows)
            throw new IllegalArgumentException("Rows in A are incompatible with rows in C");
        if (B.numCols != C.numCols)
            throw new IllegalArgumentException("Columns in B are incompatible with columns in C");
        if (A.blockLength != B.blockLength || A.blockLength != C.blockLength)
            throw new IllegalArgumentException("Block lengths are not all the same.");

        final int blockLength = A.blockLength;

        DSubmatrixD1 Asub = new DSubmatrixD1(A, 0, A.numRows, 0, A.numCols);
        DSubmatrixD1 Bsub = new DSubmatrixD1(B, 0, B.numRows, 0, B.numCols);
        DSubmatrixD1 Csub = new DSubmatrixD1(C, 0, C.numRows, 0, C.numCols);

        MatrixMult_MT_DDRB.mult(blockLength, Asub, Bsub, Csub);
    }

    public static void multTransA( DMatrixRBlock A, DMatrixRBlock B, DMatrixRBlock C ) {
        if (A.numRows != B.numRows)
            throw new IllegalArgumentException("Rows in A are incompatible with rows in B");
        if (A.numCols != C.numRows)
            throw new IllegalArgumentException("Columns in A are incompatible with rows in C");
        if (B.numCols != C.numCols)
            throw new IllegalArgumentException("Columns in B are incompatible with columns in C");
        if (A.blockLength != B.blockLength || A.blockLength != C.blockLength)
            throw new IllegalArgumentException("Block lengths are not all the same.");

        final int blockLength = A.blockLength;

        DSubmatrixD1 Asub = new DSubmatrixD1(A, 0, A.numRows, 0, A.numCols);
        DSubmatrixD1 Bsub = new DSubmatrixD1(B, 0, B.numRows, 0, B.numCols);
        DSubmatrixD1 Csub = new DSubmatrixD1(C, 0, C.numRows, 0, C.numCols);

        MatrixMult_MT_DDRB.multTransA(blockLength, Asub, Bsub, Csub);
    }

    public static void multTransB( DMatrixRBlock A, DMatrixRBlock B, DMatrixRBlock C ) {
        if (A.numCols != B.numCols)
            throw new IllegalArgumentException("Columns in A are incompatible with columns in B");
        if (A.numRows != C.numRows)
            throw new IllegalArgumentException("Rows in A are incompatible with rows in C");
        if (B.numRows != C.numCols)
            throw new IllegalArgumentException("Rows in B are incompatible with columns in C");
        if (A.blockLength != B.blockLength || A.blockLength != C.blockLength)
            throw new IllegalArgumentException("Block lengths are not all the same.");

        final int blockLength = A.blockLength;

        DSubmatrixD1 Asub = new DSubmatrixD1(A, 0, A.numRows, 0, A.numCols);
        DSubmatrixD1 Bsub = new DSubmatrixD1(B, 0, B.numRows, 0, B.numCols);
        DSubmatrixD1 Csub = new DSubmatrixD1(C, 0, C.numRows, 0, C.numCols);

        MatrixMult_MT_DDRB.multTransB(blockLength, Asub, Bsub, Csub);
    }

}
