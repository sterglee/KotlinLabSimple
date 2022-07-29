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

package org.ejml.dense.row.misc;

import javax.annotation.Generated;
import org.ejml.UtilEjml;
import org.ejml.data.DMatrix1Row;


/**
 * Performs an unrolled lower cholesky decomposition for small matrices.
 *
 * <p>DO NOT MODIFY.  Automatically generated code created by GenerateUnrolledCholesky</p>
 *
 * @author Peter Abeles
 */
@Generated("org.ejml.dense.row.misc.GenerateUnrolledCholesky")
public class UnrolledCholesky_DDRM {
    
    public static final int MAX = 7;
    public static boolean lower( DMatrix1Row A , DMatrix1Row L ) {
        switch( A.numRows ) {
            case 1: return lower1(A,L);
            case 2: return lower2(A,L);
            case 3: return lower3(A,L);
            case 4: return lower4(A,L);
            case 5: return lower5(A,L);
            case 6: return lower6(A,L);
            case 7: return lower7(A,L);
            default: return false;
        }
    }

    public static boolean upper( DMatrix1Row A , DMatrix1Row L ) {
        switch( A.numRows ) {
            case 1: return upper1(A,L);
            case 2: return upper2(A,L);
            case 3: return upper3(A,L);
            case 4: return upper4(A,L);
            case 5: return upper5(A,L);
            case 6: return upper6(A,L);
            case 7: return upper7(A,L);
            default: return false;
        }
    }

    public static boolean lower1( DMatrix1Row A , DMatrix1Row L )
    {
        double []data = A.data;

        double a11 = data[ 0 ];

        L.data[0]  = Math.sqrt(a11);
        return !UtilEjml.isUncountable(L.data[0]);
    }

    public static boolean lower2( DMatrix1Row A , DMatrix1Row L )
    {
        double []data = A.data;

        double a11 = data[ 0 ];
        double a21 = data[ 2 ];
        double a22 = data[ 3 ];

        L.data[0] = a11 = Math.sqrt(a11);
        L.data[1] = 0;
        L.data[2] = a21 = (a21)/a11;
        L.data[3]  = Math.sqrt(a22-a21*a21);
        return !UtilEjml.isUncountable(L.data[3]);
    }

    public static boolean lower3( DMatrix1Row A , DMatrix1Row L )
    {
        double []data = A.data;

        double a11 = data[ 0 ];
        double a21 = data[ 3 ];
        double a22 = data[ 4 ];
        double a31 = data[ 6 ];
        double a32 = data[ 7 ];
        double a33 = data[ 8 ];

        L.data[0] = a11 = Math.sqrt(a11);
        L.data[1] = 0;
        L.data[2] = 0;
        L.data[3] = a21 = (a21)/a11;
        L.data[4] = a22 = Math.sqrt(a22-a21*a21);
        L.data[5] = 0;
        L.data[6] = a31 = (a31)/a11;
        L.data[7] = a32 = (a32-a31*a21)/a22;
        L.data[8]  = Math.sqrt(a33-a31*a31-a32*a32);
        return !UtilEjml.isUncountable(L.data[8]);
    }

    public static boolean lower4( DMatrix1Row A , DMatrix1Row L )
    {
        double []data = A.data;

        double a11 = data[ 0 ];
        double a21 = data[ 4 ];
        double a22 = data[ 5 ];
        double a31 = data[ 8 ];
        double a32 = data[ 9 ];
        double a33 = data[ 10 ];
        double a41 = data[ 12 ];
        double a42 = data[ 13 ];
        double a43 = data[ 14 ];
        double a44 = data[ 15 ];

        L.data[0] = a11 = Math.sqrt(a11);
        L.data[1] = 0;
        L.data[2] = 0;
        L.data[3] = 0;
        L.data[4] = a21 = (a21)/a11;
        L.data[5] = a22 = Math.sqrt(a22-a21*a21);
        L.data[6] = 0;
        L.data[7] = 0;
        L.data[8] = a31 = (a31)/a11;
        L.data[9] = a32 = (a32-a31*a21)/a22;
        L.data[10] = a33 = Math.sqrt(a33-a31*a31-a32*a32);
        L.data[11] = 0;
        L.data[12] = a41 = (a41)/a11;
        L.data[13] = a42 = (a42-a41*a21)/a22;
        L.data[14] = a43 = (a43-a41*a31-a42*a32)/a33;
        L.data[15]  = Math.sqrt(a44-a41*a41-a42*a42-a43*a43);
        return !UtilEjml.isUncountable(L.data[15]);
    }

