public class CustomQueue {
  private final int MAX_QUEUE = 100000; // maximum size of queue
  private int[] items;
  private int front, back, count;
  
  public CustomQueue() {
    items = new int[MAX_QUEUE];  
    front = 0;
    back = MAX_QUEUE-1; 
    count = 0;
  }  // end default constructor
  
  public CustomQueue(int size) {
	    items = new int[size];  
	    front = 0;
	    back = MAX_QUEUE-1; 
	    count = 0;
	  }  // end default constructor

  // queue operations:
  public boolean isEmpty() {
    return count == 0;
  }  // end isEmpty
  
  public int getCount(){
	  
	  return count;
	  
  }

  public boolean isFull() {
    return count == MAX_QUEUE;
  }  // end isFull
  
  public void enqueue(int newItem) {
    if (!isFull()) {
      back = (back+1) % (MAX_QUEUE);
      items[back] = newItem;
      ++count;
    }
    
  }  // end enqueue

  public int dequeue(){
    if (!isEmpty()) {
      // queue is not empty; remove front
      int queueFront = items[front];
      front = (front+1) % (MAX_QUEUE);
      --count;
      return queueFront;
    }
    
    return -1;
  }  // end dequeue

  public void dequeueAll() {
    items = new int[MAX_QUEUE];  
    front = 0;
    back = MAX_QUEUE-1;
    count = 0;
  }  // end dequeueAll

  public int peek(){
    if (!isEmpty()) {  
      // queue is not empty; retrieve front
      return items[front];
    }
    
    return -1;
  }  // end peek  
} // end QueueArrayBased