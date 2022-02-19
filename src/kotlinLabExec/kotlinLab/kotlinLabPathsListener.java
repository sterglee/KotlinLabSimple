
package kotlinLabExec.kotlinLab;

import java.io.*;
import javax.swing.*;
import javax.swing.tree.*;
import javax.swing.event.*;

// implements the functionality for browsing using jLabScriptExplorer and jLabClassPath
public class kotlinLabPathsListener implements TreeSelectionListener   {
    JTree  pathsTree;
  // the selectedValue keeps the full pathname of the selected object for further processing
    public static String selectedValue;
    public static String selectedPath; 
    public static DefaultMutableTreeNode   parentOfSelectedNode;  // the parent node of the selected node of the JTree
    public static DefaultMutableTreeNode   selectedNode;  // the currently selected node of the JTree
    
    public kotlinLabPathsListener(JTree classPathsTree) {
      pathsTree = classPathsTree;
      
    }
    
    public void explicitUpdate()  {
                             TreePath path = pathsTree.getSelectionPath();
               if (path==null) return;  // not any tree's node selected
    		  selectedNode =  (DefaultMutableTreeNode) path.getLastPathComponent();
                parentOfSelectedNode = (DefaultMutableTreeNode) selectedNode.getParent(); 
    		Object [] objPath = selectedNode.getUserObjectPath();
                int len = objPath.length;
        // for nested objects, their path is repeated as the parent node, so concatenate the parent and the filename to build the complete path          
                  kotlinLabPathsListener.selectedValue = objPath[len-1].toString();
                  kotlinLabPathsListener.selectedPath = selectedValue.substring(0, selectedValue.lastIndexOf(File.separator));
                  
          
    }
    @Override
    	  public void valueChanged(TreeSelectionEvent event) {
              new Thread(() -> {
                  // run-out
                  SwingUtilities.invokeLater(() -> {
                      // run in  */
                      
                      TreePath path = pathsTree.getSelectionPath();
                      if (path==null) return;  // not any tree's node selected
                      selectedNode =  (DefaultMutableTreeNode) path.getLastPathComponent();
                      parentOfSelectedNode = (DefaultMutableTreeNode) selectedNode.getParent();
                      Object [] objPath = selectedNode.getUserObjectPath();
                      int len = objPath.length;
                      // for nested objects, their path is repeated as the parent node, so concatenate the parent and the filename to build the complete path
                      kotlinLabPathsListener.selectedValue = objPath[len-1].toString();
                      kotlinLabPathsListener.selectedPath = selectedValue.substring(0, selectedValue.lastIndexOf(File.separator));
                  } // Runnable-in
                  );  // Runnable-in
              } // run-out
              // Runnable-out
              ).start();  // Runnable-out
                
          }  // valueChanged


}