    public static boolean lower5( DMatrix1Row A , DMatrix1Row L )
    {
        double []data = A.data;

        double a11 = data[ 0 ];
        double a21 = data[ 5 ];
        double a22 = data[ 6 ];
        double a31 = data[ 10 ];
        double a32 = data[ 11 ];
        double a33 = data[ 12 ];
        double a41 = data[ 15 ];
        double a42 = data[ 16 ];
        double a43 = data[ 17 ];
        double a44 = data[ 18 ];
        double a51 = data[ 20 ];
        double a52 = data[ 21 ];
        double a53 = data[ 22 ];
        double a54 = data[ 23 ];
        double a55 = data[ 24 ];

        L.data[0] = a11 = Math.sqrt(a11);
        L.data[1] = 0;
        L.data[2] = 0;
        L.data[3] = 0;
        L.data[4] = 0;
        L.data[5] = a21 = (a21)/a11;
        L.data[6] = a22 = Math.sqrt(a22-a21*a21);
        L.data[7] = 0;
        L.data[8] = 0;
        L.data[9] = 0;
        L.data[10] = a31 = (a31)/a11;
        L.data[11] = a32 = (a32-a31*a21)/a22;
        L.data[12] = a33 = Math.sqrt(a33-a31*a31-a32*a32);
        L.data[13] = 0;
        L.data[14] = 0;
        L.data[15] = a41 = (a41)/a11;
        L.data[16] = a42 = (a42-a41*a21)/a22;
        L.data[17] = a43 = (a43-a41*a31-a42*a32)/a33;
        L.data[18] = a44 = Math.sqrt(a44-a41*a41-a42*a42-a43*a43);
        L.data[19] = 0;
        L.data[20] = a51 = (a51)/a11;
        L.data[21] = a52 = (a52-a51*a21)/a22;
        L.data[22] = a53 = (a53-a51*a31-a52*a32)/a33;
        L.data[23] = a54 = (a54-a51*a41-a52*a42-a53*a43)/a44;
        L.data[24]  = Math.sqrt(a55-a51*a51-a52*a52-a53*a53-a54*a54);
        return !UtilEjml.isUncountable(L.data[24]);
    }

    public static boolean lower6( DMatrix1Row A , DMatrix1Row L )
    {
        double []data = A.data;

        double a11 = data[ 0 ];
        double a21 = data[ 6 ];
        double a22 = data[ 7 ];
        double a31 = data[ 12 ];
        double a32 = data[ 13 ];
        double a33 = data[ 14 ];
        double a41 = data[ 18 ];
        double a42 = data[ 19 ];
        double a43 = data[ 20 ];
        double a44 = data[ 21 ];
        double a51 = data[ 24 ];
        double a52 = data[ 25 ];
        double a53 = data[ 26 ];
        double a54 = data[ 27 ];
        double a55 = data[ 28 ];
        double a61 = data[ 30 ];
        double a62 = data[ 31 ];
        double a63 = data[ 32 ];
        double a64 = data[ 33 ];
        double a65 = data[ 34 ];
        double a66 = data[ 35 ];

        L.data[0] = a11 = Math.sqrt(a11);
        L.data[1] = 0;
        L.data[2] = 0;
        L.data[3] = 0;
        L.data[4] = 0;
        L.data[5] = 0;
        L.data[6] = a21 = (a21)/a11;
        L.data[7] = a22 = Math.sqrt(a22-a21*a21);
        L.data[8] = 0;
        L.data[9] = 0;
        L.data[10] = 0;
        L.data[11] = 0;
        L.data[12] = a31 = (a31)/a11;
        L.data[13] = a32 = (a32-a31*a21)/a22;
        L.data[14] = a33 = Math.sqrt(a33-a31*a31-a32*a32);
        L.data[15] = 0;
        L.data[16] = 0;
        L.data[17] = 0;
        L.data[18] = a41 = (a41)/a11;
        L.data[19] = a42 = (a42-a41*a21)/a22;
        L.data[20] = a43 = (a43-a41*a31-a42*a32)/a33;
        L.data[21] = a44 = Math.sqrt(a44-a41*a41-a42*a42-a43*a43);
        L.data[22] = 0;
        L.data[23] = 0;
        L.data[24] = a51 = (a51)/a11;
        L.data[25] = a52 = (a52-a51*a21)/a22;
        L.data[26] = a53 = (a53-a51*a31-a52*a32)/a33;
        L.data[27] = a54 = (a54-a51*a41-a52*a42-a53*a43)/a44;
        L.data[28] = a55 = Math.sqrt(a55-a51*a51-a52*a52-a53*a53-a54*a54);
        L.data[29] = 0;
        L.data[30] = a61 = (a61)/a11;
        L.data[31] = a62 = (a62-a61*a21)/a22;
        L.data[32] = a63 = (a63-a61*a31-a62*a32)/a33;
        L.data[33] = a64 = (a64-a61*a41-a62*a42-a63*a43)/a44;
        L.data[34] = a65 = (a65-a61*a51-a62*a52-a63*a53-a64*a54)/a55;
        L.data[35]  = Math.sqrt(a66-a61*a61-a62*a62-a63*a63-a64*a64-a65*a65);
        return !UtilEjml.isUncountable(L.data[35]);
    }

