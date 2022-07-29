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
 * Computes a matrix decomposition such that:<br>
 * <br>
 * A = U*B*V<sup>T</sup><br>
 * <br>
 * where A is m by n, U is orthogonal and m by m, B is an m by n bidiagonal matrix, V is orthogonal and n by n.
 * This is used as a first step in computing the SVD of a matrix for the QR algorithm approach.
 * </p>
 * <p>
 * A bidiagonal matrix has zeros in every element except for the two diagonals.<br>
 * <br>
 * b_ij = 0    if i &gt; j or i &lt; j-1<br>
 * </p>
 *
 *
 * @author Peter Abeles
 */
public interface BidiagonalDecomposition<T extends Matrix>
        extends DecompositionInterface<T>
{
    /**
     * Returns the bidiagonal matrix.
     *
     * @param B If not null the results are stored here, if null a new matrix is created.
     * @return The bidiagonal matrix.
     */
    T getB(@Nullable T B, boolean compact);

    /**
     * Returns the orthogonal U matrix.
     *
     * @param U If not null then the results will be stored here.  Otherwise a new matrix will be created.
     * @return The extracted Q matrix.
     */
    T getU(@Nullable T U, boolean transpose, boolean compact);

    /**
     * Returns the orthogonal V matrix.
     *
     * @param V If not null then the results will be stored here.  Otherwise a new matrix will be created.
     * @return The extracted Q matrix.
     */
    T getV(@Nullable T V, boolean transpose, boolean compact);
}