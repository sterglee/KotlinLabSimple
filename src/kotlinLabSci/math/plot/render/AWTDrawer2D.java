package kotlinLabSci.math.plot.render;

import kotlinLabSci.math.plot.canvas.PlotCanvas;

public class AWTDrawer2D extends AWTDrawer {

	public AWTDrawer2D(PlotCanvas _canvas) {
		super(_canvas);
		projection = new Projection2D(this);
	}

}
