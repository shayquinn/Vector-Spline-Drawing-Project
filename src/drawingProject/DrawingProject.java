package drawingProject;

import java.awt.AlphaComposite;
import java.awt.BasicStroke;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.RenderingHints;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.GeneralPath;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import static javax.imageio.ImageIO.read;
import javax.swing.AbstractAction;
import javax.swing.ActionMap;
import javax.swing.ButtonGroup;
import javax.swing.InputMap;
import javax.swing.JPanel;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JRadioButtonMenuItem;
import javax.swing.JScrollPane;
import javax.swing.KeyStroke;

public class DrawingProject extends JPanel implements ActionListener {

    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    int screenW = (int) screenSize.getWidth();
    int screenH = (int) screenSize.getHeight();

    private JMenuBar menuBar;
    private JMenu file, view, option, pen, brush;
    private JMenuItem newMenuItemNew, newMenuItemOpen, newMenuItemSave, newMenuItemSaveAs, newMenuItemExit;

    private File openFile;

    // classes
    Utility u;
    Spline sp;
    Context c;
    RamerDouglasPeuckerAlgorithm rdp;
    // components
    JPanel radioPanel;
    JScrollPane jsp;
    PaintSurface paintSurface;

    Point cp = new Point(screenW / 2, screenH / 2);
    static int shapeType = 0, multyNum = 12;
    static boolean grid = true, multy = false, venus = false;
    static int penSize = 2;
    static Point staticstartDrag, staticendDrag;
    static double zoom = 1;
    float offSetX = -screenW / 2;
    float offSetY = -screenH / 2;

    Stroke[] linestyles = new Stroke[] {
            new BasicStroke(penSize, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL),
            new BasicStroke(penSize, BasicStroke.CAP_SQUARE, BasicStroke.JOIN_MITER),
            new BasicStroke(penSize, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND)
    };

    Point mouse, startDrag, endDrag;

    int scrolBW = 15;

    Color[] colors = {
        new Color(240, 240, 0, 112), new Color(240, 0, 200, 112), new Color(0, 240, 232, 112),
        new Color(255, 0, 0, 112), new Color(0, 255, 0, 112), new Color(0, 0, 255, 112),
        new Color(255, 165, 165, 112) 
    };
    int colorIndex = 0;
    int[] pRadious = { 10, 10, 10 };
    Color randomColor;
    Color lineColor = Color.black;
    Point zero = new Point(0, 0);
    boolean withDots = true;
    boolean endpoint = false;
    int pointView = 0;
    int mirrorSlice = 0;

    static List<Map<String, Object>> cubicCurveArrsy = new ArrayList<>();
    List<PointObj> tempList = new ArrayList<>();

    // QuadCurve
    List<Map<String, Object>> QuadCurveArrsy = new ArrayList<>();
    List<PointObj> shadowTemp = new ArrayList<>();
    List<PointObj> newArrayList = new ArrayList<>();

