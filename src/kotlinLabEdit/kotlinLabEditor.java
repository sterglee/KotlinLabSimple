package kotlinLabEdit;


import kotlinLabExec.kotlinLab.EditorPaneHTMLHelp;
import kotlinLabGlobal.Interpreter.GlobalValues;
import kotlinLabGlobals.JavaGlobals;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rsyntaxtextarea.SyntaxConstants;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.fife.ui.rtextarea.SearchContext;
import org.fife.ui.rtextarea.SearchEngine;
import org.fife.ui.rtextarea.SearchResult;

import javax.swing.*;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.JTextComponent;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.*;
import java.util.Vector;


public class kotlinLabEditor extends JFrame implements WindowListener {
        JMenuBar mainJMenuBar;

        JMenu  fileMenu;
        JMenuItem  saveEditorTextJMenuItem;
        JMenuItem  saveAsEditorTextJMenuItem;
        JMenuItem   loadEditorTextJMenuItem;
        JMenuItem exitJMenuItem;
        JMenu recentPaneFilesMenu = new JMenu("Recent Files");  // created dynamically to keep the recent files list
        JMenuItem clearRecentFilesJMenuItem;    
        JMenuItem  recentFileMenuItem;
        JMenu helpMenu;
        
    
        
        JMenu switchLibrariesMenu = new JMenu("Switch Libraries");
        JMenuItem switchJBLASMenuItem = new JMenuItem("Switch To JBLAS");

        private JTextField  searchField;
        private JTextField suggestionField;
        private JCheckBox regexCB;
        private JCheckBox matchCaseCB;
        static public  JLabel  progressComputationLabel = new JLabel("Computing ... ");
        
        private boolean forward=true;
        private JButton gotoLineButton;
        private JTextField gotoLineField;
        
    static boolean documentEditsPendable;
    org.fife.ui.rsyntaxtextarea.RSyntaxTextArea  jep;
    public static  JFrame currentFrame  = null;
    public boolean editorTextSaved = false;
    public String editedFileName;   // the full pathname of the file being currently edited
    public static String titleStr = "KotlinLab programmer's editor (F6 - Kotlin) "+ GlobalValues.TITLE;

    public  Vector<String>  recentPaneFiles = new Vector<String>();  // keeps the full names of the recent files
    public  String  fileWithFileListOfPaneRecentFiles = "recentsPaneFile.txt"; // the list of the recent editor's pane files

    public RTextScrollPane  scrPane;


    public EditorKeyMouseHandler   keyMouseHandler = new EditorKeyMouseHandler();
    public RSyntaxEditorMouseMotionAdapter keyMouseMotionHandler = new RSyntaxEditorMouseMotionAdapter();


    public RSyntaxTextArea  editorPane=null;    // the component that keeps and handles the editing text
        public RSyntaxDocument  docVar=null; 
        public RSyntaxDocument syntaxDocument=null;
      
    
  
    public void kotlinLabEdit(String selectedValue) {
                       
      
                   FileReader fr = null;
            try {
                fr = new FileReader(selectedValue);
                jep.read(fr, null);
                
            } catch (FileNotFoundException ex) {
                System.out.println("file "+selectedValue+" not found");
            }
            catch (IOException ex) {
                    System.out.println("cannot close file "+selectedValue);
                }
/*            finally {
                try {
                    fr.close();
                } 
                catch (IOException ex) {
                    System.out.println("cannot close file "+selectedValue);
                }
            }*/
            
            editedFileName = selectedValue;   // current file is the new loaded one
            editorTextSaved = false;  // a freshly loaded file doesn't require saving
            
            loadRecentPaneFiles();
            
            kotlinLabEditor.currentFrame.setTitle(titleStr+":  File: "+editedFileName);
           
    }
    
    
 
   
  
    public void saveRecentPaneFiles() {  // the file that keeps the recent files list is kept in GlobalValues.gLabRecentFilesList
                                                                  // at the same directory as the kotlinLab.jar executable, i.e. GlobalValues.jarFilePath
         //create streams
         try {
    // open the file for writing the recent files         
            FileOutputStream output = new FileOutputStream(fileWithFileListOfPaneRecentFiles);  

            //create writer stream
           OutputStreamWriter  recentsWriter= new OutputStreamWriter(output);
            int  fileCnt=0;  // restrict the maximum number of recent files

           for (int k=0; k<recentPaneFiles.size(); k++) {
                String currentRecentFile = (String)recentPaneFiles.elementAt(k)+"\n";
                recentsWriter.write(currentRecentFile, 0, currentRecentFile.length());
                if (fileCnt++ == GlobalValues.maxNumberOfRecentFiles)  break;
            }
            recentsWriter.close();
            output.close();
    }
        catch(java.io.IOException except)
        {
            System.out.println("IO exception in saveRecentFiles");
            System.out.println(except.getMessage());
            except.printStackTrace();
        }
    }

    // update the recent files menu with the items taken from recentFiles
    public void updateRecentPaneFilesMenu()
    {
           recentPaneFilesMenu.removeAll();  // clear previous menu items
           recentPaneFilesMenu.setFont(GlobalValues.uifont);
           clearRecentFilesJMenuItem = new JMenuItem("Clear the list of recent files");
           clearRecentFilesJMenuItem.setFont(GlobalValues.uifont);
           clearRecentFilesJMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                recentPaneFiles.clear();
                recentPaneFilesMenu.removeAll();
            }
        });

           recentPaneFilesMenu.add(clearRecentFilesJMenuItem);
           
           int numberRecentFiles = recentPaneFiles.size();
       for (int k=numberRecentFiles-1; k>=0; k--)  {     // reverse order for displaying the most recently loaded first
            final String  recentFileName = (String)recentPaneFiles.elementAt(k);   // take the recent filename
            recentFileMenuItem = new JMenuItem(recentFileName);
            recentFileMenuItem.setFont(GlobalValues.uifont);
            recentFileMenuItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
             kotlinLabEdit(recentFileName);   // reload the recent file in editor
             
            // update the workingDir
            String pathOfLoadFileName = recentFileName.substring(0, recentFileName.lastIndexOf(File.separatorChar));
            GlobalValues.workingDir = pathOfLoadFileName;
                }
            });
            recentPaneFilesMenu.add(recentFileMenuItem);    // add the menu item corresponding to the recent file
        }  // for all the recently accessed files 

            recentPaneFilesMenu.setToolTipText("Tracks \"Saved As\" Files");
            mainJMenuBar.add(recentPaneFilesMenu);   // finally add the recent files menu to the main menu bar
        
       }
    
  // load the recent files list from the disk updating also the menu
    public  void loadRecentPaneFiles() {
         // create streams
       
        boolean exists = (new File(fileWithFileListOfPaneRecentFiles)).exists();
if (exists) {
    
        try {
  // open the file containing the stored list of recent files
             FileInputStream input = new FileInputStream(fileWithFileListOfPaneRecentFiles);
             
             //create reader stream
           BufferedReader  recentsReader= new BufferedReader(new InputStreamReader(input));

          recentPaneFiles.clear();    // clear the Vector of recent files
          String currentLine;     // refill it from disk
          while ((currentLine = recentsReader.readLine()) != null)
              if (recentPaneFiles.indexOf(currentLine) == -1)    // file not already in list
                recentPaneFiles.add(currentLine);

            recentsReader.close();
            input.close();
            updateRecentPaneFilesMenu();   // update the recent files menu

         }
        catch(java.io.IOException except)
        {
            System.out.println("IO exception in readRecentsFiles. File: "+fileWithFileListOfPaneRecentFiles+"  not found");
            recentPaneFilesMenu.removeAll();  // clear previous menu items
           clearRecentFilesJMenuItem = new JMenuItem("Clear the list of recent files");
           clearRecentFilesJMenuItem.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                recentPaneFiles.clear();
                recentPaneFilesMenu.removeAll();
            }
        });

           recentPaneFilesMenu.add(clearRecentFilesJMenuItem);
            mainJMenuBar.add(recentPaneFilesMenu);   // finally add the recent files menu to the main menu bar
        
        }
     }
    }
    
    
