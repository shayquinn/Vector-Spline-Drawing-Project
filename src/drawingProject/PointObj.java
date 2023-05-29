/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawingProject;

import java.awt.Color;
import java.awt.Point;

/**
 * The PointObj class defines an object that contains three points, their radii, colors, and other
 * properties.
 */
class PointObj {

    private Point xy, c1, c2;
    private int rxy, rc1, rc2;
    private Color cxy, cc1, cc2;
    private boolean mirror, selected;

    public PointObj(Point xy, Point c1, Point c2) {
        this.xy = xy;
        this.c1 = c1;
        this.c2 = c2;
        this.rxy = 10;
        this.rc1 = 10;
        this.rc2 = 10;
        this.cxy = Color.BLUE;
        this.cc1 = Color.RED;
        this.cc2 = Color.RED;
        this.mirror = true;
        this.selected = false;
    }//end PointObj

    @Override
    public String toString() {
        return String.format(
                "xy: " + xy.x + ", " + xy.y + ",\n"
                + "c1: " + c1.x + ", " + c1.y + ",\n"
                + "c2: " + c2.x + ", " + c2.y + ",\n"
                + "rxy: " + rxy + ",\n"
                + "rc1: " + rc1 + ",\n"
                + "rc2: " + rc2 + ",\n"
                + "cxy: " + cxy + ",\n"
                + "cc1: " + cc1 + ",\n"
                + "cc2: " + cc2 + ",\n"
                + "mirror: " + mirror + ",\n"
                + "selected: " + selected + ",\n"
                + ".................................."
        );
    }//end toString

    public boolean isMirror() {
        return mirror;
    }

    public void setMirror(boolean mirror) {
        this.mirror = mirror;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public Color getCxy() {
        return cxy;
    }

    public void setCxy(Color cxy) {
        this.cxy = cxy;
    }

    public Color getCc1() {
        return cc1;
    }

    public void setCc1(Color cc1) {
        this.cc1 = cc1;
    }

    public Color getCc2() {
        return cc2;
    }

    public void setCc2(Color cc2) {
        this.cc2 = cc2;
    }

    public int getRxy() {
        return rxy;
    }

    public void setRxy(int rxy) {
        this.rxy = rxy;
    }

    public int getRc1() {
        return rc1;
    }

    public void setRc1(int rc1) {
        this.rc1 = rc1;
    }

    public int getRc2() {
        return rc2;
    }

    public void setRc2(int rc2) {
        this.rc2 = rc2;
    }

    public Point getXy() {
        return xy;
    }

    public void setXy(Point xy) {
        this.xy = xy;
    }

    public Point getC1() {
        return c1;
    }

    public void setC1(Point c1) {
        this.c1 = c1;
    }

    public Point getC2() {
        return c2;
    }

    public void setC2(Point c2) {
        this.c2 = c2;
    }

}//end PointObj
