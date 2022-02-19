
package kotlinLabEdit;

import java.awt.event.*;
import javax.swing.*;
import java.io.*;




import kotlinLabGlobal.Interpreter.GlobalValues;
import kotlinLabExec.kotlinLab.StreamGobbler;
import java.awt.BorderLayout;
import java.awt.GridLayout;





class kotlinLabSciExamplesJTreeAction extends AbstractAction {
       kotlinLabSciExamplesJTreeAction() {
           super("kotlinLabSci Examples with JTree displaying");
       }

    public void actionPerformed(ActionEvent e) {
        
   kotlinLabExec.gui.watchExamples weObj = new kotlinLabExec.gui.watchExamples();
   weObj.scanMainJarFile(".ksci");
   weObj.displayExamples("kotlinLabSci Examples");
   }
   }





   class kotlinLabSciPlotExamplesJTreeAction extends AbstractAction {
       kotlinLabSciPlotExamplesJTreeAction() {
           super("kotlinLabSci Plot Examples with JTree displaying");
       }

    public void actionPerformed(ActionEvent e) {
        
   kotlinLabExec.gui.watchExamples weObj = new kotlinLabExec.gui.watchExamples();
   weObj.scanMainJarFile(".plots-jsci");
   weObj.displayExamples("kotlinLabSci Plot Examples");
   }
   }

   


   
      

   
   
   
    

   class editAction extends AbstractAction {
       public editAction()  { super("kotlinLab Editor");}
       public void actionPerformed(ActionEvent e) {
                           SwingUtilities.invokeLater(new Runnable() {
           public void run() {  // run in  */
                   GlobalValues.myKEdit = new kotlinLabEditor(GlobalValues.workingDir+File.separatorChar+"Untitled");
           }
                     });
                    }
       }
   
   
   

           






