import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/** * <p> Title: CSC 230 Project 2: Electoral College Basketball League
 * 
 * <p> Description: This program is meant to take children who entered into a basketball league,
 * 					and add them into the league. If the child was 6, it would add them into the
 * 					league based off of the date of entry and if they were 7, it would add them
 * 					based off of their birthdates. However it would only add kids ages 6 and 7 and
 * 					it would add all the 6 year olds first and if there was space, it would add the 
 *  				7 year olds. It also must have done this with a runtime of O(nlogn).</p>
 *  
 *  <p> Due 30 November, 2016 11:59 pm</p>
 *  
 *  @author Steven Turner (N00836867@students.ncc.edu)
 *  
 *  
 * 
 *  The {@code MinPQ} class represents a priority queue of generic keys.
 *  It supports the usual <em>insert</em> and <em>delete-the-minimum</em>
 *  operations, along with methods for peeking at the minimum key,
 *  testing if the priority queue is empty, and iterating through
 *  the keys.
 *  <p>
 *  This implementation uses a binary heap.
 *  The <em>insert</em> and <em>delete-the-minimum</em> operations take
 *  logarithmic amortized time.
 *  The <em>min</em>, <em>size</em>, and <em>is-empty</em> operations take constant time.
 *  Construction takes time proportional to the specified capacity or the number of
 *  items used to initialize the data structure.
 *  <p>
 *  For additional documentation, see <a href="http://algs4.cs.princeton.edu/24pq">Section 2.4</a> of
 *  <i>Algorithms, 4th Edition</i> by Robert Sedgewick and Kevin Wayne.
 *
 *  @author Robert Sedgewick
 *  @author Kevin Wayne
 *
 *  @param <Key> the generic type of key on this priority queue
 */
public class PriorityQueue implements Iterable<Child> {
    private Child[] pq;                    // store items at indices 1 to n
    private int n;                       // number of items on priority queue
    private Comparator<Child> comparator;  // optional comparator

    /**
     * Initializes an empty priority queue with the given initial capacity.
     *
     * @param  initCapacity the initial capacity of this priority queue
     */
    @SuppressWarnings("unchecked")
	public PriorityQueue(int initCapacity) {
        pq = new Child[initCapacity + 1];
        n = 0;
    }

    /**
     * Initializes an empty priority queue.
     */
    public PriorityQueue() {
        this(1);
    }

    /**
     * Initializes an empty priority queue with the given initial capacity,
     * using the given comparator.
     *
     * @param  initCapacity the initial capacity of this priority queue
     * @param  comparator the order to use when comparing keys
     */
    public PriorityQueue(int initCapacity, Comparator<Child> comparator) {
        this.comparator = comparator;
        pq = (Child[]) new Object[initCapacity + 1];
        n = 0;
    }

    /**
     * Initializes an empty priority queue using the given comparator.
     *
     * @param  comparator the order to use when comparing keys
     */
    public PriorityQueue(Comparator<Child> comparator) {
        this(1, comparator);
    }

    /**
     * Initializes a priority queue from the array of keys.
     * <p>
     * Takes time proportional to the number of keys, using sink-based heap construction.
     *
     * @param  keys the array of keys
     */
    public PriorityQueue(Child[] keys) {
        n = keys.length;
        pq = (Child[]) new Object[keys.length + 1];
        for (int i = 0; i < n; i++)
            pq[i+1] = keys[i];
        for (int k = n/2; k >= 1; k--)
            sink(k);
        assert isMinHeap();
    }

    /**
     * Returns true if this priority queue is empty.
     *
     * @return {@code true} if this priority queue is empty;
     *         {@code false} otherwise
     */
    public boolean isEmpty() {
        return n == 0;
    }
    
    public boolean isFull(){
    	return n == pq.length;
    }

    /**
     * Returns the number of keys on this priority queue.
     *
     * @return the number of keys on this priority queue
     */
    public int size() {
        return n;
    }

    /**
     * Returns a smallest key on this priority queue.
     *
     * @return a smallest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Child min() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        return pq[1];
    }

    // helper function to double the size of the heap array
    private void resize(int capacity) {
        assert capacity > n;
        Child[] temp = new Child[capacity];
        for (int i = 1; i <= n; i++) {
            temp[i] = pq[i];
        }
        pq = temp;
    }

    /**
     * Adds a new key to this priority queue.
     *
     * @param  x the key to add to this priority queue
     */
    public void insert(Child x) {
        // double size of array if necessary
       if (n == pq.length - 1) resize(2 * pq.length);

        // add x, and percolate it up to maintain heap invariant
        pq[++n] = x;
        swim(n);
        assert isMinHeap();
    }

