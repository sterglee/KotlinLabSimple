
package kotlinLabExec.kotlinLab;


import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.net.URLDecoder;

import kotlinLabGlobals.JavaGlobals;

public class detectClassPaths {
     

    public static URL  jarPathOfClass(String className) {
        try {
            return Class.forName(className).getProtectionDomain().getCodeSource().getLocation();
        } catch (ClassNotFoundException ex) {
           System.out.println("error in jarPathOfClass"+className+")");
           ex.printStackTrace();
           return null;
        }
}
    
      public static String decodeFileName(String fileName) {
        String decodedFile = fileName;

        try {
            decodedFile = URLDecoder.decode(fileName, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            System.err.println("Encounted an invalid encoding scheme when trying to use URLDecoder.decode().");
            System.err.println("Please note that if you encounter this error and you have spaces in your directory you will run into issues. ");
        }

        return decodedFile;
    }
      
    // detects the .jar files of all the main kotlinLab's libraries
      public static void detectClassPaths()
    {
        boolean      hostIsUnix = true;
         if (File.pathSeparatorChar==';')
              hostIsUnix = false;  // Windows host
          
             if (hostIsUnix) {    
        JavaGlobals.jarFilePath = jarPathOfClass("kotlinLabExec.kotlinLab.kotlinLab").toString().replace("file:/", "/");   // kotlinLab's main .jar
        JavaGlobals.LAPACKFile = jarPathOfClass("org.netlib.lapack.LAPACK").toString().replace("file:/",  "/");   // JLAPACK
        JavaGlobals.ARPACKFile = jarPathOfClass("org.netlib.lapack.Dgels").toString().replace("file:/",  "/");    // ARPACK
        JavaGlobals.javacFile = jarPathOfClass("com.sun.tools.javac.Main").toString().replace("file:/", "/");    // Java compiler
        JavaGlobals.JASFile = jarPathOfClass("org.scilab.forge.jlatexmath.Atom").toString().replace("file:/", "/");  // Java Algebra System
        JavaGlobals.jsciFile = jarPathOfClass("JSci.maths.wavelet.Cascades").toString().replace("file:/", "/");   // JSci file
        JavaGlobals.mtjColtSGTFile = jarPathOfClass("no.uib.cipr.matrix.AbstractMatrix").toString().replace("file:/", "/");  // Matrix Toolkits for Java, Colt and Scientific Graphics Toolbox
        JavaGlobals.ejmlFile = jarPathOfClass("org.ejml.EjmlParameters").toString().replace("file:/", "/");   // EJML
        JavaGlobals.jblasFile = jarPathOfClass("org.jblas.NativeBlas").toString().replace("file:/", "/");   // JBLAS
        JavaGlobals.numalFile = jarPathOfClass("numal.Linear_algebra").toString().replace("file:/","/");   // NUMAL and Numerical Recipes routines
        JavaGlobals.ApacheCommonsFile = jarPathOfClass("org.apache.commons.math3.ode.nonstiff.ThreeEighthesIntegrator").toString().replace("file:/", "/");  // Apache Common Maths
        JavaGlobals.kotlinstdlibFile = jarPathOfClass("kotlin.collections.AbstractList").toString().replace("file:/", "/");  // Apache Common Maths
             }
         else {
        JavaGlobals.jarFilePath = jarPathOfClass("kotlinLabExec.kotlinLab.kotlinLab").toString().replace("file:/", "");

        JavaGlobals.LAPACKFile = jarPathOfClass("org.netlib.lapack.LAPACK").toString().replace("file:/",  "");
        JavaGlobals.ARPACKFile = jarPathOfClass("org.netlib.lapack.Dgels").toString().replace("file:/",  "");
        JavaGlobals.javacFile = jarPathOfClass("com.sun.tools.javac.Main").toString().replace("file:/", "");
        JavaGlobals.JASFile = jarPathOfClass("org.scilab.forge.jlatexmath.Atom").toString().replace("file:/", "");
        JavaGlobals.jsciFile = jarPathOfClass("JSci.maths.wavelet.Cascades").toString().replace("file:/", "");
        JavaGlobals.mtjColtSGTFile = jarPathOfClass("no.uib.cipr.matrix.AbstractMatrix").toString().replace("file:/", "");
        JavaGlobals.ejmlFile = jarPathOfClass("org.ejml.EjmlParameters").toString().replace("file:/", "");
        JavaGlobals.jblasFile = jarPathOfClass("org.jblas.NativeBlas").toString().replace("file:/", "");
        JavaGlobals.numalFile = jarPathOfClass("numal.Linear_algebra").toString().replace("file:/","");
        JavaGlobals.ApacheCommonsFile = jarPathOfClass("org.apache.commons.math3.ode.nonstiff.ThreeEighthesIntegrator").toString().replace("file:/", "");
        JavaGlobals.kotlinstdlibFile = jarPathOfClass("kotlin.collections.AbstractList").toString().replace("file:/", "");  // Apache Common Maths

             }   

        
             
             
    }
                  
}
