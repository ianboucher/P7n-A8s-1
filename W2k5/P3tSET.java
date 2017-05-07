import edu.princeton.cs.algs4.SET;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class PointSET {
    
    private SET<Point2D> points;
    
    // construct an empty set of points 
    public PointSET() {  
        points = new SET<Point2D>();
    }
    
    public boolean isEmpty() {
        return points.isEmpty();
    }
    
    
    // number of points in the set
    public int size() {
        return points.size();
    }
    
    
    // add the point to the set (if it is not already in the set)
    public void insert(Point2D p) {
        if (!points.contains(p)) points.add(p);
    }
    
    
    // does the set contain point p?
    public boolean contains(Point2D p) {
        return points.contains(p);
    }
    
    
    // draw all points to standard draw
    public void draw() {
        for (Point2D point : points) {
            point.draw();
        }
    }
    
    
    // return all points that are inside query rectangle
    public Iterable<Point2D> range(RectHV rect) {
        Stack<Point2D> stack = new Stack<Point2D>();
        
        for (Point2D point : points) {
            if (rect.contains(point)) {
                stack.push(point);
            }
        }
        return stack;
    }
    
    
    // return nearest neighbor in the set to point p; null if the set is empty    
    public Point2D nearest(Point2D p) {
        
        if (isEmpty()) return null;
        
        Point2D nearest = null;
        
        for (Point2D neighbor : points) {
            if (nearest == null) {
                nearest = neighbor;
            }
            else if (neighbor.distanceSquaredTo(p) < nearest.distanceSquaredTo(p)) {
                nearest = neighbor;
            }
        }     
        return nearest;
    }
        

    // basic unit testing of the methods
    public static void main(String[] args) {

        PointSET pointSet = new PointSET();
        In file = new In(args[0]);
        
        while (!file.isEmpty()) {
            Point2D point = new Point2D(file.readFloat(), file.readFloat());
            pointSet.insert(point);
        }
        
        StdOut.println("Size: " + pointSet.size());
        StdOut.println("Is empty: " + pointSet.isEmpty());
        
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        pointSet.draw();

        StdDraw.setPenColor(StdDraw.BLUE);
        StdDraw.setPenRadius();

        RectHV rect = new RectHV(0.0, 0.0, 0.5, 0.5);
        rect.draw();
        
        StdOut.println("Points in rectangle " + rect + ":");
        for (Point2D point : pointSet.range(rect)) {
            StdOut.println(point);
        }
        StdOut.println("------------------------");

        Point2D point = new Point2D(0.1, 0.1);
        point.draw();
        StdOut.println("The nearest point to point " + point + ": ");
        StdOut.println(pointSet.nearest(point));
        StdOut.println("-");
    }
}