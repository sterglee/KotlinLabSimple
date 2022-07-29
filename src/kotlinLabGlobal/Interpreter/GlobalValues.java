



package kotlinLabGlobal.Interpreter;


import edu.emory.mathcs.utils.ConcurrencyUtils;
import kotlin.KotlinVersion;
import kotlinLabEdit.kotlinLabEditor;
import kotlinLabExec.kotlinLab.kotlinLab;
import kotlinLabExec.kotlinLab.kotlinLabUtils;
import kotlinLabSci.math.array.Matrix;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;

import javax.script.*;
import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.URL;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.List;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public   class GlobalValues
{  
    
   static public   String  dateOfBuild = "29-July-22";       // tracks the date of build

    static public ScriptEngineManager manager;  // used for RS223 Kotlin scripting
    static public ScriptEngine  kotlinEngine;   // used for RS223 Kotlin scripting

    static public Bindings   globalBindings = new SimpleBindings();
    static public Bindings  engineBindings = new SimpleBindings();
    static public java.awt.Cursor waitCursor =new java.awt.Cursor(java.awt.Cursor.WAIT_CURSOR);
    static public java.awt.Cursor defaultCursor = new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR);

    // codes for kotlinLab server computations
    static public final int exitCode = -1;   // code for server to exit
    static public final int svdCode = 1;   // code to perform an SVD computation

    static public String serverIP = "127.0.0.1";  // the IP address of the server
    static public int kotlinLabServerPort = 8000;   // port on which kotlinLab server is listening
    public static int consoleCharsPerLine;


    static public int mxLenToDisplay = 300; // controls how much chars to output, i.e. for a large array
         // the executor service is used to exploit Java multithreading for asynchronous computation operations and for
    // other tasks, as e.g. matrix multiplication 
    static public ExecutorService execService =   Executors.newFixedThreadPool(ConcurrencyUtils.getNumberOfProcessors());

    // variables for multithreaded operations
    static public   int multithreadingOpLimit  = 1000;  // a matrix with more elements than this is conidered large, thus use multithreading
     // a multiplication that involves more than that number of elements, is large thus use multithreading
    // if this parameter is not specified by a property, it is computed dynamically at run time
    static public   int mulMultithreadingLimit = -1; 
    static public String  mulMultithreadingLimitProp="undefined";
      // the pendingThreads class allows to cancel task started with the Shift-F6 keystroke
     // however, cancelling Java threads that are not designed for interruption, is an involved and problematic issue
    static public PendingThreads pendingThreads = new PendingThreads();
    
    static public   String  jdkTarget = "1.12";

 // variables for implementing code completion using Java reflection
   static public JFrame completionFrame;  // the open completion Frame, it is used in order to dispose it with ESC
   static public String  textForCompletion;  // replace this text with the user selection at completion
   static public int selectionStart;
   static public int selectionEnd;
   static public boolean methodNameSpecified = false;   // method name specified at completion
   static public boolean performPackageCompletion = false;
      
   // at code completion (with F12) static members are denoted in bold font
   static public Font staticsFont = new Font("Arial", Font.BOLD, 11);
   // at code completion (with F12) instance members are denoted in plain font text
   static public Font instancesFont = new Font("Arial", Font.PLAIN, 11);
   static public Font fontForCompletionListItem = instancesFont; // font to use for rendering the current item
   static public String staticsMarker = "#####";
   static public String nonStaticsMarker = "%%%%%";
   static public boolean [] isStaticMarks;
   static public int maxItemsToDisplayAtCompletions = 15;
  
   static public RTextScrollPane   bufferedImportsScrollPane;
    
   static public boolean  rememberSizesFlag = true;   // remembers the configured sizes for the frames
   static public boolean useAlwaysDefaultImports = true;

  
   static public Desktop  desktop;
   static public boolean useSystemBrowserForHelp = true;

   static public String  userPathsFileName = "kotlinLabUserPaths.txt";
   static public  JScrollPane  outputPane;     // the System Console output scroll pane

   static public RSyntaxTextArea   globalEditorPane;   // the rsyntaxarea component based kotlinLab editor

   static public String  smallNameFullPackageSeparator = "-->";  
          
                 
   static  public String paneFontName = "Times New Roman";
   static  public int paneFontSize = 16;
   static  public boolean paneFontSpecified = true;
        
        // for main menus
   static  public String uiFontName = "Times New Roman";
   static  public String  uiFontSize = "14";
   static public Font uifont = new Font(uiFontName, Font.PLAIN, Integer.parseInt(uiFontSize));
    
        // for popup menus
   static  public String puiFontName = "Times New Roman";
   static  public String  puiFontSize = "14";
   static public Font puifont = new Font(puiFontName, Font.PLAIN, Integer.parseInt(puiFontSize));
    
        // for rest gui drawing
    static  public String guiFontName = "Times New Roman";
    static  public String  guiFontSize = "14";
    static public Font guifont = new Font(guiFontName, Font.PLAIN, Integer.parseInt(guiFontSize));
    
        // for buttons drawing
        static  public String buiFontName = "Times New Roman";
        static  public String  buiFontSize = "14";
        static public Font buifont = new Font(guiFontName, Font.PLAIN, Integer.parseInt(buiFontSize));
    
        // for Help html
        static  public String htmlFontName = "Times New Roman";
        static  public String htmlFontSize = "16";
        static public Font htmlfont = new Font(htmlFontName, Font.PLAIN, Integer.parseInt(htmlFontSize));

        
   static public boolean pathUpdateOnSaves = false;
     // Defaults for Main Help
    
    static public  String toolboxStartUpcode = "";
    static public String selectedExplorerPath; 

    static public double log2Conv = Math.log(2.0);   
    
    static public String currentExpression;   // holds the text of the current expression in execution
    
    public static SysUtils.ConsoleWindow  consoleOutputWindow;
     
    public static Dimension ScreenDim;

    
    static public boolean truncateOutput  = false;   // displays all the output results from the kotlinLab interpreter without truncating
    static public boolean globalVerboseOff = false;
    static public boolean displayAtOutputWindow = false;   // controls displaying at output window
    static public int maxNumsOutputToString = 1000;  // restricts output of toString()
        
        static public   DecimalFormat fmtString = new  DecimalFormat("0.0000");
        static public   DecimalFormat fmtMatrix = new DecimalFormat("0.000"); // format Matrix results
        static public   int  doubleFormatLen = 4; // how many digits to display for doubles, Matrix
        // these colors of the console input window  indicate the corresponding kotlinLab operating modes
        static public  Color  ColorkotlinLabSci = new Color(255, 255, 255);
        static public Map desktophints;
    
        static public boolean effectsEnabled = false;
        static public boolean mainToolbarVisible = true;
        static public float alphaComposite = 0.5f;

         /**Constant with the application title.*/
        static public String TITLE=buildTitle();

        static public long  timeForTic; // save the current time in milliseconds to implement tic-toc functionality
        
        static public  java.util.LinkedList<String>  kotlinLabShellPathsList = null;  // the current paths list with which the Kotlin's classloader is inited
        static public boolean retrieveAlsoMethods = false; // retrieve also declared methods from toolboxes
         static public Vector bindingVarValues = new Vector();  // bindings for scripting interfacing
        static public Vector bindingVarNames = new Vector();
        static public int bindingCnt = 0; // count of binded variables


    static public String [] kotlinGlobalImports={
            "import kotlinLabSci.math.array.Vec; ",  // Vector class

            "import  kotlinLabSci.math.array.Vec.*; ",

            "import kotlinLabSci.math.array.Matrix; ",  // Matrix class

            "import  kotlinLabSci.math.array.Matrix.*; ",
            "import kotlinLabSci.math.array.Mat1D; ",  // Matrix class
            "import kotlinLabSci.math.array.Mat1D.*; ",  // Matrix class

            "import    kotlinLabSci.math.array.DoubleArray.*;  ",

            "import kotlinLabSci.math.array.CCMatrix; ",  // Matrix class
            "import kotlinLabSci.math.array.CCMatrix.*; ",
            "import kotlinLabSci.math.array.Sparse; ",  // Sparse Matrix class
            "import kotlinLabSci.math.array.Sparse.*; ",
            "import kotlinLabSci.math.array.JILapack; ",  // JLapack Matrix class
            "import kotlinLabSci.math.array.JILapack.*; ",
            "import Jama.*;",
            "import kotlinLabSci.math.plot.plot.*;  ",     // plotting routines
            "import kotlinLabSci.math.plot.kplot.Companion.kplot; ",
            "import kotlinLabSciCommands.BasicCommands.*;   ",

            "import kotlinLabSci.math.array.MatrixConvs.*;   ",

            "import org.jblas.*; ",

            // JFreeChart imports
            "import JFreePlot.*;   ",
            "import JFreePlot.jFigure.*;   ",
            "import  JFreePlot.jPlot.*;  ",

            "import java.awt.*;   ",
            "import javax.swing.*;   ",   // Java standard UI and graphics support
            "import java.util.function.Function; ",
            "import java.util.function.*;",
            "import java.util.*;",
            "import java.util.concurrent.*;",
            "import  java.util.Arrays.*;",
            "import java.util.stream.*;",
            "import java.util.stream.Collectors;",
            "import  java.util.stream.Collectors.*;",

            "import java.awt.event.*;   ",
            "import java.text.DecimalFormat;   ",

            "import kotlinLabSci.FFT.ApacheFFT;   ",
            "import  kotlinLabSci.FFT.ApacheFFT.*;  ",
            "import  kotlinLabSci.FFT.FFTCommon.*;  ",
            "import  java.util.List.*;",
            "import  java.util.Set.*;"


    };

         static public String[] additionalKotlinImports = {
                 "import kotlin.math.*",
                 "import kotlinLabSci.math.array.Vec.VecOps.vrand",
                 "import kotlinLabSci.math.array.Vec.VecOps.vones",
                 "import kotlinLabSci.math.array.Vec.VecOps.vzeros",
                 "import kotlinLabSci.math.array.Vec.VecOps.vfill",
                 "import kotlinLabSci.math.array.Vec.VecOps.vlinspace",
                 "import kotlinLabSci.math.array.Vec.VecOps.Vones",
                 "import kotlinLabSci.math.array.Vec.VecOps.Vzeros",
                 "import kotlinLabSci.math.array.Vec.VecOps.Vrand",
                 "import kotlinLabSci.math.array.Vec.VecOps.Vfill",
                 "import kotlinLabSci.math.array.Vec.VecOps.Vlinspace",
                 "import kotlinLabSci.math.array.Vec.VecOps.Vlogspace",
                 "import kotlinLabSci.math.array.Mat1D.Companion.fill1d",
                 "import kotlinLabSci.math.array.Mat1D.Companion.rand1d",
                 "import kotlinLabSci.math.array.Mat1D.Companion.ones1d",
                 "import kotlinLabSci.math.array.Mat1D.Companion.zeros1d",
                 "import kotlinLabSci.math.array.BasicDSP.Companion.sfft",
                 "import kotlinLabSci.math.array.BasicDSP.Companion.fftMag",

                 "import kotlinLabSci.math.array.ExtensionsVec.plus",
                 "import kotlinLabSci.math.array.ExtensionsVec.minus",
                 "import kotlinLabSci.math.array.ExtensionsVec.times",

                 "import kotlinLabSci.math.array.ExtensionsMatrix.plus",
                 "import kotlinLabSci.math.array.ExtensionsMatrix.minus",
                 "import kotlinLabSci.math.array.ExtensionsMatrix.times",

                 "import kotlinLabSci.math.array.ExtensionsMat1D.plus",
                 "import kotlinLabSci.math.array.ExtensionsMat1D.minus",
                 "import kotlinLabSci.math.array.ExtensionsMat1D.times",


                 "import kotlinx.coroutines.*",
                 "import java.util.*",
                  "import java.util.concurrent.*",
                  "import kotlin.concurrent.*",
        "import kotlin.annotation.*",
        "import kotlin.collections.*",
        "import kotlin.comparisons.*",
        "import kotlin.io.*",
        "import kotlin.ranges.*",
        "import kotlin.sequences.*",
        "import kotlin.text.*",
        "import java.lang.*",
        "import kotlin.jvm.*",
        "import kotlin.*"
         };

           // Console Configuration
        static String defaultFontName = "Times New Roman";
        static String  defaultFontSize = "16";
        static public Font   defaultTextFont; 
        
        static public String detailHelpStringSelected="";
     
        static public boolean hostIsUnix = File.pathSeparatorChar == ':'?  true  :  false;   // Unix like system or Windows?
        static public boolean hostIsWin = !hostIsUnix;
        static public boolean hostIsWin64 = hostIsWin && System.getProperty("os.arch").toLowerCase().contains("amd64");
        static public boolean hostIsLinux = System.getProperty("os.name").toLowerCase().contains("linux");
        static public boolean hostIsLinux64 = hostIsLinux && System.getProperty("os.arch").toLowerCase().contains("amd64");
        static public boolean hostIsFreeBSD = System.getProperty("os.name").toLowerCase().contains("freebsd");
        static public boolean hostIsSolaris = System.getProperty("os.name").toLowerCase().contains("sunos");
        static public boolean hostIsMac =    System.getProperty("os.name", "").toLowerCase().contains("mac");
      
        
        static public boolean hostNotWinNotLinux = ( (hostIsUnix==true)  &&  (hostIsLinux==false) ); // Unix-like OS, not Linux  e.g. FreeBSD, MacOS, Solaris etc.
        
        static public String jarFilePath;  // the path that contains the main jar file
        static public String fullJarFilePath;
        static public String kotlinLabLibPath="";

        static public String kotlinLabClassPath="";

        static public String kotlinLabSciClassPath;
        static public Vector kotlinLabSciClassPathComponents=new Vector();
        
        static public Vector  favouriteElements = new Vector();


        
        static public String kotlinLabPropertiesFile;  // the file for obtaining configuration properties
        static public String workingDir;  // the current working directory
        static public String homeDir;  // the user's home directory
        
        static public Vector jartoolboxes =new Vector();  // load Java classes for Groovy mode from these toolboxes 
        static public HashMap<String, Boolean>  jartoolboxesLoadedFlag;  // associates each jar toolbox name with a flag that indicates whether it was loaded or not
        static public int sizeX = 600;  // the kotlinLab's main console window jFrame size
        static public int sizeY = 400;  
        static public int locX = 100;   // location of kotlinLab's main window
        static public int locY = 100; 
        
        static public int rsizeX = 600;  // the kotlinLab's RSyntaxEditor jFrame size
        static public int rsizeY = 400;  
        static public int rlocX = 10;   // location 
        static public int rlocY = 10; 
        
        static public double figAreaRelSize = 0.9;  // the relative area of the figure plot area
        
    static public int threadCnt = 0;  // the number of threads created

    static public int maxNumberOfRecentFiles = 20;
    static public String kotlinLabRecentFilesList = "kotlinLabRecentFiles.txt";   // the file for storing list of recent files
    
  
    public static String [] loadedToolboxesNames;  // the names of the toolboxes
    public static int currentToolboxId = 0; 
    public static int maxNumOfToolboxes = 30;

    public static final double nearZeroValue = 1.0e-10;
    
      
    
    // Graphics Configuration
    public static int  maxPointsToPlot = 40;  // limit on the number of points to plot when in point plot mode
    public static int  plotPointWidth = 2;    // control the size of the point at the plots 
    public static int  plotPointHeight =2; 
    public static int  markLineSize = 5;
    public static int  figGridSizeX = 30; 
    public static int  figGridSizeY = 30;     
    public static int  figFrameSizeX = 800; 
    public static int  figFrameSizeY = 600;     
    public static int  limitForLargeRangeOfValues = 10;
    public static double figZoomScaleFactor = 0.5;
    public static int currentMaxNumberOfZooms = 5;

    // global variables that change dynamically during a working session
    static public String  kotlinLabCommandHistoryFile = "kotlinLabCommandHistory.log";
    static public String kotlinLabFavoritePathsFile = "kotlinLabFavoritePaths.log";
    static public int  numOfHistoryCommandsToKeep  = 10;  // size of the command history list
    static public Properties settings;  // for load/save global properties
    static public String selectedStringForAutoCompletion;
    public static int numTokTypes = 0;   // counts the number of token types that the lexical analyzer returns
    
    // keep main objects
    public static kotlinLab kotlinLabMainFrame = null;
        public static int xSizeTab;
    public static int ySizeTab;
    public static  kotlinLabEditor  myKEdit = null;
  
    public static Runtime rt;  // the runtime for observing available memory
    public static long memAvailable;  // free memory available
    public static double helpMagnificationFactor=1.0;
    public static File forHTMLHelptempFile;
   
    
     static public boolean displayLatexOnEval = true;
    static public int FONT_SIZE_TEX = 18;
  
      
 
  static public Socket sclient = null;     // client socket
  static public InputStream   clientReadStream = null;   // client's read stream
  static public OutputStream  clientWriteStream =  null;   // client's write stream 
  static public DataInputStream    reader = null;   
  static public DataOutputStream     writer = null;

    static public ServerSocket kotlinLabServerSocket;    // socket for kotlinLab's server


    static public String buildTitle() {
        String javaVersion = System.getProperty("java.vm.version");


      String mainFrameTitle =        "kotlinLab based on Kotlin "+ ", Kotlin version ="+ KotlinVersion.CURRENT+
                   ",   "+System.getProperty("java.vm.name", "").toLowerCase()+", " +javaVersion+
              ",  "+ System.getProperty("os.name", "").toLowerCase()+
                   "  "+ System.getProperty("os.arch", "").toLowerCase()+" ,   "+ "( "+ dateOfBuild+" ) ";
    return mainFrameTitle;
    }



    public static void initKotlinEngine() {


          // initialize the classpath using the CLASSPATH manifest entry of the jar file
        List<URL> clp = PathUtils.INSTANCE.classpathFromJar(new File(GlobalValues.jarFilePath));

        StringBuilder sb=new StringBuilder();
     clp.stream().forEach( x->
             {
                 var file = x.getPath();
                 System.out.println(file);
                 if (GlobalValues.hostIsUnix)
                  sb.append(file+":");
                 else  // Windows
                  sb.append(file+";");

             });

        String  extraJarsForKotlinClasspathFolder = GlobalValues.kotlinLabLibPath.replace("lib", "extraJarsForKotlinClasspath");

        File [] toolboxesFolderFiles = (new java.io.File(extraJarsForKotlinClasspathFolder)).listFiles();  // get the list of files at the default toolboxes folder
        if (toolboxesFolderFiles!=null) {  // extraJarsForKotlinClasspath folder not empty
            int numFiles = toolboxesFolderFiles.length;
            for (int f=0; f < numFiles;  f++) {
                String currentFileName = toolboxesFolderFiles[f].getAbsolutePath();

                if (currentFileName.endsWith(".jar")) {

                    if (GlobalValues.hostIsUnix)
                     sb.append(currentFileName+":");
                    else   // Windows
                        sb.append(currentFileName+";");
             }  // endsWith("jar")
            }   // for all files of then extraJarsForKotlinsFClasspath folder
        }   // extraJarsForKotlinClasspath folder not empty

        System.setProperty("kotlin.script.classpath", sb.toString());


         kotlinEngine = new ScriptEngineManager().getEngineByName("kotlin");
        if (kotlinEngine == null) {
            JOptionPane.showMessageDialog(null, "Kotlin engine is null!!");
            System.exit(1);
        }



         try {
            for (String str: GlobalValues.kotlinGlobalImports) {
                     System.out.println("evaluating: " + str);

                    kotlinEngine.eval(str);

                }

            for (String str : GlobalValues.additionalKotlinImports) {
                System.out.println("evaluating kotlin: " + str);

                kotlinEngine.eval(str);

            }

        }

        catch (ScriptException e)  {
            e.printStackTrace();
        }
    }


    public static void initGlobals()
    {


         new GlobalValues();    // it is required to init properly
         
         
         if (Desktop.isDesktopSupported())
             desktop = Desktop.getDesktop();
         else
             useSystemBrowserForHelp = false;  // cannot use system browser
         
         GlobalValues.fmtString.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
         
         Toolkit tk =Toolkit.getDefaultToolkit();
         desktophints = (Map)(tk.getDesktopProperty("awt.font.desktophints"));
         
         hostIsUnix = true;
         if (File.separatorChar!='/')
             hostIsUnix=false;
         
         myKEdit = null;
         loadedToolboxesNames = new String[maxNumOfToolboxes];
         
         jartoolboxes= new Vector();
         jartoolboxesLoadedFlag = new HashMap<String, Boolean>();
         
         GlobalValues.fmtMatrix.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
         GlobalValues.fmtString.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
         
         java.util.Map<String, String>  userEnv = System.getenv();
         
         int idx = GlobalValues.jarFilePath.lastIndexOf(File.separatorChar);
         if (idx==-1) {
             GlobalValues.homeDir = ".";
         }
         else
             GlobalValues.homeDir = GlobalValues.jarFilePath.substring(0, idx);
         
         System.out.println("homeDir= "+GlobalValues.homeDir);
         
         
         GlobalValues.workingDir = System.getProperty("user.dir");
         
         hostIsUnix = true;
         if (File.separatorChar!='/')
             hostIsUnix=false;
         
         myKEdit = null;
         loadedToolboxesNames = new String[maxNumOfToolboxes];
         
         boolean foundConfigFileFlag = false;   //exists configuration file?
         try
         {
             settings = new Properties();
             
             FileInputStream in = null;
             
             // the kotlinLab's configuration file
             String configFileName = workingDir+File.separatorChar+"kotlinLab.props";
             File configFile = new File(configFileName);
             if (configFile.exists())   {  // configuration file exists
                 in = new FileInputStream(configFile);
                 settings.load(in);   // load the settings
                 foundConfigFileFlag = true;
                 GlobalValues.kotlinLabPropertiesFile = configFileName;
             }
         }
         
         catch (IOException e)
         {
             e.printStackTrace();
         }
         ScreenDim = Toolkit.getDefaultToolkit().getScreenSize();
         
         if (foundConfigFileFlag == false)   { // configuration file not exists, thus pass default configuration
             
             rememberSizesFlag = false;   // since configured sizes for frames do not exist use the default sizes
             
             //position the frame in the centre of the screen
             int xSizeMainFrame = (int)((double)ScreenDim.width/1.4);
             int ySizeMainFrame = (int)((double)ScreenDim.height/1.4);
             GlobalValues.locX  = (int)((double)ScreenDim.width/10.0);
             GlobalValues.locY = (int)((double)ScreenDim.height/10.0);
             GlobalValues.rlocX  = 10;
             GlobalValues.rlocY = 10;
             
             GlobalValues.sizeX = xSizeMainFrame;
             GlobalValues.sizeY = ySizeMainFrame;
             
             GlobalValues.rsizeX = xSizeMainFrame;
             GlobalValues.rsizeY = ySizeMainFrame;

             if (GlobalValues.useAlwaysDefaultImports)
                 settings.put("useAlwaysDefaultImportsProp", "true");
             else
                 settings.put("useAlwaysDefaultImportsProp", "false");
             
             
             settings.put("widthProp",  String.valueOf(xSizeMainFrame));
             settings.put("heightProp", String.valueOf(ySizeMainFrame));
             settings.put("xlocProp",  String.valueOf(GlobalValues.locX));
             settings.put("ylocProp",  String.valueOf(GlobalValues.locY));
             
             settings.put("rwidthProp",  String.valueOf(xSizeMainFrame));
             settings.put("rheightProp", String.valueOf(ySizeMainFrame));
             settings.put("rxlocProp",  String.valueOf(GlobalValues.rlocX));
             settings.put("rylocProp",  String.valueOf(GlobalValues.rlocY));
             
             
             settings.setProperty("uiFontNameProp","Times New Roman");
             settings.setProperty("uiFontSizeProp", "14");
             settings.setProperty("outConsFontNameProp", "Lucida");
             settings.setProperty("outConsFontSizeProp", "14");
             
             String  prefix = "/";
             if (hostIsUnix==false)
                 prefix = "C:\\";
             
             String initialgLabPath  =prefix;
             
             String userDir = workingDir;
             
             
             settings.put("gLabWorkingDirProp", userDir);
             
         } // configuration file not exists
         
         
         GlobalValues.fmtMatrix.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
         GlobalValues.fmtString.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
         myKEdit = null;
         
         
    
        
        
    }   
    
    // Initialises the global values 
    public GlobalValues()
    {
                   
    }   

    
    
    public static void incrementToolboxCount() {
        currentToolboxId++;
        if (currentToolboxId > maxNumOfToolboxes)  { 
            JOptionPane.showMessageDialog(null, "Maximum toolbox count exceeded", "Cannot load additional toolboxes", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    // approximates the matrix size for which multithreading becomes faster than serial implementation
    public static int computeProperMatrixSizeThresholdForMultithreading() {
    
        int testedSize  = 50;  // starting value
        
        while (true)  {
            Matrix m = Matrix.rand(testedSize, testedSize);  // create a matrix to test the serial case vs the multithreaded case
            
            kotlinLabSciCommands.BasicCommands.tic();
            
            // benchmark serial multiplication
            Matrix mm = m.multiplyTest(m, false).multiplyTest(m, false).multiplyTest(m, false).multiplyTest(m, false);  // perform a multiplication in order to time it
            double delaySerial = kotlinLabSciCommands.BasicCommands.toc();
            
            // benchmark parallel multiplication
            kotlinLabSciCommands.BasicCommands.tic();
            Matrix mm2 = m.multiplyTest(m, true).multiplyTest(m, true).multiplyTest(m, true).multiplyTest(m, true);  // perform a multiplication in order to time it
            double delayParallel = kotlinLabSciCommands.BasicCommands.toc();
            
            if (delayParallel > delaySerial)   // increment to larger size
                testedSize += testedSize;
            else break;  // exit the loop
                  
        }
        
        return testedSize;  // return size about at which parallel implementation becomes faster
        
        
    } 
    
    
// pass properties readed from settings Property String to the kotlinLab workspace structures
    public static void passPropertiesFromSettingsToWorkspace(Properties settings)
     {
          
        String locXstr = settings.getProperty("xlocProp");
        if (locXstr != null)      locX = Integer.parseInt(locXstr);  // locX specified
        String locYstr = settings.getProperty("ylocProp");
        if (locYstr != null)      locY = Integer.parseInt(locYstr);  // locY specified
        
       String sizeXstr = settings.getProperty("widthProp");
        if (sizeXstr != null)      sizeX = Integer.parseInt(sizeXstr);  // sizeX specified
        String sizeYstr = settings.getProperty("heightProp");
        if (sizeYstr != null)      sizeY = Integer.parseInt(sizeYstr);  // sizeY specified
        
        // RSyntaxArea editor frame size
        String rlocXstr = settings.getProperty("rxlocProp");
        if (rlocXstr != null)      rlocX = Integer.parseInt(rlocXstr);  // rlocX specified
        String rlocYstr = settings.getProperty("rylocProp");
        if (rlocYstr != null)      rlocY = Integer.parseInt(rlocYstr);  // rlocY specified
        
       String rsizeXstr = settings.getProperty("rwidthProp");
        if (rsizeXstr != null)      rsizeX = Integer.parseInt(rsizeXstr);  // rsizeX specified
        String rsizeYstr = settings.getProperty("rheightProp");
        if (rsizeYstr != null)      rsizeY = Integer.parseInt(rsizeYstr);  // rsizeY specified
             
        // main menus
        String uiFontName = settings.getProperty("uiFontNameProp");
        if (uiFontName==null) uiFontName= GlobalValues.uiFontName;
        String uiFontSize = settings.getProperty("uiFontSizeProp");
        if (uiFontSize==null) uiFontSize= GlobalValues.uiFontSize;
        GlobalValues.uiFontName = uiFontName;
        GlobalValues.uiFontSize = uiFontSize;
        GlobalValues.uifont = new Font(GlobalValues.uiFontName, Font.PLAIN, Integer.parseInt(GlobalValues.uiFontSize));
                
          // pop-up menus
        String puiFontName = settings.getProperty("puiFontNameProp");
        if (puiFontName==null)  puiFontName= GlobalValues.puiFontName;
        String puiFontSize = settings.getProperty("puiFontSizeProp");
        if (puiFontSize==null) puiFontSize= GlobalValues.puiFontSize;
        GlobalValues.puiFontName = puiFontName;
        GlobalValues.puiFontSize = puiFontSize;
        GlobalValues.puifont = new Font(GlobalValues.puiFontName, Font.PLAIN, Integer.parseInt(GlobalValues.puiFontSize));
        
          // general GUI components
        String guiFontName = settings.getProperty("guiFontNameProp");
        if (guiFontName==null)  guiFontName= GlobalValues.guiFontName;
        String guiFontSize = settings.getProperty("guiFontSizeProp");
        if (guiFontSize==null) guiFontSize= GlobalValues.guiFontSize;
        GlobalValues.guiFontName = guiFontName;
        GlobalValues.guiFontSize = guiFontSize;
        GlobalValues.guifont = new Font(GlobalValues.guiFontName, Font.PLAIN, Integer.parseInt(GlobalValues.guiFontSize));
        
        
          // html components
        String htmlFontName = settings.getProperty("htmlFontNameProp");
        if (htmlFontName==null)  guiFontName= GlobalValues.htmlFontName;
        String htmlFontSize = settings.getProperty("htmlFontSizeProp");
        if (htmlFontSize==null) htmlFontSize= GlobalValues.htmlFontSize;
        GlobalValues.htmlFontName = htmlFontName;
        GlobalValues.htmlFontSize = htmlFontSize;
        GlobalValues.htmlfont = new Font(GlobalValues.htmlFontName, Font.PLAIN, Integer.parseInt(GlobalValues.htmlFontSize));
    
              // buttons components
        String buiFontName = settings.getProperty("buiFontNameProp");
        if (buiFontName==null)  buiFontName= GlobalValues.buiFontName;
        String buiFontSize = settings.getProperty("buiFontSizeProp");
        if (buiFontSize==null) buiFontSize= GlobalValues.buiFontSize;
        GlobalValues.buiFontName = buiFontName;
        GlobalValues.buiFontSize = buiFontSize;
        GlobalValues.buifont = new Font(GlobalValues.buiFontName, Font.PLAIN, Integer.parseInt(GlobalValues.buiFontSize));

        //  Decimal digit formatting properties
        String vecDigitsSetting = settings.getProperty("VecDigitsProp");
        if (vecDigitsSetting != null) {
            int vprec = Integer.parseInt(vecDigitsSetting);
            kotlinLabSci.PrintFormatParams.setVecDigitsPrecision(vprec);
          }
         
        String matDigitsSetting = settings.getProperty("MatDigitsProp");
        if (matDigitsSetting != null) {
            int mprec = Integer.parseInt(matDigitsSetting);
            kotlinLabSci.PrintFormatParams.setMatDigitsPrecision(mprec);
        }
         
         String mxRowsSetting = settings.getProperty("mxRowsProp");
         if (mxRowsSetting != null) {
            int mxrows = Integer.parseInt(mxRowsSetting);
            kotlinLabSci.PrintFormatParams.setMatMxRowsToDisplay(mxrows);
         }
        
         String mxColsSetting = settings.getProperty("mxColsProp");
         if (mxColsSetting != null)  {
            int mxcols = Integer.parseInt(mxColsSetting);
            kotlinLabSci.PrintFormatParams.setMatMxColsToDisplay(mxcols);
         }
         
         String verboseOutputSetting = settings.getProperty("verboseOutputProp");
         if (verboseOutputSetting!=null)
           if (verboseOutputSetting.equalsIgnoreCase("true"))
              kotlinLabSci.PrintFormatParams.setVerbose(true);
           else
              kotlinLabSci.PrintFormatParams.setVerbose(false);
        
          
        boolean paneFontSpecified = true;
        String paneFontName = settings.getProperty("paneFontNameProp");
        if (paneFontName!=null)  
             GlobalValues.paneFontName = paneFontName;
        else
            paneFontSpecified = false;
        String paneFontSize = settings.getProperty("paneFontSizeProp");
        if (paneFontSize!=null)   
            GlobalValues.paneFontSize =  Integer.valueOf(paneFontSize);
        else
            paneFontSpecified = false;
      
        GlobalValues.paneFontSpecified = paneFontSpecified;

    } 
    
 // updates the paths of targetVector by adding additionalPaths and any subdirectories
public static void updatePathVectors(Vector targetVector,  String additionalPaths, boolean recurse) {
    if (targetVector != null)  {
            StringTokenizer  tokenizer;
            if (hostIsUnix) tokenizer = new StringTokenizer(additionalPaths, "\n:\t ");
            else tokenizer = new StringTokenizer(additionalPaths,"\n;\t ");
            while (tokenizer.hasMoreTokens())  {  // construct full paths to search for j-files
                String nextToken = tokenizer.nextToken()+File.separatorChar;
                if (recurse == false)
                    targetVector.add(nextToken);
                else
                    kotlinLabUtils.appendAllSubDirectories(nextToken, targetVector);
          }
          
    }
}
    
// pass properties from the kotlinLab workspace structures to the settings Property String to
    public static void passPropertiesFromWorkspaceToSettings(Properties settings)
     {
         
     
         settings.setProperty("mulMultithreadingLimitProp", Integer.toString(GlobalValues.mulMultithreadingLimit));
         
        
        settings.setProperty("widthProp", String.valueOf(globalEditorPane.getSize().width));
        settings.setProperty("heightProp", String.valueOf(globalEditorPane.getSize().height));
        int xloc = globalEditorPane.getLocation().x;
        int yloc = globalEditorPane.getLocation().y;
        settings.setProperty("xlocProp", String.valueOf(xloc));
        settings.setProperty("ylocProp", String.valueOf(yloc));
        
        // RSyntaxArea editor
        settings.setProperty("rwidthProp", String.valueOf(kotlinLabEditor.currentFrame.getWidth()));
        settings.setProperty("rheightProp", String.valueOf(kotlinLabEditor.currentFrame.getHeight()));
        int rxloc = kotlinLabEditor.currentFrame.getLocation().x;
        int ryloc = kotlinLabEditor.currentFrame.getLocation().y;
        settings.setProperty("rxlocProp", String.valueOf(rxloc));
        settings.setProperty("rylocProp", String.valueOf(ryloc));
        
     settings.setProperty("paneFontNameProp", String.valueOf(GlobalValues.globalEditorPane.getFont().getName()));
     int paneFontSize =   GlobalValues.globalEditorPane.getFont().getSize();
     settings.setProperty("paneFontSizeProp", String.valueOf(paneFontSize)); 
        
     
        Font outConsFont =  GlobalValues.outputPane.getFont();
        settings.setProperty("outConsFontNameProp", outConsFont.getName());
        settings.setProperty("outConsFontSizeProp", String.valueOf(outConsFont.getSize()));
        
        // main menus
        settings.setProperty("uiFontNameProp", GlobalValues.uifont.getName());
        settings.setProperty("uiFontSizeProp", String.valueOf(GlobalValues.uifont.getSize()));
        
        // popup menus
        settings.setProperty("puiFontNameProp", GlobalValues.puifont.getName());
        settings.setProperty("puiFontSizeProp", String.valueOf(GlobalValues.puifont.getSize()));
        
        // html help
        settings.setProperty("htmlFontNameProp", GlobalValues.htmlfont.getName());
        settings.setProperty("htmlFontSizeProp", String.valueOf(GlobalValues.htmlfont.getSize()));
        
        // rest GUI components
        settings.setProperty("guiFontNameProp", GlobalValues.guifont.getName());
        settings.setProperty("guiFontSizeProp", String.valueOf(GlobalValues.guifont.getSize()));
        
        // GUI buttons
        settings.setProperty("buiFontNameProp", GlobalValues.buifont.getName());
        settings.setProperty("buiFontSizeProp", String.valueOf(GlobalValues.buifont.getSize()));
        
        settings.setProperty("outConsFontNameProp", String.valueOf(GlobalValues.consoleOutputWindow.output.getFont().getName()));
        settings.setProperty("outConsFontSizeProp", String.valueOf(GlobalValues.consoleOutputWindow.output.getFont().getSize()));
        
        
        //  Decimal digit formatting properties
        
         int vprec = kotlinLabSci.PrintFormatParams.getVecDigitsPrecision();
         settings.setProperty("VecDigitsProp", String.valueOf(vprec));
         
         int mprec = kotlinLabSci.PrintFormatParams.getMatDigitsPrecision();
         settings.setProperty("MatDigitsProp", String.valueOf(vprec));
         
         int mxrows = kotlinLabSci.PrintFormatParams.getMatMxRowsToDisplay();
         settings.setProperty("mxRowsProp", String.valueOf(mxrows));
         
         int mxcols = kotlinLabSci.PrintFormatParams.getMatMxColsToDisplay();
         settings.setProperty("mxColsProp", String.valueOf(mxcols));
         
         
         if (kotlinLabSci.PrintFormatParams.getVerbose()==true)
             settings.setProperty("verboseOutputProp", "true");
         else
             settings.setProperty("verboseOutputProp", "false");
         
        
     }

       
   
    // read the user defined  classpath components 
    public static void readUserPaths() {
     try {
      File file = new File(GlobalValues.userPathsFileName);  // the file name that keeps the user paths
      FileReader fr = new FileReader(file);
      BufferedReader in = new BufferedReader(fr);
      String currentLine;
      GlobalValues.kotlinLabSciClassPathComponents.clear();
      
      while ( (currentLine = in.readLine())!= null)  {
          if (GlobalValues.kotlinLabSciClassPathComponents.contains(currentLine)==false)
             GlobalValues.kotlinLabSciClassPathComponents.add(currentLine);
       }
     }
            catch (IOException ioe) {
                System.out.println("Exception trying to read "+GlobalValues.userPathsFileName);
                 return;
            }
        
    }
    
    
    
    public static void writeUserPaths() {
    
        StringBuffer sb = new StringBuffer();
        
        // handle any specified additional user specified paths
        if (GlobalValues.kotlinLabSciClassPathComponents !=null)  {
            int userSpecPathsCnt = GlobalValues.kotlinLabSciClassPathComponents.size();
            String userPathsSpecString="";
            for (int k=0; k<userSpecPathsCnt; k++) {
                String currentToolbox  = GlobalValues.kotlinLabSciClassPathComponents.elementAt(k).toString().trim();
                sb.append(currentToolbox+"\n");
             }
          }
         
        try {
                // take the program's text and save it to a temporary file
                File tempFile = new File(GlobalValues.userPathsFileName);
                FileWriter fw = new FileWriter(tempFile);
                fw.write(sb.toString(), 0, sb.length());
                fw.close();
             }
            catch (IOException ioe) {
                System.out.println("Exception trying to write Glab user paths ");
                 return;
            }
    }
    
    
// clear the properties 
    public static void clearProperties()  {
       settings.setProperty("kotlinSciClassPathProp", homeDir);
       settings.setProperty("kotlinLabWorkingDirProp","");
       }
       
    /** @return actual working directory */
    protected String getWorkingDirectory()
    {
        return workingDir;
    }

    /** @param set working directory */
    protected void setWorkingDirectory(String _workingDir)
    {
        workingDir = _workingDir;
        
    }

    
          
 }

