package kotlinLabSciCommands;
import kotlinLabGlobal.Interpreter.GlobalValues;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FontMetrics;
import java.awt.Graphics;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.ListCellRenderer;

// it is useful to visualize some properties of the completion list items, e.g.
// whether they are static
// For that purpose we implement a specialized font cell renderer
public class FontCellRenderer extends JComponent implements ListCellRenderer {
            private String currentText;  // the current text of the completion ite
            private Color background;
            private Color foreground;
            private int currentIndex;
            
        @Override
        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
            currentText = (String)value;
            background = isSelected ? list.getSelectionBackground() : list.getBackground();
            foreground = isSelected ? list.getSelectionForeground() :  list.getForeground();
            currentIndex = index;  // keep current index
            return this;
        }
        
        public void paintComponent(Graphics g) {
              // adjust font properly
            if (GlobalValues.isStaticMarks[currentIndex])
                GlobalValues.fontForCompletionListItem = GlobalValues.staticsFont;
            else
                GlobalValues.fontForCompletionListItem = GlobalValues.instancesFont;
            
            FontMetrics fm = g.getFontMetrics(GlobalValues.fontForCompletionListItem);
            g.setColor(background);
            g.fillRect(0, 0, getWidth(), getHeight());
            g.setColor(foreground);
            g.setFont(GlobalValues.fontForCompletionListItem);
            g.drawString(currentText, 0, fm.getAscent());
                     }
        
        public Dimension getPreferredSize() {
            String text = GlobalValues.fontForCompletionListItem.getFamily();
            Graphics g = getGraphics();
            FontMetrics fm = g.getFontMetrics(GlobalValues.fontForCompletionListItem);
            return new Dimension(fm.stringWidth(currentText), fm.getHeight());
        }
            
        }
       