    public static boolean lower7( DMatrix1Row A , DMatrix1Row L )
    {
        double []data = A.data;

        double a11 = data[ 0 ];
        double a21 = data[ 7 ];
        double a22 = data[ 8 ];
        double a31 = data[ 14 ];
        double a32 = data[ 15 ];
        double a33 = data[ 16 ];
        double a41 = data[ 21 ];
        double a42 = data[ 22 ];
        double a43 = data[ 23 ];
        double a44 = data[ 24 ];
        double a51 = data[ 28 ];
        double a52 = data[ 29 ];
        double a53 = data[ 30 ];
        double a54 = data[ 31 ];
        double a55 = data[ 32 ];
        double a61 = data[ 35 ];
        double a62 = data[ 36 ];
        double a63 = data[ 37 ];
        double a64 = data[ 38 ];
        double a65 = data[ 39 ];
        double a66 = data[ 40 ];
        double a71 = data[ 42 ];
        double a72 = data[ 43 ];
        double a73 = data[ 44 ];
        double a74 = data[ 45 ];
        double a75 = data[ 46 ];
        double a76 = data[ 47 ];
        double a77 = data[ 48 ];

        L.data[0] = a11 = Math.sqrt(a11);
        L.data[1] = 0;
        L.data[2] = 0;
        L.data[3] = 0;
        L.data[4] = 0;
        L.data[5] = 0;
        L.data[6] = 0;
        L.data[7] = a21 = (a21)/a11;
        L.data[8] = a22 = Math.sqrt(a22-a21*a21);
        L.data[9] = 0;
        L.data[10] = 0;
        L.data[11] = 0;
        L.data[12] = 0;
        L.data[13] = 0;
        L.data[14] = a31 = (a31)/a11;
        L.data[15] = a32 = (a32-a31*a21)/a22;
        L.data[16] = a33 = Math.sqrt(a33-a31*a31-a32*a32);
        L.data[17] = 0;
        L.data[18] = 0;
        L.data[19] = 0;
        L.data[20] = 0;
        L.data[21] = a41 = (a41)/a11;
        L.data[22] = a42 = (a42-a41*a21)/a22;
        L.data[23] = a43 = (a43-a41*a31-a42*a32)/a33;
        L.data[24] = a44 = Math.sqrt(a44-a41*a41-a42*a42-a43*a43);
        L.data[25] = 0;
        L.data[26] = 0;
        L.data[27] = 0;
        L.data[28] = a51 = (a51)/a11;
        L.data[29] = a52 = (a52-a51*a21)/a22;
        L.data[30] = a53 = (a53-a51*a31-a52*a32)/a33;
        L.data[31] = a54 = (a54-a51*a41-a52*a42-a53*a43)/a44;
        L.data[32] = a55 = Math.sqrt(a55-a51*a51-a52*a52-a53*a53-a54*a54);
        L.data[33] = 0;
        L.data[34] = 0;
        L.data[35] = a61 = (a61)/a11;
        L.data[36] = a62 = (a62-a61*a21)/a22;
        L.data[37] = a63 = (a63-a61*a31-a62*a32)/a33;
        L.data[38] = a64 = (a64-a61*a41-a62*a42-a63*a43)/a44;
        L.data[39] = a65 = (a65-a61*a51-a62*a52-a63*a53-a64*a54)/a55;
        L.data[40] = a66 = Math.sqrt(a66-a61*a61-a62*a62-a63*a63-a64*a64-a65*a65);
        L.data[41] = 0;
        L.data[42] = a71 = (a71)/a11;
        L.data[43] = a72 = (a72-a71*a21)/a22;
        L.data[44] = a73 = (a73-a71*a31-a72*a32)/a33;
        L.data[45] = a74 = (a74-a71*a41-a72*a42-a73*a43)/a44;
        L.data[46] = a75 = (a75-a71*a51-a72*a52-a73*a53-a74*a54)/a55;
        L.data[47] = a76 = (a76-a71*a61-a72*a62-a73*a63-a74*a64-a75*a65)/a66;
        L.data[48]  = Math.sqrt(a77-a71*a71-a72*a72-a73*a73-a74*a74-a75*a75-a76*a76);
        return !UtilEjml.isUncountable(L.data[48]);
    }

