import java.util.ArrayList;
import java.util.Arrays;

public class FastCollinearPoints {
    
    private ArrayList<LineSegment> segments = new ArrayList<LineSegment>();

    // finds all line segments containing 4 points
    public FastCollinearPoints(Point[] pointsIn) {
        
        if (pointsIn == null) throw new NullPointerException();
        
        Point[] points = pointsIn.clone(); // copy to prevent mutation of input
        Arrays.sort(points);
        checkForDuplicates(points);

        for (int i = 0; i < points.length - 3; i++) { // choose a reference point
            
            // first, re-sort in numerical order to ensure endpoints in correct order
            Arrays.sort(points);
            
            // sort by slope order wrt reference point
            Arrays.sort(points, points[i].slopeOrder());
            
            // iterate through other points. Set 'first' as first collinear point
            // and 'last' as the endpoint. 'last' is advanced while the slope to
            // the endpoint == slope to the first point.
            for (int p = 0, first = 1, last = 2; last < points.length; last++) {
                
                // note use of Double.compare() for checking float equality
                while (last < points.length && 
                       Double.compare(points[p].slopeTo(points[first]), 
                                      points[p].slopeTo(points[last])) == 0) {                       
                    last++; // advance endpoint;
                }
                
                // if endpoint is >= 3 points advanced from ref point, log the segment
                if (last - first >= 3 && points[p].compareTo(points[first]) < 0) {
                    segments.add(new LineSegment(points[p], points[last - 1])); // last - 1 is last collinear;
                }
                
                // reset first point and begin scan for next segment
                first = last; 
            }
        }
    }
        
    // the number of line segments
    public int numberOfSegments() {
        return segments.size();
    }
    
    // the line segments
    public LineSegment[] segments() {
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