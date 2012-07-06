package ca.taulabs.yesternoon;

/**
 * A base model for the Counter. 
 * 
 * @author Dinko Harbinja
 */
public class Counter 
{
  private int     mID;
  private String  mName;
  private int     mCount;
  
  public Counter(int id, String name)
  {
    mID = id;
    mName = name;
    mCount = 0;
  }
  
  public Counter(int id, String name, int count)
  {
    mID = id;
    mName = name;
    mCount = count;
  }
  
  public int getID()
  {
    return mID;
  }
  
  public int getCount()
  {
    return mCount;
  }
  
  public void setCount(int count)
  {
    this.mCount = count;
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
