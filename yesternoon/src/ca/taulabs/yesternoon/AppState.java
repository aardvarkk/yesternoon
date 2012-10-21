package ca.taulabs.yesternoon;

import java.util.Vector;

public class AppState
{
  public Vector<Counter> mCounters;
  public int             mCurrentCounterIdx;

  public AppState()
  {
    mCounters = new Vector<Counter>();
  }
}
