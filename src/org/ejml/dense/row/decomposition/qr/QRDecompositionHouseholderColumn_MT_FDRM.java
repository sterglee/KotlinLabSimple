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
import org.ejml.concurrency.EjmlConcurrency;
import org.ejml.data.FMatrixRMaj;
import org.ejml.dense.row.decomposition.UtilDecompositons_FDRM;
import org.jetbrains.annotations.Nullable;

/**
 * <p>
 * Concurrent extension of {@link QRDecompositionHouseholderColumn_FDRM}.
 * </p>
 *
 * @author Peter Abeles
 */
@SuppressWarnings("NullAway.Init")
@Generated("org.ejml.dense.row.decomposition.qr.QRDecompositionHouseholderColumn_MT_DDRM")
public class QRDecompositionHouseholderColumn_MT_FDRM extends QRDecompositionHouseholderColumn_FDRM {
    @Override
    public FMatrixRMaj getQ( @Nullable FMatrixRMaj Q, boolean compact ) {
        if (compact) {
            Q = UtilDecompositons_FDRM.ensureIdentity(Q, numRows, minLength);
        } else {
            Q = UtilDecompositons_FDRM.ensureIdentity(Q, numRows, numRows);
        }

        for (int j = minLength - 1; j >= 0; j--) {
            float[] u = dataQR[j];

            // This is a fairly modest speed up since only one of the loops can be made concurrent
            QrHelperFunctions_MT_FDRM.rank1UpdateMultR_u0(Q, u, 1.0f, gammas[j], j, j, numRows, v);
        }

        return Q;
    }

    @Override
    protected void updateA( int w ) {
        final float[] u = dataQR[w];

        EjmlConcurrency.loopFor(w + 1, numCols, j -> {
            final float[] colQ = dataQR[j];
            float val = colQ[w];

            for (int k = w + 1; k < numRows; k++) {
                val += u[k]*colQ[k];
            }
            val *= gamma;

            colQ[w] -= val;
            for (int i = w + 1; i < numRows; i++) {
                colQ[i] -= u[i]*val;
            }
        });
    }
}