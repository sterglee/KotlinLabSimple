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
import static org.ejml.ops.FMonoids.*;

/**
 * as defined in the graphblas c-api (https://people.eecs.berkeley.edu/~aydin/GraphBLAS_API_C_v13.pdf)
 * p. 27-28
 * Note: some donât have a multiplicative annihilator (thus no "true" semi-rings")
 */
@Generated("org.ejml.ops.DSemiRings")
public final class FSemiRings {
    public static final FSemiRing PLUS_TIMES = new FSemiRing(PLUS, TIMES);
    public static final FSemiRing MIN_PLUS = new FSemiRing(MIN, PLUS);
    public static final FSemiRing MAX_PLUS = new FSemiRing(MAX, PLUS);
    public static final FSemiRing MIN_TIMES = new FSemiRing(MIN, TIMES);
    public static final FSemiRing MIN_MAX = new FSemiRing(MIN, MAX);
    public static final FSemiRing MAX_MIN = new FSemiRing(MAX, MIN);
    public static final FSemiRing MAX_TIMES = new FSemiRing(MAX, TIMES);
    public static final FSemiRing PLUS_MIN = new FSemiRing(PLUS, MIN);

    public static final FSemiRing OR_AND = new FSemiRing(OR, AND);
    public static final FSemiRing AND_OR = new FSemiRing(AND, OR);
    public static final FSemiRing XOR_AND = new FSemiRing(XOR, AND);
    public static final FSemiRing XNOR_OR = new FSemiRing(XNOR, OR);

    // only private as they have no identity element, hence can only be used for add
    private static final FMonoid FIRST = new FMonoid(Float.NaN, ( x, y ) -> x);
    private static final FMonoid SECOND = new FMonoid(Float.NaN, ( x, y ) -> y);

    // semi-rings with no multiplicative annihilator
    public static final FSemiRing MIN_FIRST = new FSemiRing(MIN, FIRST);
    public static final FSemiRing MIN_SECOND = new FSemiRing(MIN, SECOND);
    public static final FSemiRing MAX_FIRST = new FSemiRing(MAX, FIRST);
    public static final FSemiRing MAX_SECOND = new FSemiRing(MAX, SECOND);
}