// update fields denoting the document in the editor, necessary when a new document is edited
  public  RSyntaxDocument updateDocument()  {
         
          docVar = (RSyntaxDocument) editorPane.getDocument();
          syntaxDocument = docVar;
          
          return syntaxDocument;
  }
               
     
  
   public  String  getCurrentLine() {
       if (docVar==null)
           updateDocument();
           
       RSyntaxDocument  myDoc = syntaxDocument;
       
       int caretpos = editorPane.getCaretPosition();
       int startpos = editorPane.getCaretOffsetFromLineStart();
       int scanpos = caretpos-startpos;
       String s = "";
       try {
            char ch = myDoc.charAt(scanpos);
       while (ch!='\n') {
                s += myDoc.charAt(scanpos);
            
           scanpos += 1;
           ch = myDoc.charAt(scanpos);
       }
       } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
       
       return s;
   }
       
  
         
   public  String   getSelectedTextOrCurrentLine() {
       String selectedTextOrCurrentLine = GlobalValues.globalEditorPane.getSelectedText();
       if (selectedTextOrCurrentLine==null)
           selectedTextOrCurrentLine = getCurrentLine();
       
       return selectedTextOrCurrentLine;
   }
   
    // perform common editing operations for the file selectedValue.
    // if the isMainFrame flag is true then the file will be edited in the main editor window,
    // that has attached at its bottom the Console's output, therefore some different chores
    // should be performed
    public RSyntaxTextArea  commonEditingActions(String selectedValue, boolean isMainFrame) {
        currentFrame = new JFrame("Editing "+selectedValue);    // keep the current frame handle
        editedFileName = selectedValue;    // keep the edited filename
        jep = new  RSyntaxTextArea();   // construct a JEditorPane component

  //      jep.setToolTipText("F6 for Kotlin  executes selected text or current line. F5 clears console window");

        jep.setFont(new Font(GlobalValues.paneFontName, Font.PLAIN, GlobalValues.paneFontSize));
      
        jep.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_JAVA);
        jep.setCodeFoldingEnabled(true);

        // create a toolbar with searching options
        JToolBar toolBar = new JToolBar();
        
        gotoLineButton = new JButton("Go To Line: ");
        gotoLineButton.setToolTipText("Positions the cursor to the line entered at the corresponding field ");
        toolBar.add(gotoLineButton);
        gotoLineButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
        int lineNo = Integer.parseInt(gotoLineField.getText());
        int apos;
                try {
        apos = GlobalValues.globalEditorPane.getLineStartOffset(lineNo-1);
        GlobalValues.globalEditorPane.setCaretPosition(apos);
                } catch (BadLocationException ex) {
                    ex.printStackTrace();
                }
        
            }
        });
        
        gotoLineField = new JTextField();
        toolBar.add(gotoLineField);
        
        
        searchField = new JTextField(30);
        toolBar.add(searchField);
        
        JLabel suggestionsLabel=new JLabel("suggestions: ");
        suggestionField = new JTextField(50);
        
        toolBar.add(suggestionsLabel);
        toolBar.add(suggestionField);
        
        final JButton nextButton = new JButton("Find Next");
        nextButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
          forward  = true;
          performSearch(forward);
            }
        });
        toolBar.add(nextButton);
        searchField.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
         nextButton.doClick();
            }
        });
        toolBar.add(searchField);
        
        JButton prevButton = new JButton("Find Previous");
        prevButton.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
          forward = false;
          performSearch(forward);                 
            }
        });
        toolBar.add(prevButton);

        regexCB = new JCheckBox("Regex");
        toolBar.add(regexCB);

        matchCaseCB = new JCheckBox("Match Case");
        toolBar.add(matchCaseCB);
        
        
        // add the key and mouse handlers
        jep.addKeyListener (keyMouseHandler);
     //  jep.addMouseMotionListener(keyMouseMotionHandler);
        
        mainJMenuBar = new JMenuBar();
                        
        fileMenu = new JMenu("File");
        fileMenu.setMnemonic('F');
        fileMenu.setToolTipText("File editing operations");
        fileMenu.setFont(GlobalValues.uifont);
                
     
        
         
         
                
 JMenu KotlinMenu = new JMenu("Kotlin");
 KotlinMenu.setToolTipText("Kotlin Scripting");
 KotlinMenu.setFont(GlobalValues.uifont);
  
  
           
                  JMenuItem executeWithKotlinJMenuItem = new JMenuItem("Execute Code with Kotlin (use also F6 key to execute code with KotlinEngine,  F5 to clear output results)");
           executeWithKotlinJMenuItem.setFont(GlobalValues.puifont);
           executeWithKotlinJMenuItem.addActionListener((ActionEvent e) -> {
                          keyMouseHandler.clickExecuteScriptEngine();
           });
           
           KotlinMenu.add(executeWithKotlinJMenuItem);
        



        

        saveEditorTextJMenuItem = new JMenuItem("Save Editor Text ");
        saveEditorTextJMenuItem.addActionListener(new saveEditorTextAction());
        saveEditorTextJMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl S"));
        saveEditorTextJMenuItem.setFont(GlobalValues.uifont);
                
        saveAsEditorTextJMenuItem = new JMenuItem("Save As Editor Text to File");
        saveAsEditorTextJMenuItem.addActionListener(new saveAsEditorTextAction());
        saveAsEditorTextJMenuItem.setFont(GlobalValues.uifont);
                
        loadEditorTextJMenuItem = new JMenuItem("Load  File to Editor");
        loadEditorTextJMenuItem.addActionListener(new loadEditorTextAction());
        loadEditorTextJMenuItem.setAccelerator(KeyStroke.getKeyStroke("ctrl L"));
        loadEditorTextJMenuItem.setFont(GlobalValues.uifont);
        
        
        exitJMenuItem = new JMenuItem("Exit");
        exitJMenuItem.setFont(GlobalValues.uifont); 
        
        exitJMenuItem.addActionListener(new ActionListener() {

          public void actionPerformed(ActionEvent e) {
                    String fullNameOfPropsFile = "kotlinLab.props";
    
            try {
    File outPropFile = new File(fullNameOfPropsFile);
               
   FileOutputStream outFile= new FileOutputStream(outPropFile);
   GlobalValues.passPropertiesFromWorkspaceToSettings(GlobalValues.settings);  // update properties to the current values kept in workspace
   GlobalValues.settings.store(outFile, "Saved kotlinLab global conf parameters");
   outFile.close();
    }
     catch (Exception fnfe) {
        JOptionPane.showMessageDialog(null, "Cannot write configuration file. Perhaps you do not have access rights for write, try making a shortcut to kotlinLab using a proper \"Start in\" directory ","Cannot write configuration file", JOptionPane.INFORMATION_MESSAGE);
        System.out.println("error opening file for writing configuration");
        fnfe.printStackTrace();
        
        }
          saveRecentPaneFiles();
          System.exit(0);
            }
        });

        
        fileMenu.add(saveEditorTextJMenuItem);
        fileMenu.add(saveAsEditorTextJMenuItem);
        fileMenu.add(loadEditorTextJMenuItem);
        fileMenu.add(exitJMenuItem);

       
    JMenuItem interfaceWithLibrariesJMenuItem = new JMenuItem("Matrix Assignment - Tip - Matrix Conversions");
    interfaceWithLibrariesJMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
             EditorPaneHTMLHelp  inPlaceHelpPane = new EditorPaneHTMLHelp("MatrixConversions.html");
       if (GlobalValues.useSystemBrowserForHelp==false) {
          inPlaceHelpPane.setSize(GlobalValues.figFrameSizeX, GlobalValues.figFrameSizeY);
          inPlaceHelpPane.setLocation(GlobalValues.sizeX/4, GlobalValues.sizeY/4);
          inPlaceHelpPane.setVisible(true);
          
              }
            }
        });
        

        JMenu appearanceMenu = new JMenu("Appearance");
        appearanceMenu.setFont(GlobalValues.uifont);
        appearanceMenu.setToolTipText("Appearance related settings (e.g. fonts, etc. )");
        
                   
               JMenuItem increaseRSyntaxFontMenuItem = new JMenuItem("Increase the font size of the rsyntaxarea editor");
               increaseRSyntaxFontMenuItem.setToolTipText("Increases the font size of the rsyntaxarea editor");
               increaseRSyntaxFontMenuItem.setFont(GlobalValues.guifont);
               increaseRSyntaxFontMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {  // increase the font size of the rsyntaxarea editor
                      Font currentFont = GlobalValues.globalEditorPane.getFont();
                      Font newFont = new Font(currentFont.getFontName(), currentFont.getStyle(), currentFont.getSize()+1);
                      GlobalValues.globalEditorPane.setFont(newFont);
                    }
                });
               
               
               JMenuItem decreaseRSyntaxFontMenuItem = new JMenuItem("Decrease the font size of the rsyntaxarea editor");
               decreaseRSyntaxFontMenuItem.setToolTipText("Decreases the font size of the rsyntaxarea editor");
               decreaseRSyntaxFontMenuItem.setFont(GlobalValues.guifont);
               decreaseRSyntaxFontMenuItem.addActionListener((ActionEvent e) -> {
                   // increase the font size of the rsyntaxarea editor
                   Font currentFont = GlobalValues.globalEditorPane.getFont();
                   Font newFont = new Font(currentFont.getFontName(), currentFont.getStyle(), currentFont.getSize()-1);
                   GlobalValues.globalEditorPane.setFont(newFont);
                });
               
               
               
               
              appearanceMenu.add(increaseRSyntaxFontMenuItem);
              appearanceMenu.add(decreaseRSyntaxFontMenuItem);
                      
                      

              
  
    JMenuItem basicPlotsDirectlyImportJMenuItem = new JMenuItem("Basic Plots Imports");
    basicPlotsDirectlyImportJMenuItem.setFont(GlobalValues.uifont);
    basicPlotsDirectlyImportJMenuItem.setToolTipText("Injects the statements for the JMathPlot based routines");
    basicPlotsDirectlyImportJMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                 kotlinLabEdit.importsHelper.injectBasicPlotsImports();
                    }
                });
         
    
    JMenuItem kotlinLabSciImportJMenuItem = new JMenuItem("kotlinLabSci Imports");
     kotlinLabSciImportJMenuItem.setFont(GlobalValues.uifont);
     kotlinLabSciImportJMenuItem.setToolTipText("Injects directly the statements for the kotlinLabSci based routines");
     kotlinLabSciImportJMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                 kotlinLabEdit.importsHelper.injectkotlinLabSciImports();
                    }
                });

         
    JMenuItem javaSwingImportJMenuItem = new JMenuItem("Java Swing Imports");
    javaSwingImportJMenuItem.setFont(GlobalValues.uifont);
    javaSwingImportJMenuItem.setToolTipText("Injects the statements for the Java Swing based routines");
    javaSwingImportJMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                 kotlinLabEdit.importsHelper.injectJavaSwing();
                    }
                });
    
    
    JMenuItem apacheCommonMathImportJMenuItem = new JMenuItem("Apache Common Maths Imports");
    apacheCommonMathImportJMenuItem.setFont(GlobalValues.uifont);
    apacheCommonMathImportJMenuItem.setToolTipText("Injects the statements for the Apache Common Maths routines");
    apacheCommonMathImportJMenuItem.addActionListener(new ActionListener() {

                    public void actionPerformed(ActionEvent e) {
                 kotlinLabEdit.importsHelper.injectApacheCommonMaths();
                    }
                });
    

    loadRecentPaneFiles();
    
  JMenu importsMenu = new JMenu("Imports");
  importsMenu.setFont(GlobalValues.uifont);
  importsMenu.setToolTipText("injects some imports to facilitate the programmer");
  importsMenu.add(basicPlotsDirectlyImportJMenuItem);
  importsMenu.add(kotlinLabSciImportJMenuItem);
  importsMenu.add(javaSwingImportJMenuItem);

