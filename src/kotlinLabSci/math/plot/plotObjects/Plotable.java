package kotlinLabSci.math.plot.plotObjects;

import kotlinLabSci.math.plot.render.AbstractDrawer;
import java.awt.*;


// interface that an object should implement in order to be plottable
public interface Plotable {
    public void plot(AbstractDrawer draw);
    public void setVisible(boolean v);
    public boolean getVisible();
    public void setColor(Color c);
    public Color getColor();

}