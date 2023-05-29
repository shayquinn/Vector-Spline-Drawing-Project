/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawingProject;

import java.awt.Point;
import java.awt.geom.CubicCurve2D;
import java.awt.geom.GeneralPath;
import java.util.ArrayList;
import java.util.List;

/**
 * The `Utility` class contains various methods for performing mathematical calculations and creating
 * Java 2D shapes, such as cubic Bezier curves and elliptical paths.
 */
public class Utility {

    private int screenW, screenH;

    // The `public Utility(int screenW, int screenH)` is a constructor for the `Utility` class that
    // takes in two integer parameters `screenW` and `screenH`. It initializes the `screenW` and
    // `screenH` instance variables of the `Utility` class with the values passed in as parameters.
    // This constructor is used to create an instance of the `Utility` class with the specified screen
    // width and height.
    public Utility(int screenW, int screenH) {
        this.screenW = screenW;
        this.screenH = screenH;
    }

    // The `public CubicCurve2D.Float drawCubicCurve(Point xy1, Point c1, Point c2, Point xy2)` method
    // takes in four `Point` objects as parameters: `xy1`, `c1`, `c2`, and `xy2`. It then creates and
    // returns a new `CubicCurve2D.Float` object that represents a cubic curve with the specified
    // control points and end points. The `xy1` and `xy2` parameters represent the start and end points
    // of the curve, while `c1` and `c2` represent the control points that determine the shape of the
    // curve. The `CubicCurve2D.Float` class is a Java 2D shape that represents a cubic Bezier curve
    // defined by two anchor points and two control points.
    public CubicCurve2D.Float drawCubicCurve(Point xy1, Point c1, Point c2, Point xy2) {
        return new CubicCurve2D.Float(xy1.x, xy1.y, c1.x, c1.y, c2.x, c2.y, xy2.x, xy2.y);
    }//end drawCubicCurve

    // The `public double[] convert(double ang, double x, double y, double cx, double cy)` method takes
    // in five double parameters: `ang`, `x`, `y`, `cx`, and `cy`. It then performs a mathematical
    // transformation on the `x` and `y` coordinates based on the angle `ang` and the center point `cx`
    // and `cy`. The method returns a double array with two elements, representing the new `x` and `y`
    // coordinates after the transformation. This method is used to rotate a point around a center
    // point by a specified angle.
    public double[] convert(double ang, double x, double y, double cx, double cy) {
        return new double[]{
            cx + Math.cos(ang * Math.PI / 180) * (x - cx) - Math.sin(ang * Math.PI / 180) * (y - cy),
            cy + Math.sin(ang * Math.PI / 180) * (x - cx) + Math.cos(ang * Math.PI / 180) * (y - cy)
        };
    }//end convert

    // The `public Point convert(int ang, Point xy, Point cxy)` method takes in three parameters: an
    // integer `ang`, and two `Point` objects `xy` and `cxy`. It performs a mathematical transformation
    // on the `xy` point based on the angle `ang` and the center point `cxy`. The method returns a new
    // `Point` object representing the new coordinates of the transformed point. This method is used to
    // rotate a point around a center point by a specified angle.
    public Point convert(int ang, Point xy, Point cxy) {
        return new Point(
                cxy.x + (int) Math.cos(ang * Math.PI / 180) * (xy.x - cxy.x) - (int) Math.sin(ang * Math.PI / 180) * (xy.y - cxy.y),
                cxy.y + (int) Math.sin(ang * Math.PI / 180) * (xy.x - cxy.x) + (int) Math.cos(ang * Math.PI / 180) * (xy.y - cxy.y));

    }//end convert

