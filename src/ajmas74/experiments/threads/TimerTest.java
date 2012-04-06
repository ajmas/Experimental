package ajmas74.experiments.threads;

import java.util.*;

public class TimerTest implements Runnable {

  Timer _timer;

  TimerTest () {
    _timer = new Timer();
    _timer.schedule(new RemindTask(),0,1000);
  }
  
  public void run() {
    
  }
  
  class RemindTask extends TimerTask {
    public void run() {
        System.out.println("Time's up!");
        //_timer.cancel(); //Terminate the timer thread
    }  
  }  
  
  /**
   * @param args
   */
  public static void main(String[] args) {
    // TODO Auto-generated method stub
    TimerTest mt = new TimerTest();
  }

}
