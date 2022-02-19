package kotlinLabEdit;

import kotlin.io.LineReader;
import kotlin.script.experimental.api.ResultWithDiagnostics;
import kotlinLabGlobal.Interpreter.GlobalValues;
import kotlinLabExec.gui.AutoCompletionFrame;
import kotlinLabExec.gui.DetailHelpFrame;

import java.awt.Container;
import java.awt.Point;

import java.awt.event.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import javax.script.ScriptException;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import org.fife.ui.rsyntaxtextarea.RSyntaxDocument;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;



public class EditorKeyMouseHandler extends MouseAdapter implements KeyListener
{
    IntStream x;
        int caretPos = 0;      // track the cursor position 
        int prevCaretPos = 0;
        int  textLen = 0;  // the text lenngth
        int fromLoc = 0;
        int toLoc = 0;
       
        public RSyntaxTextArea  editorPane=null;    // the component that keeps and handles the editing text
        public RSyntaxDocument  docVar=null; 
        public RSyntaxDocument syntaxDocument=null;
    private ResultWithDiagnostics<?> result;

    public EditorKeyMouseHandler()
	{
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
       String selectedTextOrCurrentLine = editorPane.getSelectedText();
       if (selectedTextOrCurrentLine==null)
           selectedTextOrCurrentLine = getCurrentLine();
       
       return selectedTextOrCurrentLine;
   }
     

        
         
    public void keyTyped(KeyEvent e){
       /* int  keyValue = e.getKeyChar();
       
        if (keyValue == KeyEvent.VK_F10);
                 display_help();      */
   }


        
        private static void  processListSelection(JList clList) { 
                String selected = (String) clList.getSelectedValue();
              
                GlobalValues.globalEditorPane.setSelectionStart(GlobalValues.selectionStart);
                GlobalValues.globalEditorPane.setSelectionEnd(GlobalValues.selectionEnd);
                
                int leftParenthesisIndex = selected.indexOf('(');
                if (leftParenthesisIndex != -1) 
                    selected = selected.substring(0, leftParenthesisIndex+1);
                    
                if (GlobalValues.methodNameSpecified==false)
                    selected = "." + selected;  // append a dot
                
                GlobalValues.globalEditorPane.replaceSelection( selected);
                  GlobalValues.selectionEnd = GlobalValues.selectionStart+selected.length();
        }
        
              
        
