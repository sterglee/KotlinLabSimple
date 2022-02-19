
package kotlinLabSciCommands;

// this package implements basic commands for the KotlinSci's console

import kotlinLabSci.math.array.Matrix;
import kotlinLabGlobal.Interpreter.GlobalValues;
import kotlinLabSci.help.PDFHelpFrame;
import java.io.*;
import java.math.BigDecimal;
import javax.swing.JOptionPane;
import utils.*;
import java.lang.reflect.*;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.ArrayList;
import java.util.Locale;

public class BasicCommands {
        public static String commands = "                              \n-------------- Basic System Commands ----------------\n\n                      "+
              "\ndir()       :   displays the directory contents" + "\nls()       :   displays the directory contents (as dir())"+
              "\ndirR()       :   recursively displays the directory contents"+ "\nlsR()       :   recursively displays the directory contents"+
              "\ncd(\"directory\") : changes the current directory"+
              "\nmd(\"directory\"): creates the specified directory"+
              "\nmkdir(\"directory\"): creates the specified directory"+
              "\nwhos()      :   displays the variables of the workspace"+  "\ncls()      : clears the display"+
              "\ntic()           : starts the timer"+ "\ntoc()           : retrieves the time passed from the previous tick"+
              "\ninspect(Object o)   : displays information for object o"+
              "\narrayGrow(Object o) : expands dynamically the array object o"+
              "\nString getFile(String initialDirectory, [String messageToUser]): gets a File with a File Dialog, starting at directory initialDirectory, specifying optionally a message to display to the user"+
              " \ndouble getDouble(String messageOfDialogBox, [double defaultValue]): gets a double value with a dialog box, specifying an optional default parameter"+  
              " \nint getInt(String messageOfDialogBox, [int defaultValue]): gets an int  value with a dialog box, specifying an optional default parameter"+
              " \nString getString(String messageOfDialogBox, [String defaultValue]): gets a String value with a dialog box, specifying an optional default parameter"+
              "\n int  load(String fileName) :     // loads the Matlab .mat file contents to KotlinSci workspace"+
              "\nboolean  save(String fileName) :      // writes to the Matlab .mat file the contents of the kotlinSci workspace"+
              "\nboolean  save(String fileName, String variableNameToSave)"+
              "\nint format(int decPoints)  // controls how many decimal points to display for doubles, sets to decPoints, returns previous setting";

     
        public static boolean setSymbolicVerbose(boolean state) {  // sets whether to display pretty LaTeX maths and returns the previous state"+
            boolean prevState = GlobalValues.displayLatexOnEval;
            GlobalValues.displayLatexOnEval = state;
            return prevState;
        }
        
        public static boolean getSymbolicVerbose() {
            return GlobalValues.displayLatexOnEval;
        }
        
              
 public static void drows(int N) {
     kotlinLabSci.math.array.DoubleArray.maxRows = N;
 }
 
 public static void dcols(int N) {
     kotlinLabSci.math.array.DoubleArray.maxCols = N;
 }
        // controls how many decimal points to display for doubles, sets to decPoints, returns previous setting
  public static int format(int decPoints)   {
            int prevFmtLen = GlobalValues.doubleFormatLen;
            GlobalValues.doubleFormatLen = decPoints;
            String s="0.";
            for (int k=0; k<decPoints;k++)  s +="0";
            GlobalValues.fmtString = new DecimalFormat(s);
            GlobalValues.fmtString.setDecimalFormatSymbols(new DecimalFormatSymbols(new Locale("us")));
            return prevFmtLen;
        }
                
  public static long  tic() {    
      System.gc();
      GlobalValues.timeForTic = System.currentTimeMillis();   // save the current time
        return  GlobalValues.timeForTic;
  }

  public static double  toc() {
         double delay = (double)(System.currentTimeMillis()-GlobalValues.timeForTic)/1000.0;
        return delay;
  }

  
 
  public static void disp(String str) {
         System.out.println(str);
  }

  public static void fail(String str) {
      System.out.println(str);
  }
  

  public static String  inspect(Object o) {
      String inspectStr  = new ObjectAnalyzer().toString(o);
      return inspectStr;
  }
          
  

  public static  void cd(String  newDirectory)    {
      if (newDirectory.equals(".."))  {   // change to "upper" directory
          String currentDirectory = GlobalValues.workingDir;
          int pathSepIndex = currentDirectory.lastIndexOf(File.separatorChar);
          currentDirectory = currentDirectory.substring(0, pathSepIndex);
          newDirectory = currentDirectory;
      }
      try {
      File dir=null;
	if (newDirectory.charAt(0)==File.separatorChar 	|| newDirectory.charAt(1)==':')   
	     dir = new File(newDirectory); 	// absolute file path name
	else  // relative path name to the current working directory
             dir = new File(GlobalValues.workingDir, newDirectory);  
		 	
		if (dir.isDirectory())
	              GlobalValues.workingDir = dir.toString();  // set the new system's current directory		
		}
		catch (Exception e)
		{
			System.out.println("cd: IO exception");
		}		    
       
  }

