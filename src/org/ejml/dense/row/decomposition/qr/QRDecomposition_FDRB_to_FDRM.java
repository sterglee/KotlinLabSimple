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
import org.ejml.EjmlParameters;
import org.ejml.data.FMatrixRBlock;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.block.MatrixOps_FDRB;
import org.ejml.dense.block.decomposition.qr.QRDecompositionHouseholder_FDRB;
import org.ejml.dense.row.decomposition.BaseDecomposition_FDRB_to_FDRM;
import org.ejml.dense.row.decomposition.UtilDecompositons_FDRM;
import org.ejml.interfaces.decomposition.QRDecomposition;
import org.jetbrains.annotations.Nullable;

/**
 * Wrapper that allows {@link QRDecomposition}(FMatrixRBlock) to be used
 * as a {@link QRDecomposition}(FMatrixRMaj).
 *
 * @author Peter Abeles
 */
@Generated("org.ejml.dense.row.decomposition.qr.QRDecomposition_DDRB_to_DDRM")
public class QRDecomposition_FDRB_to_FDRM
        extends BaseDecomposition_FDRB_to_FDRM implements QRDecomposition<FMatrixRMaj> {

    public QRDecomposition_FDRB_to_FDRM() {
        super(new QRDecompositionHouseholder_FDRB(), EjmlParameters.BLOCK_WIDTH);
    }

    @Override
    public FMatrixRMaj getQ( @Nullable FMatrixRMaj Q, boolean compact ) {

        int minLength = Math.min(Ablock.numRows, Ablock.numCols);
        if (compact) {
            Q = UtilDecompositons_FDRM.ensureIdentity(Q, Ablock.numRows, minLength);
        } else {
            Q = UtilDecompositons_FDRM.ensureIdentity(Q, Ablock.numRows, Ablock.numRows);
        }

        FMatrixRBlock Qblock = new FMatrixRBlock();
        Qblock.numRows = Q.numRows;
        Qblock.numCols = Q.numCols;
        Qblock.blockLength = blockLength;
        Qblock.data = Q.data;

        ((QRDecompositionHouseholder_FDRB)alg).getQ(Qblock, compact);

        convertBlockToRow(Q.numRows, Q.numCols, Q.data);

        return Q;
    }

    @Override
    public FMatrixRMaj getR( @Nullable FMatrixRMaj R, boolean compact ) {
        FMatrixRBlock Rblock;

        Rblock = ((QRDecompositionHouseholder_FDRB)alg).getR(null, compact);

        if (R == null) {
            R = new FMatrixRMaj(Rblock.numRows, Rblock.numCols);
        }
        MatrixOps_FDRB.convert(Rblock, R);

        return R;
    }
}
