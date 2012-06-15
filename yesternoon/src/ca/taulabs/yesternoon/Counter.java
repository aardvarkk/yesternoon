package ca.taulabs.yesternoon;

/**
 * A base model for the Counter. 
 * 
 * @author Dinko Harbinja
 */
public class Counter 
{
  private String  mName;
  private int     mCount;
  
  public Counter(String name)
  {
    mName = name;
    mCount = 0;
  }
  
  public Counter(String name, int count)
  {
    mName = name;
    mCount = count;
  }
  
  public int getCount()
  {
    return mCount;
  }
  
  public void setName(String name)
  {
    mName = name;
  }
  
  public String getName()
  {
    return mName;
  }
  
  public void increment()
  {
    mCount++;
  }
  
  public void decrement()
  {
    mCount--;
  }
}
