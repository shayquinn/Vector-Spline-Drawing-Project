/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package drawingProject;

import java.awt.geom.Point2D; 
import java.util.ArrayList;
import java.util.List;
 
public class RamerDouglasPeuckerAlgorithm {
    private static double perpendicularDistance(Point2D.Double pt, Point2D.Double lineStart, Point2D.Double lineEnd) {
         
        double dx = lineEnd.x - lineStart.x;
        double dy = lineEnd.y - lineStart.y;
 
        // Normalize
        double mag = Math.hypot(dx, dy);
        if (mag > 0.0) {
            dx /= mag;
            dy /= mag;
        }
        double pvx = pt.x - lineStart.x;
        double pvy = pt.y - lineStart.y;
 
        // Get dot product (project pv onto normalized direction)
        double pvdot = dx * pvx + dy * pvy;
 
        // Scale line direction vector and subtract it from pv
        double ax = pvx - pvdot * dx;
        double ay = pvy - pvdot * dy;
 
        return Math.hypot(ax, ay);
    }
 
    public static void ramerDouglasPeucker(List<Point2D.Double> pointList, double epsilon, List<Point2D.Double> out) {
        if (pointList.size() < 2) throw new IllegalArgumentException("Not enough points to simplify");
 
        // Find the point with the maximum distance from line between the start and end
        double dmax = 0.0;
        int index = 0;
        int end = pointList.size() - 1;
        for (int i = 1; i < end; ++i) {
            double d = perpendicularDistance(pointList.get(i), pointList.get(0), pointList.get(end));
            if (d > dmax) {
                index = i;
                dmax = d;
            }
        }
 
        // If max distance is greater than epsilon, recursively simplify
        if (dmax > epsilon) {
            List<Point2D.Double> recResults1 = new ArrayList<>();
            List<Point2D.Double> recResults2 = new ArrayList<>();
            List<Point2D.Double> firstLine = pointList.subList(0, index + 1);
            List<Point2D.Double> lastLine = pointList.subList(index, pointList.size());
            ramerDouglasPeucker(firstLine, epsilon, recResults1);
            ramerDouglasPeucker(lastLine, epsilon, recResults2);
 
            // build the result list
            out.addAll(recResults1.subList(0, recResults1.size() - 1));
            out.addAll(recResults2);
            if (out.size() < 2) throw new RuntimeException("Problem assembling output");
        } else {
            // Just return start and end points
            out.clear();
            out.add(pointList.get(0));
            out.add(pointList.get(pointList.size() - 1));
        }
    }
    /*
    public static void main(String[] args) {
        List<Point2D.Double> pointList = new ArrayList<>();
                pointList.add(new Point2D.Double(0.0, 0.0));
                pointList.add(new Point2D.Double(1.0, 0.1));
                pointList.add(new Point2D.Double(2.0, -0.1));
                pointList.add(new Point2D.Double(3.0, 5.0));
                pointList.add(new Point2D.Double(4.0, 6.0));
                pointList.add(new Point2D.Double(5.0, 7.0));
                pointList.add(new Point2D.Double(6.0, 8.1));
                pointList.add(new Point2D.Double(7.0, 9.0));
                pointList.add(new Point2D.Double(8.0, 9.0));
                pointList.add(new Point2D.Double(9.0, 9.0));
        
        List<Point2D.Double> pointListOut = new ArrayList<>();
        ramerDouglasPeucker(pointList, 1.0, pointListOut);
        System.out.println("Points remaining after simplification:");
        // pointListOut.forEach(System.out::println);
        pointListOut.forEach((p) -> {
            System.out.println(p);
        });

    }
    */
}
