package kotlinLabExec.gui;

import java.util.*;

/**  class AutoCompletionWorkspace implements autocompletion support for the variables in global workspace
 *  this support is triggered with the TAB key
 */
public class AutoCompletionWorkspace
{
    static public int mxCntOfVariables = 100;  // init time load of variables  for autocompletion
    static public Vector  scanVariables = new Vector(mxCntOfVariables);
    static String []  listOfAllVariables;
    
    public AutoCompletionWorkspace()
    {
     
   }
   
   /**
     * Return those functions starting with the prefix.

     * @param prefix Prefix of the function name.
     * @return An array of function (full) names. If nothing can be matched, it
     * returns null
     */
    public String[] getMatched(String prefix)
    {
        if (prefix.equals(""))    {  // all methods match to a null string
            return listOfAllVariables;
         }
        int i = firstIndexOfMatchedString(prefix);
        int j = lastIndexOfMatchedString(prefix, i);
        String[] matches = new String[j-i];

        for (int k = 0; k < matches.length; k++)
        {
            matches[k] = listOfAllVariables[i+k];
        }

        return matches;
    }



    private int firstIndexOfMatchedString(String prefix)
    {
        int up = 1;
        int low = 0;
        int ce;  // curerntly examined element
        int prefLen = prefix.length();
        do
        {
            low += up/2;
            up = 1;
            ce = up+low-1;
            while (ce < AutoCompletionWorkspace.scanVariables.size())
            {
                String currentDescription = listOfAllVariables[ce];
                int cLen = currentDescription.length();
                
                int k = (cLen < prefLen) ? cLen : prefLen;
                int m = currentDescription.substring (0, k).compareToIgnoreCase (prefix);
                if (cLen >= prefLen && m >= 0)
                {
                    low  += up/2;
                    if (up == 1) { break; }
                    up = 1;
                }
                else
                {
                    up *= 2;
                }
                ce = up+low-1;
            
            }
        }
        while (up != 1);

        return low;
    }



    private int lastIndexOfMatchedString(String prefix, int startingPoint)
    {
     
        int up = 1;
        int low = startingPoint;
        int ce;  // curerntly examined element
        int prefLen = prefix.length();
        int currentMethodCnt = AutoCompletionWorkspace.scanVariables.size();
            
        do
        {
            low += up/2;
            up = 1;
            ce = up+low-1;
            while (ce < currentMethodCnt)
            {
                String currentDescription = listOfAllVariables[ce];
                int cLen = currentDescription.length();
                
                int k = (cLen < prefLen) ? cLen : prefLen;
                int m = currentDescription.substring (0, k).compareToIgnoreCase (prefix);
                if (cLen >= prefLen && m > 0)
                {
                    low  += up/2;
                    if (up == 1) { break; }
                    up = 1;
                }
                else
                {
                    up *= 2;
                }
                ce = up+low-1;
            
            }
        }
        while (up != 1);

        return low;
    }
}


