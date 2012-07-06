package ca.taulabs.yesternoon;

import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.util.Log;

/**
 * A Storage for the Counters. This particular class
 * uses the Android SharedPreferences object as persistent 
 * storage for the Counters.
 * 
 * @author Dinko Harbinja
 */
public class PreferencesStorage
{
  private static final String TAG = "PreferencesStorage";
  
  private static final String PREFS_NAME = "YesternoonPrefs";
  private static final String COUNTER_PREFIX = "Counter_";
  private static final String VALUE_PREFIX = "Value_";
  
  private Context mContext;
  private static PreferencesStorage mInstance; // singleton instance
  private Vector<Counter> mCounters;
  
  /**
   * Private constructor to create the object.
   * @param context The application context.
   */
  private PreferencesStorage(Context context)
  {
    mContext = context;
    loadCounters();
  }
  
  /**
   * Singleton class, allows us to retrieve the instance of the singleton.
   * 
   * @param context The application context.
   * @return PreferncesStorage singleton instance.
   */
  public static PreferencesStorage getPreferencesStorage(Context context)
  {
    if (mInstance == null)
    {
      mInstance = new PreferencesStorage(context);
    }
    return mInstance;
  }
  
  /**
   * Retrieves the list of counters in storage.
   * @return Vector<Counter> The counters
   */
  public Vector<Counter> getCounters()
  {
    return mCounters;
  }
  
  /**
   * Generates a unique ID for a counter based on the size of the counters.
   * IDs are 0 based.
   * 
   * @return int The unique id.
   */
  public int generateID()
  {
    return mCounters.size();
  }

  /**
   * Saves all the counters in the storage to the SharedPreferences object.
   */
	public void saveCounters() 
	{
	  SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
	  Editor edit = settings.edit();
	  edit.clear(); // clear any previous counters
	  
	  // save each counter
	  for (int nI = 0; nI < mCounters.size(); nI++)
	  {
	    edit.putString(COUNTER_PREFIX + nI, mCounters.get(nI).getName());
	    edit.putInt(VALUE_PREFIX + nI, mCounters.get(nI).getCount());
	  }
	  
	  // commit the changes
	  edit.commit();
	  
    Log.d(TAG, this.toString());
	}
	
	/**
	 * Saves only a single counter to the shared preferences.
	 * 
	 * @param counter The counter to save.
	 */
	public void saveCounter(Counter counter)
	{
	  SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
	  Editor edit = settings.edit();
	  
    edit.putString(COUNTER_PREFIX + counter.getID(), counter.getName());
    edit.putInt(VALUE_PREFIX + counter.getID(), counter.getCount());
    
    // commit the changes
    edit.commit();
    
    Log.d(TAG, this.toString());
	}
	
	///////////////////////////////////////////////////////////////////////////
	// HELPERS
  ///////////////////////////////////////////////////////////////////////////
	
	/**
	 * Loads all the counters from the SharedPreferences object
	 */
  private void loadCounters() 
  {
    mCounters = new Vector<Counter>();
    
    // loop through shared prefs loading each counter
    SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
    int idx = 0;
    // each counter name will be stored under key "Counter_<idx>"
    while(settings.getString(COUNTER_PREFIX + idx, null) != null)
    {
      String name = settings.getString(COUNTER_PREFIX + idx, null);
      int count = settings.getInt(VALUE_PREFIX + idx, 0);
      Counter tempCounter = new Counter(idx, name, count);
      mCounters.add(tempCounter);
      idx++;
    }
    
    Log.d(TAG, this.toString());
  }
  
  public String toString()
  {
    String retVal = "";
    SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
    
    for (int nI = 0; nI < mCounters.size(); nI++)
    {
      retVal += COUNTER_PREFIX + nI + "=" + settings.getString(COUNTER_PREFIX + nI, null) + ", ";
      retVal += VALUE_PREFIX + nI + "=" + settings.getInt(VALUE_PREFIX + nI, -1) + "\n";
    }
    
    return retVal;
  }
}