//  importsMenu.add(apacheCommonMathImportJMenuItem);
  
                JMenu  librariesMenu = new JMenu("Libraries");
                librariesMenu.setToolTipText("Investigates routines of various libraries using Java reflection in order to provide help");
                librariesMenu.setFont(GlobalValues.uifont);
                
      JMenuItem nrMenuItem = new JMenuItem("Numerical Recipes ");
                nrMenuItem.setFont(GlobalValues.uifont);
                librariesMenu.add(nrMenuItem);
                nrMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
         kotlinLabExec.gui.WatchClasses  watchClassesOfNRNumAl = new kotlinLabExec.gui.WatchClasses();

         Vector NRNumALClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanNRNumAlLibs(JavaGlobals.numalFile);

         int k=1;
         watchClassesOfNRNumAl.displayClassesAndMethods( NRNumALClasses, "Numerical Recipes", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
                

                JMenuItem scalaSciRoutinesMenuItem = new JMenuItem("kotlinLabSci  routines");
                scalaSciRoutinesMenuItem.setToolTipText("Display information using reflection for all the kotlinLabSci classes and methods");
                scalaSciRoutinesMenuItem.setFont(GlobalValues.uifont);
                librariesMenu.add(scalaSciRoutinesMenuItem);
                
                scalaSciRoutinesMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfkotlinLabSci = new kotlinLabExec.gui.WatchClasses();

         Vector scalaSciClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.jarFilePath, "kotlinLabSci");

         int k=1;
         watchClassesOfkotlinLabSci.displayClassesAndMethods( scalaSciClasses, "kotlinLabSci Classes", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });

                JMenuItem kotlinSciGraphicsRoutinesMenuItem = new JMenuItem("kotlinLabSci  Plotting routines");
                kotlinSciGraphicsRoutinesMenuItem.setToolTipText("Display information using reflection for the kotlinLabSci plotting classes and methods");
                kotlinSciGraphicsRoutinesMenuItem.setFont(GlobalValues.uifont);
                librariesMenu.add(kotlinSciGraphicsRoutinesMenuItem);
                
                kotlinSciGraphicsRoutinesMenuItem.addActionListener((ActionEvent e) -> {
                    kotlinLabExec.gui.WatchClasses  watchClassesOfkotlinLabSci = new kotlinLabExec.gui.WatchClasses();
                    
                    Vector kotlinSciPlottingClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.jarFilePath, "kotlinLabSci/math/plot");
                    
                    int k=1;
                    watchClassesOfkotlinLabSci.displayClassesAndMethods( kotlinSciPlottingClasses, "kotlinLabSci Plotting Classses", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
                });
                
                
                JMenuItem ejmlRoutinesMenuItem = new JMenuItem("EJML  routines");
                ejmlRoutinesMenuItem.setToolTipText("Display information using reflection for the EJML library classes and methods");
                ejmlRoutinesMenuItem.setFont(GlobalValues.uifont);
                librariesMenu.add(ejmlRoutinesMenuItem);
                
                ejmlRoutinesMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfEJML = new kotlinLabExec.gui.WatchClasses();

         Vector EJMLClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.ejmlFile, "org");

         int k=1;
         watchClassesOfEJML.displayClassesAndMethods( EJMLClasses, "EJML Library", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
                
                JMenuItem apacheCommonsRoutinesJMenuItem = new JMenuItem("Apache Commons Math Routines");
                apacheCommonsRoutinesJMenuItem.setToolTipText("Display information using reflection for the Apache Commons math library classes and methods");
                apacheCommonsRoutinesJMenuItem.setFont(GlobalValues.uifont);
                librariesMenu.add(apacheCommonsRoutinesJMenuItem);
                
                apacheCommonsRoutinesJMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfApacheCommonsMath = new kotlinLabExec.gui.WatchClasses();

         Vector ApacheCommonsClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.ApacheCommonsFile, "org/apache/commons/math3");

         int k=1;
         watchClassesOfApacheCommonsMath.displayClassesAndMethods( ApacheCommonsClasses, "Apache Common Maths Library", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });





        JMenuItem kotlinRoutinesJMenuItem = new JMenuItem("Kotlin Routines");
        kotlinRoutinesJMenuItem.setToolTipText("Display information using reflection for the Kotlin standard library routines");
        kotlinRoutinesJMenuItem.setFont(GlobalValues.uifont);
        librariesMenu.add(kotlinRoutinesJMenuItem);

        kotlinRoutinesJMenuItem.addActionListener(new ActionListener() {

            @Override
            public void actionPerformed(ActionEvent e) {
                kotlinLabExec.gui.WatchClasses  watchClassesOfApacheCommonsMath = new kotlinLabExec.gui.WatchClasses();

                Vector kotlinClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.kotlinstdlibFile, "kotlin");

                int k=1;
                watchClassesOfApacheCommonsMath.displayClassesAndMethods( kotlinClasses, "Kotlin Library", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
            }
        });



        JMenuItem JASRoutinesJMenuItem = new JMenuItem("Java Algebra System (JAS) Routines");
                JASRoutinesJMenuItem.setToolTipText("Display information using reflection for the Java Algebra System (JAS library classes and methods");
                JASRoutinesJMenuItem.setFont(GlobalValues.uifont);
                librariesMenu.add(JASRoutinesJMenuItem);
                
                JASRoutinesJMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfJAS  = new kotlinLabExec.gui.WatchClasses();

         Vector JASClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.JASFile, "edu/jas");

         int k=1;
         watchClassesOfJAS.displayClassesAndMethods( JASClasses, "Java Algebra System (JAS) Library", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
                
                JMenuItem MathEclipseRoutinesJMenuItem = new JMenuItem("Math Eclipse  Routines");
                MathEclipseRoutinesJMenuItem.setToolTipText("Display information using reflection for the Math Eclipse system for symbolic maths");
                MathEclipseRoutinesJMenuItem.setFont(GlobalValues.uifont);
                librariesMenu.add(MathEclipseRoutinesJMenuItem);
                
                MathEclipseRoutinesJMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfMathEclipse  = new kotlinLabExec.gui.WatchClasses();

         Vector MathEclipseClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.JASFile, "org/matheclipse/core");

         int k=1;
         watchClassesOfMathEclipse.displayClassesAndMethods( MathEclipseClasses, "Math Eclipse symbolic math evaluator", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
                
                JMenuItem numalMenuItem = new JMenuItem("NUMAL routines");
                numalMenuItem.setToolTipText("Display information using reflection for the NUMAL classes and methods");
                numalMenuItem.setFont(GlobalValues.uifont);
                librariesMenu.add(numalMenuItem);
                numalMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfNRNumAl = new kotlinLabExec.gui.WatchClasses();

         Vector numalClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.numalFile, "numal");

         int k=1;
         watchClassesOfNRNumAl.displayClassesAndMethods( numalClasses, "NUMAL", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
                
                
                
                
                JMenuItem jlapackMenuItem = new JMenuItem("JLAPACK routines");
                jlapackMenuItem.setToolTipText("Display information using reflection for the JLAPACK  classes and methods");
                jlapackMenuItem.setFont(GlobalValues.uifont);
                librariesMenu.add(jlapackMenuItem);
                jlapackMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfJLAPACK = new kotlinLabExec.gui.WatchClasses();

         Vector numalClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.LAPACKFile, "org");

         int k=1;
         watchClassesOfJLAPACK.displayClassesAndMethods( numalClasses, "JLAPACK", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
                
                
                JMenuItem jsciMenuItem = new JMenuItem("JSci  routines (contains Wavelet library, plotting, statistical routines)");
                jsciMenuItem.setToolTipText("Display information using reflection for the JSci classes and methods");
                jsciMenuItem.setFont(GlobalValues.uifont);
                librariesMenu.add(jsciMenuItem);
                jsciMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfJSci = new kotlinLabExec.gui.WatchClasses();

         Vector JSciClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.jsciFile, "JSci");

         int k=1;
         watchClassesOfJSci.displayClassesAndMethods( JSciClasses, "JSci Library Routines", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
                
                
                
                JMenuItem joregonDSPMenuItem = new JMenuItem("Oregon Digital Signal Processing library routines");
                joregonDSPMenuItem.setToolTipText("Display information using reflection for the classes and methods of  the Oregon DSP  library ");
                joregonDSPMenuItem.setFont(GlobalValues.uifont);
                librariesMenu.add(joregonDSPMenuItem);
                joregonDSPMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfOregonDSP = new kotlinLabExec.gui.WatchClasses();

         Vector oregonDSPClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.numalFile, "DSP");

         
         int k=1;
         watchClassesOfOregonDSP.displayClassesAndMethods( oregonDSPClasses, "Oregon DSP Library", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });

                
                
               JMenuItem mtjMenuItem = new JMenuItem("Matrix Toolkit for Java  routines");
               mtjMenuItem.setToolTipText("Display information using reflection for the MTJ classes and methods");
               mtjMenuItem.setFont(GlobalValues.uifont);
               librariesMenu.add(jsciMenuItem);
               mtjMenuItem.addActionListener((ActionEvent e) -> {
                   kotlinLabExec.gui.WatchClasses  watchClassesOfJSci = new kotlinLabExec.gui.WatchClasses();
                   
                   Vector  mtjClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.mtjColtSGTFile, "no");
                   
                   int k=1;
                   watchClassesOfJSci.displayClassesAndMethods( mtjClasses, "MTJ Library Routines", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
                });
                
        
                
                JMenuItem coltMenuItem = new JMenuItem("CERN Colt  routines");
                coltMenuItem.setToolTipText("Display information using reflection for the classes and methods of  the Colt Linear Algebra Library of CERN ");
                coltMenuItem.setFont(GlobalValues.uifont);
                librariesMenu.add(coltMenuItem);
                coltMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfNRNumAl = new kotlinLabExec.gui.WatchClasses();

         Vector NRNumALClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.mtjColtSGTFile, "cern");

         int k=1;
         watchClassesOfNRNumAl.displayClassesAndMethods( NRNumALClasses, "Colt  Linear Algebra Library of CERN", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
                
                
                
                JMenuItem csparseMenuItem = new JMenuItem("CSparse library routines");
                csparseMenuItem.setToolTipText("Display information using reflection for the classes and methods of  the CSparse library  for sparse matrices");
                csparseMenuItem.setFont(GlobalValues.uifont);
                librariesMenu.add(csparseMenuItem);
                csparseMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfCSparse  = new kotlinLabExec.gui.WatchClasses();

         String csparseFile = JavaGlobals.jarFilePath;
         
         Vector csparseClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(csparseFile, "edu");

         int k=1;
         watchClassesOfCSparse.displayClassesAndMethods( csparseClasses, "CSparse Library for sparse matrices", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
                
                
                JMenuItem jtransformsMenuItem = new JMenuItem("JTransforms  library routines");
                jtransformsMenuItem.setToolTipText("Display information using reflection for the classes and methods of  the Jtransforms  library ");
                jtransformsMenuItem.setFont(GlobalValues.uifont);
                librariesMenu.add(jtransformsMenuItem);
                jtransformsMenuItem.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfJTransforms = new kotlinLabExec.gui.WatchClasses();

         String jtransformsFile = JavaGlobals.mtjColtSGTFile;
         
         Vector jtransformsClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(jtransformsFile, "edu");

         int k=1;
         watchClassesOfJTransforms.displayClassesAndMethods( jtransformsClasses, "JTransforms Library", kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });

                
                
                // now prepare the "Search Keyword" menu items
                
                JMenuItem kotlinLabRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in kotlinLab Routines");
                kotlinLabRoutinesMenuItemWithKeyword.setToolTipText("Display information for the kotlinLab classes and methods having a keyword");
                kotlinLabRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                librariesMenu.add(kotlinLabRoutinesMenuItemWithKeyword);
                
                kotlinLabRoutinesMenuItemWithKeyword.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfkotlinLabSci = new kotlinLabExec.gui.WatchClasses();

         Vector kotlinLabSciClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.jarFilePath, "kotlinLabSci");

         int k=1;
         String filterString = kotlinLabSciCommands.BasicCommands.getString("Search for keyword");
         watchClassesOfkotlinLabSci.displayClassesAndMethodsAsString(kotlinLabSciClasses, "kotlinLabSci ", filterString,  kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
                

                JMenuItem kotlinLabSciPlottingRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in kotlinLabSci Plotting Routines");
                kotlinLabSciPlottingRoutinesMenuItemWithKeyword.setToolTipText("Display information for the kotlinLabSci Plotting classes and methods having a keyword");
                kotlinLabSciPlottingRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                librariesMenu.add(kotlinLabSciPlottingRoutinesMenuItemWithKeyword);
                
                kotlinLabSciPlottingRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    kotlinLabExec.gui.WatchClasses  watchClassesOfScalaSci = new kotlinLabExec.gui.WatchClasses();
                    
                    Vector kotlinLabSciPlottingClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.jarFilePath, "kotlinLabSci/math/plot");
                    
                    int k=1;
                    String filterString = kotlinLabSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfScalaSci.displayClassesAndMethodsAsString(kotlinLabSciPlottingClasses,  "kotlinLabSci Plot", filterString,  kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
                });
                
                JMenuItem EJMLRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in EJML");
                EJMLRoutinesMenuItemWithKeyword.setToolTipText("Display information for the EJML classes and methods having a keyword");
                EJMLRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                librariesMenu.add(EJMLRoutinesMenuItemWithKeyword);
                
                EJMLRoutinesMenuItemWithKeyword.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfkotlinLabSci = new kotlinLabExec.gui.WatchClasses();

         Vector EJMLClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.ejmlFile, "org");

         int k=1;
         String filterString = kotlinLabSciCommands.BasicCommands.getString("Search for keyword");
         watchClassesOfkotlinLabSci.displayClassesAndMethodsAsString(EJMLClasses, "EJML Classses and Methods relevant to keyword", filterString,  kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
                

                
                JMenuItem ApacheCommonsRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in Apache Commons");
                ApacheCommonsRoutinesMenuItemWithKeyword.setToolTipText("Display information for the Apache Commons classes and methods having a keyword");
                ApacheCommonsRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                librariesMenu.add(ApacheCommonsRoutinesMenuItemWithKeyword);
                
               ApacheCommonsRoutinesMenuItemWithKeyword.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfApacheCommons = new kotlinLabExec.gui.WatchClasses();

         Vector ApacheCommonsClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.ApacheCommonsFile, "org/apache/commons/math3");

         int k=1;
         String filterString = kotlinLabSciCommands.BasicCommands.getString("Search for keyword");
         watchClassesOfApacheCommons.displayClassesAndMethodsAsString(ApacheCommonsClasses, "Apache Commons Math", filterString,  kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
                
                
                JMenuItem JASRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in Java Algebra System (JAS)");
                JASRoutinesMenuItemWithKeyword.setToolTipText("Display information for the Java Algebra System (JAS) classes and methods having a keyword");
                JASRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                librariesMenu.add(JASRoutinesMenuItemWithKeyword);
                
                JASRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    kotlinLabExec.gui.WatchClasses  watchClassesOfJAS= new kotlinLabExec.gui.WatchClasses();
                    
                    Vector JASClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.JASFile, "edu/jas");
                    
                    int k=1;
                    String filterString = kotlinLabSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfJAS.displayClassesAndMethodsAsString(JASClasses, "Java Algebra System (JAS) ", filterString,  kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
                });
                
                
                
                JMenuItem NUMALRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in NUMAL");
                NUMALRoutinesMenuItemWithKeyword.setToolTipText("Display information for the NUMAL classes and methods having a keyword");
                NUMALRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                librariesMenu.add(NUMALRoutinesMenuItemWithKeyword);
                
                NUMALRoutinesMenuItemWithKeyword.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfkotlinLabSci = new kotlinLabExec.gui.WatchClasses();

         Vector NUMALClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.numalFile, "numal");

         int k=1;
         String filterString = kotlinLabSciCommands.BasicCommands.getString("Search for keyword");
         watchClassesOfkotlinLabSci.displayClassesAndMethodsAsString(NUMALClasses, "NUMAL ", filterString,  kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
                

                JMenuItem JLAPACKRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in JLAPACK");
                JLAPACKRoutinesMenuItemWithKeyword.setToolTipText("Display information for the JLAPACK classes and methods having a keyword");
                JLAPACKRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                librariesMenu.add(JLAPACKRoutinesMenuItemWithKeyword);
                
                JLAPACKRoutinesMenuItemWithKeyword.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfJLAPACK = new kotlinLabExec.gui.WatchClasses();

         Vector JLAPACKClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.LAPACKFile, "org");

         int k=1;
         String filterString = kotlinLabSciCommands.BasicCommands.getString("Search for keyword");
         watchClassesOfJLAPACK.displayClassesAndMethodsAsString(JLAPACKClasses, "JLAPACK ", filterString,  kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
             
                
                JMenuItem jsciRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in JSci");
                jsciRoutinesMenuItemWithKeyword.setToolTipText("Display information for the JSci classes and methods having a keyword");
                jsciRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                librariesMenu.add(jsciRoutinesMenuItemWithKeyword);
                
                jsciRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    kotlinLabExec.gui.WatchClasses  watchClassesOfJSci = new kotlinLabExec.gui.WatchClasses();
                    
                    Vector jsciClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.jsciFile, "JSci");
                    
                    int k=1;
                    String filterString = kotlinLabSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfJSci.displayClassesAndMethodsAsString(jsciClasses, "JSci Classses and Methods relevant to keyword", filterString,  kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
                });
                
                JMenuItem nrRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in Numerical Recipes");
                nrRoutinesMenuItemWithKeyword.setToolTipText("Display information for the Numerical Recipes classes and methods having a keyword");
                nrRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                librariesMenu.add(nrRoutinesMenuItemWithKeyword);
                
                nrRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    kotlinLabExec.gui.WatchClasses  watchClassesOfNUMAL = new kotlinLabExec.gui.WatchClasses();
                    
                    Vector nrClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.numalFile, "com");
                    
                    int k=1;
                    String filterString = kotlinLabSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfNUMAL.displayClassesAndMethodsAsString(nrClasses, "Numerical Recipes ", filterString,  kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
                });
                
                JMenuItem mtjRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in MTJ");
                mtjRoutinesMenuItemWithKeyword.setToolTipText("Display information for the MTJ classes and methods having a keyword");
                mtjRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                librariesMenu.add(EJMLRoutinesMenuItemWithKeyword);
                
                mtjRoutinesMenuItemWithKeyword.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfMTJ = new kotlinLabExec.gui.WatchClasses();

         Vector mtjClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.mtjColtSGTFile, "no");

         int k=1;
         String filterString = kotlinLabSciCommands.BasicCommands.getString("Search for keyword");
         watchClassesOfMTJ.displayClassesAndMethodsAsString(mtjClasses, "EJML ", filterString,  kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });
                
                
                
                
                JMenuItem csparseRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in CSparse");
                csparseRoutinesMenuItemWithKeyword.setToolTipText("Display information for the CSparse classes and methods having a keyword");
                csparseRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                librariesMenu.add(csparseRoutinesMenuItemWithKeyword);
                
                csparseRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    kotlinLabExec.gui.WatchClasses  watchClassesOfCSparse = new kotlinLabExec.gui.WatchClasses();
                    
                    String csparseFile = JavaGlobals.jarFilePath;
                    Vector csparseClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(csparseFile, "edu");
                    
                    int k=1;
                    String filterString = kotlinLabSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfCSparse.displayClassesAndMethodsAsString(csparseClasses, "CSparse ", filterString,  kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
                });

                
                                
                
                JMenuItem joregonDSPMenuItemWithKeyword = new JMenuItem("Search Keyword in Oregon Digital Signal Processing library routines");
                joregonDSPMenuItemWithKeyword.setToolTipText("Display information using reflection for the classes and methods of  the Oregon DSP  library having a keyword");
                joregonDSPMenuItemWithKeyword.setFont(GlobalValues.uifont);
                librariesMenu.add(joregonDSPMenuItemWithKeyword);
                joregonDSPMenuItemWithKeyword.addActionListener(new ActionListener() {

                    @Override
                    public void actionPerformed(ActionEvent e) {
        kotlinLabExec.gui.WatchClasses  watchClassesOfOregonDSP = new kotlinLabExec.gui.WatchClasses();

         Vector oregonDSPClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.jarFilePath, "DSP");
         
         int k=1;
         String filterString = kotlinLabSciCommands.BasicCommands.getString("Search for keyword");
         watchClassesOfOregonDSP.displayClassesAndMethodsAsString( oregonDSPClasses, "com.oregondsp", filterString, kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
             }
                    });


                JMenuItem sgtRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in Scientific Graphics Toolbox (SGT)");
                sgtRoutinesMenuItemWithKeyword.setToolTipText("Display information for the Scientific Graphics Toolkit (SGT) classes and methods having a keyword");
                sgtRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                librariesMenu.add(sgtRoutinesMenuItemWithKeyword);
                
                sgtRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    kotlinLabExec.gui.WatchClasses  watchClassesOfSGT = new kotlinLabExec.gui.WatchClasses();
                    
                    Vector sgtClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.mtjColtSGTFile, "gov");
                    
                    int k=1;
                    String filterString = kotlinLabSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfSGT.displayClassesAndMethodsAsString(sgtClasses, "Scientifc Graphics Toolkit (SGT) ", filterString,  kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
                });
                
                JMenuItem coltRoutinesMenuItemWithKeyword = new JMenuItem("Search keyword in CERN Colt");
                coltRoutinesMenuItemWithKeyword.setToolTipText("Display information for the CERN Colt classes and methods having a keyword");
                coltRoutinesMenuItemWithKeyword.setFont(GlobalValues.uifont);
                librariesMenu.add(coltRoutinesMenuItemWithKeyword);
                
                coltRoutinesMenuItemWithKeyword.addActionListener((ActionEvent e) -> {
                    kotlinLabExec.gui.WatchClasses  watchClassesOfCERN = new kotlinLabExec.gui.WatchClasses();
                    
                    Vector coltClasses    =  kotlinLabExec.ClassLoaders.JarClassLoader.scanLib(JavaGlobals.mtjColtSGTFile, "cern");
                    
                    int k=1;
                    String filterString = kotlinLabSciCommands.BasicCommands.getString("Search for keyword");
                    watchClassesOfCERN.displayClassesAndMethodsAsString(coltClasses, "CERN Colt ", filterString,  kotlinLabExec.gui.WatchClasses.watchXLoc+k*50, kotlinLabExec.gui.WatchClasses.watchYLoc+k*50);
                });
    
    
                           
        JMenu examplesMenu = new JMenu("Demos  ", true);
        examplesMenu.setFont(GlobalValues.uifont);
        examplesMenu.setToolTipText("Examples and Demos for  kotlinLab  Sci");
        
        JMenuItem kotlinLabSciExamplesHelpJMenuItem = new JMenuItem("kotlinLabSci Examples ");
        kotlinLabSciExamplesHelpJMenuItem.setFont(GlobalValues.uifont);
        kotlinLabSciExamplesHelpJMenuItem.addActionListener(new kotlinLabSciExamplesJTreeAction());
        kotlinLabSciExamplesHelpJMenuItem.setToolTipText("Provides examples of kotlinLabSci scripts. To execute Copy and Paste in editor");



        JMenuItem kotlinLabSciExamplesHelpJTreeJMenuItem  = new JMenuItem("kotlinLabSci Examples with JTree format ");
        kotlinLabSciExamplesHelpJTreeJMenuItem.setFont(GlobalValues.uifont);
        kotlinLabSciExamplesHelpJTreeJMenuItem.addActionListener(new kotlinLabSciExamplesJTreeAction());
        kotlinLabSciExamplesHelpJTreeJMenuItem.setToolTipText("Provides examples of kotlinLabSci scripts with a convenient JTree displaying. To execute Copy and Paste in editor");
        
