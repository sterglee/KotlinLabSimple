package kotlinLabSci.math.plot.plotObjects;

import kotlinLabSci.math.plot.render.AbstractDrawer;


public interface Noteable {
    public double[] isSelected(int[] screenCoord, AbstractDrawer draw);
    public void note(AbstractDrawer draw);
}