    /**
     * Removes and returns a smallest key on this priority queue.
     *
     * @return a smallest key on this priority queue
     * @throws NoSuchElementException if this priority queue is empty
     */
    public Child delMin() {
        if (isEmpty()) throw new NoSuchElementException("Priority queue underflow");
        exch(1, n);
        Child min = pq[n--];
        sink(1);
        pq[n+1] = null;         // avoid loitering and help with garbage collection
        if ((n > 0) && (n == (pq.length - 1) / 4)) resize(pq.length  / 2);
        assert isMinHeap();
        return min;
    }


   /***************************************************************************
    * Helper functions to restore the heap invariant.
    ***************************************************************************/

    private void swim(int k) {
        while (k > 1 && greater(k/2, k)) {
            exch(k, k/2);
            k = k/2;
        }
    }

    private void sink(int k) {
        while (2*k <= n) {
            int j = 2*k;
            if (j < n && greater(j, j+1)) j++;
            if (!greater(k, j)) break;
            exch(k, j);
            k = j;
        }
    }

   /***************************************************************************
    * Helper functions for compares and swaps.
    ***************************************************************************/
    /*
     * This greater method doesn't use comparators, however it uses primitive data types. If the age
     * of kid in index i is greater than the age of kid in index k, return true otherwise,
     * compare the Date of Entry if the kid is 6 and compare the age in Millis if the kid is 7.
     */
    public boolean greater(int i, int k){
    	if(pq[i].ageInYears() > pq[k].ageInYears()){
    		return true;
    	}
    	
    	else if(pq[i].ageInYears() == 6 && pq[k].ageInYears() == 6){
			return pq[i].DOEinMillis() > (pq[k].DOEinMillis());
	}
	else {
			return pq[i].ageInMillis() > pq[k].ageInMillis();
		}

	
	}
//    private boolean greater(int i, int j) {
//        if (comparator == null) {
//            return ((Comparable<T>) pq[i]).compareTo(pq[j]) > 0;
//        }
//        else {
//            return comparator.compare(pq[i], pq[j]) > 0;
//        }
//    }

    private void exch(int i, int j) {
        Child swap = pq[i];
        pq[i] = pq[j];
        pq[j] = swap;
    }

    // is pq[1..N] a min heap?
    private boolean isMinHeap() {
        return isMinHeap(1);
    }

    // is subtree of pq[1..n] rooted at k a min heap?
    private boolean isMinHeap(int k) {
        if (k > n) return true;
        int left = 2*k;
        int right = 2*k + 1;
        if (left  <= n && greater(k, left))  return false;
        if (right <= n && greater(k, right)) return false;
        return isMinHeap(left) && isMinHeap(right);
    }


    /**
     * Returns an iterator that iterates over the keys on this priority queue
     * in ascending order.
     * <p>
     * The iterator doesn't implement {@code remove()} since it's optional.
     *
     * @return an iterator that iterates over the keys in ascending order
     */
    public Iterator<Child> iterator() { return new HeapIterator(); }

    private class HeapIterator implements Iterator<Child> {
        // create a new pq
        private PriorityQueue copy;

        // add all items to copy of heap
        // takes linear time since already in heap order so no keys move
        public HeapIterator() {
            if (comparator == null) copy = new PriorityQueue(size());
            else                    copy = new PriorityQueue(size(), comparator);
            for (int i = 1; i <= n; i++)
                copy.insert(pq[i]);
        }

        public boolean hasNext()  { return !copy.isEmpty();                     }
        public void remove()      { throw new UnsupportedOperationException();  }

        public Child next() {
            if (!hasNext()) throw new NoSuchElementException();
            return copy.delMin();
        }
    }

    /**
     * Unit tests the {@code MinPQ} data type.
     *
     * @param args the command-line arguments
     */
   //Prints Queue in a 2 digit, numbered format.
    public void printQueue(){
    	String str;
    	int count = 1;
    	str = ". ";
    	while(!isEmpty()){
    		System.out.printf("%2d", count);
    		str += delMin().toString() + "\n";
    		System.out.printf("%s", str);
    		count++;
    		str = ". ";
    	}
    }
//    public String toString(){
//    	String str = "League Players: \n";
//    	int count = 1;
//    	while(!isEmpty()){
//    		str+= count +". ";
//    		str += delMin().toString();
//    		str += "\n";
//    		count++;
//    	}
//    	return str;
//    }

}