    int current;
    boolean hover = false;
    boolean shapeHover = false;
    boolean shapSelected = false;
    int cubicCurveArrsylevel;
    int pointtype;
    Point snapXY;
    Point snapc1;
    Point snapc2;



    
    // The below code is creating a JFrame window with the title "Testing" and
    // setting it to exit the
    // program when closed. It sets the window to be maximized and uses a
    // BorderLayout for layout. It adds
    // a new instance of a class called DrawingProject to the frame, packs the frame
    // to fit the contents,
    // centers it on the screen, and makes it visible.
    public static void main(String[] args) {
        JFrame frame = new JFrame("Testing");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH);
        frame.setLayout(new BorderLayout());
        frame.add(new DrawingProject());
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }// end main

    // The below code is creating a menu bar with several menus and menu items for a
    // drawing application.
    // The "File" menu contains options for creating a new file, opening an existing
    // file, saving the
    // current file, saving the current file with a new name, and exiting the
    // application. The "Option"
    // menu contains options for selecting different drawing tools such as draw,
    // line, oval, rectangle,
    // cubic curve, multy, and grid. It also contains an option to clear the drawing
    // area. The other menus,
    // "View", "Pen", and "Brush", do not have any menu items added to
    private JMenuBar createMenuBar() {
        menuBar = new JMenuBar();
        file = new JMenu("File");
        file.setMnemonic(KeyEvent.VK_F);
        file.setToolTipText("ALT-F");

        view = new JMenu("View");
        view.setMnemonic(KeyEvent.VK_V);
        view.setToolTipText("ALT-V");

        option = new JMenu("Option");
        option.setMnemonic(KeyEvent.VK_O);
        option.setToolTipText("ALT-O");

        pen = new JMenu("Pen");
        pen.setMnemonic(KeyEvent.VK_P);
        pen.setToolTipText("ALT-P");

        brush = new JMenu("Brush");
        brush.setMnemonic(KeyEvent.VK_B);
        brush.setToolTipText("ALT-B");

        newMenuItemNew = new JMenuItem("New");
        newMenuItemNew.setMnemonic(KeyEvent.VK_N);
        KeyStroke keyStrokeToOpen3 = KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK);
        newMenuItemNew.setAccelerator(keyStrokeToOpen3);
        newMenuItemNew.setActionCommand("n");
        newMenuItemNew.addActionListener(this);

        newMenuItemOpen = new JMenuItem("Open");
        newMenuItemOpen.setMnemonic(KeyEvent.VK_O);
        KeyStroke keyStrokeToOpen1 = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
        newMenuItemOpen.setAccelerator(keyStrokeToOpen1);
        newMenuItemOpen.setActionCommand("o");
        newMenuItemOpen.addActionListener(this);

        newMenuItemSave = new JMenuItem("Save");
        newMenuItemSave.setMnemonic(KeyEvent.VK_S);
        KeyStroke keyStrokeToOpen2 = KeyStroke.getKeyStroke(KeyEvent.VK_S, KeyEvent.CTRL_DOWN_MASK);
        newMenuItemSave.setAccelerator(keyStrokeToOpen2);
        newMenuItemSave.setActionCommand("s");
        newMenuItemSave.addActionListener(this);

        newMenuItemSaveAs = new JMenuItem("Save As...");
        newMenuItemSaveAs.setActionCommand("sa");
        newMenuItemSaveAs.addActionListener(this);

        newMenuItemExit = new JMenuItem("Exit");
        newMenuItemExit.setActionCommand("e");
        newMenuItemExit.addActionListener(this);

        file.add(newMenuItemNew);
        file.add(newMenuItemOpen);
        file.add(newMenuItemSave);
        file.add(newMenuItemSaveAs);
        file.add(newMenuItemExit);
        // file.addSeparator();

        ButtonGroup group = new ButtonGroup();

        JRadioButtonMenuItem drawButton = new JRadioButtonMenuItem("Draw");
        drawButton.setMnemonic(KeyEvent.VK_D);
        KeyStroke keyStrokeToOpenr1 = KeyStroke.getKeyStroke(KeyEvent.VK_D, KeyEvent.CTRL_DOWN_MASK);
        drawButton.setAccelerator(keyStrokeToOpenr1);

        JRadioButtonMenuItem lineButton = new JRadioButtonMenuItem("Line");
        lineButton.setMnemonic(KeyEvent.VK_L);
        KeyStroke keyStrokeToOpenr2 = KeyStroke.getKeyStroke(KeyEvent.VK_L, KeyEvent.CTRL_DOWN_MASK);
        lineButton.setAccelerator(keyStrokeToOpenr2);

        JRadioButtonMenuItem ovalButton = new JRadioButtonMenuItem("Oval");
        ovalButton.setMnemonic(KeyEvent.VK_O);
        KeyStroke keyStrokeToOpenr3 = KeyStroke.getKeyStroke(KeyEvent.VK_O, KeyEvent.CTRL_DOWN_MASK);
        ovalButton.setAccelerator(keyStrokeToOpenr3);

        JRadioButtonMenuItem rectangleButton = new JRadioButtonMenuItem("Rectangle");
        rectangleButton.setMnemonic(KeyEvent.VK_R);
        KeyStroke keyStrokeToOpenr4 = KeyStroke.getKeyStroke(KeyEvent.VK_R, KeyEvent.CTRL_DOWN_MASK);
        rectangleButton.setAccelerator(keyStrokeToOpenr4);

        JRadioButtonMenuItem cubicCurveButton = new JRadioButtonMenuItem("CubicCurve");
        cubicCurveButton.setMnemonic(KeyEvent.VK_C);
        KeyStroke keyStrokeToOpenr5 = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK);
        cubicCurveButton.setAccelerator(keyStrokeToOpenr5);

        JRadioButtonMenuItem multyButton = new JRadioButtonMenuItem("Multy");
        multyButton.setMnemonic(KeyEvent.VK_M);
        KeyStroke keyStrokeToOpenr6 = KeyStroke.getKeyStroke(KeyEvent.VK_M, KeyEvent.CTRL_DOWN_MASK);
        multyButton.setAccelerator(keyStrokeToOpenr6);

        JRadioButtonMenuItem gridButton = new JRadioButtonMenuItem("Grid");
        gridButton.setMnemonic(KeyEvent.VK_G);
        KeyStroke keyStrokeToOpenr8 = KeyStroke.getKeyStroke(KeyEvent.VK_G, KeyEvent.CTRL_DOWN_MASK);
        gridButton.setAccelerator(keyStrokeToOpenr8);

        JMenuItem clearButton = new JMenuItem("Clear");
        clearButton.setMnemonic(KeyEvent.VK_C);
        KeyStroke keyStrokeToOpenr9 = KeyStroke.getKeyStroke(KeyEvent.VK_C, KeyEvent.CTRL_DOWN_MASK);
        clearButton.setAccelerator(keyStrokeToOpenr9);

        group.add(drawButton);
        group.add(lineButton);
        group.add(ovalButton);
        group.add(rectangleButton);
        group.add(cubicCurveButton);

        drawButton.addActionListener(this);
        lineButton.addActionListener(this);
        ovalButton.addActionListener(this);
        rectangleButton.addActionListener(this);
        cubicCurveButton.addActionListener(this);
        multyButton.addActionListener(this);
        gridButton.addActionListener(this);
        clearButton.addActionListener(this);

        drawButton.setSelected(true);
        gridButton.setSelected(true);

        /*
         * <option value="0">Simple pencil</option>
         * <option value="1">Smooth connections</option>
         * <option value="2">Edge smoothing with shadows</option>
         * <option value="3">Point-based approach</option>
         * <option value="4">Point-based with shadow</option>
         * <option value="5">Edge smoothing with radial gradient</option>
         * <option value="6">Edge smoothing with radial gradient 2</option>
         * <option value="7">Bezier curves</option>
         * <option value="8">Brush, Fur, Pen</option>
         * <option value="9">Fur (rotating strokes)</option>
         * <option value="10">Pen (variable segment width)</option>
         * <option value="11">Pen #2 (multiple strokes)</option>
         * <option value="12">Thick brush</option>
         * <option value="13">"Sliced" strokes</option>
         * <option value="14">"Sliced" strokes with opacity</option>
         * <option value="15">Multiple lines</option>
         * <option value="16">Multiple lines with opacity</option>
         * <option value="17">Stamp Basic concept</option>
         * <option value="18">Stamp Trail effect</option>
         * <option value="19">Stamp Random radius, opacity</option>
         * <option value="20">Stamp Shapes</option>
         * <option value="21">Stamp Shapes with rotation</option>
         * <option value="22">Stamp Randomize everything!</option>
         * <option value="23">Colored pixels</option>
         * <option value="24">Pattern-based brushes Dots pattern</option>
         * <option value="25">Pattern-based brushes Lines pattern</option>
         * <option value="26">Pattern-based brushes Double-color lines pattern</option>
         * <option value="27">Pattern-based brushes Rainbow</option>
         * <option value="28">Pattern-based brushes Image</option>
         * <option value="29">Spray</option>
         * <option value="30">Time-based spray</option>
         * <option value="31">Time-based spray with round distribution</option>
         * <option value="32">Time-based spray with round distribution Randomizing
         * dots</option>
         * <option value="33">Neighbor points connection All-points connection</option>
         * <option value="34">Neighbor points</option>
         * <option value="35">Fur via neighbor points</option>
         */

        option.add(drawButton);
        option.add(lineButton);
        option.add(ovalButton);
        option.add(rectangleButton);
        option.add(cubicCurveButton);
        option.addSeparator();
        option.add(multyButton);
        option.add(gridButton);
        option.add(clearButton);

        menuBar.add(file);
        menuBar.add(view);
        menuBar.add(option);
        menuBar.add(pen);
        menuBar.add(brush);

        return menuBar;
    }// end createMenuBar

    // The below code is the constructor of a Java class called DrawingProject. It
    // initializes various
    // objects and components such as Utility, Spline, Context,
    // RamerDouglasPeuckerAlgorithm, menuBar,
    // paintSurface, and jsp. It also sets up key bindings for the space key and
    // adds them to the input
    // and action maps. Finally, it sets the focusable property to true and requests
    // focus in the window.
    public DrawingProject() {
        u = new Utility(screenW, screenH);
        sp = new Spline();
        c = new Context();
        rdp = new RamerDouglasPeuckerAlgorithm();

        this.setSize(screenW, screenH);

        menuBar = createMenuBar();
        menuBar.setBorder(javax.swing.BorderFactory.createEmptyBorder());
        this.add(menuBar);

        // radioPanel = menu();
        // this.add(radioPanel, BorderLayout.NORTH);
        paintSurface = new PaintSurface();
        jsp = new JScrollPane(paintSurface);
        jsp.getVerticalScrollBar().setPreferredSize(new Dimension(scrolBW, 0));
        jsp.getHorizontalScrollBar().setPreferredSize(new Dimension(0, scrolBW));
        jsp.getVerticalScrollBar().setBackground(Color.gray);
        jsp.getHorizontalScrollBar().setBackground(Color.gray);
        this.add(jsp, BorderLayout.CENTER);

        // this.setUndecorated(true);
        this.setVisible(true);

        InputMap im = getInputMap(WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = getActionMap();

        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SPACE, 0, false), "pressed");
        // im.put(KeyStroke.getKeyStroke(KeyEvent.VK_1, 0, true), "released");

        am.put("pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println("VK_SPACE");
                if (pointView < 2) {
                    pointView++;
                } else {
                    pointView = 0;
                }
                repaint();
            }
        });
        /*
         * am.put("released", new AbstractAction() {
         * 
         * @Override
         * public void actionPerformed(ActionEvent e) {
         * System.out.println("released");
         * }
         * });
         */

        setFocusable(true);
        requestFocusInWindow();

        /*
         * this.requestFocusInWindow();
         * this.addKeyListener(k);
         * jp.addKeyListener(k);
         * radioPanel.addKeyListener(k);
         * jsp.addKeyListener(k);
         * paintSurface.addKeyListener(k);
         */
    }// end constructer

    // The below code is an implementation of the actionPerformed method in Java. It
    // is handling various
    // actions based on the action command received. It creates a JFileChooser
    // object and opens a file
    // dialog when the "o" action command is received. It displays a confirmation
    // dialog when the "s" and
    // "e" action commands are received. It sets the shapeType variable based on the
    // action command
    // received for drawing shapes. It toggles the multy, grid, and venus variables
    // based on the action
    // command received. It clears the cubicCurveArray and tempList when the "Clear"
    // action command is
    // received. Finally
    @Override
    public void actionPerformed(ActionEvent e) {
        final JFileChooser fc = new JFileChooser();
        switch (e.getActionCommand()) {

            case "n":
                break;
            case "o":
                int returnVal = fc.showOpenDialog(DrawingProject.this);
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File file = fc.getSelectedFile();
                    openFile = file;
                    try {
                        read(openFile);
                    } catch (IOException ex) {
                        Logger.getLogger(DrawingProject.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                break;
            case "s":
                Object[] options = { "Yes", "No" };
                int n = JOptionPane.showOptionDialog(null, "Are you sure you want to save?", "Exit Sudoku Solver",
                        JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[1]);
                if (n == JOptionPane.YES_OPTION) {
                    // write();
                } else if (n == JOptionPane.NO_OPTION) {
                }
                break;
            case "sa":
                break;
            case "e":
                Object[] options1 = { "Yes", "No" };
                int n2 = JOptionPane.showOptionDialog(
                        null,
                        "Are you sure you want to Exit",
                        "Exit Sudoku Solver",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE,
                        null,
                        options1,
                        options1[0]);
                if (n2 == JOptionPane.YES_OPTION) {
                    System.exit(0);
                } else if (n2 == JOptionPane.NO_OPTION) {
                }
                break;
            case "Draw":
                shapeType = 0;
                break;
            case "Line":
                shapeType = 1;
                break;
            case "Oval":
                shapeType = 2;
                break;
            case "Rectangle":
                shapeType = 3;
                break;
            case "QuadCurve":
                shapeType = 4;
                break;
            case "CubicCurve":
                shapeType = 5;
                break;
            case "Multy":
                multy = !multy;
                break;
            case "Move":
                break;
            case "Grid":
                grid = !grid;
                repaint();
                break;
            case "venus":
                venus = !venus;
                repaint();
                break;
            case "Clear":
                cubicCurveArrsy.clear();
                tempList.clear();
                repaint();
                break;

            default:
                System.out.println("radiobutton actionlistener switch error");
                break;
        }
        requestFocusInWindow();
    }// end actionPerformed

    // The below code is defining a private method called "createMap" that takes in
    // several parameters and
    // returns a Map object. The method creates a Map object and populates it with
    // key-value pairs based on
    // the input parameters. The keys include "type", "p", "sc", "fc", "w", "m",
    // "comp", "t", "h", and
    // "sel", which correspond to the type of object being created, a list of
    // points, stroke color, fill
    // color, stroke width, whether the object is a compound object, whether to
    // trace the object, whether
    // to handle the
    private Map<String, Object> createMap(List<PointObj> p, String type, Color sc, Color fc, int w, boolean m,
            boolean comp,
            boolean trace, boolean handel, boolean sel) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("type", type);
        map.put("p", p);
        map.put("sc", sc);
        map.put("fc", fc);
        map.put("w", w);
        map.put("m", m);
        map.put("comp", comp);
        map.put("t", trace);
        map.put("h", handel);
        map.put("sel", sel);
        return map;
    }// end createMap

    private class PaintSurface extends JComponent {

        public PaintSurface() {
            this.setPreferredSize(new Dimension(screenW, screenH));
            this.setMinimumSize(new Dimension(screenW, screenH));
            this.addMouseWheelListener(new MouseAdapter() {
                @Override
                public void mouseWheelMoved(MouseWheelEvent e) {
                    /*
                     * int steps = e.getWheelRotation();
                     * if (zoom > 0.5 && zoom < 2) {
                     * zoom += steps;
                     * System.out.println(zoom + ", " + (int) (screenW * zoom) + ", " + (int)
                     * (screenH * zoom));
                     * paintSurface.setSize((int) (screenW * zoom), (int) (screenH * zoom));
                     * }
                     * repaint();
                     */
                }
            });

            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                }// end mouseClicked

                @Override
                public void mouseEntered(MouseEvent e) {
                }// end mouseEntered

                @Override
                public void mouseExited(MouseEvent e) {
                }// end mouseExited

                @Override
                public void mousePressed(MouseEvent e) {
                    randomColor = newColor();
                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (shapSelected) {

                        } else if (hover) {
                            takesnap(current);
                        } else {
                            startDrag = new Point(e.getX(), e.getY());

                            endDrag = startDrag;
                            staticstartDrag = startDrag;
                            staticendDrag = endDrag;
                            switch (shapeType) {
                                case 0:
                                    tempList.add(new PointObj(startDrag, startDrag, startDrag));
                                    break;
                                case 1:
                                    tempList.add(new PointObj(startDrag, startDrag, startDrag));
                                    break;
                                case 2:
                                    tempList.add(new PointObj(startDrag, startDrag, startDrag));
                                    break;
                                case 3:
                                    break;
                                case 4:
                                    // tempQuadCurve.add(new Point(e.getX(), e.getY()));
                                    break;
                                case 5:
                                    tempList.add(new PointObj(startDrag, startDrag, startDrag));
                                    break;
                                default:
                                    System.out.println("mousePressed Switck error 397");
                                    break;
                            }
                        }
                    }
                    if (e.getButton() == MouseEvent.BUTTON2) {
                        // System.out.println("Middle Click!");
                    }
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        // System.out.println("Right Click!");
                        if (shapeHover) {
                            if (cubicCurveArrsy.size() > 0) {
                                Map<String, Object> m = cubicCurveArrsy.get(cubicCurveArrsylevel);
                                if (shapSelected) {
                                    shapSelected = false;
                                    m.put("sel", shapSelected);
                                    m.put("sc", Color.BLACK);
                                    m.put("w", 2);
                                } else {
                                    shapSelected = true;
                                    m.put("sel", shapSelected);
                                    m.put("sc", Color.red);
                                    m.put("w", 5);
                                }
                            }
                        } else {
                            c.doPop(e);
                        }
                    }
                    repaint();
                }// end mousePressed

                @Override
                public void mouseReleased(MouseEvent e) {
                    List<PointObj> newArrayList;

                    if (e.getButton() == MouseEvent.BUTTON1) {
                        if (!(shapSelected)) {
                            if (hover) {
                                if (endpoint) {
                                    Map<String, Object> m = cubicCurveArrsy.get(cubicCurveArrsy.size() - 1);
                                    List<PointObj> lp =  (List<PointObj>) m.get("p");
                                    boolean comp = (boolean) m.get("comp");
                                    if (current == lp.size() - 1) {
                                        if (!(comp)) {
                                            lp.remove(lp.size() - 1);
                                            m.put("comp", true);
                                        }
                                    } else if (current == 0) {
                                        if (!(comp)) {
                                            lp.remove(0);
                                            m.put("comp", true);
                                        }
                                    }
                                }
                            } else {
                                switch (shapeType) {
                                    case 0:
                                        newArrayList = new ArrayList<PointObj>(tempList);
                                        tempList.clear();
                                        cubicCurveArrsy.add(createMap(sp.drawSplines(newArrayList), "Draw", lineColor,
                                                randomColor, penSize, multy, false, false, true, false));
                                        break;
                                    case 1:
                                        tempList.add(new PointObj(new Point(e.getX(), e.getY()), zero, zero));
                                        newArrayList = new ArrayList<PointObj>(tempList);
                                        tempList.clear();
                                        cubicCurveArrsy.add(createMap(newArrayList, "Line", lineColor, null, penSize,
                                                multy, false, false, true, false));
                                        break;
                                    case 2:
                                        tempList.add(new PointObj(new Point(e.getX(), e.getY()), zero, zero));
                                        newArrayList = new ArrayList<PointObj>(tempList);
                                        tempList.clear();
                                        cubicCurveArrsy.add(createMap(
                                                u.ellipPointObj(newArrayList.get(0), newArrayList.get(1)), "Oval",
                                                lineColor, randomColor, penSize, multy, true, false, true, false));
                                        break;
                                    case 3:
                                        endDrag = new Point(e.getX(), e.getY());
                                        tempList.add(new PointObj(startDrag, startDrag, startDrag));
                                        tempList.add(new PointObj(new Point(startDrag.x, endDrag.y),
                                                new Point(startDrag.x, endDrag.y), new Point(startDrag.x, endDrag.y)));
                                        tempList.add(new PointObj(endDrag, endDrag, endDrag));
                                        tempList.add(new PointObj(new Point(endDrag.x, startDrag.y),
                                                new Point(endDrag.x, startDrag.y), new Point(endDrag.x, startDrag.y)));
                                        newArrayList = new ArrayList<PointObj>(tempList);
                                        tempList.clear();
                                        cubicCurveArrsy.add(createMap(newArrayList, "Rectangel", lineColor, randomColor,
                                                penSize, multy, true, false, true, false));
                                        break;
                                    case 4:

                                        break;
                                    case 5:

                                        break;
                                    default:
                                        System.out.println("Switck mouseReleased error 447");
                                }
                                startDrag = null;
                                endDrag = null;
                                repaint();
                            }
                        }
                    }
                    if (e.getButton() == MouseEvent.BUTTON2) {
                        // System.out.println("Middle Click!");
                    }
                    if (e.getButton() == MouseEvent.BUTTON3) {
                        // System.out.println("Right Click!");
                        if (shapeType == 5) {
                            if (tempList.size() > 0) {
                                newArrayList = new ArrayList<PointObj>(tempList);
                                tempList.clear();
                                cubicCurveArrsy.add(createMap(newArrayList, "CubicCurve", lineColor, randomColor,
                                        penSize, multy, false, false, true, false));
                            }
                        } else {
                            // c.doPop(e);
                        }
                    }
                }// end mouseReleased
            });// end addMouseListener

            this.addMouseMotionListener(new MouseMotionAdapter() {
                @Override
                public void mouseDragged(MouseEvent e) {
                    mouse = new Point(e.getX(), e.getY());
                    if (hover) {
                        Map<String, Object> m = cubicCurveArrsy.get(cubicCurveArrsy.size() - 1);
                        List<PointObj> lp = (List<PointObj>) m.get("p");
                        boolean mirror = (boolean) m.get("m");
                        boolean comp = (boolean) m.get("comp");
                        if (!(comp)) {
                            if (lp.size() > 0) {
                                if (current == 0 || current == lp.size() - 1) {
                                    checkendpoint(lp, mirror);
                                }

                            }
                        }

                        switch (pointtype) {
                            case 1:
                                if (mirror) {
                                    double ang = (360 / multyNum) * (multyNum - mirrorSlice);
                                    double[] newmouse = u.convert(ang, mouse.x, mouse.y, cp.x, cp.y);
                                    lp.get(current).setXy(new Point((int) newmouse[0], (int) newmouse[1]));
                                    lp.get(current).setC1(
                                            new Point((int) newmouse[0] - snapc1.x, (int) newmouse[1] - snapc1.y));
                                    lp.get(current).setC2(
                                            new Point((int) newmouse[0] - snapc2.x, (int) newmouse[1] - snapc2.y));
                                } else {
                                    lp.get(current).setXy(mouse);
                                    lp.get(current).setC1(new Point(mouse.x - snapc1.x, mouse.y - snapc1.y));
                                    lp.get(current).setC2(new Point(mouse.x - snapc2.x, mouse.y - snapc2.y));
                                }
                                break;
                            case 2:
                                if (mirror) {
                                    double ang = (360 / multyNum) * (multyNum - mirrorSlice);
                                    double[] newmouse = u.convert(ang, mouse.x, mouse.y, cp.x, cp.y);
                                    lp.get(current).setC1(new Point((int) newmouse[0], (int) newmouse[1]));
                                    if (lp.get(current).isMirror()) {
                                        double[] vert1 = u.convert(180, (int) newmouse[0], (int) newmouse[1],
                                                lp.get(current).getXy().x, lp.get(current).getXy().y);
                                        lp.get(current).setC2(new Point((int) vert1[0], (int) vert1[1]));
                                    }
                                } else {
                                    lp.get(current).setC1(mouse);
                                    if (lp.get(current).isMirror()) {
                                        double[] vert1 = u.convert(180, mouse.x, mouse.y, lp.get(current).getXy().x,
                                                lp.get(current).getXy().y);
                                        lp.get(current).setC2(new Point((int) vert1[0], (int) vert1[1]));
                                    }
                                }
                                break;
                            case 3:
                                if (mirror) {
                                    double ang = (360 / multyNum) * (multyNum - mirrorSlice);
                                    double[] newmouse = u.convert(ang, mouse.x, mouse.y, cp.x, cp.y);
                                    lp.get(current).setC2(new Point((int) newmouse[0], (int) newmouse[1]));
                                    if (lp.get(current).isMirror()) {
                                        double[] vert2 = u.convert(180, (int) newmouse[0], (int) newmouse[1],
                                                lp.get(current).getXy().x, lp.get(current).getXy().y);
                                        lp.get(current).setC1(new Point((int) vert2[0], (int) vert2[1]));
                                    }
                                } else {
                                    lp.get(current).setC2(mouse);
                                    if (lp.get(current).isMirror()) {
                                        double[] vert2 = u.convert(180, mouse.x, mouse.y, lp.get(current).getXy().x,
                                                lp.get(current).getXy().y);
                                        lp.get(current).setC1(new Point((int) vert2[0], (int) vert2[1]));
                                    }
                                }
                                break;
                            default:
                                break;
                        }

                        // m.put("p", sp.drawSP(lp));
                    } else {
                        switch (shapeType) {
                            case 0:
                                tempList.add(new PointObj(new Point(e.getX(), e.getY()), zero, zero));
                                staticendDrag = endDrag;
                                break;
                            case 1:

                            case 2:

                            case 3:

                            case 4:

                            case 5:

                            case 6:

                                break;
                            default:
                                System.out.println("mouseDragged Switck error 499");
                                break;
                        }
                        repaint();
                        switch (e.getButton()) {
                            case MouseEvent.BUTTON1:
                                System.out.println("left Click!");
                                break;
                            case MouseEvent.BUTTON2:
                                System.out.println("Middle Click!");
                                break;
                            case MouseEvent.BUTTON3:
                                System.out.println("Right Click!");
                                break;
                            default:
                                break;
                        }
                    }
                    repaint();
                }// end mouseDragged

                @Override
                public void mouseMoved(MouseEvent e) {
                    mouse = new Point(e.getX(), e.getY());
                    if (cubicCurveArrsy.size() > 0) {
                        Map<String, Object> m = cubicCurveArrsy.get(cubicCurveArrsy.size() - 1);
                        List<PointObj> lp = (List<PointObj>) m.get("p");
                        boolean mirror = (boolean) m.get("m");
                        boolean comp = (boolean) m.get("comp");
                        checkHover(lp, mirror);
                        if (comp && !(hover)) {
                            for (int i = cubicCurveArrsy.size() - 1; i >= 0; i--) {
                                Map<String, Object> mm = cubicCurveArrsy.get(i);
                                List<PointObj> lpp = (List<PointObj>) mm.get("p");
                                shapeHover = isPointInPolygon(mouse, lpp);
                                if (shapeHover) {
                                    cubicCurveArrsylevel = i;
                                    break;
                                }
                            }

                            System.out.println(shapeHover + " " + cubicCurveArrsylevel);
                        }
                    }
                    // checkHover(tempList, multy);
                    // if(tempList.size()>0){
                    // tempList.get(0).setCxy(Color.MAGENTA);
                    // }
                    // System.out.println(tempList.size());
                    // for(PointObj po:tempList){
                    // System.out.println(po);
                    // }

                    repaint();
                }// end mouseMoved

                public boolean isPointInPolygon(Point p, List<PointObj> points) {
                    Polygon polygon = new Polygon();// java.awt.Polygon
                    points.forEach((po) -> {
                        polygon.addPoint(po.getXy().x, po.getXy().y);
                    });
                    return polygon.contains(p);
                }

                private void checkendpoint(List<PointObj> array, boolean mir) {
                    endpoint = false;
                    if (mir) {
                        for (int j = 0; j < multyNum; j++) {
                            double ang = (360 / multyNum) * j;
                            if (current == array.size() - 1) {
                                double[] vert1 = u.convert(ang, array.get(0).getXy().x, array.get(0).getXy().y, cp.x,
                                        cp.y);
                                if (intersects(new Point((int) vert1[0], (int) vert1[1]), array.get(0).getRxy())) {
                                    endpoint = true;
                                    array.get(0).setRxy(25);
                                    array.get(0).setCxy(Color.MAGENTA);
                                    break;
                                }
                            } else if (current == 0) {
                                double[] vert1 = u.convert(ang, array.get(array.size() - 1).getXy().x,
                                        array.get(array.size() - 1).getXy().y, cp.x, cp.y);
                                if (intersects(new Point((int) vert1[0], (int) vert1[1]),
                                        array.get(array.size() - 1).getRxy())) {
                                    endpoint = true;
                                    array.get(array.size() - 1).setRxy(25);
                                    array.get(array.size() - 1).setCxy(Color.MAGENTA);
                                    break;
                                }
                            } else {
                                endpoint = false;
                            }
                        }
                    } else {
                        if (current == array.size() - 1) {
                            if (intersects(array.get(0).getXy(), array.get(0).getRxy())) {
                                endpoint = true;
                                array.get(0).setRxy(25);
                                array.get(0).setCxy(Color.MAGENTA);
                            }
                        } else if (current == 0) {
                            if (intersects(array.get(array.size() - 1).getXy(), array.get(array.size() - 1).getRxy())) {
                                endpoint = true;
                                array.get(array.size() - 1).setRxy(25);
                                array.get(array.size() - 1).setCxy(Color.MAGENTA);
                            }
                        } else {
                            endpoint = false;
                            array.get(0).setRxy(10);
                            array.get(0).setCxy(Color.BLUE);
                            array.get(array.size() - 1).setRxy(10);
                            array.get(array.size() - 1).setCxy(Color.BLUE);
                        }

                    }
                }// end checkendpoint

                private void checkHover(List<PointObj> array, boolean mir) {
                    hover = false;
                    endpoint = false;
                    for (int i = 0; i < array.size(); i++) {
                        if (mir) {
                            for (int j = 0; j < multyNum; j++) {
                                double ang = (360 / multyNum) * j;
                                if (pointView == 1 || pointView == 2) {
                                    double[] vert1 = u.convert(ang, array.get(i).getXy().x, array.get(i).getXy().y,
                                            cp.x, cp.y);
                                    if (intersects(new Point((int) vert1[0], (int) vert1[1]), array.get(i).getRxy())) {
                                        hover = true;
                                        current = i;
                                        pointtype = 1;
                                        mirrorSlice = j;
                                        array.get(i).setRxy(20);
                                        array.get(i).setCxy(Color.GREEN);
                                        break;
                                    } else {
                                        array.get(i).setRxy(10);
                                        array.get(i).setCxy(Color.BLUE);
                                    }
                                }
                                if (pointView == 2) {
                                    double[] vert1 = u.convert(ang, array.get(i).getC1().x, array.get(i).getC1().y,
                                            cp.x, cp.y);
                                    if (intersects(new Point((int) vert1[0], (int) vert1[1]), array.get(i).getRc1())) {
                                        hover = true;
                                        current = i;
                                        pointtype = 2;
                                        mirrorSlice = j;
                                        array.get(i).setRc1(20);
                                        array.get(i).setCc1(Color.YELLOW);
                                        break;
                                    } else {
                                        array.get(i).setRc1(10);
                                        array.get(i).setCc1(Color.RED);
                                    }
                                    double[] vert2 = u.convert(ang, array.get(i).getC2().x, array.get(i).getC2().y,
                                            cp.x, cp.y);
                                    if (intersects(new Point((int) vert2[0], (int) vert2[1]), array.get(i).getRc2())) {
                                        hover = true;
                                        current = i;
                                        pointtype = 3;
                                        array.get(i).setRc2(20);
                                        array.get(i).setCc2(Color.YELLOW);
                                        break;
                                    } else {
                                        array.get(i).setRc2(10);
                                        array.get(i).setCc2(Color.RED);
                                    }
                                }
                            }
                        } else {
                            if (pointView == 1 || pointView == 2) {
                                if (intersects(array.get(i).getXy(), array.get(i).getRxy())) {
                                    hover = true;
                                    current = i;
                                    pointtype = 1;
                                    array.get(i).setRxy(20);
                                    array.get(i).setCxy(Color.GREEN);
                                } else {
                                    array.get(i).setRxy(10);
                                    array.get(i).setCxy(Color.BLUE);
                                }
                            }
                            if (pointView == 2) {
                                if (intersects(array.get(i).getC1(), array.get(i).getRc1())) {
                                    hover = true;
                                    current = i;
                                    pointtype = 2;
                                    array.get(i).setRc1(20);
                                    array.get(i).setCc1(Color.YELLOW);
                                } else {
                                    array.get(i).setRc1(10);
                                    array.get(i).setCc1(Color.RED);
                                }
                                if (intersects(array.get(i).getC2(), array.get(i).getRc2())) {
                                    hover = true;
                                    current = i;
                                    pointtype = 3;
                                    array.get(i).setRc2(20);
                                    array.get(i).setCc2(Color.YELLOW);
                                } else {
                                    array.get(i).setRc2(10);
                                    array.get(i).setCc2(Color.RED);
                                }
                            }
                        }
                    }

                }// end checkHover
            });

        }// end PaintSurface

        private void paintBackground(Graphics2D g2) {

            g2.setPaint(Color.LIGHT_GRAY);
            for (int i = 0; i < getSize().width; i += 10) {
                Shape line = new Line2D.Float(i, 0, i, getSize().height);
                g2.draw(line);
            }

            for (int i = 0; i < getSize().height; i += 10) {
                Shape line = new Line2D.Float(0, i, getSize().width, i);
                g2.draw(line);
            }
        }// end paintBackground

        @Override
        public void paint(Graphics g) {

            super.paint(g);
            Graphics2D g2 = (Graphics2D) g;
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, getWidth(), getHeight());
            if (grid) {
                paintBackground(g2);
            }

            g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

            // draw atual shape
            // .................................................................................
            // draw outline
            // .....................................................................................................................
            if (cubicCurveArrsy.size() > 0) {
                cubicCurveArrsy.forEach((Map<String, Object> m) -> {
                    paintSpline(g2, m);
                });
            }

            // draw
            if (shapeType == 0) {
                Map<String, Object> nm = null;
                if (tempList.size() > 0) {
                    if (tempList.size() > 2) {
                        List<Point2D.Double> in = new ArrayList<>();
                        List<Point2D.Double> out = new ArrayList<>();
                        tempList.forEach((po) -> {
                            in.add(new Point2D.Double(po.getXy().x, po.getXy().y));
                        });
                        RamerDouglasPeuckerAlgorithm.ramerDouglasPeucker(in, 1.0, out);
                        tempList.clear();
                        out.forEach((pd) -> {
                            tempList.add(new PointObj(new Point((int) pd.x, (int) pd.y),
                                    new Point((int) pd.x, (int) pd.y), new Point((int) pd.x, (int) pd.y)));
                        });
                    }
                    nm = createMap(sp.drawSplines(tempList), "Draw", lineColor, randomColor, penSize, multy, false,
                            false, false, false);
                    paintSpline(g2, nm);
                }
            }
            // splaine trace
            if (shapeType == 5) {
                Map<String, Object> nm = null;
                tempList = sp.drawSplines(tempList);
                if (!(tempList.isEmpty())) {

                    if (tempList.size() == 1) {
                        shadowTemp.add(new PointObj(tempList.get(0).getXy(), tempList.get(0).getXy(),
                                tempList.get(0).getXy()));
                        shadowTemp.add(new PointObj(mouse, mouse, mouse));
                        newArrayList = new ArrayList<>(shadowTemp);
                        shadowTemp.clear();
                        nm = createMap(newArrayList, "CubicCurve", Color.GRAY, randomColor, penSize, multy, true, true,
                                true, false);
                        paintSpline(g2, nm);
                    } else if (tempList.size() == 2) {
                        shadowTemp.add(new PointObj(tempList.get(0).getXy(), tempList.get(0).getXy(),
                                tempList.get(0).getXy()));
                        shadowTemp.add(new PointObj(tempList.get(1).getXy(), tempList.get(1).getXy(),
                                tempList.get(1).getXy()));
                        newArrayList = new ArrayList<>(sp.drawSplines(shadowTemp));
                        shadowTemp.clear();
                        nm = createMap(newArrayList, "CubicCurve", lineColor, randomColor, penSize, multy, false, false,
                                true, false);
                        paintSpline(g2, nm);
                        shadowTemp.add(new PointObj(tempList.get(0).getXy(), tempList.get(0).getXy(),
                                tempList.get(0).getXy()));
                        shadowTemp.add(new PointObj(tempList.get(1).getXy(), tempList.get(1).getXy(),
                                tempList.get(1).getXy()));
                        shadowTemp.add(new PointObj(mouse, mouse, mouse));
                        shadowTemp = sp.drawSplines(shadowTemp);
                        newArrayList = new ArrayList<>(sp.drawSplines(shadowTemp));
                        shadowTemp.clear();
                        nm = createMap(newArrayList, "CubicCurve", Color.GRAY, randomColor, penSize, multy, false, true,
                                true, false);
                        paintSpline(g2, nm);
                    } else if (tempList.size() > 2) {
                        for (int i = 0; i < tempList.size(); i++) {
                            shadowTemp.add(new PointObj(tempList.get(i).getXy(), tempList.get(i).getXy(),
                                    tempList.get(i).getXy()));
                        }
                        newArrayList = new ArrayList<>(sp.drawSplines(shadowTemp));
                        shadowTemp.clear();
                        nm = createMap(newArrayList, "CubicCurve", lineColor, randomColor, penSize, multy, false, false,
                                true, false);
                        paintSpline(g2, nm);

                        shadowTemp.add(new PointObj(
                                tempList.get(tempList.size() - 3).getXy(),
                                tempList.get(tempList.size() - 3).getC1(),
                                tempList.get(tempList.size() - 3).getC2()));
                        shadowTemp.add(new PointObj(
                                tempList.get(tempList.size() - 2).getXy(),
                                tempList.get(tempList.size() - 2).getC1(),
                                tempList.get(tempList.size() - 2).getC2()));
                        shadowTemp.add(new PointObj(
                                tempList.get(tempList.size() - 1).getXy(),
                                tempList.get(tempList.size() - 1).getC1(),
                                tempList.get(tempList.size() - 1).getC2()));
                        shadowTemp.add(new PointObj(mouse, mouse, mouse));

                        newArrayList = new ArrayList<>(sp.drawSplines(shadowTemp));
                        shadowTemp.clear();
                        nm = createMap(newArrayList, "CubicCurve", Color.GRAY, randomColor, penSize, multy, false, true,
                                true, false);
                        paintSpline(g2, nm);
                    }
                }
            }

            if (startDrag != null && endDrag != null) {
                Map<String, Object> nm = null;
                switch (shapeType) {
                    case 0:
                        break;
                    case 1:
                        shadowTemp.add(new PointObj(startDrag, zero, zero));
                        shadowTemp.add(new PointObj(mouse, zero, zero));
                        newArrayList = new ArrayList<>(shadowTemp);
                        shadowTemp.clear();
                        nm = createMap(newArrayList, "Line", lineColor, null, penSize, multy, false, true, true, false);
                        // nm = createMap(shadowTemp, "Line", lineColor, null, penSize, multy, false,
                        // true, true);
                        paintSpline(g2, nm);
                        break;
                    case 2:
                        shadowTemp = u.ellipPointObj(
                                new PointObj(startDrag, zero, zero),
                                new PointObj(mouse, zero, zero));
                        newArrayList = new ArrayList<>(shadowTemp);
                        shadowTemp.clear();
                        nm = createMap(newArrayList, "Oval", lineColor, randomColor, penSize, multy, true, true, false,
                                false);
                        paintSpline(g2, nm);
                        break;
                    case 3:
                        shadowTemp.add(new PointObj(startDrag, startDrag, startDrag));
                        shadowTemp.add(new PointObj(new Point(mouse.x, startDrag.y), new Point(mouse.x, startDrag.y),
                                new Point(mouse.x, startDrag.y)));
                        shadowTemp.add(new PointObj(mouse, mouse, mouse));
                        shadowTemp.add(new PointObj(new Point(startDrag.x, mouse.y), new Point(startDrag.x, mouse.y),
                                new Point(startDrag.x, mouse.y)));
                        newArrayList = new ArrayList<>(shadowTemp);
                        shadowTemp.clear();
                        nm = createMap(newArrayList, "Rectangel", lineColor, randomColor, penSize, multy, true, true,
                                false, false);
                        paintSpline(g2, nm);
                        break;
                    case 4:
                        // List<Point> tempQuadCurve = new ArrayList<>();
                        break;
                    case 5:
                        break;
                    default:
                        System.out.println("outline Switck error 734");
                        break;
                }

            } else {

            }
        }// end paint

        private void paintSpline(Graphics2D g2, Map<String, Object> m) {
            // spline
            List<PointObj> lp = (List<PointObj>) m.get("p");
            boolean comp = (boolean) m.get("comp");
            boolean mirror = (boolean) m.get("m");
            boolean trace = (boolean) m.get("t");
            boolean handel = (boolean) m.get("h");
            Color sc = (Color) m.get("sc");
            Color fc = (Color) m.get("fc");
            int w = (int) m.get("w");
            BasicStroke lw = new BasicStroke(w);
            Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 9 }, 0);
            boolean mul = false;
            if (trace) {
                mul = multy;
            } else {
                mul = mirror;
            }
            Shape r;
            if (comp) {
                if (mul) {
                    GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
                    for (int i = 0; i < multyNum; i++) {
                        double ang = (360 / multyNum) * i;

                        double[] vert1 = u.convert(ang, lp.get(0).getXy().x, lp.get(0).getXy().y, cp.x, cp.y);
                        p.moveTo(vert1[0], vert1[1]);
                        double[] vert2 = u.convert(ang, lp.get(0).getC2().x, lp.get(0).getC2().y, cp.x, cp.y);
                        double[] vert3 = u.convert(ang, lp.get(1).getC1().x, lp.get(1).getC1().y, cp.x, cp.y);
                        double[] vert4 = u.convert(ang, lp.get(1).getXy().x, lp.get(1).getXy().y, cp.x, cp.y);
                        p.curveTo(vert2[0], vert2[1], vert3[0], vert3[1], vert4[0], vert4[1]);
                        int j;
                        for (j = 2; j < lp.size(); j += 1) {
                            double[] vert5 = u.convert(ang, lp.get(j - 1).getC2().x, lp.get(j - 1).getC2().y, cp.x,
                                    cp.y);
                            double[] vert6 = u.convert(ang, lp.get(j).getC1().x, lp.get(j).getC1().y, cp.x, cp.y);
                            double[] vert7 = u.convert(ang, lp.get(j).getXy().x, lp.get(j).getXy().y, cp.x, cp.y);
                            p.curveTo(vert5[0], vert5[1], vert6[0], vert6[1], vert7[0], vert7[1]);
                        }
                        double[] vert8 = u.convert(ang, lp.get(lp.size() - 1).getC2().x,
                                lp.get(lp.size() - 1).getC2().y, cp.x, cp.y);
                        double[] vert9 = u.convert(ang, lp.get(0).getC1().x, lp.get(0).getC1().y, cp.x, cp.y);
                        p.curveTo(vert8[0], vert8[1], vert9[0], vert9[1], vert1[0], vert1[1]);
                    }
                    g2.setStroke(lw);
                    if (trace) {
                        g2.setStroke(dashed);
                    }
                    g2.setPaint(sc);
                    g2.draw(p);
                    g2.setPaint(fc);
                    g2.fill(p);
                } else {
                    GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
                    p.moveTo(lp.get(0).getXy().x, lp.get(0).getXy().y);
                    p.curveTo(
                            lp.get(0).getC2().x, lp.get(0).getC2().y,
                            lp.get(1).getC1().x, lp.get(1).getC1().y,
                            lp.get(1).getXy().x, lp.get(1).getXy().y);
                    int i;
                    for (i = 2; i < lp.size(); i += 1) {
                        p.curveTo(
                                lp.get(i - 1).getC2().x, lp.get(i - 1).getC2().y,
                                lp.get(i).getC1().x, lp.get(i).getC1().y,
                                lp.get(i).getXy().x, lp.get(i).getXy().y);
                    }
                    p.curveTo(
                            lp.get(lp.size() - 1).getC2().x, lp.get(lp.size() - 1).getC2().y,
                            lp.get(0).getC1().x, lp.get(0).getC1().y,
                            lp.get(0).getXy().x, lp.get(0).getXy().y);
                    g2.setStroke(lw);
                    if (trace) {
                        g2.setStroke(dashed);
                    }
                    g2.setPaint(sc);
                    g2.draw(p);
                    g2.setPaint(fc);
                    g2.fill(p);
                }

                if (handel) {
                    if (mul) {
                        for (int i = 0; i < multyNum; i++) {
                            double ang = (360 / multyNum) * i;
                            lp.forEach((po) -> {
                                Shape s;

                                double[] vert12 = u.convert(ang, po.getXy().x, po.getXy().y, cp.x, cp.y);

                                if (pointView == 2) {
                                    double[] vert10 = u.convert(ang, po.getC1().x, po.getC1().y, cp.x, cp.y);
                                    double[] vert11 = u.convert(ang, po.getC2().x, po.getC2().y, cp.x, cp.y);
                                    s = makeLine((int) vert10[0], (int) vert10[1], (int) vert12[0], (int) vert12[1]);
                                    g2.setStroke(new BasicStroke(2));
                                    g2.setPaint(Color.BLACK);
                                    g2.draw(s);

                                    s = makeLine((int) vert11[0], (int) vert11[1], (int) vert12[0], (int) vert12[1]);
                                    g2.setStroke(new BasicStroke(2));
                                    g2.setPaint(Color.BLACK);
                                    g2.draw(s);

                                    s = drawEllipse((int) vert10[0], (int) vert10[1], po.getRc1());
                                    g2.setStroke(new BasicStroke(2));
                                    g2.setPaint(po.getCc1());
                                    g2.fill(s);
                                    g2.setPaint(Color.BLACK);
                                    g2.draw(s);

                                    s = drawEllipse((int) vert11[0], (int) vert11[1], po.getRc2());
                                    g2.setStroke(new BasicStroke(2));
                                    g2.setPaint(po.getCc2());
                                    g2.fill(s);
                                    g2.setPaint(Color.BLACK);
                                    g2.draw(s);
                                }
                                if (pointView == 1 || pointView == 2) {
                                    s = drawEllipse((int) vert12[0], (int) vert12[1], po.getRxy());
                                    g2.setStroke(new BasicStroke(2));
                                    g2.setPaint(po.getCxy());
                                    g2.fill(s);
                                    g2.setPaint(Color.BLACK);
                                    g2.draw(s);
                                }

                            });
                        }
                    } else {
                        lp.forEach((po) -> {
                            Shape s;
                            if (pointView == 2) {
                                s = makeLine(
                                        po.getC1().x,
                                        po.getC1().y,
                                        po.getXy().x,
                                        po.getXy().y);
                                g2.setStroke(new BasicStroke(2));
                                g2.setPaint(Color.BLACK);
                                g2.draw(s);

                                s = makeLine(
                                        po.getC2().x,
                                        po.getC2().y,
                                        po.getXy().x,
                                        po.getXy().y);
                                g2.setStroke(new BasicStroke(2));
                                g2.setPaint(Color.BLACK);
                                g2.draw(s);

                                s = drawEllipse(po.getC1().x, po.getC1().y, po.getRc1());
                                g2.setStroke(new BasicStroke(2));
                                g2.setPaint(po.getCc1());
                                g2.fill(s);
                                g2.setPaint(Color.BLACK);
                                g2.draw(s);

                                s = drawEllipse(po.getC2().x, po.getC2().y, po.getRc2());
                                g2.setStroke(new BasicStroke(2));
                                g2.setPaint(po.getCc2());
                                g2.fill(s);
                                g2.setPaint(Color.BLACK);
                                g2.draw(s);
                            }
                            if (pointView == 1 || pointView == 2) {
                                s = drawEllipse(po.getXy().x, po.getXy().y, po.getRxy());
                                g2.setStroke(new BasicStroke(2));
                                g2.setPaint(po.getCxy());
                                g2.fill(s);
                                g2.setPaint(Color.BLACK);
                                g2.draw(s);
                            }
                        });

                    }
                }
            } else {
                if (!(lp.isEmpty())) {
                    if (lp.size() == 1) {

                        if (mul) {
                            for (int i = 0; i < multyNum; i++) {
                                double ang = (360 / multyNum) * i;
                                double[] vert1 = u.convert(ang, lp.get(0).getXy().x, lp.get(0).getXy().y, cp.x, cp.y);
                                r = drawEllipse((int) vert1[0], (int) vert1[1], 10);
                                g2.setStroke(lw);
                                g2.setPaint(sc);
                                g2.draw(r);
                            }
                        } else {
                            r = drawEllipse(lp.get(0).getXy().x, lp.get(0).getXy().y, 10);
                            g2.setStroke(lw);
                            g2.setPaint(sc);
                            g2.draw(r);
                        }
                    } else if (lp.size() == 2) {
                        if (mul) {
                            GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
                            for (int i = 0; i < multyNum; i++) {
                                double ang = (360 / multyNum) * i;
                                double[] vert1 = u.convert(ang, lp.get(0).getXy().x, lp.get(0).getXy().y, cp.x, cp.y);
                                double[] vert2 = u.convert(ang, lp.get(1).getXy().x, lp.get(1).getXy().y, cp.x, cp.y);
                                p.moveTo((int) vert1[0], (int) vert1[1]);
                                p.lineTo((int) vert2[0], (int) vert2[1]);

                            }
                            g2.setStroke(lw);
                            if (trace) {
                                g2.setStroke(dashed);
                            }
                            g2.setPaint(sc);
                            g2.draw(p);
                        } else {
                            GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
                            p.moveTo(lp.get(0).getXy().x, lp.get(0).getXy().y);
                            p.lineTo(lp.get(1).getXy().x, lp.get(1).getXy().y);
                            g2.setStroke(lw);
                            if (trace) {
                                g2.setStroke(dashed);
                            }
                            g2.setPaint(sc);
                            g2.draw(p);
                        }
                    } else if (lp.size() == 3) {
                        if (mul) {
                            GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
                            for (int i = 0; i < multyNum; i++) {

                                double ang = (360 / multyNum) * i;
                                double[] vert1 = u.convert(ang, lp.get(0).getXy().x, lp.get(0).getXy().y, cp.x, cp.y);
                                double[] vert2 = u.convert(ang, lp.get(1).getC1().x, lp.get(1).getC1().y, cp.x, cp.y);
                                double[] vert3 = u.convert(ang, lp.get(1).getXy().x, lp.get(1).getXy().y, cp.x, cp.y);
                                double[] vert4 = u.convert(ang, lp.get(1).getC2().x, lp.get(1).getC2().y, cp.x, cp.y);
                                double[] vert5 = u.convert(ang, lp.get(2).getXy().x, lp.get(2).getXy().y, cp.x, cp.y);
                                p.moveTo((int) vert1[0], (int) vert1[1]);
                                p.quadTo((int) vert2[0], (int) vert2[1], (int) vert3[0], (int) vert3[1]);
                                p.quadTo((int) vert4[0], (int) vert4[1], (int) vert5[0], (int) vert5[1]);
                            }
                            g2.setStroke(lw);
                            if (trace) {
                                g2.setStroke(dashed);
                            }
                            g2.setPaint(sc);
                            g2.draw(p);
                        } else {
                            GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
                            p.moveTo(lp.get(0).getXy().x, lp.get(0).getXy().y);
                            p.quadTo(lp.get(1).getC1().x, lp.get(1).getC1().y, lp.get(1).getXy().x,
                                    lp.get(1).getXy().y);
                            p.quadTo(lp.get(1).getC2().x, lp.get(1).getC2().y, lp.get(2).getXy().x,
                                    lp.get(2).getXy().y);
                            g2.setStroke(lw);
                            if (trace) {
                                g2.setStroke(dashed);
                            }
                            g2.setPaint(sc);
                            g2.draw(p);
                        }
                    } else if (lp.size() > 3) {
                        if (mul) {
                            if (trace) {
                                for (int i = 0; i < multyNum; i++) {
                                    double ang = (360 / multyNum) * i;
                                    GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
                                    double[] vert1 = u.convert(ang, lp.get(lp.size() - 3).getXy().x,
                                            lp.get(lp.size() - 3).getXy().y, cp.x, cp.y);
                                    p.moveTo((int) vert1[0], (int) vert1[1]);
                                    for (int j = 2; j < lp.size() - 1; j += 1) {
                                        double[] vert2 = u.convert(ang, lp.get(lp.size() - 3).getC2().x,
                                                lp.get(lp.size() - 3).getC2().y, cp.x, cp.y);
                                        double[] vert3 = u.convert(ang, lp.get(lp.size() - 2).getC1().x,
                                                lp.get(lp.size() - 2).getC1().y, cp.x, cp.y);
                                        double[] vert4 = u.convert(ang, lp.get(lp.size() - 2).getXy().x,
                                                lp.get(lp.size() - 2).getXy().y, cp.x, cp.y);
                                        p.curveTo(
                                                (int) vert2[0], (int) vert2[1],
                                                (int) vert3[0], (int) vert3[1],
                                                (int) vert4[0], (int) vert4[1]);
                                    }
                                    double[] vert5 = u.convert(ang, lp.get(lp.size() - 2).getC2().x,
                                            lp.get(lp.size() - 2).getC2().y, cp.x, cp.y);
                                    double[] vert6 = u.convert(ang, lp.get(lp.size() - 1).getXy().x,
                                            lp.get(lp.size() - 1).getXy().y, cp.x, cp.y);
                                    p.quadTo(
                                            (int) vert5[0], (int) vert5[1],
                                            (int) vert6[0], (int) vert6[1]);
                                    g2.setStroke(lw);
                                    g2.setStroke(dashed);
                                    g2.setPaint(sc);
                                    g2.draw(p);
                                }
                            } else {
                                for (int i = 0; i < multyNum; i++) {
                                    double ang = (360 / multyNum) * i;
                                    GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
                                    double[] vert1 = u.convert(ang, lp.get(0).getXy().x, lp.get(0).getXy().y, cp.x,
                                            cp.y);
                                    double[] vert2 = u.convert(ang, lp.get(1).getC1().x, lp.get(1).getC1().y, cp.x,
                                            cp.y);
                                    double[] vert3 = u.convert(ang, lp.get(1).getXy().x, lp.get(1).getXy().y, cp.x,
                                            cp.y);
                                    p.moveTo((int) vert1[0], (int) vert1[1]);
                                    p.quadTo((int) vert2[0], (int) vert2[1], (int) vert3[0], (int) vert3[1]);
                                    for (int j = 2; j < lp.size() - 1; j += 1) {
                                        double[] vert4 = u.convert(ang, lp.get(j - 1).getC2().x,
                                                lp.get(j - 1).getC2().y, cp.x, cp.y);
                                        double[] vert5 = u.convert(ang, lp.get(j).getC1().x, lp.get(j).getC1().y, cp.x,
                                                cp.y);
                                        double[] vert6 = u.convert(ang, lp.get(j).getXy().x, lp.get(j).getXy().y, cp.x,
                                                cp.y);
                                        p.curveTo(
                                                (int) vert4[0], (int) vert4[1],
                                                (int) vert5[0], (int) vert5[1],
                                                (int) vert6[0], (int) vert6[1]);
                                    }
                                    double[] vert7 = u.convert(ang, lp.get(lp.size() - 2).getC2().x,
                                            lp.get(lp.size() - 2).getC2().y, cp.x, cp.y);
                                    double[] vert8 = u.convert(ang, lp.get(lp.size() - 1).getXy().x,
                                            lp.get(lp.size() - 1).getXy().y, cp.x, cp.y);
                                    p.quadTo(
                                            (int) vert7[0], (int) vert7[1],
                                            (int) vert8[0], (int) vert8[1]);
                                    g2.setStroke(lw);
                                    g2.setPaint(sc);
                                    g2.draw(p);
                                }
                            }
                        } else {
                            if (trace) {
                                GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
                                p.moveTo(lp.get(lp.size() - 3).getXy().x, lp.get(lp.size() - 3).getXy().y);
                                p.curveTo(
                                        lp.get(lp.size() - 3).getC2().x, lp.get(lp.size() - 3).getC2().y,
                                        lp.get(lp.size() - 2).getC1().x, lp.get(lp.size() - 2).getC1().y,
                                        lp.get(lp.size() - 2).getXy().x, lp.get(lp.size() - 2).getXy().y);
                                p.quadTo(
                                        lp.get(lp.size() - 2).getC2().x, lp.get(lp.size() - 2).getC2().y,
                                        lp.get(lp.size() - 1).getXy().x, lp.get(lp.size() - 1).getXy().y);
                                g2.setStroke(lw);
                                g2.setStroke(dashed);
                                g2.setPaint(sc);
                                g2.draw(p);
                            } else {
                                GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
                                p.moveTo(lp.get(0).getXy().x, lp.get(0).getXy().y);
                                p.quadTo(lp.get(1).getC1().x, lp.get(1).getC1().y, lp.get(1).getXy().x,
                                        lp.get(1).getXy().y);
                                for (int i = 2; i < lp.size() - 1; i += 1) {
                                    p.curveTo(
                                            lp.get(i - 1).getC2().x, lp.get(i - 1).getC2().y,
                                            lp.get(i).getC1().x, lp.get(i).getC1().y,
                                            lp.get(i).getXy().x, lp.get(i).getXy().y);
                                }
                                p.quadTo(
                                        lp.get(lp.size() - 2).getC2().x, lp.get(lp.size() - 2).getC2().y,
                                        lp.get(lp.size() - 1).getXy().x, lp.get(lp.size() - 1).getXy().y);
                                g2.setStroke(lw);
                                g2.setPaint(sc);
                                g2.draw(p);
                            }
                        }
                    }
                }
                if (handel) {

                    if (mul) {
                        for (int i = 0; i < multyNum; i++) {
                            double ang = (360 / multyNum) * i;
                            for (int j = 0; j < lp.size(); j++) {
                                Shape s;
                                double[] vert12 = u.convert(ang, lp.get(j).getXy().x, lp.get(j).getXy().y, cp.x, cp.y);
                                if (pointView == 2) {
                                    if (j > 0 && j < lp.size() - 1) {
                                        double[] vert10 = u.convert(ang, lp.get(j).getC1().x, lp.get(j).getC1().y, cp.x,
                                                cp.y);
                                        double[] vert11 = u.convert(ang, lp.get(j).getC2().x, lp.get(j).getC2().y, cp.x,
                                                cp.y);

                                        s = makeLine((int) vert10[0], (int) vert10[1], (int) vert12[0],
                                                (int) vert12[1]);
                                        g2.setStroke(new BasicStroke(2));
                                        g2.setPaint(Color.BLACK);
                                        g2.draw(s);

                                        s = makeLine((int) vert11[0], (int) vert11[1], (int) vert12[0],
                                                (int) vert12[1]);
                                        g2.setStroke(new BasicStroke(2));
                                        g2.setPaint(Color.BLACK);
                                        g2.draw(s);

                                        s = drawEllipse((int) vert10[0], (int) vert10[1], lp.get(j).getRc1());
                                        g2.setStroke(new BasicStroke(2));
                                        g2.setPaint(lp.get(j).getCc1());
                                        g2.fill(s);
                                        g2.setPaint(Color.BLACK);
                                        g2.draw(s);
                                        s = drawEllipse((int) vert11[0], (int) vert11[1], lp.get(j).getRc2());
                                        g2.setStroke(new BasicStroke(2));
                                        g2.setPaint(lp.get(j).getCc2());
                                        g2.fill(s);
                                        g2.setPaint(Color.BLACK);
                                        g2.draw(s);
                                    }
                                }
                                if (pointView == 1 || pointView == 2) {
                                    s = drawEllipse((int) vert12[0], (int) vert12[1], lp.get(j).getRxy());
                                    g2.setStroke(new BasicStroke(2));
                                    g2.setPaint(lp.get(j).getCxy());
                                    g2.fill(s);
                                    g2.setPaint(Color.BLACK);
                                    g2.draw(s);
                                }
                            }
                        }
                    } else {
                        for (int j = 0; j < lp.size(); j++) {
                            Shape s;
                            if (pointView == 2) {
                                if (j > 0 && j < lp.size() - 1) {
                                    s = makeLine(
                                            lp.get(j).getC1().x,
                                            lp.get(j).getC1().y,
                                            lp.get(j).getXy().x,
                                            lp.get(j).getXy().y);
                                    g2.setStroke(new BasicStroke(2));
                                    g2.setPaint(Color.BLACK);
                                    g2.draw(s);

                                    s = makeLine(
                                            lp.get(j).getC2().x,
                                            lp.get(j).getC2().y,
                                            lp.get(j).getXy().x,
                                            lp.get(j).getXy().y);
                                    g2.setStroke(new BasicStroke(2));
                                    g2.setPaint(Color.BLACK);
                                    g2.draw(s);

                                    s = drawEllipse(lp.get(j).getC1().x, lp.get(j).getC1().y, lp.get(j).getRc1());
                                    g2.setStroke(new BasicStroke(2));
                                    g2.setPaint(lp.get(j).getCc1());
                                    g2.fill(s);
                                    g2.setPaint(Color.BLACK);
                                    g2.draw(s);

                                    s = drawEllipse(lp.get(j).getC2().x, lp.get(j).getC2().y, lp.get(j).getRc2());
                                    g2.setStroke(new BasicStroke(2));
                                    g2.setPaint(lp.get(j).getCc2());
                                    g2.fill(s);
                                    g2.setPaint(Color.BLACK);
                                    g2.draw(s);
                                }
                            }
                            if (pointView == 1 || pointView == 2) {
                                s = drawEllipse(lp.get(j).getXy().x, lp.get(j).getXy().y, lp.get(j).getRxy());
                                g2.setStroke(new BasicStroke(2));
                                g2.setPaint(lp.get(j).getCxy());
                                g2.fill(s);
                                g2.setPaint(Color.BLACK);
                                g2.draw(s);

                            }
                        }
                    }
                }
            }
        }// end paintSpline

        private Line2D.Float makeLine(int x1, int y1, int x2, int y2) {
            return new Line2D.Float(x1, y1, x2, y2);
        }// end makeLine

        private Ellipse2D.Float drawEllipse(int x1, int y1, int size) {
            return new Ellipse2D.Float(x1 - (size / 2), y1 - (size / 2), size, size);
        }// end drawEllipse

 
        private Color newColor() {
            return colors[(colorIndex++) % 6];
        }// end newColor

        private boolean intersects(Point circle, int r) {
            int areaX = mouse.x - circle.x;
            int areaY = mouse.y - circle.y;
            return areaX * areaX + areaY * areaY <= r * r;
        }// end intersects

        private void takesnap(int num) {
            Map<String, Object> m = cubicCurveArrsy.get(cubicCurveArrsy.size() - 1);
            
            List<PointObj> lp = (List<PointObj>) m.get("p");
            snapXY = lp.get(num).getXy();
            snapc1 = new Point(snapXY.x - lp.get(num).getC1().x, snapXY.y - lp.get(num).getC1().y);
            snapc2 = new Point(snapXY.x - lp.get(num).getC2().x, snapXY.y - lp.get(num).getC2().y);
        }// end takesnap

    }// end PaintSurface

}// end DrawingPanel
