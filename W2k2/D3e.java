import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    
    private Node first, last;
    private int size;
    
    private class Node {
        private Item item;
        private Node next;
        private Node prev;
    }
    
    public Deque() {
        // construct an empty deque
        first = null;
        last  = null;
        size  = 0;
    }
    
    public boolean isEmpty() {
        // is the deque empty?
        return size == 0;
//        return first == null;
    }
    
    public int size() {
        // return the number of items on the deque
        return size;
    }
    
    public void addFirst(Item item) {
        // add the item to the front
        if (item == null) {
            throw new NullPointerException("Cannot add null items to the deque");
        }
        
        Node oldFirst = first;
        first = new Node();
        first.item = item;
        
        if (isEmpty()) last = first;
        else { 
            first.next = oldFirst;
            oldFirst.prev = first; // circular reference is the key - remove last needs 'prev' and iterator needs 'next'
        }
        
        size += 1;
    }
    
    public void addLast(Item item) {
        // add the item to the end
        if (item == null) {
            throw new NullPointerException("Cannot add null items to the deque");
        }
        
        Node oldLast = last;
        last = new Node();
        last.item = item;
        
        if (isEmpty()) first = last;
        else { 
            oldLast.next = last;
            last.prev = oldLast; // circular reference is the key - removeLast needs 'prev' and iterator needs 'next'
        }
        
        size += 1;        
    }
    
    public Item removeFirst() {
        // remove and return the item from the front
        if (isEmpty()) {
            throw new NoSuchElementException("Stack underflow");
        }
        else {
            Item item = first.item;
            first = first.next;
            if (isEmpty()) last = null;
            size -= 1;
            return item;
        }
    }
    
    public Item removeLast() {
        // remove and return the item from the end
        if (isEmpty()) throw new NoSuchElementException("Stack underflow");
        
        Item item = last.item;

        if (last.prev == null) last = first = null;
        else {
            last = last.prev;
            last.next = null;
        }

        size -= 1;
        return item;
    }
    
    public Iterator<Item> iterator() {
        // return an iterator over items in order from front to end
        return new ListIterator();
    }
    
    public static void main(String[] args) {
        // unit testing (optional)
    }
    
    private class ListIterator implements Iterator<Item> {
        
        private Node current = first;
        
        public boolean hasNext() {
            return current != null;
        }
        
        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current   = current.next;
            return item;
        }
        
        public void remove() {
            throw new UnsupportedOperationException();
        }
    }
}