import java.util.Iterator;
import java.util.NoSuchElementException;
import edu.princeton.cs.algs4.StdRandom;

public class RandomizedQueue<Item> implements Iterable<Item> {
    
    private Item[] queue;
    private int size = 0;
    
    public RandomizedQueue() {
        // construct an empty randomized queue
        queue = (Item[]) new Object[1];
    }
    
    public boolean isEmpty() {
        // is the queue empty?
        return size == 0;
    }
    
    public int size() {
        // return the number of items on the queue
        return size;
    }
    
    public void enqueue(Item item) {
        // add the item
        if (item == null) {
            throw new NullPointerException("Cannot add null items to queue");
        }
        
        if (size == queue.length) resize(2 * queue.length); 
        queue[size++] = item;
    }
   
    public Item dequeue() {
        // remove and return a random item
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot de-queue from empty queue");
        }
        
        if (size > 0 && size == queue.length / 4) resize(queue.length / 2);
        int random = StdRandom.uniform(0, size);
        exch(queue, random, size - 1);
        Item item = queue[--size];
        queue[size] = null; // prevent loitering
        return item;
    }
   
    public Item sample() {
        // return (but do not remove) a random item
        if (isEmpty()) {
            throw new NoSuchElementException("Cannot sample from empty queue");
        }
        
        int random = StdRandom.uniform(size);
        return queue[random];
    }
    
    public Iterator<Item> iterator() {
        // return an independent iterator over items in random order
        return new QueueIterator();
    }
    
    public static void main(String[] args) {
        // unit testing (optional)
    }
    
    private void resize(int capacity) {
        Item [] copy = (Item[]) new Object[capacity];
        for (int i = 0; i < size; i += 1) {
            copy[i] = queue[i];
        }
        queue = copy;
    }
    
    // exchange a[i] and a[j]
    private static void exch(Object[] a, int i, int j) {
        Object swap = a[i];
        a[i] = a[j];
        a[j] = swap;
    }
    
    private class QueueIterator implements Iterator<Item> {
        
        private int i = size;
        
        public boolean hasNext() {
            return i > 0;
        }
        
        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            return queue[--i]; // check order (i-- vs --i)
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}