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

package org.ejml.sparse.csc.mult;

import javax.annotation.Generated;
import org.ejml.data.FGrowArray;
import org.ejml.data.FMatrixSparseCSC;
import org.ejml.data.IGrowArray;

/**
 * Workspace for concurrent algorithms.
 *
 * @author Peter Abeles
 */
@Generated("org.ejml.sparse.csc.mult.Workspace_MT_DSCC")
public class Workspace_MT_FSCC {
    public final IGrowArray gw = new IGrowArray();
    public final FGrowArray gx = new FGrowArray();
    public final FMatrixSparseCSC mat = new FMatrixSparseCSC(1, 1);
}
