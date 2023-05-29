package drawingProject;


import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

/**
 * The Spline class provides a method to draw splines based on a list of points using control points.
 */
public class Spline {

    static double tension = 0.5;

    // The `drawSplines` method in the `Spline` class takes a list of `PointObj` objects representing
    // points on a curve and calculates the control points needed to draw a spline curve through those
    // points. It returns a new list of `PointObj` objects with the original points and their corresponding
    // control points.
    public  List<PointObj> drawSplines(List<PointObj> tempQuadCurve) {
        if (tempQuadCurve.size() >= 2) {
            for (int i = 1; i < tempQuadCurve.size() - 1; i++) {
                double[] cpsvar = ctlpts(
                        tempQuadCurve.get(i - 1).getXy().x,
                        tempQuadCurve.get(i - 1).getXy().y,
                        tempQuadCurve.get(i).getXy().x,
                        tempQuadCurve.get(i).getXy().y,
                        tempQuadCurve.get(i + 1).getXy().x,
                        tempQuadCurve.get(i + 1).getXy().y
                );
                tempQuadCurve.get(i).setC1(new Point((int) cpsvar[0], (int) cpsvar[1]));
                tempQuadCurve.get(i).setC2(new Point((int) cpsvar[2], (int) cpsvar[3]));
            }
        }
        return tempQuadCurve;
    }//end drawSplines

    // The `public List<PointObj> drawSplines(Point[] p)` method in the `Spline` class takes an array
    // of `Point` objects representing points on a curve and calculates the control points needed to
    // draw a spline curve through those points. It returns a new list of `PointObj` objects with the
    // original points and their corresponding control points. This method is an overloaded version of
    // the `drawSplines` method that takes a list of `PointObj` objects as input.
    public List<PointObj> drawSplines(Point[] p) {
        List<PointObj> rpa = new ArrayList<PointObj>();
        if (p.length > 0) {
            for (int i = 0; i <= p.length - 1; i++) {
                double[] cpsvar = null;
                switch (i) {
                    case 0:
                        cpsvar = ctlpts(
                                p[3].x,
                                p[3].y,
                                p[i].x,
                                p[i].y,
                                p[i + 1].x,
                                p[i + 1].y
                        );
                        break;
                    case 1:
                    case 2:
                        cpsvar = ctlpts(
                                p[i - 1].x,
                                p[i - 1].y,
                                p[i].x,
                                p[i].y,
                                p[i + 1].x,
                                p[i + 1].y
                        );
                        break;
                    case 3:
                        cpsvar = ctlpts(
                                p[i - 1].x,
                                p[i - 1].y,
                                p[i].x,
                                p[i].y,
                                p[0].x,
                                p[0].y
                        );
                        break;
                }
                Point c1 = new Point((int) cpsvar[0], (int) cpsvar[1]);
                Point c2 = new Point((int) cpsvar[2], (int) cpsvar[3]);

                rpa.add(new PointObj(p[i], c1, c2));
            }

        }
        return rpa;
    }

    // The `public Point[] ellipseSplines(Point[] pa)` method in the `Spline` class takes an array of
    // `Point` objects representing the four points of an ellipse and calculates the control points
    // needed to draw a spline curve through those points. It returns a new array of `Point` objects
    // with the calculated control points.
    public Point[] ellipseSplines(Point[] pa) {
        Point[] npa = new Point[8];
        double[] cp1 = ctlpts(pa[3].x, pa[3].y, pa[0].x, pa[0].y, pa[1].x, pa[1].y);
        npa[0] = new Point((int) cp1[0], (int) cp1[1]);
        npa[1] = new Point((int) cp1[2], (int) cp1[3]);
        double[] cp2 = ctlpts(pa[0].x, pa[0].y, pa[1].x, pa[1].y, pa[2].x, pa[2].y);
        npa[2] = new Point((int) cp2[0], (int) cp2[1]);
        npa[3] = new Point((int) cp2[2], (int) cp2[3]);
        double[] cp3 = ctlpts(pa[1].x, pa[1].y, pa[2].x, pa[2].y, pa[3].x, pa[3].y);
        npa[4] = new Point((int) cp3[0], (int) cp3[1]);
        npa[5] = new Point((int) cp3[2], (int) cp3[3]);
        double[] cp4 = ctlpts(pa[2].x, pa[2].y, pa[3].x, pa[3].y, pa[0].x, pa[0].y);
        npa[6] = new Point((int) cp4[0], (int) cp4[1]);
        npa[7] = new Point((int) cp4[2], (int) cp4[3]);
        return npa;
    }

