import java.util.concurrent.atomic.AtomicInteger;

public class SenseBarrier {
  AtomicInteger count;     
  int size;                
  volatile boolean sense;  
  ThreadLocal<Boolean> threadSense;

  public SenseBarrier(int n) {
    count = new AtomicInteger(n);
    size = n;
    sense = false;
    threadSense = new ThreadLocal<Boolean>() {
      protected Boolean initialValue() { return !sense; };
    };
  }

  public void await() {
    boolean mySense = threadSense.get();
    int position = count.getAndDecrement();
    if (position == 1) { 
      count.set(this.size);     
      sense = mySense;          
    } else {
      while (sense != mySense) {} 
    }
    threadSense.set(!mySense);
  }
}
