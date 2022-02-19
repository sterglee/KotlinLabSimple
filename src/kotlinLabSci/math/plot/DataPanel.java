package kotlinLabSci.math.plot;

import kotlinLabSci.math.plot.components.DataToolBar;
import kotlinLabSci.math.io.StringPrintable;
import kotlinLabSci.math.io.FilePrintable;
import kotlinLabSci.math.io.ClipBoardPrintable;
import java.awt.*;
import java.awt.event.*;
import java.io.*;

import javax.swing.*;


// 09-Jan
public abstract class DataPanel extends JPanel implements ComponentListener, FilePrintable, ClipBoardPrintable, StringPrintable {
    protected DataToolBar toolBar;
    protected JScrollPane scrollPane;
    public static int[] dimension = new int[] { 300, 300 };

    public DataPanel() {
        setLayout(new BorderLayout());
        initToolBar();
        init();
        }

    protected void initToolBar() {
        toolBar = new DataToolBar(this);
        add(toolBar, BorderLayout.NORTH);
        toolBar.setFloatable(false);
    }

    protected void initSize() {
    	scrollPane.setSize(dimension[0], dimension[1]); //this.getSize());
		// scrollPane.setPreferredSize(this.getSize());
}

    protected void init() {
	addComponentListener(this);
    }

    public void update() {
	toWindow();
	repaint();
    }
    protected abstract void toWindow();

    public abstract void toClipBoard();

    public abstract void toASCIIFile(File file);

    public void componentHidden(ComponentEvent e) {
    }

    public void componentMoved(ComponentEvent e) {
    }

    public void componentResized(ComponentEvent e) {
		
        initSize();
    }

    public void componentShown(ComponentEvent e) {
    }

}