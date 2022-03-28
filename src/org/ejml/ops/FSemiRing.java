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
 * An algebraic structure, defined over the `floats` by two monoids + and *, called addition and multiplication.
 */
@Generated("org.ejml.ops.DSemiRing")
public class FSemiRing {
    public final FMonoid add;
    public final FMonoid mult;

    public FSemiRing( FMonoid add, FMonoid mult ) {
        this.add = add;
        this.mult = mult;
    }
}