  public static void mkdir(String newDirectory) {
      md(newDirectory);
  }

  public static  void md(String  newDirectory)    {
       try {
                File fNewDirectory = null;
                String newDirectoryFullPathName = newDirectory;

      if (newDirectory.charAt(0)==File.separatorChar 	|| newDirectory.charAt(1)==':')
	     fNewDirectory = new File(newDirectory); 	// absolute file path name
	else  // relative path name to the current working directory
     {
	                // create the specified directory actually at the filesystem
                String directoryPath = GlobalValues.workingDir;

                newDirectoryFullPathName = directoryPath+File.separator+newDirectory;
                fNewDirectory = new File(newDirectoryFullPathName);
    }

                int userResponse = JOptionPane.YES_OPTION;
                boolean  dirExists = fNewDirectory.exists();
                if (dirExists)     // directory already exists
                     JOptionPane.showMessageDialog(null, "Directory: "+newDirectoryFullPathName+" already exists");

                if (dirExists==false)   {  // create new directory and update tree
                    // create new directory
                     try {
               fNewDirectory.mkdir();  // creates the corresponding directory
                     }
                     catch (SecurityException ioEx)  {
    JOptionPane.showMessageDialog(null, "IOException trying to create file"+newDirectoryFullPathName, "IO Exception", JOptionPane.INFORMATION_MESSAGE );
                     }


      if (fNewDirectory.isDirectory())
	              GlobalValues.workingDir = fNewDirectory.toString();  // set the new system's current directory
		}
       }

                catch (Exception e)
		{
			System.out.println("cd: IO exception");
		}

  }



// return the type of the object
public static String typeOf(Object currentVarValue)  {
    

    String tv;
  for (;;) {
  
    if ( currentVarValue instanceof  double [])   // a double[]
    {
        tv = "double ["+((double [])currentVarValue).length+"]";
        break;
    }
  
  if (( currentVarValue instanceof  double [][]))
       {
    tv = "double ["+((double [][])currentVarValue).length+"]"+"["+((double [][])currentVarValue)[0].length+"]";
    break; 
       }
  
  if  (currentVarValue instanceof  Integer) 
             {
    tv = "Integer";
    break;
             }
  
  if ( currentVarValue instanceof Long)  {
    tv = "Long";                 
    break;
  }
  
  if ( currentVarValue instanceof Float)  {
    tv = "Float";                 
    break;
  }
  
  if ( currentVarValue instanceof Double)  {
    tv = "Double";
    break;
  }
  
  if ( currentVarValue instanceof kotlinLabSci.math.array.Matrix)  {
      kotlinLabSci.math.array.Matrix myMatrix = (kotlinLabSci.math.array.Matrix) currentVarValue;
      int nrows  =   myMatrix.getRowsNumber();
      int ncols = myMatrix.getColumnsNumber();
              
      tv = "Matrix ["+nrows+","+ncols+"]";
      break;
    }
   
  
      if (currentVarValue instanceof BigDecimal)   // simple scalar numeric values  or Strings can be treated as Strings and passed to gLab 
                {
   tv  = "BigDecimal";
   break;
  }
  
      if (currentVarValue instanceof String) {
   tv = "String";       
   break;
      }
  
      // we cannot handle all possible types explicitly, therefore return the Java canonical name 
      // if the class is not any of our familiar classes
      tv = currentVarValue.getClass().getCanonicalName();
      break;
      
}
      
    return tv;
}
  
  

