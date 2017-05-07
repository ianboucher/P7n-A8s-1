import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class BruteCollinearPoints {
    
    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();
    private HashMap<Point, Boolean> seen = new HashMap<Point, Boolean>();
    
    // finds all line segments containing 4 points
    public BruteCollinearPoints(Point[] pointsIn) {

        if (pointsIn == null) throw new NullPointerException();
        
        Point[] points = pointsIn.clone(); // copy to prevent mutation of input
        
        Arrays.sort(points);
        checkForDuplicates(points);

        for (int p = 0; p < points.length - 3; p++) {
                        
            for (int q = p + 1; q < points.length - 2; q++) {
                double slope = points[p].slopeTo(points[q]);
                
                for (int r = q + 1; r < points.length - 1; r++) {
                    if (Double.compare(points[p].slopeTo(points[r]), slope) == 0) {
                    
                        for (int s = r + 1; s < points.length; s++) {
                            if (Double.compare(points[r].slopeTo(points[s]), slope) == 0) {
                                
                                segments.add(new LineSegment(points[p], points[s]));
                                
                                // force increment of q, r, s to prevent different
                                // combinations of same segment
                                q = s - 1;
                                r = s;
                                s = s + 1;                              
                            }                                
                        }
                    }
                }
            }
        }
    }
    
    public int numberOfSegments() {
        // the number of line segments
        return segments.size();
    }
    
    public LineSegment[] segments() {
        // the line segments
        return segments.toArray(new LineSegment[segments.size()]);
    }
    
    // originally implemented with HashMap and checked for duplicate keys to avoid
    // this extra iteration; however, this was disallowed by course as "hashing
    // does not typically lead to good *worst case* performance guarantees"
    private void checkForDuplicates(Point[] points) {
        for (int i = 0; i < points.length - 1; i++) {
            if (points[i].compareTo(points[i + 1]) == 0) {
                throw new IllegalArgumentException("Duplicate point: " + points[i]);
            }
        }
    }
}