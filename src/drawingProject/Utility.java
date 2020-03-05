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

public class Utility {

    private int screenW, screenH;

    public Utility(int screenW, int screenH) {
        this.screenW = screenW;
        this.screenH = screenH;
    }

    public CubicCurve2D.Float drawCubicCurve(Point xy1, Point c1, Point c2, Point xy2) {
        return new CubicCurve2D.Float(xy1.x, xy1.y, c1.x, c1.y, c2.x, c2.y, xy2.x, xy2.y);
    }//end drawCubicCurve

    public double[] convert(double ang, double x, double y, double cx, double cy) {
        return new double[]{
            cx + Math.cos(ang * Math.PI / 180) * (x - cx) - Math.sin(ang * Math.PI / 180) * (y - cy),
            cy + Math.sin(ang * Math.PI / 180) * (x - cx) + Math.cos(ang * Math.PI / 180) * (y - cy)
        };
    }//end convert

    public Point convert(int ang, Point xy, Point cxy) {
        return new Point(
                cxy.x + (int) Math.cos(ang * Math.PI / 180) * (xy.x - cxy.x) - (int) Math.sin(ang * Math.PI / 180) * (xy.y - cxy.y),
                cxy.y + (int) Math.sin(ang * Math.PI / 180) * (xy.x - cxy.x) + (int) Math.cos(ang * Math.PI / 180) * (xy.y - cxy.y));

    }//end convert

    public int findDistance(Point po1, Point po2) {
        return (int) Math.round(Math.sqrt(((po1.x - po2.x) * (po1.x - po2.x)) + ((po1.y - po2.y) * (po1.y - po2.y))));
    }//end findDistance

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

    public Point findAwayPoint(double d, double a, Point ppoint) {
        return new Point((int) ((d * Math.cos(a)) + ppoint.x), (int) ((d * Math.sin(a)) + ppoint.y));
    }//end findAwayPoint

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
}
