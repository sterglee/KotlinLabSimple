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

package org.ejml.dense.row.misc;

import javax.annotation.Generated;
import org.ejml.data.FMatrixD1;
import org.ejml.data.FMatrixRMaj;
import org.ejml.data.ElementLocation;
import org.jetbrains.annotations.Nullable;

import static org.ejml.UtilEjml.checkSameShape;
import static org.ejml.UtilEjml.reshapeOrDeclare;

/**
 * Implementations of common ops routines for {@link FMatrixRMaj}.  In general
 * there is no need to directly invoke these functions.
 *
 * @author Peter Abeles
 */
@Generated("org.ejml.dense.row.misc.ImplCommonOps_DDRM")
public class ImplCommonOps_FDRM {
    public static void extract( FMatrixRMaj src,
                                int srcY0, int srcX0,
                                FMatrixRMaj dst,
                                int dstY0, int dstX0,
                                int numRows, int numCols ) {
        for (int y = 0; y < numRows; y++) {
            int indexSrc = src.getIndex(y + srcY0, srcX0);
            int indexDst = dst.getIndex(y + dstY0, dstX0);
            System.arraycopy(src.data, indexSrc, dst.data, indexDst, numCols);
        }
    }

    public static float elementMax( FMatrixD1 a, @Nullable ElementLocation loc ) {
        final int size = a.getNumElements();

        int bestIndex = 0;
        float max = a.get(0);
        for (int i = 1; i < size; i++) {
            float val = a.get(i);
            if (val >= max) {
                bestIndex = i;
                max = val;
            }
        }

        if (loc != null) {
            loc.row = bestIndex/a.numCols;
            loc.col = bestIndex%a.numCols;
        }

        return max;
    }

    public static float elementMaxAbs( FMatrixD1 a, @Nullable ElementLocation loc ) {
        final int size = a.getNumElements();

        int bestIndex = 0;
        float max = 0;
        for (int i = 0; i < size; i++) {
            float val = Math.abs(a.get(i));
            if (val > max) {
                bestIndex = i;
                max = val;
            }
        }

        if (loc != null) {
            loc.row = bestIndex/a.numCols;
            loc.col = bestIndex%a.numCols;
        }

        return max;
    }

    public static float elementMin( FMatrixD1 a, @Nullable ElementLocation loc ) {
        final int size = a.getNumElements();

        int bestIndex = 0;
        float min = a.get(0);
        for (int i = 1; i < size; i++) {
            float val = a.get(i);
            if (val < min) {
                bestIndex = i;
                min = val;
            }
        }

        if (loc != null) {
            loc.row = bestIndex/a.numCols;
            loc.col = bestIndex%a.numCols;
        }

        return min;
    }

    public static float elementMinAbs( FMatrixD1 a, @Nullable ElementLocation loc ) {
        final int size = a.getNumElements();

        int bestIndex = 0;
        float min = Float.MAX_VALUE;
        for (int i = 0; i < size; i++) {
            float val = Math.abs(a.get(i));
            if (val < min) {
                bestIndex = i;
                min = val;
            }
        }

        if (loc != null) {
            loc.row = bestIndex/a.numCols;
            loc.col = bestIndex%a.numCols;
        }

        return min;
    }

    public static void elementMult( FMatrixD1 A, FMatrixD1 B ) {
        checkSameShape(A, B, true);

        int length = A.getNumElements();

        for (int i = 0; i < length; i++) {
            A.times(i, B.get(i));
        }
    }

    public static <T extends FMatrixD1> T elementMult( T A, T B, @Nullable T output ) {
        checkSameShape(A, B, true);
        output = reshapeOrDeclare(output, A);

        int length = A.getNumElements();

        for (int i = 0; i < length; i++) {
            output.set(i, A.get(i)*B.get(i));
        }

        return output;
    }

    public static void elementDiv( FMatrixD1 A, FMatrixD1 B ) {
        checkSameShape(A, B, true);

        int length = A.getNumElements();

        for (int i = 0; i < length; i++) {
            A.div(i, B.get(i));
        }
    }

    public static <T extends FMatrixD1> T elementDiv( T A, T B, @Nullable T output ) {
        checkSameShape(A, B, true);
        output = reshapeOrDeclare(output, A);

        int length = A.getNumElements();

        for (int i = 0; i < length; i++) {
            output.set(i, A.get(i)/B.get(i));
        }

        return output;
    }

    public static float elementSum( FMatrixD1 mat ) {
        float total = 0;

        int size = mat.getNumElements();

        for (int i = 0; i < size; i++) {
            total += mat.get(i);
        }

        return total;
    }

    public static float elementSumAbs( FMatrixD1 mat ) {
        float total = 0;

        int size = mat.getNumElements();

        for (int i = 0; i < size; i++) {
            total += Math.abs(mat.get(i));
        }

        return total;
    }

    public static <T extends FMatrixD1> T elementPower( T A, T B, @Nullable T output ) {
        checkSameShape(A, B, true);
        output = reshapeOrDeclare(output, A);

        int size = A.getNumElements();
        for (int i = 0; i < size; i++) {
            output.data[i] = (float)Math.pow(A.data[i], B.data[i]);
        }

        return output;
    }

    public static <T extends FMatrixD1> T elementPower( float a, T B, @Nullable T output ) {
        output = reshapeOrDeclare(output, B);

        int size = B.getNumElements();
        for (int i = 0; i < size; i++) {
            output.data[i] = (float)Math.pow(a, B.data[i]);
        }

        return output;
    }

    public static <T extends FMatrixD1> T elementPower( T A, float b, @Nullable T output ) {
        output = reshapeOrDeclare(output, A);

        int size = A.getNumElements();
        for (int i = 0; i < size; i++) {
            output.data[i] = (float)Math.pow(A.data[i], b);
        }

        return output;
    }

    public static <T extends FMatrixD1> T elementLog( T A, @Nullable T output ) {
        output = reshapeOrDeclare(output, A);

        int size = A.getNumElements();
        for (int i = 0; i < size; i++) {
            output.data[i] = (float)Math.log(A.data[i]);
        }

        return output;
    }

    public static <T extends FMatrixD1> T elementExp( T A, @Nullable T output ) {
        output = reshapeOrDeclare(output, A);

        int size = A.getNumElements();
        for (int i = 0; i < size; i++) {
            output.data[i] = (float)Math.exp(A.data[i]);
        }

        return output;
    }
}