    // The `public int findDistance(Point po1, Point po2)` method takes in two `Point` objects as
    // parameters: `po1` and `po2`. It then calculates and returns the distance between these two
    // points using the distance formula. The method first calculates the difference between the `x`
    // coordinates and the `y` coordinates of the two points, squares these differences, adds them
    // together, and then takes the square root of the result to get the distance between the two
    // points.
    public int findDistance(Point po1, Point po2) {
        return (int) Math.round(Math.sqrt(((po1.x - po2.x) * (po1.x - po2.x)) + ((po1.y - po2.y) * (po1.y - po2.y))));
    }//end findDistance

    // The `public double findAngle(Point xy, Point cxy, int ty)` method takes in three parameters: two
    // `Point` objects `xy` and `cxy`, and an integer `ty`. It calculates and returns the angle between
    // the line connecting the two points and the horizontal axis, in either radians or degrees
    // depending on the value of `ty`.
    public double findAngle(Point xy, Point cxy, int ty) {
        double ang = 0;
        if (ty == 0) {
            ang = Math.PI + Math.atan2(xy.x - cxy.x, xy.y - cxy.y); // In radians
        } else if (ty == 1) {
            double an = Math.PI + Math.atan2(xy.x - cxy.x, xy.y - cxy.y);
            ang = Math.round(an * (180 / Math.PI)); //degrees
        }
        return ang;
    }//end findAngle

    // The `evenPoints` method takes in an array of `Point` objects `pa` as a parameter. It then
    // calculates and returns a new array of two `Point` objects representing the start and end points
    // of a cubic curve that passes through the middle point of the input array. The method first
    // calculates the middle point of the input array using a `Spline` object. It then calculates the
    // distance between the middle point and the two end points of the input array, and selects the end
    // point that is closest to the middle point. Finally, it uses the `convert` method to rotate the
    // selected end point by 180 degrees around the middle point, and returns the middle point and the
    // rotated end point as the two points of the output array.
    public Point[] evenPoints(Point[] pa) {
        Point blue = pa[1];
        Point[] rpa = new Point[2];
        Spline sp = new Spline();
        Point[] oc = sp.oneCurve(pa);
        int c1 = findDistance(oc[0], blue), c2 = findDistance(oc[1], blue);
        int sel = 0;
        if (c1 == c2) {
            sel = 0;
        } else if (c1 > c2) {
            sel = 0;
        } else if (c1 < c2) {
            sel = 1;
        }
        rpa[0] = oc[sel];
        double[] d1 = convert(180, oc[sel].x, oc[sel].y, blue.x, blue.y);
        rpa[1] = new Point((int) d1[0], (int) d1[1]);
        return rpa;
    }//end evenPoints

    // The `public Point findAwayPoint(double d, double a, Point ppoint)` method takes in three
    // parameters: a double `d`, a double `a`, and a `Point` object `ppoint`. It calculates and returns
    // a new `Point` object that represents a point that is a distance `d` away from the `ppoint` point
    // at an angle `a` from the horizontal axis. The method uses trigonometry to calculate the `x` and
    // `y` coordinates of the new point based on the distance and angle parameters. This method is used
    // to find a point that is a certain distance away from a given point in a specified direction.
    public Point findAwayPoint(double d, double a, Point ppoint) {
        return new Point((int) ((d * Math.cos(a)) + ppoint.x), (int) ((d * Math.sin(a)) + ppoint.y));
    }//end findAwayPoint