    public static boolean upper1( DMatrix1Row A , DMatrix1Row R )
    {
        double []data = A.data;

        double a11 = data[ 0 ];

        R.data[0]  = Math.sqrt(a11);
        return !UtilEjml.isUncountable(R.data[0]);
    }

    public static boolean upper2( DMatrix1Row A , DMatrix1Row R )
    {
        double []data = A.data;

        double a11 = data[ 0 ];
        double a12 = data[ 1 ];
        double a22 = data[ 3 ];

        R.data[0] = a11 = Math.sqrt(a11);
        R.data[2] = 0;
        R.data[1] = a12 = (a12)/a11;
        R.data[3]  = Math.sqrt(a22-a12*a12);
        return !UtilEjml.isUncountable(R.data[3]);
    }

    public static boolean upper3( DMatrix1Row A , DMatrix1Row R )
    {
        double []data = A.data;

        double a11 = data[ 0 ];
        double a12 = data[ 1 ];
        double a22 = data[ 4 ];
        double a13 = data[ 2 ];
        double a23 = data[ 5 ];
        double a33 = data[ 8 ];

        R.data[0] = a11 = Math.sqrt(a11);
        R.data[3] = 0;
        R.data[6] = 0;
        R.data[1] = a12 = (a12)/a11;
        R.data[4] = a22 = Math.sqrt(a22-a12*a12);
        R.data[7] = 0;
        R.data[2] = a13 = (a13)/a11;
        R.data[5] = a23 = (a23-a12*a13)/a22;
        R.data[8]  = Math.sqrt(a33-a13*a13-a23*a23);
        return !UtilEjml.isUncountable(R.data[8]);
    }

    public static boolean upper4( DMatrix1Row A , DMatrix1Row R )
    {
        double []data = A.data;

        double a11 = data[ 0 ];
        double a12 = data[ 1 ];
        double a22 = data[ 5 ];
        double a13 = data[ 2 ];
        double a23 = data[ 6 ];
        double a33 = data[ 10 ];
        double a14 = data[ 3 ];
        double a24 = data[ 7 ];
        double a34 = data[ 11 ];
        double a44 = data[ 15 ];

        R.data[0] = a11 = Math.sqrt(a11);
        R.data[4] = 0;
        R.data[8] = 0;
        R.data[12] = 0;
        R.data[1] = a12 = (a12)/a11;
        R.data[5] = a22 = Math.sqrt(a22-a12*a12);
        R.data[9] = 0;
        R.data[13] = 0;
        R.data[2] = a13 = (a13)/a11;
        R.data[6] = a23 = (a23-a12*a13)/a22;
        R.data[10] = a33 = Math.sqrt(a33-a13*a13-a23*a23);
        R.data[14] = 0;
        R.data[3] = a14 = (a14)/a11;
        R.data[7] = a24 = (a24-a12*a14)/a22;
        R.data[11] = a34 = (a34-a13*a14-a23*a24)/a33;
        R.data[15]  = Math.sqrt(a44-a14*a14-a24*a24-a34*a34);
        return !UtilEjml.isUncountable(R.data[15]);
    }

