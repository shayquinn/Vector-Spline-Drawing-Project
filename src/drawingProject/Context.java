/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawingProject;

import java.awt.event.MouseEvent;

/**
 *
 * @author shays
 */
public class Context {
    public void doPop(MouseEvent e) {
    PopUpDemo menu = new PopUpDemo();
    menu.show(e.getComponent(), e.getX(), e.getY());
    }//end doPop
}
