
package kotlinLabEdit;



import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.Point;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.script.ScriptException;
import javax.swing.text.BadLocationException;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import kotlinLabGlobal.Interpreter.GlobalValues;
import javax.swing.text.Document;


class RSyntaxEditorMouseMotionAdapter  extends MouseMotionAdapter {
    @Override
     public void  mouseMoved(MouseEvent e)  {  // mouseMoved
   
        RSyntaxTextArea  editor = (RSyntaxTextArea) e.getSource();
       if (editor != null) {
         Point   pt = new Point(e.getX(), e.getY());
         if (pt.getX() > 0 && pt.getY() > 0 ) {
           int  pos = editor.viewToModel(pt);
           Document  doc = editor.getDocument();

         // try to detect the word part before the cursor
         boolean  exited = false;
         String   wb = "";
         int   offset = pos;
       while (offset >= 0 && exited==false) {
         char  ch=' ';
            try {
                ch = doc.getText(offset, 1).charAt(0);
            } catch (BadLocationException ex) {
                Logger.getLogger(RSyntaxEditorMouseMotionAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
         boolean   isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9');
         if (!isalphaNumeric)  exited=true;
          else {
           wb = wb + ch;
           offset -= 1;
          }
          }
       
       // try to detect the word part after the cursor
       String   wa = "";
       int  docLen = doc.getLength();
       offset = pos+1;
       exited = false;
       while (offset < docLen && exited==false) {
        char  ch=' ';
            try {
                ch = doc.getText(offset, 1).charAt(0);
            } catch (BadLocationException ex) {
                Logger.getLogger(RSyntaxEditorMouseMotionAdapter.class.getName()).log(Level.SEVERE, null, ex);
            }
         boolean  isalphaNumeric = ( ch >= 'a' && ch <='z')  || (ch >= 'A' && ch <='Z') || (ch >= '0' && ch <='9');
         if (!isalphaNumeric)  exited=true;
           else {
         wa = wa + ch;
         offset += 1;
           }
         }
       
       // reverse string at the left of cursor
       StringBuffer sb = new StringBuffer(wb);
       StringBuffer rsb = new StringBuffer(wb);
       int p = 0;
       for (int k= wb.length()-1; k>=0; k--)
           rsb.setCharAt(p++, sb.charAt(k));
              
       // concatenate to form the whole word
       final String  wordAtCursor = (rsb.toString()+wa).trim();

            if (wordAtCursor.length() > 0 && Character.isLetter(wordAtCursor.charAt(0))) {
                   try {
                       String evResult = GlobalValues.kotlinEngine.eval(wordAtCursor).toString();

                       if (evResult.length() > 1 ) {
                           GlobalValues.globalEditorPane.setToolTipText(evResult);

                       }
                   }
                       catch (ScriptException ex) {
                     // ex.printStackTrace();
                  }
              }

       } // wordAtCursor.length() > 0
     
          else
      GlobalValues.globalEditorPane.setToolTipText("");
          
    }  

}
}  // mouseMoved

  
  
