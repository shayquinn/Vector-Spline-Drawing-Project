package drawingProject;


import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Spline {

    static double tension = 0.5;

    public  List<PointObj> drawSplines(List<PointObj> tempQuadCurve) {
        if (tempQuadCurve.size() >= 2) {
            for (int i = 1; i < tempQuadCurve.size() - 1; i++) {
                int len = tempQuadCurve.size();
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

    

   

    public List<PointObj> drawSplines(Point[] p) {
        List<PointObj> rpa = new ArrayList<PointObj>();
        if (p.length > 0) {
            for (int i = 0; i <= p.length - 1; i++) {
                int len = p.length;
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

                /* 
                int d1 = findDistance(c1, p[i]);
                int d2 = findDistance(c2, p[i]);
                
                double[] con1 = convert(180, c1.x, c1.y, p[i].x, p[i].y);  
                double[] con2 = convert(180, c2.x, c2.y, p[i].x, p[i].y);
                
                Point pcon1 = new Point((int)con1[0], (int)con1[1]);
                Point pcon2 = new Point((int)con2[0], (int)con2[1]);
                
                if(d1>d2){
                    rpa[i] = new PointObj(p[i], c1, pcon1, ia, false, false);
                }else if(d1<d2){
                    rpa[i] = new PointObj(p[i], pcon2, c2, ia, false, false);
                }else{
                    rpa[i] = new PointObj(p[i], c1, c2, ia, false, false);
                }
                 */
            }

        }
        return rpa;
    }

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

    public Point[] oneCurve(Point[] pa) {
        Point[] npa = new Point[2];
        double[] cp1 = ctlpts(pa[0].x, pa[0].y, pa[1].x, pa[1].y, pa[2].x, pa[2].y);
        npa[0] = new Point((int) cp1[0], (int) cp1[1]);
        npa[1] = new Point((int) cp1[2], (int) cp1[3]);
        return npa;
    }

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

    public double[] va(double[] arr, int i, int j) {
        double[] ba = {arr[2 * j] - arr[2 * i], arr[2 * j + 1] - arr[2 * i + 1]};
        return ba;
    }//end va

    public double dista(double[] arr, int i, int j) {
        return Math.sqrt(Math.pow(arr[2 * i] - arr[2 * j], 2) + Math.pow(arr[2 * i + 1] - arr[2 * j + 1], 2));
    }//end dista

    public int findDistance(Point po1, Point po2) {
        return (int) Math.round(Math.sqrt(((po1.x - po2.x) * (po1.x - po2.x)) + ((po1.y - po2.y) * (po1.y - po2.y))));
    }//end findDistance

    public double[] convert(double ang, double x, double y, double cx, double cy) {
        return new double[]{
            cx + Math.cos(ang * Math.PI / 180) * (x - cx) - Math.sin(ang * Math.PI / 180) * (y - cy),
            cy + Math.sin(ang * Math.PI / 180) * (x - cx) + Math.cos(ang * Math.PI / 180) * (y - cy)
        };
    }//end convert

}//end Spline