        private static String detectWordAtCursor() {
                
         RSyntaxTextArea  editor = GlobalValues.globalEditorPane;   // get RSyntaxArea based editor instance
         
         int  pos = editor.getCaretPosition()-1;      // position of the caret
         Document  doc = editor.getDocument();  // the document being edited
       
         GlobalValues.methodNameSpecified = false;
         GlobalValues.selectionStart = -1;
         
       boolean  exited = false;
        // take word part before cursor position
       String  wb = "";  // constructs the word before the cursor
       int  offset = pos;
       while (offset >= 0 && exited==false) {
         char  ch = 0;
            try {
                ch = doc.getText(offset, 1).charAt(0);
                if (ch == '.') {  // a method specified
                    GlobalValues.methodNameSpecified = true;
                    GlobalValues.selectionStart = offset+1;   // mark the start of the method
                }
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
         boolean  isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9') || ch=='.' || ch=='_';
         if (!isalphaNumeric)  exited = true;
          else {
           wb = wb + ch;
           offset--;
            }
          }
         
       if (GlobalValues.selectionStart == -1)  // a method name is not specified, thus set selection start to the beginning of the word
      GlobalValues.selectionStart = pos+1;
    
          // take word part after cursor position
       String  wa = "";
       int  docLen = doc.getLength();
       offset = pos+1;
       exited = false;
       while (offset < docLen && exited==false) {
         char  ch = 0;
            try {
                ch = doc.getText(offset, 1).charAt(0);
                if (ch == '.')  GlobalValues.methodNameSpecified = true;
            } catch (BadLocationException ex) {
                ex.printStackTrace();
            }
         boolean  isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9') || ch=='.' ||  ch=='_';
         if (!isalphaNumeric)  exited = true;
           else {
         wa = wa + ch;
         offset++;
           }
         }
       GlobalValues.selectionEnd = offset;
  
      //   form total word that is under caret position
         // reverse string at the left of cursor
       StringBuffer sb = new StringBuffer(wb);
       StringBuffer rsb = new StringBuffer(wb);
       int p = 0;
       for (int k= wb.length()-1; k>=0; k--)
           rsb.setCharAt(p++, sb.charAt(k));
        
       // concatenate to form the whole word
       String  wordAtCursor = rsb.toString()+wa;

       wordAtCursor = wordAtCursor.trim();  // trim any spaces

       GlobalValues.textForCompletion = wordAtCursor; 
       
        return wordAtCursor;
        }



        public void clickComplete() {
            String currentTextForEval = getSelectedTextOrCurrentLine().trim();
        }

        public void clickExecuteScriptEngine() {
            Runnable execCode = ()-> {
                String currentTextForEval = getSelectedTextOrCurrentLine().trim();

                try {
                    if (GlobalValues.kotlinEngine == null) {
                        return;
                    }

                    GlobalValues.globalEditorPane.setCursor(GlobalValues.waitCursor);

          Object evalResult =   GlobalValues.kotlinEngine.eval(currentTextForEval);


          // for assignment statements, display the value assigned
        String[] leftRight = currentTextForEval.split("=");
              String varname = leftRight[0];
                    if (varname != null) {

                        if (varname.contains("(") == false) { // not a function
                  varname = varname.replace("var", "");
                  varname = varname.replace("val", "");
                  varname = varname.trim();
                  System.out.println(varname);

                      Object ores = GlobalValues.kotlinEngine.eval(varname);
                      if (ores != null) {
                          String res = ores.toString();
                          System.out.println(res);
                      }
                      else
                          System.out.println(evalResult);
                  }
              } // not a function
          //}
              GlobalValues.globalEditorPane.setCursor(GlobalValues.defaultCursor);

                } catch (Exception ex) {
                    System.out.println(ex.getMessage());
                    GlobalValues.globalEditorPane.setCursor(GlobalValues.defaultCursor);
                 }
                } ;

                GlobalValues.execService.execute(execCode);



        }



        
        
	/**Interpret key presses*/
    public void keyPressed(final KeyEvent e)
    {
        kotlinLabEditor.documentEditsPendable = true;
        int keyValue = e.getKeyCode();
        editorPane  = (RSyntaxTextArea)e.getSource();  // editor's keystrokes have as source the inputTextComponent JTextComponent
        prevCaretPos = caretPos;   
        
        switch (keyValue) {
                        

            case   KeyEvent.VK_ENTER:
                caretPos = editorPane.getCaretPosition();
                String text = editorPane.getText();
                int newLineCnt = 0;
                int idx = 0;
                while (idx<caretPos)   {
                    if (text.charAt(idx) == '\n') 
                       newLineCnt++;
                    idx++;
                    
                }
                break;







                
          // Execute code with Kotlin
            case KeyEvent.VK_F6:
                clickExecuteScriptEngine();
                e.consume();
                break;

            case KeyEvent.VK_F10:
                clickComplete();
                e.consume();
                break;

     case KeyEvent.VK_F5:
         GlobalValues.consoleOutputWindow.resetText( " ");
         e.consume();
         break;
        
            case KeyEvent.VK_F2:
     String etext =  editorPane.getText();
     int currentTextLen = etext.length();
     if  (currentTextLen != textLen)   // text altered at the time between F2 clicks
      {
         fromLoc = 0;    // reset
     }
    
     int cursorLoc = editorPane.getCaretPosition();
     if (cursorLoc < toLoc)  {
     // reset if cursor is within the already executed part
         fromLoc = 0;
     }
     toLoc = cursorLoc;
     String textToExec = etext.substring(fromLoc, toLoc);
     
     editorPane.setSelectionStart(fromLoc);
     editorPane.setSelectionEnd(toLoc);
     editorPane.setSelectedTextColor(java.awt.Color.RED);
     textToExec = textToExec.substring(0, textToExec.lastIndexOf("\n"));
     fromLoc += textToExec.length();
     
      String grResult = ""; 
     GlobalValues.consoleOutputWindow.output.append("\n"+grResult);
     GlobalValues.consoleOutputWindow.output.setCaretPosition(GlobalValues.consoleOutputWindow.output.getText().length());

      e.consume();
    break;

            

                    
            default:
                caretPos = editorPane.getCaretPosition();
                break;            
          }
    }
    
         
    public void mouseClicked(MouseEvent me)
        { 
            
   if (me.getClickCount()>=2)  {  //only on ndouble-clicks
       RSyntaxTextArea    editor = (RSyntaxTextArea) me.getSource();
       Point  pt = new Point(me.getX(), me.getY());
       int  pos = editor.viewToModel(pt);
       javax.swing.text.Document  doc = editor.getDocument();
       
       boolean  exited = false;
       String  wb = "";
       int  offset = pos;
         // extract the part of the word before the mouse click position
       while (offset >= 0 && exited==false) {
         char  ch=' ';
                try {
                    ch = doc.getText(offset, 1).charAt(0);
                } catch (BadLocationException ex) {
                    System.out.println("Bad Location exception");
                    ex.printStackTrace();
                }
         boolean  isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9');
         if (!isalphaNumeric)  exited=true;
          else {
           wb = wb + ch;
           offset -= 1;
           }
          }
       
       String  wa = "";
       int  docLen = doc.getLength();
       offset = pos+1;
       exited = false;
         // extract the part of the word after the mouse click position
       while (offset < docLen && exited==false) {
         char ch=' ';
                try {
                    ch = doc.getText(offset, 1).charAt(0);
                } catch (BadLocationException ex) {
                     System.out.println("Bad Location exception");
                     ex.printStackTrace();
               }
         boolean  isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9');
         if (!isalphaNumeric)  exited=true;
           else {
         wa = wa + ch;
         offset += 1;
           }
         }
         
         StringBuffer wbreverse = new StringBuffer();
         for (int k=wb.length()-1; k>=0; k--)
             wbreverse.append(wb.charAt(k));
         
         String  wordAtCursor = wbreverse.toString()+wa;       
          
      
       }
      }
              
    
    
    void display_help() {
        JFrame helpFrame = new JFrame("Glab help");
        helpFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        helpFrame.setSize(400, 400);
        Container container = helpFrame.getContentPane();
        JTextArea helpText = new JTextArea();

        int classCnt = 0;
        Hashtable  clTable= new Hashtable(); 
        Enumeration enumer = clTable.elements();
        TreeSet  sortedClasses =  new TreeSet();
        while(enumer.hasMoreElements())
		{
		    Object next = (Object)enumer.nextElement();
		    Class currentClass = (Class)next;
                    String className = currentClass.getCanonicalName();
                    sortedClasses.add(className);
                    classCnt++;
        }

          Iterator iter = sortedClasses.iterator();
          while (iter.hasNext()) {
                    String className = (String)iter.next();
                    helpText.append(className+"\n");
            }
          JScrollPane  helpPane = new JScrollPane(helpText);
        
        container.add(helpPane);
        helpFrame.setVisible(true);  
                
      }
    
        
    
    public void keyReleased(KeyEvent e)
    {
    	        
    }



       
 
}
