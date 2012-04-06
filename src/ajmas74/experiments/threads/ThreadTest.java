package ajmas74.experiments.threads;

public class ThreadTest {
 
  int x = 0;
  
  ThreadTest() {
    RunnableY runY = new RunnableY("counter");
    Thread thready = new Thread(runY);
    thready.start();
    (new ThreadX("a",thready)).start();
    //(new ThreadX("b")).start();    
  }
  
  
  class ThreadX extends Thread {
    String name;
    int i;
    Thread counterThread;
    ThreadX(String name, Thread counterThread) {
      this.name = name;
      this.counterThread = counterThread;
    }

    public void run() {
      while ( true ) {
        System.out.println(name+"-"+(x));
        counterThread.notify();
      }
    }
  }  
 
  class RunnableY implements Runnable {
    String name;
    int i;
    RunnableY(String name) {
      this.name = name;
    }

    public void run() {
      while ( true ) {
        System.out.println(name+"-"+(i++));
        try {
          x = i++;
          Thread.
          Thread.currentThread().wait();
          x = 0;
          //Thread.sleep(100);
        } catch (InterruptedException e) {  }
      }
    }
  }  
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    ThreadTest instance = new ThreadTest();
  }

}
