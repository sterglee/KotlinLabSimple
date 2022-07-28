# KotlinLab: Easy and effective MATLAB-like scientific programming with Kotlin 


## License:  MIT License


## Installation

The installation of KotlinLab is very simple: 

  * *Step 1* Download and unzip the .zip download.
  
  * *Step 2* Run the proper script for your platform, i.e. the .sh scripts for Unix like systems and the .bat scripts for Windows. 


## Building 

KotlinLab can be built with the superb JetBrains IntelliJ IDEA environment.

One important point however for successfully building the project,
is that from the "Settings" -> "Build, Execution, Deployment" -> "Annotation Processors",
we should disable the option "Enable annotation processing".


## Project Summary

The KotlinLab environment aims to provide a MATLAB/Scilab like scientific computing platform that is supported by a scripting engine of the Kotlin language. The KotlinLab user can work either with a MATLAB-like command console, or with a flexible editor based on the rsyntaxarea  component, that offers more convenient code development. KotlinLab supports extensive plotting facilities and can exploit effectively a lot of powerful Java scientific libraries, as JLAPACK , Apache Common Maths , EJML , MTJ , NUMAL translation to Java , Numerical Recipes Java translation , Colt etc. 

KotlinLab aims to explore the superb DSL construction facilities of the Kotlin Language and the JShell environment of Java to provide an effective scientific programming environment for the JVM.


## Basic Keystrokes of KotlinLab

To execute the current line or a piece of selected Kotlin code in KotlinLab use the F6 key.

To clear the output use the F5 key.


## Examples

## Plotting

KotlinLab supports MATLAB style routines for plotting, implemented on top of the jmathplot system: 
https://github.com/yannrichet/jmathplot

The class  kotlinLabSci.math.plot.plot implemented in Java provides the MATLAB-like interface as static methods. Some of these methods are:

```
static public static void figure( int  figno)

static public static void subplot( int  p)

static public void title(String titleStr)

static public void xlabel(String xLabelStr)

static public void ylabel(String yLabelStr)

static public void zlabel(String zLabelStr) 

static public PlotPanel plot(double [] x, double [] y, double [] z, int low, int high, Color color, String

 static public PlotPanel splinePlot( double [] xvals, double [] yvals, int NP, Color color, String name)
 
      
```

Examples of simple plots:

```

val N=3000
val axis = linspace(0.0, 10.0, N)
val x1 = 2.3*sin(4.5*axis)
val x2 = -4.5*cos(axis*7.8)+67.8*sin(16.7*axis)
val x3 = x1-7.8*x2+0.7*sin(3.4*axis)

figure(1)
subplot(3,1,1); plot(axis, x1); title("First signal x1")
subplot(3,1,2); plot(axis, x2); title("Second signal x2")
subplot(3,1,3); plot(axis, x3); title("Third signal x3")

```

## Fast matrix multiplication using native openBLAS

KotlinLab performs matrix multiplication by exploiting fast native routines from the OpenBLAS project. For example



```
var N=4000

var A = rand(N,N)    // create a large random matrix in 2-D storage representation, i.e. as Java's 2-D arrays

tic()   // start timer
var AA = A*A  // multiply 
var tm=toc()
println("performing native multiplication in $tm secs")


```

To execute the code, paste it in KotlinLab's editor, then select the code block, and press F6 (i.e. the function key F6).


We can obtain faster times using the 1-D row-major  matrix representation of the Mat1D KotlinLab class,
that exploits the more efficient 1-D row-major representation of the matrix:


```
var N=4000

var A = rand1d(N,N)    // create a large random matrix in 1-D row-major storage representation
tic()   // start timer
var AA = A*A  // multiply using native OpenBLAS
var tm=toc()
println("performing native multiplication in $tm secs")

```

## Matrix operations using the efficient 1-D row major representation

The class kotlinLabSci.math.array.Mat1D implements matrices using the efficient, cache friendly 1-D storage representation. The corresponding companion object implements the static operations that are imported automatically and are thus available as top-level functions to the KotlinLab user.
Some of such operations are:

