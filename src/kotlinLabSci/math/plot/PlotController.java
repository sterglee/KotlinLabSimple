package kotlinLabSci.math.plot;
import java.awt.Color;

// a class to plot in an object-oriented way
public class PlotController  {

    //  define the possible line types
    public enum  LineTypes {
        ContinuousLine, DottedLine, RoundDottedLine, CrossDotLine, ThickLine, PatternLine
    };
    
    // keeps track of the line type of the controller context
    LineTypes lineType = LineTypes.ContinuousLine;  
    
// these routines affect AbstractDrawer properties and can be used to change the defaults 
// for external plotting routines that do not specify their own values
       // set drawing to continuous lines
    
    // resets the default settings of the graphics context
   public void resetDefaultSettings()   {
       setContinuousLine();
       lineType = LineTypes.ContinuousLine;
       setlineWidth(1);
       setColor(Color.RED);
       setxlabel("X-axis");
       setylabel("Y-axis");
       setzlabel("Z-axis");
       }
   
 // this routine is useful when we have a particular PlotController context that we want to
// use for some plot operations  
   public void  passControllerContextToPlotContext()  {
       switch (lineType)  {
         case  ContinuousLine:  setContinuousLine(); break;
         case  DottedLine:  setDottedLine(); break;
         case  RoundDottedLine:   setRoundDotLine();  break;
         case  CrossDotLine:  setCrossDotLine();  break;
         case  ThickLine:  setThickLine();   break;
         case  PatternLine:  setPatternLine(); break;
         default:  setContinuousLine();
     }
   }
   

       
    public void  setContinuousLine()  {
        kotlinLabSci.math.plot.render.AbstractDrawer.line_type = kotlinLabSci.math.plot.render.AbstractDrawer.CONTINOUS_LINE;
        lineType = LineTypes.ContinuousLine;
    }
       // set drawing to dotted lines
    public void  setDottedLine()  { 
        kotlinLabSci.math.plot.render.AbstractDrawer.line_type = kotlinLabSci.math.plot.render.AbstractDrawer.DOTTED_LINE;
        lineType =  LineTypes.DottedLine;
    }
       // set drawing to Round Dot
    public void  setRoundDotLine()  { 
        kotlinLabSci.math.plot.render.AbstractDrawer.dot_type = kotlinLabSci.math.plot.render.AbstractDrawer.ROUND_DOT;
        lineType = LineTypes.RoundDottedLine;
    }
       // set drawing to cross dot
    public void  setCrossDotLine()  { 
        kotlinLabSci.math.plot.render.AbstractDrawer.dot_type = kotlinLabSci.math.plot.render.AbstractDrawer.CROSS_DOT;
        lineType = LineTypes.CrossDotLine;
    }
    // set drawing to thick line
    public void setThickLine() {
        kotlinLabSci.math.plot.render.AbstractDrawer.line_type = kotlinLabSci.math.plot.render.AbstractDrawer.THICK_LINE;
        lineType = LineTypes.ThickLine;
    }
        // set drawing to pattern line
    public void setPatternLine() {
        kotlinLabSci.math.plot.render.AbstractDrawer.line_type = kotlinLabSci.math.plot.render.AbstractDrawer.PATTERN_LINE;
        lineType = LineTypes.PatternLine;
    }

    
       // set font
    public void  setFont(java.awt.Font font)  { kotlinLabSci.math.plot.PlotGlobals.defaultAbstractDrawerFont = font; }
       // set Color
    public void  setColor(java.awt.Color  color)   { kotlinLabSci.math.plot.PlotGlobals.defaultAbstractDrawerColor = color; }
       // set line width
    public void  setlineWidth(int lw)   { kotlinLabSci.math.plot.render.AbstractDrawer.line_width = lw; }
    
      // references to the data for plot
    double []  xData = new double [1];
    double []  yData = new  double [1];
    double []  zData = new double [1]; 
        
      // adjust the data references for plotting to the actual data   
    public void   setX( double [] x )  { xData = x; }  // sets the x-data for plotting
    public void   setY( double [] y )  { yData = y; }  // sets the y-data for plotting
    public void   setZ( double [] z )   { zData = z;  }  // sets the z-data for plotting
    
    public void  setX(kotlinLabSci.math.array.Vec  x)  { xData = x.getv(); }  // sets the x-data using Vec
    public void  setY(kotlinLabSci.math.array.Vec  y)  { yData = y.getv(); }  // sets the y-data using Vec
    public void  setZ(kotlinLabSci.math.array.Vec  z)  { zData = z.getv(); }  // sets the z-data using Vec
    
    public void  setX(kotlinLabSci.math.array.Matrix  x)  { xData = x.getv(); }  // sets the x-data using Matrix
    public void  setY(kotlinLabSci.math.array.Matrix  y)  { yData = y.getv(); }  // sets the y-data using Matrix
    public void  setZ(kotlinLabSci.math.array.Matrix  z)  { zData = z.getv(); }  // sets the z-data using Matrix
    
    String  xlabelStr = "X-axis";
    public void  setxlabel( String  xl )  {  xlabelStr = xl; }
    
    String   ylabelStr = "Y-axis";
    public void  setylabel( String yl )  { ylabelStr = yl; }
    
    String  zlabelStr = "Z-axis";
    public void  setzlabel( String zl )  {  zlabelStr = zl; }
    
    String   plotTitle2D = "2-D Plot";
    public void   setplotTitle2D(String  plTitle)  { plotTitle2D = plTitle; }
    String   plotTitle3D = "3-D Plot";
    public void   setplotTitle3D(String  plTitle)  { plotTitle3D = plTitle; }
   
       
    // perform the plot using the object's properties
    public void mkplot()  { 
        kotlinLabSci.math.plot.plot.plot(xData, yData, kotlinLabSci.math.plot.PlotGlobals.defaultAbstractDrawerColor, plotTitle2D);
        kotlinLabSci.math.plot.plot.xlabel(xlabelStr);
        kotlinLabSci.math.plot.plot.ylabel(ylabelStr);
        
       }
 
 // perform the plot using the object's properties
    public void  mkplot3D()  { 
        
        kotlinLabSci.math.plot.plot.plot(xData, yData, zData, kotlinLabSci.math.plot.PlotGlobals.defaultAbstractDrawerColor,  plotTitle3D);
        kotlinLabSci.math.plot.plot.xlabel(xlabelStr);
        kotlinLabSci.math.plot.plot.ylabel(ylabelStr);
        kotlinLabSci.math.plot.plot.zlabel(zlabelStr);
       }
 }
    