    // The `public Point[] oneCurve(Point[] pa)` method in the `Spline` class takes an array of three
    // `Point` objects representing the start point, control point, and end point of a curve and
    // calculates the control points needed to draw a spline curve through those points. It returns a
    // new array of two `Point` objects with the calculated control points. This method is used to draw
    // a single curve, as opposed to a series of connected curves.
    public Point[] oneCurve(Point[] pa) {
        Point[] npa = new Point[2];
        double[] cp1 = ctlpts(pa[0].x, pa[0].y, pa[1].x, pa[1].y, pa[2].x, pa[2].y);
        npa[0] = new Point((int) cp1[0], (int) cp1[1]);
        npa[1] = new Point((int) cp1[2], (int) cp1[3]);
        return npa;
    }

    // The `ctlpts` method in the `Spline` class takes in three sets of x and y coordinates
    // representing three points on a curve and calculates the control points needed to draw a spline
    // curve through those points. It uses the `va` method to calculate the vector between the first
    // and last points, and then calculates the distances between the first and second points and the
    // second and third points. It then uses these values to calculate the control points for the
    // second point on the curve and returns them as an array of doubles.
    public double[] ctlpts(double x1, double y1, double x2, double y2, double x3, double y3) {
        double[] args = {x1, y1, x2, y2, x3, y3};
        double t = tension;
        double[] v = va(args, 0, 2);
        double d01 = dista(args, 0, 1);
        double d12 = dista(args, 1, 2);
        double d012 = d01 + d12;
        double[] ra = {
            x2 - v[0] * t * d01 / d012, y2 - v[1] * t * d01 / d012,
            x2 + v[0] * t * d12 / d012, y2 + v[1] * t * d12 / d012
        };
        return ra;
    }//end ctlpts

    // The `va` method in the `Spline` class takes in an array of doubles representing x and y
    // coordinates of three points on a curve, as well as two indices `i` and `j` representing the
    // first and last points. It calculates the vector between the first and last points and returns it
    // as an array of doubles.
    public double[] va(double[] arr, int i, int j) {
        double[] ba = {arr[2 * j] - arr[2 * i], arr[2 * j + 1] - arr[2 * i + 1]};
        return ba;
    }//end va

    // The `dista` method in the `Spline` class takes in an array of doubles representing x and y
    // coordinates of three points on a curve, as well as two indices `i` and `j` representing the
    // first and last points. It calculates the Euclidean distance between the two points represented
    // by the indices `i` and `j` in the array and returns the distance as a double.
    public double dista(double[] arr, int i, int j) {
        return Math.sqrt(Math.pow(arr[2 * i] - arr[2 * j], 2) + Math.pow(arr[2 * i + 1] - arr[2 * j + 1], 2));
    }//end dista

    // The `findDistance` method in the `Spline` class takes in two `Point` objects representing two
    // points in a 2D space and calculates the Euclidean distance between them. It returns the distance
    // as an integer.
    public int findDistance(Point po1, Point po2) {
        return (int) Math.round(Math.sqrt(((po1.x - po2.x) * (po1.x - po2.x)) + ((po1.y - po2.y) * (po1.y - po2.y))));
    }//end findDistance

    // The `convert` method in the `Spline` class takes in an angle `ang` in degrees, as well as five
    // doubles representing x and y coordinates of two points and the center point of a circle. It
    // calculates the new x and y coordinates of the first point after rotating it by the given angle
    // around the center point of the circle. It returns the new x and y coordinates as an array of
    // doubles.
    public double[] convert(double ang, double x, double y, double cx, double cy) {
        return new double[]{
            cx + Math.cos(ang * Math.PI / 180) * (x - cx) - Math.sin(ang * Math.PI / 180) * (y - cy),
            cy + Math.sin(ang * Math.PI / 180) * (x - cx) + Math.cos(ang * Math.PI / 180) * (y - cy)
        };
    }//end convert

}//end Spline
