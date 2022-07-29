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

package org.ejml.interfaces.decomposition;

import org.ejml.data.Matrix;
import org.jetbrains.annotations.Nullable;


/**
 * <p>
 * Cholesky LDL<sup>T</sup> decomposition.
 * </p>
 * <p>
 * A Cholesky LDL decomposition decomposes positive-definite symmetric matrices into:<br>
 * <br>
 * L*D*L<sup>T</sup>=A<br>
 * <br>
 * where L is a lower triangular matrix and D is a diagonal matrix.  The main advantage of LDL versus LL or RR Cholesky is that
 * it avoid a square root operation.
 * </p>
 *
 * @author Peter Abeles
 */
public interface CholeskyLDLDecomposition<MatrixType extends Matrix>
        extends DecompositionInterface<MatrixType> {


    /**
     * <p>
     * Returns the lower triangular matrix from the decomposition.
     * </p>
     *
     * <p>
     * If an input is provided that matrix is used to write the results to.
     * Otherwise a new matrix is created and the results written to it.
     * </p>
     *
     * @param L If not null then the decomposed matrix is written here.
     * @return A lower triangular matrix.
     */
    MatrixType getL(@Nullable MatrixType L);

    /**
     * <p>
     * Returns the diagonal matrixfrom the decomposition.
     * </p>
     *
     * <p>
     * If an input is provided that matrix is used to write the results to.
     * Otherwise a new matrix is created and the results written to it.
     * </p>
     *
     * @param D If not null it will be used to store the diagonal matrix
     * @return D Square diagonal matrix
     */
    MatrixType getD(@Nullable MatrixType D);
}