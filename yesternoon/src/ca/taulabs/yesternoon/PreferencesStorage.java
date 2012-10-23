package ca.taulabs.yesternoon;

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
  private static final String PREFS_NAME = "YesternoonPrefs";
  private static final String COUNTER_PREFIX = "Counter_";
  private static final String VALUE_PREFIX = "Value_";
  private static final String CURRENT_COUNTER = "Current";
  
  private Context mContext;
  private static PreferencesStorage mInstance; // singleton instance
  
  // Our state. We store it in a class so it's passed by ref and we can edit it anywhere
  private AppState mState;
  
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
  public AppState getState()
  {
    return mState;
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
	  for (int nI = 0; nI < mState.mCounters.size(); nI++)
	  {
	    edit.putString(COUNTER_PREFIX + nI, mState.mCounters.get(nI).getName());
	    edit.putInt(VALUE_PREFIX + nI, mState.mCounters.get(nI).getCount());
	  }
	  
	  edit.putInt(CURRENT_COUNTER, mState.mCurrentCounterIdx);
	  
	  // commit the changes
	  edit.commit();
	  
    Log.d(YesternoonActivity.TAG, this.toString());
	}
	
	/**
	 * Loads all the counters from the SharedPreferences object
	 */
  private void loadCounters() 
  {
    mState = new AppState();
    
    // loop through shared prefs loading each counter
    SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
    int idx = 0;
    // each counter name will be stored under key "Counter_<idx>"
    while(settings.getString(COUNTER_PREFIX + idx, null) != null)
    {
      String name = settings.getString(COUNTER_PREFIX + idx, null);
      int count = settings.getInt(VALUE_PREFIX + idx, 0);
      mState.mCounters.add(new Counter(name, count));
      idx++;
    }
    
    mState.mCurrentCounterIdx = settings.getInt(CURRENT_COUNTER, 0);
        
    Log.d(YesternoonActivity.TAG, this.toString());
  }
  
  public String toString()
  {
    String retVal = "";
    SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
    
    for (int nI = 0; nI < mState.mCounters.size(); nI++)
    {
      retVal += COUNTER_PREFIX + nI + "=" + settings.getString(COUNTER_PREFIX + nI, null) + ", ";
      retVal += VALUE_PREFIX + nI + "=" + settings.getInt(VALUE_PREFIX + nI, -1) + ", ";
    }
    
    retVal += CURRENT_COUNTER + "=" + settings.getInt(CURRENT_COUNTER, 0);
    retVal += "\n";
    
    return retVal;
  }
}
