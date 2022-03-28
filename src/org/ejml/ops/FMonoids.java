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

package org.ejml.ops;

import javax.annotation.Generated;
/**
 * as defined in the graphblas c-api (https://people.eecs.berkeley.edu/~aydin/GraphBLAS_API_C_v13.pdf)
 * p. 26
 */
@Generated("org.ejml.ops.DMonoids")
public final class FMonoids {
    public static final FMonoid AND = new FMonoid(1, ( a, b ) -> (a == 0 || b == 0) ? 0 : 1);
    public static final FMonoid OR = new FMonoid(0, ( a, b ) -> (a != 0 || b != 0) ? 1 : 0);
    public static final FMonoid XOR = new FMonoid(0, ( a, b ) -> ((a == 0 && b == 0) || (a != 0 && b != 0)) ? 0 : 1);
    public static final FMonoid XNOR = new FMonoid(0, ( a, b ) -> ((a == 0 && b == 0) || (a != 0 && b != 0)) ? 1 : 0);

    public static final FMonoid PLUS = new FMonoid(0, Float::sum);
    public static final FMonoid TIMES = new FMonoid(1, ( a, b ) -> a*b);

    public final static FMonoid MIN = new FMonoid(Float.MAX_VALUE, ( a, b ) -> (a <= b) ? a : b);
    public final static FMonoid MAX = new FMonoid(Float.MIN_VALUE, ( a, b ) -> (a >= b) ? a : b);
}