  public static String dir()  {
        StringBuilder sb = new StringBuilder();
        String directoryToDisplay;
        directoryToDisplay = GlobalValues.workingDir;
        
        try {
        File pathName = new  File(directoryToDisplay);
        String [] fileNames = pathName.list();
        
        // enumerate all files in the directory
        for (int i=0; i<fileNames.length; i++)
        {
            File f = new File(pathName.getPath(), fileNames[i]);
            if (f.isDirectory())
                sb.append("\n "+fileNames[i]+"    <dir>");
            else
                sb.append("\n"+fileNames[i]);
         }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sb.toString();
  }
    
  
    public static String ls()  {   // for UNIX users
        return dir();
  }
    
   public static String lsR()  {   // for UNIX users
        return dirR();
  }

   public static String lsR(String directory)  {   // for UNIX users
        return dirR(directory);
  }

    public static String dir(String directory)  {
        StringBuilder sb = new StringBuilder();
        String directoryToDisplay;
        if (directory == "")   
             directoryToDisplay = GlobalValues.workingDir;
        else  // the user has specified a directory, i.e. dir  <directoryToDisplay> 
             directoryToDisplay = directory; 
        
        try {
        File pathName = new  File(directoryToDisplay);
        String [] fileNames = pathName.list();
        
        // enumerate all files in the directory
        for (int i=0; i<fileNames.length; i++)
        {
            File f = new File(pathName.getPath(), fileNames[i]);
            if (f.isDirectory())  
                sb.append("\n "+fileNames[i]+"    <dir>");
            else
                sb.append("\n"+fileNames[i]);
         }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sb.toString();
  }
    
    
    // recursively display the contents of the subdirectories
   public static String dirR(String directory)  {
        StringBuilder sb = new StringBuilder();
        String currentWorkingDirectory = directory;
        
        try {
        File pathName = new  File(currentWorkingDirectory);
        String [] fileNames = pathName.list();
        
        // enumerate all files in the directory
        for (int i=0; i<fileNames.length; i++)
        {
            File f = new File(pathName.getPath(), fileNames[i]);
            if (f.isDirectory())  {
                sb.append("\n "+fileNames[i]+"    <dir>");
                String nested = dirR(fileNames[i]);
                sb.append("\n\n"+nested+"\n\n");
             }
            else
                sb.append("\n"+fileNames[i]);
         }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sb.toString();
        }
   
    // recursively display the contents of the subdirectories
   public static String dirR()  {
        StringBuilder sb = new StringBuilder();
        String currentWorkingDirectory = GlobalValues.workingDir;
        
        try {
        File pathName = new  File(currentWorkingDirectory);
        String [] fileNames = pathName.list();
        
        // enumerate all files in the directory
        for (int i=0; i<fileNames.length; i++)
        {
            File f = new File(pathName.getPath(), fileNames[i]);
            if (f.isDirectory())  {
                sb.append("\n "+fileNames[i]+"    <dir>");
                String nested = dirR(currentWorkingDirectory+File.separatorChar+fileNames[i]);
                sb.append("\n\n"+nested+"\n\n");
             }
            else
                sb.append("\n"+fileNames[i]);
         }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return sb.toString();
        }
   
public static double getDouble(String msg)  {
    String strMsg = JOptionPane.showInputDialog(msg);
    double val = Double.valueOf(strMsg);
    return val;
}

public static double getDouble()  {
    String strMsg = JOptionPane.showInputDialog("Give double value");
    double val = Double.valueOf(strMsg);
    return val;
}

public static int  getInt(String msg)  {
    String strMsg = JOptionPane.showInputDialog(msg);
    int val = Integer.valueOf(strMsg);
    return val;
}

public static int  getInt()  {
    String strMsg = JOptionPane.showInputDialog("Give int value");
    int val = Integer.valueOf(strMsg);
    return val;
}


public static String getString(String msg)  {
    String strMsg = JOptionPane.showInputDialog(msg);
    return  strMsg;
}

public static String getString()  {
    String strMsg = JOptionPane.showInputDialog("Give String value");
    return  strMsg;
}

public static double getDouble(String msg, double defaultVal)  {
    String strMsg = JOptionPane.showInputDialog(msg, Double.toString(defaultVal));
    double val = Double.valueOf(strMsg);
    return val;
}


public static int  getInt(String msg, int defaultVal)  {
    String strMsg = JOptionPane.showInputDialog(msg, Integer.toString(defaultVal));
    int val = Integer.valueOf(strMsg);
    return val;
}


public static String getString(String msg, String defaultVal)  {
    String strMsg = JOptionPane.showInputDialog(msg,  defaultVal);
    return  strMsg;
}

public static String getFile() {
    javax.swing.JFileChooser chooser = new javax.swing.JFileChooser(GlobalValues.workingDir);
    chooser.setDialogTitle("Specify file for loading data");
    chooser.setVisible(true);
    chooser.showOpenDialog(GlobalValues.globalEditorPane);
    File file = chooser.getSelectedFile();
    return file.getAbsolutePath();   // the full file name
}  

public static String getFile(String dir) {
    javax.swing.JFileChooser chooser = new javax.swing.JFileChooser(dir);
    chooser.setDialogTitle("Specify file for loading data");
    chooser.setVisible(true);
    chooser.showOpenDialog(GlobalValues.globalEditorPane);
    File file = chooser.getSelectedFile();
    return file.getAbsolutePath();   // the full file name
}  

public static String getFile(String dir, String messageToUser) {
    javax.swing.JFileChooser chooser = new javax.swing.JFileChooser(dir);
    chooser.setDialogTitle(messageToUser);
    chooser.setVisible(true);
    chooser.showOpenDialog(GlobalValues.globalEditorPane);
    File file = chooser.getSelectedFile();
    return file.getAbsolutePath();   // the full file name
}  

public static  Object arrayGrow( Object o)  {
    Class  cl = o.getClass();
    if (!cl.isArray() )   return  null;
    Class componentType = cl.getComponentType();
    int  length = Array.getLength(o);
    int  newLength = length * 11 / 10 + 10;
    
    Object newArray = Array.newInstance(componentType, newLength);
    System.arraycopy(o, 0,  newArray, 0, length);
    return newArray;
}

public static Matrix m(ArrayList al)  {
     return new Matrix(al);
}

public static Matrix M(ArrayList al)  {
     return new Matrix(al);
}


}
    

