
package kotlinLabSci.math.plot.plotObjects;

import kotlinLabSci.math.plot.render.AbstractDrawer;
import kotlinLabSci.math.plot.Plot2DPanel;
import kotlinLabSci.math.plot.Plot3DPanel;
import kotlinLabSci.math.plot.PlotPanel;
import kotlinLabSci.math.plot.FrameView;
import java.awt.*;
import java.io.*;

import javax.swing.*;


public class RasterImage implements Plotable{

    File source;
    Image img;
    double[] xyzSW, xyzSE,xyzNW;
	
    boolean visible = true;
    float alpha;
	
    public RasterImage(File _source, float _alpha, double[] _xyzSW, double[] _xyzSE, double[] _xyzNW) {
        source = _source;
        img =  Toolkit.getDefaultToolkit().getImage(source.getPath());
        xyzSW = _xyzSW;
        xyzSE = _xyzSE;
        xyzNW=_xyzNW;
        alpha = _alpha;
}
	
    @Override
    public void plot(AbstractDrawer draw) {
        if (!visible) return;
        draw.drawImage(img,alpha, xyzSW, xyzSE,xyzNW);
}

    @Override
    public void setVisible(boolean v) {
        visible = v;
}

    @Override
    public boolean getVisible() {
        return visible;
}
	
    @Override
    public void setColor(Color c) {
        throw new IllegalArgumentException("method not available for this Object: PlotImage");
}

    @Override
    public Color getColor() {
        return null;
}

    public static void main(String[] args) {
        Plot2DPanel p2 = new Plot2DPanel();
        for (int i = 0; i < 1; i++) {
            double[][] XYZ = new double[10][2];
            for (int j = 0; j < XYZ.length; j++) {
	XYZ[j][0] =/*1 + */Math.random();
	XYZ[j][1] = /*100 * */Math.random();
	}
   p2.addScatterPlot("toto" + i, XYZ);
   }
		

   p2.setLegendOrientation(PlotPanel.SOUTH);
   new FrameView(p2).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
    Plot3DPanel p = new Plot3DPanel();
    for (int i = 0; i < 1; i++) {
            double[][] XYZ = new double[10][3];
            for (int j = 0; j < XYZ.length; j++) {
	XYZ[j][0] = /*1 +*/ Math.random();
	XYZ[j][1] = /*100 **/ Math.random();
	XYZ[j][2] = /*0.0001 **/ Math.random();
	}
	p.addScatterPlot("toto" + i, XYZ);
    }

			// TODO this following case is not totally supported...
		//p.addPlotable(new PlotImage(new File("test.jpg"),0.5f,  new double[] {1,0,0},new double[] {1,1,0},new double[] {0,0,1}));

		
		p.setLegendOrientation(PlotPanel.SOUTH);
		p.setPreferredSize(new Dimension(600,600));
		new FrameView(p).setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);		
	}
	
}
