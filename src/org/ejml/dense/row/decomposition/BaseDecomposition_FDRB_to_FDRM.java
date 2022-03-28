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

package org.ejml.dense.row.decomposition;

import javax.annotation.Generated;
import org.ejml.data.FGrowArray;
import org.ejml.data.FMatrixRBlock;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.block.MatrixOps_FDRB;
import org.ejml.interfaces.decomposition.DecompositionInterface;

/**
 * Generic interface for wrapping a {@link FMatrixRBlock} decomposition for
 * processing of {@link FMatrixRMaj}.
 *
 * @author Peter Abeles
 */
@Generated("org.ejml.dense.row.decomposition.BaseDecomposition_DDRB_to_DDRM")
public class BaseDecomposition_FDRB_to_FDRM implements DecompositionInterface<FMatrixRMaj> {

    protected DecompositionInterface<FMatrixRBlock> alg;

    protected FGrowArray workspace = new FGrowArray();
    protected FMatrixRBlock Ablock = new FMatrixRBlock();
    protected int blockLength;

    public BaseDecomposition_FDRB_to_FDRM( DecompositionInterface<FMatrixRBlock> alg,
                                           int blockLength ) {
        this.alg = alg;
        this.blockLength = blockLength;
    }

    @Override
    public boolean decompose( FMatrixRMaj A ) {
        Ablock.numRows = A.numRows;
        Ablock.numCols = A.numCols;
        Ablock.blockLength = blockLength;
        Ablock.data = A.data;

        // doing an in-place convert is much more memory efficient at the cost of a little
        // but of CPU
        MatrixOps_FDRB.convertRowToBlock(A.numRows, A.numCols, Ablock.blockLength, A.data, workspace);

        boolean ret = alg.decompose(Ablock);

        // convert it back to the normal format if it wouldn't have been modified
        if (!alg.inputModified()) {
            MatrixOps_FDRB.convertBlockToRow(A.numRows, A.numCols, Ablock.blockLength, A.data, workspace);
        }

        return ret;
    }

    public void convertBlockToRow( int numRows, int numCols, float[] data ) {
        MatrixOps_FDRB.convertBlockToRow(numRows, numCols, Ablock.blockLength, data, workspace);
    }

    @Override
    public boolean inputModified() {
        return alg.inputModified();
    }
}
