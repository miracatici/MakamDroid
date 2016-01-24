package synth;

/**
 * To simulate a guitar string, we use the Karplus-Strong algorithm, a
 * mathematical model that allows us to create the sound of a guitar from a
 * simple formula. The basic idea is that we can simulate what happens when a
 * guitar string is plucked. The string's vibration is what creates the sound we
 * hear. We model a guitar string by sampling its displacement (a real number
 * between -1/2 and +1/2) at N equally spaced points (in time), where N equals
 * the sampling rate (44,100) divided by the fundamental frequency (more on this
 * later). Thus we must track a buffer of N values that represent the sound
 * being generated.
 * 
 * Plucking a string causes it to vibrate, which we simulate by filling the
 * buffer with white noise -- simply fill the N entries in the buffer with
 * random values between -0.5 and 0.5. (Check out the Math.random() function).
 * 
 * After the string is plucked, the string vibrates. The pluck causes a
 * displacement which spreads wave-like over time. The Karplus-Strong algorithm
 * simulates this vibration by maintaining a buffer of the N samples: the
 * algorithm repeatedly deletes the first sample from the buffer and adds to the
 * end of the buffer the average of the first two samples, scaled by an energy
 * decay factor of 0.996.
 * 
 * This implementation of a guitar string will use a bounded queue to represent
 * the buffer. The elements in the queue will be real numbers (doubles) to
 * represent the samples.
 * 
 * 
 */
public class GuitarString {

	private final int SAMPLING_RATE = 44100;
	private final double DECAY_FACTOR = 0.996;
	private BddQueue<Double> string;
	private int tics;

	/**
	 * Creates a guitar string of the given frequency.
	 * 
	 * The constructor creates a buffer whose size is the sampling rate (44,100)
	 * divided by frequency and rounded to the nearest integer. The resulting
	 * buffer is initialized to represent a guitar string at rest by populating
	 * it with all zeros.
	 * 
	 * This operation should throw an IllegalArgumentException if the frequency
	 * is less than 1.0 or greater than the default sampling rate.
	 */
	public GuitarString() {
	
	}
	public void createString(float frequency) {
		// TODO Implement this method
		if(frequency < 1.0 || frequency > SAMPLING_RATE)
			throw new IllegalArgumentException("Freqency less than 1.0");
		
		double size_old = SAMPLING_RATE/frequency;		
		double size_new = (double) Math.round(size_old*100)/100;
		
		int size_int = (int) size_new;
		
		string = new ArrayBddQueue<Double>(size_int);
		
		for(int i = 0; i < size_int; i++)
			string.enqueue(0.0);
	}
	/**
	 * Sets the buffer to white noise.
	 * 
	 * Replaces all elements in the buffer with random values between -0.5 and
	 * +0.5
	 */
	public void pluck() {
		// TODO Implement this method
		
		while(this.string.isEmpty() != true)
			this.string.dequeue();
		
		for(int i = 0; i < this.string.capacity(); i++){
			double random = Math.random() * (0.5 - (-0.5)) + (-0.5);
//			DecimalFormat df = new DecimalFormat("#.#");      
//			random = Double.valueOf(df.format(random));			
			
			this.string.enqueue(random);
		}
		
		//this.string.toString();
	}
	
	//DONE
	/**
	 * Advances the simulation one time step.
	 * 
	 * This is done by applying the Karplus-Strong update. Specifically, delete
	 * the sample at the front of the buffer and to the end of the buffer add
	 * the average of the first two samples, multiplied by the energy decay
	 * factor.
	 */
	public void tic() {
		// TODO Implement this method
		double first = this.string.dequeue();
		double second = this.string.peek();
		
		double result = ((first + second)/2) * DECAY_FACTOR;
		this.string.enqueue(result);
		tics++;
	}
	
	//DONE
	/**
	 * Returns the current sample.
	 * 
	 * @return The first sample in the buffer.
	 */
	public double sample() {
		// TODO Implement this method
		return this.string.peek();
	}
	
	//DONE
	/**
	 * Returns the number of tics.
	 * 
	 * @return The number of times tic has been called.
	 */
	public int time() {
		// TODO Implement this method
		return this.tics;
	}
	public float[] getBuffer(){
		float[] buff = new float[88200];
		for (int i = 0; i < buff.length; i++) {
			double temp = sample();
			buff[i] = (float) temp;
			tic();
		}
		return buff;
	}
}
