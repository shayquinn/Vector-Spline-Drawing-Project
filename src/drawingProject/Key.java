
package drawingProject;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

/**
 * The Key class implements the KeyListener interface and handles key events by printing the key code
 * and performing different actions based on the key pressed.
 */
public class Key implements KeyListener {

    @Override
    public void keyTyped(KeyEvent e) {
    }//end keyTyped

    @Override
    public void keyPressed(KeyEvent e) {
        System.out.println("key: " + e.getKeyCode()+"\n");
        switch (e.getKeyCode()) {
            case KeyEvent.CTRL_DOWN_MASK:
                System.out.println("CTRL_DOWN_MASK");
                break;
            case KeyEvent.VK_0:
                break;
            case KeyEvent.VK_1:
                break;
            case KeyEvent.VK_2:
                break;
            case KeyEvent.VK_3:
                break;
            case KeyEvent.VK_4:
                break;
            case KeyEvent.VK_5:
                break;
            case KeyEvent.VK_6:
                break;
            case KeyEvent.VK_7:
                break;
            case KeyEvent.VK_8:
                break;
            case KeyEvent.VK_9:
                break;
            case KeyEvent.VK_LEFT:
                break;
            case KeyEvent.VK_RIGHT:
                break;
            case KeyEvent.VK_UP:
                break;
            case KeyEvent.VK_DOWN:
                break;
            case KeyEvent.VK_Q:
                break;
            case KeyEvent.VK_A:
                break;
            case KeyEvent.VK_SPACE:
                break;
            default:
                System.out.println("keyPressed switch Error!");
                break;
        }
    }//end keyPressed

    @Override
    public void keyReleased(KeyEvent e) {
    }//end keyReleased

}
