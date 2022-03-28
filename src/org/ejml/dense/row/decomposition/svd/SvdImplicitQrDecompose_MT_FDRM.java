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

package org.ejml.dense.row.decomposition.svd;

import javax.annotation.Generated;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.CommonOps_MT_FDRM;
import org.ejml.dense.row.decomposition.bidiagonal.BidiagonalDecompositionRow_MT_FDRM;
import org.ejml.dense.row.decomposition.bidiagonal.BidiagonalDecompositionTall_MT_FDRM;
import org.jetbrains.annotations.NotNull;

/**
 * <p>Concurrent version of {@link SvdImplicitQrDecompose_FDRM}</p>
 *
 * @author Peter Abeles
 */
@SuppressWarnings("NullAway.Init")
@Generated("org.ejml.dense.row.decomposition.svd.SvdImplicitQrDecompose_MT_DDRM")
public class SvdImplicitQrDecompose_MT_FDRM extends SvdImplicitQrDecompose_FDRM {

    public SvdImplicitQrDecompose_MT_FDRM( boolean compact, boolean computeU, boolean computeV,
                                           boolean canUseTallBidiagonal ) {
        super(compact, computeU, computeV, canUseTallBidiagonal);
    }

    @Override
    protected void transpose( @NotNull FMatrixRMaj V, FMatrixRMaj Vt ) {
        CommonOps_MT_FDRM.transpose(Vt, V);
    }

    @Override
    protected void declareBidiagonalDecomposition() {
        if (canUseTallBidiagonal && numRows > numCols*2 && !computeU) {
            if (bidiag == null || !(bidiag instanceof BidiagonalDecompositionTall_MT_FDRM)) {
                bidiag = new BidiagonalDecompositionTall_MT_FDRM();
            }
        } else if (bidiag == null || !(bidiag instanceof BidiagonalDecompositionRow_MT_FDRM)) {
            bidiag = new BidiagonalDecompositionRow_MT_FDRM();
        }
    }
}