//kotlinLabSciExamplesHelpJMenuItem.setIcon(new ImageIcon(groovyImage));
       

 
        
        examplesMenu.add(kotlinLabSciExamplesHelpJMenuItem);
        examplesMenu.add(kotlinLabSciExamplesHelpJTreeJMenuItem);

             



    mainJMenuBar.add(fileMenu);
    mainJMenuBar.add(importsMenu);
    mainJMenuBar.add(KotlinMenu);
    mainJMenuBar.add(librariesMenu);
    //mainJMenuBar.add(classpathMenu);
    mainJMenuBar.add(examplesMenu);
    mainJMenuBar.add(appearanceMenu);
    mainJMenuBar.add(recentPaneFilesMenu);
    
    
    currentFrame.setJMenuBar(mainJMenuBar);

    currentFrame.setTitle(titleStr+":  File: "+selectedValue);
        
// use user settings for edit frames to adjust location and size
        currentFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
      
        
        
  // load the file      
                   FileReader fr = null;
            try {
                fr = new FileReader(selectedValue);
                if (fr != null)
                  jep.read(fr, null);
                
            } catch (FileNotFoundException ex) {
                System.out.println("file "+selectedValue+" not found");
            }
            catch (IOException ex) {
                    System.out.println("cannot close file "+selectedValue);
                }
            finally {
                try {
        if (fr!=null)
            fr.close();
   
        
                } 
                
                catch (IOException ex) {
                    System.out.println("cannot close file "+selectedValue);
                }
            
            }
        
        Rectangle  b = GraphicsEnvironment.getLocalGraphicsEnvironment().getMaximumWindowBounds();
        if (GlobalValues.rememberSizesFlag == false) {
        currentFrame.setSize( (b.width / 2)-20, b.height * 5 / 6 );
        currentFrame.setLocation(50, 100);
        }
        else {
            currentFrame.setSize(GlobalValues.rsizeX, GlobalValues.rsizeY);
            currentFrame.setLocation(GlobalValues.rlocX, GlobalValues.rlocY);
        }
        currentFrame.setVisible(true);
     
        JPopupMenu popup = jep.getPopupMenu();
        popup.addSeparator();

     
        
        scrPane = new RTextScrollPane(jep);
        //scrPane.setFoldIndicatorEnabled(true);
        
        toolBar.add(progressComputationLabel);
        progressComputationLabel.setVisible(false);
        
        currentFrame.add(toolBar, BorderLayout.NORTH);
 
        currentFrame.add(scrPane);
        currentFrame.setTitle(titleStr+":  File: "+selectedValue);
        
        //   if that Editor Frame is the main Editor frame, additional settings is required e.g.
        //   adding  the console output frame at the bottom 
   if (isMainFrame) {
        
        JSplitPane sp = new JSplitPane(SwingConstants.HORIZONTAL);
        sp.setTopComponent(scrPane);
        sp.setBottomComponent(GlobalValues.outputPane);
        
              
        currentFrame.add(sp);
     //   currentFrame.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        currentFrame.setVisible(true);
        sp.setDividerLocation( 0.7 );
                
            
   }
        else {
 fileMenu.add(exitJMenuItem); 
  }
            
        return jep;
      
    }


        @Override
    public void windowOpened(WindowEvent e) {
    }

    @Override
    public void windowClosing(WindowEvent e) {
       System.exit(0);
    }

    @Override
    public void windowClosed(WindowEvent e) {
      System.exit(0);
    }

    @Override
    public void windowIconified(WindowEvent e) {
    }

    @Override
    public void windowDeiconified(WindowEvent e) {
    }

    @Override
    public void windowActivated(WindowEvent e) {
    }

    @Override
    public void windowDeactivated(WindowEvent e) {
    }


        

    
    // gets the signal name that the caret is sitting on
    public String getSignalNameAtCaret(JTextComponent tc) throws BadLocationException {
        int caret = tc.getCaretPosition();
        int start = caret;
        Document doc = tc.getDocument();
        while (start > 0) {
            char ch = doc.getText(start-1, 1).charAt(0);
            if (isSignalNameChar(ch)) {
                start--;
            }
            else {
                break;
            }
          }
        int end = caret;
        while (end < doc.getLength()) {
            char ch = doc.getText(end, 1).charAt(0);
            if (isSignalNameChar(ch)) {
                end++;
            }
            else {
                break;
            }
        }
        return doc.getText(start, end-start);
    }
    
    public boolean isSignalNameChar(char ch) {
        return Character.isLetterOrDigit(ch) || ch == '_';
    }
    

    private void performSearch(boolean forward) {
        SearchContext context = new SearchContext();
        String text = searchField.getText();
        if (text.length() ==0)
            return;
        context.setSearchFor(text);
        context.setMatchCase(matchCaseCB.isSelected());
        context.setRegularExpression(regexCB.isSelected());
        context.setSearchForward(forward);
        context.setWholeWord(false);
        
            SearchResult found = SearchEngine.find(jep, context);
        if (!found.wasFound())
            JOptionPane.showMessageDialog(this, "Text not found");
    }
    
   
        
        
        
        
   
    // edit the file with name selectedValue
    public kotlinLabEditor(String selectedValue) {
        RSyntaxTextArea  jep = commonEditingActions(selectedValue, false);
        GlobalValues.globalEditorPane = jep;
        
 }
    

         // edit the file with name selectedValue
    public kotlinLabEditor(String selectedValue, boolean initConsoleWindow) {
        RSyntaxTextArea  jep = commonEditingActions(selectedValue, true);
        GlobalValues.globalEditorPane = jep;
        
    }
   
      
   

    // save the current file kept in editor