    // The `public GeneralPath ellipsepath(Point sp, Point ep)` method takes in two `Point` objects as
    // parameters: `sp` and `ep`. It then creates and returns a new `GeneralPath` object that
    // represents an elliptical path defined by the two points. The method first calculates the width
    // and height of the ellipse based on the difference between the `x` and `y` coordinates of the two
    // points. It then calculates the control point offsets and end points of the curve using a
    // mathematical formula, and uses the `moveTo` and `curveTo` methods of the `GeneralPath` class to
    // create a cubic Bezier curve that approximates the elliptical shape. The resulting `GeneralPath`
    // object can be used to draw or fill the elliptical shape on a Java 2D graphics context.
    public GeneralPath ellipsepath(Point sp, Point ep) {
        GeneralPath p = new GeneralPath(GeneralPath.WIND_NON_ZERO);
        int w = (screenW - sp.x) - (screenW - ep.x);
        int h = (screenH - sp.y) - (screenH - ep.y);
        double kappa = .5522848,
                ox = (w / 2) * kappa, // control point offset horizontal
                oy = (h / 2) * kappa, // control point offset vertical
                xe = sp.x + w, // x-end
                ye = sp.y + h, // y-end
                xm = sp.x + w / 2, // x-middle
                ym = sp.y + h / 2;       // y-middle

        p.moveTo(sp.x, ym);
        p.curveTo(sp.x, ym - oy, xm - ox, sp.y, xm, sp.y);
        p.curveTo(xm + ox, sp.y, xe, ym - oy, xe, ym);
        p.curveTo(xe, ym + oy, xm + ox, ye, xm, ye);
        p.curveTo(xm - ox, ye, sp.x, ym + oy, sp.x, ym);
        return p;

    }//end ellipsepath

    // The `ellipPointObj` method takes in two `PointObj` objects as parameters: `sp` and `ep`. It then
    // calculates and returns a list of four `PointObj` objects representing the control points of a
    // cubic Bezier curve that approximates an elliptical shape defined by the two input points. The
    // method first calculates the width and height of the ellipse based on the difference between the
    // `x` and `y` coordinates of the two points. It then calculates the control point offsets and end
    // points of the curve using a mathematical formula, and creates four `PointObj` objects
    // representing the control points for each of the four segments of the curve. The resulting list
    // of `PointObj` objects can be used to draw or fill the elliptical shape on a Java 2D graphics
    // context.
    public List<PointObj> ellipPointObj(PointObj sp, PointObj ep) {
        List<PointObj> p = new ArrayList<>();

        //int difx = (screenW - staticstartDrag.x) - (screenW - ep.x);
        //int dify = (screenW - staticstartDrag.y) - (screenW - ep.y);
        //sp.x = sp.x += difx;
        //sp.y = sp.y += dify;
        int w = (screenW - sp.getXy().x) - (screenW - ep.getXy().x);
        int h = (screenH - sp.getXy().y) - (screenH - ep.getXy().y);
        double kappa = .5522848,
                ox = (w / 2) * kappa, // control point offset horizontal
                oy = (h / 2) * kappa, // control point offset vertical
                xe = sp.getXy().x + w, // x-end
                ye = sp.getXy().y + h, // y-end
                xm = sp.getXy().x + w / 2, // x-middle
                ym = sp.getXy().y + h / 2;    // y-middle
        p.add(new PointObj(
                new Point((int) sp.getXy().x, (int) ym),
                new Point((int) sp.getXy().x, (int) (ym + oy)),
                new Point(sp.getXy().x, (int) (ym - oy))
                ));

        p.add(new PointObj(
                new Point((int) xm, sp.getXy().y),
                new Point((int) (xm - ox), (int) sp.getXy().y),
                new Point((int) (xm + ox), sp.getXy().y)
                ));

        p.add(new PointObj(
                new Point((int) xe, (int) ym),
                new Point((int) xe, (int) (ym - oy)),
                new Point((int) xe, (int) (ym + oy))
                ));

        p.add(new PointObj(
                new Point((int) xm, (int) ye),
                new Point((int) (xm + ox), (int) ye),
                new Point((int) (xm - ox), (int) ye)
                ));
        /* 
           sp.x, ym
                   
           sp.x, ym - oy,   xm-ox, sp.y,   xm, sp.y
            
           xm+ox, sp.y,   xe, ym-oy,   xe, ym
            
           xe, ym+oy,   xm+ox, ye,   xm, ye
            
           xm-ox, ye,   sp.x, ym+oy,   sp.x, ym
         */
        return p;

    }//end ellipsepath
}//end class
