// helps to perform conveniently imports for basic application types
package kotlinLabEdit;

import kotlinLabGlobal.Interpreter.GlobalValues;
  // Java standard UI and graphics support

public class importsHelper {
    

 public static void  injectJavaSwing()   {
      String  javaSwingStr = "import  java.awt.* ; \n"+ 
    "import  java.awt.event.*;\n"+
    "import  javax.swing.*; \n"+
    "import  javax.swing.event.*; \n";
    
    GlobalValues.globalEditorPane.setText(javaSwingStr + GlobalValues.globalEditorPane.getText());
    }
 
 public static void  injectApacheCommonMaths()   {
    }
 
 public static void injectkotlinLabSciImports() {
    String allImportsStr =  "import  kotlinLabSci.math.array.BasicDSP.*; \n"+
                "import kotlinLabSci.math.array.Vec; \n"+  // Vector class
                    "import  kotlinLabSci.math.array.Vec.*; \n"+
                    "import kotlinLabSci.math.array.Matrix; \n"+  // Matrix class
                    "import  kotlinLabSci.math.array.Matrix.*; \n"+
                    "import kotlinLabSci.math.array.CCMatrix; \n"+  // Matrix class
                    "import kotlinLabSci.math.array.CCMatrix.*; \n"+
                    "import kotlinLabSci.math.array.Sparse; \n"+  // Sparse Matrix class
                    "import kotlinLabSci.math.array.Sparse.*; \n"+
                    "import kotlinLabSci.math.array.JILapack; \n"+  // JLapack Matrix class
                    "import kotlinLabSci.math.array.JILapack.*; \n"+
                    "import Jama.*;\n"+
                    "import numal.*;\n"+    // numerical analysis library routines
                   "import  kotlinLabSci.math.plot.plot.*;\n"+     // plotting routines
                
                   "import java.awt.*; \n"+
                   "import javax.swing.*; \n"+   // Java standard UI and graphics support
                   "import java.awt.event.*; \n"+ 
                   "import java.text.DecimalFormat; \n"+
                   "import kotlinLabSciCommands.BasicCommands.*;\n"+  // support for KotlinSci's console commands
                   "import  kotlinLabSci.math.array.DoubleArray.*;\n"+
                  " import JSci.maths.*; \n"+
                  "import JSci.maths.wavelet.*; \n"+
                  "import JSci.maths.wavelet.daubechies2.*; \n"+
                  "import kotlinLabSci.math.array.*; \n"+
                   "import kotlinLabSci.FFT.ApacheFFT;  \n"+
                    "import  kotlinLabSci.FFT.ApacheFFT.*; \n"+
                "import NR.*; \n"+
                "import NR.gaussj.*;  \n"+
                "import com.nr.sp.*; \n"+
                "import java.lang.Math.*;  \n";    // standard Java math routines, allows calling directly e.g sin(9.8) instead of Math.sin(9.8)
                
    GlobalValues.globalEditorPane.setText(allImportsStr + GlobalValues.globalEditorPane.getText());
 }

 public static void injectBasicPlotsImports() {
    String basicPlotsImportsStr =  "import  kotlinLabSci.math.plot.plot.*;\n"+     // plotting routines
                  "import java.awt.*; \n"+
                   "import javax.swing.*; \n";   // Java standard UI and graphics support
                   
    GlobalValues.globalEditorPane.setText(basicPlotsImportsStr  + GlobalValues.globalEditorPane.getText());
 }

  




}