```
 fun size(M: Mat1D): IntArray  
 fun fill1d(nrows: Int, ncols: Int, value: Double): Mat1D 
 fun rand1d(nrows: Int, ncols: Int): Mat1D 
 fun ones1d(nrows: Int, ncols: Int): Mat1D 
 fun zeros1d(nrows: Int, ncols: Int): Mat1D 
 ```
 
 
 
## Matrix operations using the  2-D storage representation (i.e. Java's 2-D array)

The class kotlinLabSci.math.array.Matrix implements matrices using the 2-D Java's array representation. Since the class is implemented in Java the corresponding imported functions in KotlinLab are implemented as static methods.

Some of such operations are:

```
public static Matrix read(File f)
public static Matrix read(String filename) 
public static void write(File f, Matrix M)
public static void write(String filename,Matrix M) 
public  static void mapf(Matrix M, computeFunction c)
public static void resize(Matrix M, int m, int n)
public static Matrix find(Matrix M)

 public  static Matrix sum(Matrix M)
 
 public static Matrix sin(Matrix M)  // similarly all trigonometric i.e. cos, tan, atan etc
 public  static Matrix rand(int m,int  n)
 
public static Matrix inc(double begin, double pitch, double end)
public static Matrix  linspace(double startv,  double endv, int nP)
public static Matrix  logspace(double  startOrig,  double endOrig,  int nP)

 
 ```              
 
 
## Using JFreeChart based plots

The following code demonstrates using the JFreeChart based plotting routines. 
To execute the example, select the code, and press F6.


```
jfigure(1)

var t = inc(0.0, 0.01, 10.0)


var x = sin(t)

var lineSpecs="."

jplot(t,x, lineSpecs)

println(t*8.8)

jfigure(2)

jsubplot(222)

var x11 = sin(t*4.56)

jplot(t,x11)

jhold(true)

lineSpecs = ":g"

jplot(t,sin(5.0 * x11 ), lineSpecs)

jsubplot(223)

lineSpecs = ":r"

jplot(t,x11, lineSpecs)



// create a new figure and preform a plot at subplot 3,2,1

  jfigure(3)
  
 jsubplot(3,2,1)
 
 var t2 = inc(0.0, 0.01, 10.0);  
 
 var x2 = sin(4.56 *t2 )+cos(t2 * 8.7)
 
 jplot(t2,x2, ".-")
 
jsubplot(3,2,3)

var x3 = cos(t2 * 7.8)+sin(8.9 * t2) * 6.7

jplot(t2, x3)

jlineColor(1, Color.RED)

jsubplot(3,2,5)

var x4 = cos(t2 * 7.66)+sin(3.23 *t2 )*8.665

jplot(t2, x4+x3)

jlineColor(1, Color.GREEN)

jsubplot(3,2,6)

jplot(t2, x4 * 8.77+x3)

jtitle("x4*8.77+x3")

jlineColor(1, Color.BLUE)


```


## Ikeda Chaotic map



The following code generates and plots the Ikeda chaotic map.



```

// the Ikeda map

var c1 = 0.4

var c2 = 0.9

var c3 = 9.0

var rho = 0.85

var x = 0.5

var y = 0.5

var niters = 200000

var xall = DoubleArray(niters)

var yall = DoubleArray(niters)

tic()

for ( k in 0..niters-1 )  {

  var xp = x; var yp=y
  
  var taut = c1-c3/(1.0 + xp * xp+yp * yp)
  
  x = rho + c2 * xp * cos(taut)-yp * sin(taut)
  
  y = c2*(xp * sin(taut)+yp * cos(taut))
  
  xall[k] = x
  
  yall[k] = y
  
}

var tm = toc()

scatterPlotsOn()

figure(1)

plot(xall, yall, "time = "+tm)

```










## KotlinLab Developer

Stergios Papadimitriou

International Hellenic University

Dept of Informatics 

Kavala, Greece

email: sterg@cs.ihu.gr
