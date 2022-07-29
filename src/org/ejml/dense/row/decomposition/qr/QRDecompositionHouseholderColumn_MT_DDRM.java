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

import org.ejml.concurrency.EjmlConcurrency;
import org.ejml.data.DMatrixRMaj;
import org.ejml.dense.row.decomposition.UtilDecompositons_DDRM;
import org.jetbrains.annotations.Nullable;

/**
 * <p>
 * Concurrent extension of {@link QRDecompositionHouseholderColumn_DDRM}.
 * </p>
 *
 * @author Peter Abeles
 */
@SuppressWarnings("NullAway.Init")
public class QRDecompositionHouseholderColumn_MT_DDRM extends QRDecompositionHouseholderColumn_DDRM {
    @Override
    public DMatrixRMaj getQ( @Nullable DMatrixRMaj Q, boolean compact ) {
        if (compact) {
            Q = UtilDecompositons_DDRM.ensureIdentity(Q, numRows, minLength);
        } else {
            Q = UtilDecompositons_DDRM.ensureIdentity(Q, numRows, numRows);
        }

        for (int j = minLength - 1; j >= 0; j--) {
            double[] u = dataQR[j];

            // This is a fairly modest speed up since only one of the loops can be made concurrent
            QrHelperFunctions_MT_DDRM.rank1UpdateMultR_u0(Q, u, 1.0, gammas[j], j, j, numRows, v);
        }

        return Q;
    }

    @Override
    protected void updateA( int w ) {
        final double[] u = dataQR[w];

        EjmlConcurrency.loopFor(w + 1, numCols, j -> {
            final double[] colQ = dataQR[j];
            double val = colQ[w];

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