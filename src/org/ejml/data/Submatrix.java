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

package org.ejml.data;

/**
 * <p>
 * Describes a rectangular submatrix.
 * </p>
 *
 * <p>
 * Rows are row0 &le; i &lt; row1 and Columns are col0 &le; j &lt; col1
 * </p>
 *
 * @author Peter Abeles
 */
public abstract class Submatrix<M extends Matrix> {
    @SuppressWarnings("NullAway.Init")
    public M original;

    // bounding rows and columns
    public int row0,col0;
    public int row1,col1;

    public void set(M original,
                    int row0, int row1, int col0, int col1) {
        this.original = original;
        this.row0 = row0;
        this.col0 = col0;
        this.row1 = row1;
        this.col1 = col1;
    }

    public void set(M original) {
        this.original = original;
        row1 = original.getNumRows();
        col1 = original.getNumCols();
    }

    public int getRows() {
        return row1 - row0;
    }

    public int getCols() {
        return col1 - col0;
    }

    public abstract void print();
}
