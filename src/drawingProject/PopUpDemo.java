
package drawingProject;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

class PopUpDemo extends JPopupMenu {
    JMenuItem o1, o2, o3, o4;
    public PopUpDemo() {
        o1 = new JMenuItem("Open");
        o2 = new JMenuItem("Close");
        o3 = new JMenuItem("Edit");
        o4 = new JMenuItem("Save");
       
        add(o1);
        add(o2);
        add(o3);
        add(o4);
    }
}