    public static boolean upper5( DMatrix1Row A , DMatrix1Row R )
    {
        double []data = A.data;

        double a11 = data[ 0 ];
        double a12 = data[ 1 ];
        double a22 = data[ 6 ];
        double a13 = data[ 2 ];
        double a23 = data[ 7 ];
        double a33 = data[ 12 ];
        double a14 = data[ 3 ];
        double a24 = data[ 8 ];
        double a34 = data[ 13 ];
        double a44 = data[ 18 ];
        double a15 = data[ 4 ];
        double a25 = data[ 9 ];
        double a35 = data[ 14 ];
        double a45 = data[ 19 ];
        double a55 = data[ 24 ];

        R.data[0] = a11 = Math.sqrt(a11);
        R.data[5] = 0;
        R.data[10] = 0;
        R.data[15] = 0;
        R.data[20] = 0;
        R.data[1] = a12 = (a12)/a11;
        R.data[6] = a22 = Math.sqrt(a22-a12*a12);
        R.data[11] = 0;
        R.data[16] = 0;
        R.data[21] = 0;
        R.data[2] = a13 = (a13)/a11;
        R.data[7] = a23 = (a23-a12*a13)/a22;
        R.data[12] = a33 = Math.sqrt(a33-a13*a13-a23*a23);
        R.data[17] = 0;
        R.data[22] = 0;
        R.data[3] = a14 = (a14)/a11;
        R.data[8] = a24 = (a24-a12*a14)/a22;
        R.data[13] = a34 = (a34-a13*a14-a23*a24)/a33;
        R.data[18] = a44 = Math.sqrt(a44-a14*a14-a24*a24-a34*a34);
        R.data[23] = 0;
        R.data[4] = a15 = (a15)/a11;
        R.data[9] = a25 = (a25-a12*a15)/a22;
        R.data[14] = a35 = (a35-a13*a15-a23*a25)/a33;
        R.data[19] = a45 = (a45-a14*a15-a24*a25-a34*a35)/a44;
        R.data[24]  = Math.sqrt(a55-a15*a15-a25*a25-a35*a35-a45*a45);
        return !UtilEjml.isUncountable(R.data[24]);
    }

    public static boolean upper6( DMatrix1Row A , DMatrix1Row R )
    {
        double []data = A.data;

        double a11 = data[ 0 ];
        double a12 = data[ 1 ];
        double a22 = data[ 7 ];
        double a13 = data[ 2 ];
        double a23 = data[ 8 ];
        double a33 = data[ 14 ];
        double a14 = data[ 3 ];
        double a24 = data[ 9 ];
        double a34 = data[ 15 ];
        double a44 = data[ 21 ];
        double a15 = data[ 4 ];
        double a25 = data[ 10 ];
        double a35 = data[ 16 ];
        double a45 = data[ 22 ];
        double a55 = data[ 28 ];
        double a16 = data[ 5 ];
        double a26 = data[ 11 ];
        double a36 = data[ 17 ];
        double a46 = data[ 23 ];
        double a56 = data[ 29 ];
        double a66 = data[ 35 ];

        R.data[0] = a11 = Math.sqrt(a11);
        R.data[6] = 0;
        R.data[12] = 0;
        R.data[18] = 0;
        R.data[24] = 0;
        R.data[30] = 0;
        R.data[1] = a12 = (a12)/a11;
        R.data[7] = a22 = Math.sqrt(a22-a12*a12);
        R.data[13] = 0;
        R.data[19] = 0;
        R.data[25] = 0;
        R.data[31] = 0;
        R.data[2] = a13 = (a13)/a11;
        R.data[8] = a23 = (a23-a12*a13)/a22;
        R.data[14] = a33 = Math.sqrt(a33-a13*a13-a23*a23);
        R.data[20] = 0;
        R.data[26] = 0;
        R.data[32] = 0;
        R.data[3] = a14 = (a14)/a11;
        R.data[9] = a24 = (a24-a12*a14)/a22;
        R.data[15] = a34 = (a34-a13*a14-a23*a24)/a33;
        R.data[21] = a44 = Math.sqrt(a44-a14*a14-a24*a24-a34*a34);
        R.data[27] = 0;
        R.data[33] = 0;
        R.data[4] = a15 = (a15)/a11;
        R.data[10] = a25 = (a25-a12*a15)/a22;
        R.data[16] = a35 = (a35-a13*a15-a23*a25)/a33;
        R.data[22] = a45 = (a45-a14*a15-a24*a25-a34*a35)/a44;
        R.data[28] = a55 = Math.sqrt(a55-a15*a15-a25*a25-a35*a35-a45*a45);
        R.data[34] = 0;
        R.data[5] = a16 = (a16)/a11;
        R.data[11] = a26 = (a26-a12*a16)/a22;
        R.data[17] = a36 = (a36-a13*a16-a23*a26)/a33;
        R.data[23] = a46 = (a46-a14*a16-a24*a26-a34*a36)/a44;
        R.data[29] = a56 = (a56-a15*a16-a25*a26-a35*a36-a45*a46)/a55;
        R.data[35]  = Math.sqrt(a66-a16*a16-a26*a26-a36*a36-a46*a46-a56*a56);
        return !UtilEjml.isUncountable(R.data[35]);
    }

