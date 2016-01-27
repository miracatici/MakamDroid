package org.example.trainear.synth;

/**
 * A bounded queue is a FIFO (first in - first out) sequence of objects.
 * The size of a bounded queue cannot exceed its capacity
 *
 * A typical bounded queue is [ e_0, e_1, e_2, ..., e_k ] where k < capacity
 *
 * @requires E is Immutable
 *
 * @param <E>
 *
 */
public interface BddQueue<E> {

	// constructors

	/*
	 * Creates an empty queue and sets its capacity
	 *
	 * @ensures: this = [ ] and this.capacity = capacity
	 * BddQueue(int capacity);
	 */

	// mutators

	/**
	 * Adds the specified element to the end of the queue.
	 *
	 * @requires element != null   [ else throws NullPointerException ]
	 * @requires |this| < capacity [ else throws IllegalStateException ]
	 * @modifies this
	 * @ensures this = old_this + [element]
	 *
	 * @param element
	 */
	void enqueue(E element);

	/**
	 * Removes and returns the first element in the queue.
	 *
	 * @requires |this| > 0 [ else throws IllegalStateException ]
	 * @modifies this
	 * @ensures this = ALL_BUT_FIRST(old_this)
	 * @ensures result = FIRST(this)
	 *
	 * @return the first element in the queue
	 */
	E dequeue();

	// observers

	/**
	 * Returns (but does not remove) the first element of the queue.
	 *
	 * @requires |this| > 0 [ else throws IllegalStateException ]
	 * @ensures result = FIRST(this)
	 *
	 * @return first element in the queue
	 */
	E peek();

	/**
	 * Returns the size of the queue
	 *
	 * @ensures result = |this|
	 *
	 * @return size of the queue
	 */
	int size();

	/**
	 * Returns the capacity of the queue
	 *
	 * @ensures result = this.capacity
	 *
	 * @return capacity of the queue
	 */
	int capacity();

	/**
	 * Returns true if the queue is empty.
	 *
	 * @ensures result = ( |this| = 0 )
	 *
	 * @return true if the queue is empty, false otherwise
	 */
	boolean isEmpty();

	/**
	 * Returns true if the queue is full.
	 *
	 * @ensures result = ( |this| = this.capacity )
	 *
	 * @return true if the queue is full, false otherwise
	 */
	boolean isFull();
	
	float[] getElements();
}
