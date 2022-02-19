
package kotlinLabSci.math.plot.plots;

import kotlinLabSci.math.plot.render.AbstractDrawer;

public abstract class LayerPlot extends Plot {
    Plot plot;
    public LayerPlot(String name, Plot p) {
        super(name, p.color);
        plot = p;
    }

    public double[] isSelected(int[] screenCoordTest, AbstractDrawer draw) {
        return null;
    }
}