class saveEditorTextAction extends AbstractAction  {
    public saveEditorTextAction() { super("Save Editor Text"); }
    public void actionPerformed(ActionEvent e)  {
        String saveFileName = editedFileName;   // file name to save is the one currently edited
        if (saveFileName == null)  { // not file specified thus open a FileChooser in order the user to determine it
        javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
        
        int retVal = chooser.showSaveDialog(GlobalValues.globalEditorPane);
        
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 saveFileName = selectedFile.getAbsolutePath();
                 editedFileName = saveFileName;    // update the edited file
                 GlobalValues.myKEdit.setTitle(titleStr+":  File: "+editedFileName);
   
         }
        }
        
        File saveFile = new File(saveFileName);
                    try {
                        FileWriter fw = new FileWriter(saveFile);
                        jep.write(fw);
                        editorTextSaved = true;  //  not need to save anything yet
                        
                    } catch (FileNotFoundException ex) {
                        System.out.println("Cannot open file "+saveFile+" for saving editor text "+ex.getMessage());
                    }
                    catch (Exception ex) {
                        System.out.println("Exception writing editor's text "+ex.getMessage());
                    }
                           
    }
  }

  // save the contents of the edit buffer to a file, asking the user to specify it 
class saveAsEditorTextAction extends AbstractAction  {
    public saveAsEditorTextAction() { super("Save As Editor Text"); }
    public void actionPerformed(ActionEvent e)  {
        javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
        
        int retVal = chooser.showSaveDialog(GlobalValues.globalEditorPane);
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 String saveFileName = selectedFile.getAbsolutePath();
                 File saveFile = new File(saveFileName);
                    try {
                        FileWriter fw = new FileWriter(saveFile);
                        jep.write(fw);
                        editorTextSaved = true;  //  not need to save anything yet
                     
                        kotlinLabEditor.currentFrame.setTitle(titleStr+":  File: "+editedFileName);

                        //  add the loaded file to the recent files menu
            if (recentPaneFiles.contains(saveFileName) ==  false)  {
                recentPaneFiles.add(saveFileName);
                updateRecentPaneFilesMenu();
              }

            // update the workingDir
            String pathOfLoadFileName = saveFileName.substring(0, saveFileName.lastIndexOf(File.separatorChar));
            GlobalValues.workingDir = pathOfLoadFileName;
            
                    } catch (FileNotFoundException ex) {
                        System.out.println("Cannot open file "+saveFile+" for saving editor text "+ex.getMessage());
                    }
                    catch (Exception ex) {
                        System.out.println("Exception writing editor's text "+ex.getMessage());
                    }
                           
    }
  }
    }



