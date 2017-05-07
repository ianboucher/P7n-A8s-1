import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class KdTree {
    
    private static final boolean HORIZONTAL = true;
    private static final boolean VERTICAL   = false;
    private static final RectHV UNITSQUARE  = new RectHV(0, 0, 1, 1);
    
    private static class Node {
        private Point2D point;
        private RectHV rect;
        private Node lb;
        private Node rt;
        private boolean orientation;
        
        public Node(Point2D p, boolean orientation) {
            this.point = p;
            this.rect  = null;
            this.lb    = null;
            this.rt    = null;
            this.orientation = orientation;
        }
    }
    

    private Node root;
    private int size;

    
    //initialize empty tree
    public KdTree() {
        root = null; 
        size = 0;
    }
    

    public boolean isEmpty() {
        return size == 0;
    }
    
    // number of points in the tree
    public int size() {
        return size;
    }
    
    
    // add point to the tree (uses private recursive method insert())
    public void insert(Point2D point) {
        if (point == null) throw new NullPointerException("Cannot insert null point");
        root = insert(point, root, null, VERTICAL);
    }
    
    // does the tree contain point p?    
    public boolean contains(Point2D p) {
        if (p == null) throw new NullPointerException("Query point cannot be null");
        return get(p) != null;
    }

    
    // draw all points to standard draw
    public void draw() {
        Iterable<Point2D> points = pointsInLevelOrder();
        
        for (Point2D point : points) {
            Node node = root;
            
            while (node.point.compareTo(point) != 0) {
                int cmp = comparePoints(point, node.point, node.orientation);
                if      (cmp < 0) node = node.lb;
                else if (cmp > 0) node = node.rt;
            }
            
            StdDraw.setPenColor(StdDraw.BLACK);
            StdDraw.setPenRadius(0.01);
            point.draw();
            
            StdDraw.setPenRadius(0.001);
            
            if (node.orientation == VERTICAL) {
                StdDraw.setPenColor(StdDraw.RED);
                StdDraw.line(point.x(), node.rect.ymin(), point.x(), node.rect.ymax());
            }
            else if (node.orientation == HORIZONTAL) {
                StdDraw.setPenColor(StdDraw.BLUE);
                StdDraw.line(node.rect.xmin(), point.y(), node.rect.xmax(), point.y());
            }
        }
    }
    
    // return all points that are inside the query rectangle
    public Iterable<Point2D> range(RectHV rect) {
        if (rect == null) throw new NullPointerException("Query rect cannot be null");
        Queue<Point2D> queue = new Queue<Point2D>();
        Node node = root;
        
        range(rect, node, queue);
                
        return queue;
    }
            
    
    // return nearest neighbor in the tree to point p; null if the tree is empty    
    public Point2D nearest(Point2D p) {
        if (p == null) throw new NullPointerException("Query point cannot be null");        
        if (isEmpty()) return null;
        
        Point2D point = nearest(p, root.point, root);
        
        return point;
    }
        

    // basic unit testing of the methods
    public static void main(String[] args) {
        KdTree tree = new KdTree(); 
        In file     = new In(args[0]);
        
        while (!file.isEmpty()) {
            Point2D point = new Point2D(file.readFloat(), file.readFloat());
            tree.insert(point);
        }
        tree.draw();
        
        Iterable<Point2D> pointsInRange = tree.range(new RectHV(0.5, 0.0, 1.0, 0.5));
        
        StdOut.println("Size: " + tree.size());
        StdOut.println("Is empty: " + tree.isEmpty());
        StdOut.println(pointsInRange);      
        StdOut.println("nearest: " + tree.nearest(new Point2D(0.5, 0.95)));
        StdOut.println("found: "   + tree.get(new Point2D(0.2, 0.0)));
        StdOut.println("found: "   + tree.get(new Point2D(0.7, 0.1)));
        StdOut.println("found: "   + tree.get(new Point2D(1.0, 1.0)));
    }
    
    
    
    private Node insert(Point2D key, Node node, Node parent, boolean orientation) {
        
        if (node == null) return addNewNode(key, parent, orientation);
        
        int cmp = comparePoints(key, node.point, node.orientation);
        
        if      (cmp < 0) node.lb = insert(key, node.lb, node, !orientation);
        else if (cmp > 0) node.rt = insert(key, node.rt, node, !orientation);
//        else node.point = key;
        
        return node;
    }
    
    
    private int comparePoints(Point2D p, Point2D q, boolean orientation) {
        int cmp = 0;
        if (p.compareTo(q) == 0) return 0; // for duplicate points
        
        if      (orientation == HORIZONTAL) cmp = Point2D.Y_ORDER.compare(p, q);
        else if (orientation == VERTICAL)   cmp = Point2D.X_ORDER.compare(p, q);
    
        return (cmp < 0) ? -1 : 1; // prioritize existing node if x OR y key equal
    }
    
    
    private Node addNewNode(Point2D key, Node parent, boolean orientation) {
        size++;
        Node newNode = new Node(key, orientation);
        if (parent == null) newNode.rect = UNITSQUARE;
        else newNode.rect = getBoundingRectangle(newNode, parent); 
        
        return newNode;
    }
    
    
    // returns bounding rectangle for a new node by taking the parent rectangle 
    // and modifying in one dimension by the parent's point
    private RectHV getBoundingRectangle(Node node, Node parent) { 

        RectHV rect  = null;
        RectHV r = parent.rect;
        boolean orientation = parent.orientation;

        int cmp = comparePoints(node.point, parent.point, parent.orientation);
        if (cmp < 0) {
            if (orientation == VERTICAL) 
                 rect = new RectHV(r.xmin(), r.ymin(), parent.point.x(), r.ymax());
            else rect = new RectHV(r.xmin(), r.ymin(), r.xmax(), parent.point.y());
        }
        else if (cmp > 0) {
            if (orientation == VERTICAL)
                 rect = new RectHV(parent.point.x(), r.ymin(), r.xmax(), r.ymax());
            else rect = new RectHV(r.xmin(), parent.point.y(), r.xmax(), r.ymax());
        }
        return rect;
    }
        
    
    private Point2D get(Point2D key) {
        Node node = root;
        while (node != null) {
            int cmp = comparePoints(key, node.point, node.orientation);
            if      (cmp < 0) node = node.lb;
            else if (cmp > 0) node = node.rt;
            else return node.point;
        }
        return null;
    }
    
    
    // private recursive helper method for range(). Adds points that lie
    // within a query rectangle to a queue.
    private void range(RectHV queryRect, Node node, Queue<Point2D> queue) {
                        
        if (node == null) return; 
                
        if (queryRect.contains(node.point)) queue.enqueue(node.point);
            
        if (queryRect.intersects(node.rect)) {
            range(queryRect, node.lb, queue);
            range(queryRect, node.rt, queue);
        }
    }
    
    
    // private recursive helper method for nearest neighbor search
    private Point2D nearest(Point2D p, Point2D candidate, Node node) {
        
        if (node == null) return candidate;
        
        double dToNearestPoint = p.distanceSquaredTo(candidate);
        double dToRectangle    = node.rect.distanceSquaredTo(p);
            
        if (dToNearestPoint > dToRectangle) { // rect may contain better candidate
                
            double newDist = p.distanceSquaredTo(node.point);
            if (newDist < dToNearestPoint) candidate = node.point;
                
            int cmp = comparePoints(p, candidate, node.orientation);
            if (cmp < 0) { // check left subtree first
                candidate = nearest(p, candidate, node.lb);
                candidate = nearest(p, candidate, node.rt);
            }
            if (cmp > 0) { // check right subtree first
                candidate = nearest(p, candidate, node.rt);
                candidate = nearest(p, candidate, node.lb);
            }
        }
            
        return candidate;
    }
    
    
    private Iterable<Point2D> pointsInLevelOrder() {
        Queue<Point2D> points = new Queue<Point2D>();
        Queue<Node>    nodes  = new Queue<Node>();
        
        nodes.enqueue(root);
        
        while (!nodes.isEmpty()) {
            Node node = nodes.dequeue();
            if (node == null) continue;
            points.enqueue(node.point);
            nodes.enqueue(node.lb);
            nodes.enqueue(node.rt);
        }
        return points;
    }
}