package ca.taulabs.yesternoon;

import java.util.Vector;

public interface Storage
{  
  public Vector<Counter> loadCounters();
  public void saveCounters(Vector<Counter> counters);
  public void saveCounter(Counter counter);
}