// load a new file for editing
class loadEditorTextAction extends AbstractAction  {
    public loadEditorTextAction() { super("Load Editor Text"); }
    public void actionPerformed(ActionEvent e)  {
           int userOption = JOptionPane.CANCEL_OPTION;
            if (editorTextSaved == false ) 
      userOption = JOptionPane.showConfirmDialog(null, "File: "+editedFileName +" not saved. Proceed? ", 
                        "Warning: Exit without Save?", JOptionPane.CANCEL_OPTION);
            else userOption = JOptionPane.YES_OPTION;
            if (userOption == JOptionPane.YES_OPTION)  {
         
        javax.swing.JFileChooser chooser = new JFileChooser(new File(GlobalValues.workingDir));
        
        int retVal = chooser.showOpenDialog(GlobalValues.globalEditorPane);
        if (retVal == JFileChooser.APPROVE_OPTION) { 
                 File selectedFile = chooser.getSelectedFile();
                 String loadFileName = selectedFile.getAbsolutePath();
                       
                   FileReader fr = null;
            try {
                fr = new FileReader(loadFileName);
                jep.read(fr, null);
  
           //  add the loaded file to the recent files menu
            if (recentPaneFiles.contains(loadFileName) ==  false)  {
                recentPaneFiles.add(loadFileName);
                updateRecentPaneFilesMenu();
              }
        }
            catch (FileNotFoundException ex) {
                System.out.println("file "+loadFileName+" not found");
            }
            catch (IOException ex) {
                    System.out.println("cannot close file "+loadFileName);
                }
            finally {
                try {
                    fr.close();
                } 
                catch (IOException ex) {
                    System.out.println("cannot close file "+loadFileName);
                }
            }
            
            editedFileName = loadFileName;   // current file is the new loaded one
            // update the workingDir
            String pathOfLoadFileName = editedFileName.substring(0, editedFileName.lastIndexOf(File.separatorChar));
            GlobalValues.workingDir = pathOfLoadFileName;
            
            editorTextSaved = true;  // a freshly loaded file doesn't require saving
            kotlinLabEditor.currentFrame.setTitle(titleStr+":  File: "+editedFileName);
                           
     }
   }
 }
    }
}


       
