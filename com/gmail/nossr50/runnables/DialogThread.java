package com.gmail.nossr50.runnables;

import javax.swing.JOptionPane;

/**
 * Dialogs cause the program to hang if they aren't in their own thread
 * This class creates a dialog outside of the main thread
 * @author nossr50
 *
 */
public class DialogThread implements Runnable {
    
    private String dialogTxt;
    
    public DialogThread(String txt)
    {
        dialogTxt = txt;
    }

    @Override
    public void run() {
        // TODO Auto-generated method stub
        JOptionPane.showMessageDialog(null, dialogTxt);
    }

}
