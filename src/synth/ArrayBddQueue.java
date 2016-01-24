package synth;
/**
 * This BddQueue implementation uses a circular array.
 *
 * <ul>
 * <li>elements - an array containing the elements of the queue</li>
 * <li>first - the index of the first element in the queue</li>
 * <li>length - the number of elements in the queue</li>
 * </ul>
 *
 * The elements in the list may "wrap" around the array depending on the index
 * of the first element and the length of the queue.
 *
 * Consider an queue with capacity 10. When the queue is first created its
 * representation is the following, where "/" are elements we can ignore:
 *
 * elements = [ / / / / / / / / / / ] and first = 0 and length = 0
 *
 * If we enqueue elements A-J (in that order) we end up with:
 *
 * elements = [ A B C D E F G H I J ] and first = 0 and length = 10
 *
 * If we dequeue the first five elements we get:
 *
 * elements = [ / / / / / F G H I J ] and first = 5 and length = 5
 *
 * If we add elements A and B (in that order) we need to "wrap" around the
 * queue, so we get:
 *
 * elements = [ A B / / / F G H I J ] and first = 5 and length = 7
 *
 * To get this "wrapping" action, use the % operator. For example, adding A
 * after J would normally put it at index 10. But the array does not have index
 * 10, so instead is goes in index 10 % 10 (which equals 0). Likewise rather
 * than putting B at index 11, you put it at index 11 % 10 = 1
 *
 * @param <E>
 */
public class ArrayBddQueue<E> implements BddQueue<E> {

	private final int CAPACITY;
	private E[] elements;
	private int first;
	private int length;

	//DONE
	@SuppressWarnings("unchecked")
	public ArrayBddQueue(int capacity) {
		// TODO Implement this method
		length = 0;
		first = 0;
		this.CAPACITY = capacity;
		this.elements = (E[]) new Object[capacity];

	}

	//DONE
	@Override
	public void enqueue(E element) {
		// TODO Implement this method
		boolean flag = false;
		isEmpty();

		if(this.elements == null)
			throw new NullPointerException("Queue is currently NULL.");

		if(this.CAPACITY == 0)
			System.out.println("0 Capacity in Queue");

		for(int i = 0; i < CAPACITY; i++){
			if(this.elements[i] == null){
				this.elements[i] = element;
				this.length++;
				flag = true;
				break;
			}
		}

		if(flag == false)
			throw new IllegalStateException("No more space in Queue to add new item");

	}

	@Override
	public E dequeue() {
		// TODO Implement this method

		if(this.size() < 0)
			throw new IllegalStateException("No items in Queue");

		this.length--;
		E element = this.elements[first];
		this.elements[first] = null;
		this.first++;

		if(this.first >= this.CAPACITY)
			this.first = 0;

		return element;
	}

	//DONE
	@Override
	public E peek() {
		// TODO Implement this method
		if(this.size() <= 0)
			throw new IllegalStateException("No items in Queue");

		return this.elements[first];
	}

	//DONE
	@Override
	public int size() {
		// TODO Implement this method
		return this.length;
	}

	//DONE
	@Override
	public int capacity() {
		// TODO Implement this method
		return this.CAPACITY;
	}

	//DONE
	@Override
	public boolean isEmpty() {
		// TODO Implement this method
		for(int i = 0; i < CAPACITY; i++)
			if(this.elements[i] != null)
				return false;

		this.first = 0;
		return true;
	}

	//DONE
	@Override
	public boolean isFull() {
		// TODO Implement this method
		if(this.size() == this.CAPACITY){
			return true;
		}

		return false;
	}

	//DONE
	/**
	 * An empty queue should print the string "[ ]" (note the space). A queue
	 * containing strings A, B, and C in that order should print the string
	 * "[ A B C ]" (note the spaces).
	 */
	@Override
	public String toString() {
		// TODO Implement this method
		if(isEmpty()){
			System.out.println("[ ]");
			return "";
		}

		System.out.print("[ ");
		for(int i = 0; i < CAPACITY; i++){
			//if(this.elements[i] != null)
				System.out.print(this.elements[i] + " ");
		}
		System.out.println("]");

		return "";
	}

	//DONE
	/**
	 * Two queues are equal if they are the same size and elements at the same
	 * positions are equal. Note: Implement this in such a way that even if the
	 * other BddQueue is not an ArrayBddQueue, it will still work if they have
	 * the same elements.
	 */
	@Override
	public boolean equals(Object o) {
		// TODO Implement this method
		if(o == null)
			return false;

		if(getClass() != o.getClass())
			return false;

		@SuppressWarnings({ "unchecked" })
		final ArrayBddQueue<E> other = (ArrayBddQueue<E>) o;

		if(this.size() != other.size())
			return false;

		for(int i = 0; i < CAPACITY; i++)
			if(this.elements[i] != other.elements[i])
				return false;

		return true;
	}

	//DONE
	/**
	 * See "Effective Java" by Bloch for advice on implementing hashCode.
	 */
	@Override
	public int hashCode() {
		// TODO Implement this method
		int hash = 17;
		hash = 31 * hash + this.elements.hashCode();
		hash = 31 * hash + this.length;
		hash = 31 * hash + this.first;
		hash = 31 * hash + this.CAPACITY;

		return hash;
	}
	public float[] getElements(){
		float[] temp = new float[elements.length];
		for (int i = 0; i < temp.length; i++) {
			double dd = (double) elements[i];
			temp[i] = (float) dd;
		}
		return temp;
	}
}
