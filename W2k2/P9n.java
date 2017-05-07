import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class Permutation {
    
    public static void main(String[] args) {
        
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> queue = new RandomizedQueue<String>();
        
        // add input strings to queue
        while (!StdIn.isEmpty()) {
            String item = StdIn.readString();
            queue.enqueue(item);
        }
        
//        for (int i = 0; i < k; i += 1) {
//            if (!StdIn.isEmpty()) {
//                String item = StdIn.readString();
//                queue.enqueue(item);
//            }
//        }
        
        // print k items and remove them from the queue
        for (int j = 0; j < k; j += 1) {
            String item = queue.dequeue();
            StdOut.println(item);
        }
    }     
}