    public static boolean upper7( DMatrix1Row A , DMatrix1Row R )
    {
        double []data = A.data;

        double a11 = data[ 0 ];
        double a12 = data[ 1 ];
        double a22 = data[ 8 ];
        double a13 = data[ 2 ];
        double a23 = data[ 9 ];
        double a33 = data[ 16 ];
        double a14 = data[ 3 ];
        double a24 = data[ 10 ];
        double a34 = data[ 17 ];
        double a44 = data[ 24 ];
        double a15 = data[ 4 ];
        double a25 = data[ 11 ];
        double a35 = data[ 18 ];
        double a45 = data[ 25 ];
        double a55 = data[ 32 ];
        double a16 = data[ 5 ];
        double a26 = data[ 12 ];
        double a36 = data[ 19 ];
        double a46 = data[ 26 ];
        double a56 = data[ 33 ];
        double a66 = data[ 40 ];
        double a17 = data[ 6 ];
        double a27 = data[ 13 ];
        double a37 = data[ 20 ];
        double a47 = data[ 27 ];
        double a57 = data[ 34 ];
        double a67 = data[ 41 ];
        double a77 = data[ 48 ];

        R.data[0] = a11 = Math.sqrt(a11);
        R.data[7] = 0;
        R.data[14] = 0;
        R.data[21] = 0;
        R.data[28] = 0;
        R.data[35] = 0;
        R.data[42] = 0;
        R.data[1] = a12 = (a12)/a11;
        R.data[8] = a22 = Math.sqrt(a22-a12*a12);
        R.data[15] = 0;
        R.data[22] = 0;
        R.data[29] = 0;
        R.data[36] = 0;
        R.data[43] = 0;
        R.data[2] = a13 = (a13)/a11;
        R.data[9] = a23 = (a23-a12*a13)/a22;
        R.data[16] = a33 = Math.sqrt(a33-a13*a13-a23*a23);
        R.data[23] = 0;
        R.data[30] = 0;
        R.data[37] = 0;
        R.data[44] = 0;
        R.data[3] = a14 = (a14)/a11;
        R.data[10] = a24 = (a24-a12*a14)/a22;
        R.data[17] = a34 = (a34-a13*a14-a23*a24)/a33;
        R.data[24] = a44 = Math.sqrt(a44-a14*a14-a24*a24-a34*a34);
        R.data[31] = 0;
        R.data[38] = 0;
        R.data[45] = 0;
        R.data[4] = a15 = (a15)/a11;
        R.data[11] = a25 = (a25-a12*a15)/a22;
        R.data[18] = a35 = (a35-a13*a15-a23*a25)/a33;
        R.data[25] = a45 = (a45-a14*a15-a24*a25-a34*a35)/a44;
        R.data[32] = a55 = Math.sqrt(a55-a15*a15-a25*a25-a35*a35-a45*a45);
        R.data[39] = 0;
        R.data[46] = 0;
        R.data[5] = a16 = (a16)/a11;
        R.data[12] = a26 = (a26-a12*a16)/a22;
        R.data[19] = a36 = (a36-a13*a16-a23*a26)/a33;
        R.data[26] = a46 = (a46-a14*a16-a24*a26-a34*a36)/a44;
        R.data[33] = a56 = (a56-a15*a16-a25*a26-a35*a36-a45*a46)/a55;
        R.data[40] = a66 = Math.sqrt(a66-a16*a16-a26*a26-a36*a36-a46*a46-a56*a56);
        R.data[47] = 0;
        R.data[6] = a17 = (a17)/a11;
        R.data[13] = a27 = (a27-a12*a17)/a22;
        R.data[20] = a37 = (a37-a13*a17-a23*a27)/a33;
        R.data[27] = a47 = (a47-a14*a17-a24*a27-a34*a37)/a44;
        R.data[34] = a57 = (a57-a15*a17-a25*a27-a35*a37-a45*a47)/a55;
        R.data[41] = a67 = (a67-a16*a17-a26*a27-a36*a37-a46*a47-a56*a57)/a66;
        R.data[48]  = Math.sqrt(a77-a17*a17-a27*a27-a37*a37-a47*a47-a57*a57-a67*a67);
        return !UtilEjml.isUncountable(R.data[48]);
    }

}
