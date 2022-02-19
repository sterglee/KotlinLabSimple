package kotlinLabSci.math.plot.plotObjects;

import kotlinLabSci.math.plot.render.AbstractDrawer;


public interface Editable {
    public double[] isSelected(int[] screenCoord, AbstractDrawer draw);
    public void edit(Object editParent);
    public void editnote(AbstractDrawer draw);
}
