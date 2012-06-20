package ca.taulabs.yesternoon;

import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesStorage
{
  public static final String PREFS_NAME = "YesternoonPrefs";
  public static final String COUNTER_PREFIX = "Counter_";
  public static final String VALUE_PREFIX = "Value_";
  
  private Context mContext;
  private static PreferencesStorage mInstance; // singleton instance
  private Vector<Counter> mCounters;
  
  private PreferencesStorage(Context context)
  {
    mContext = context;
    loadCounters();
  }
  
  public static PreferencesStorage getPreferencesStorage(Context context)
  {
    if (mInstance == null)
    {
      mInstance = new PreferencesStorage(context);
    }
    return mInstance;
  }
  
  public Vector<Counter> getCounters()
  {
    return mCounters;
  }

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
	}
	
	///////////////////////////////////////////////////////////////////////////
	// HELPERS
  ///////////////////////////////////////////////////////////////////////////
	
  private void loadCounters() 
  {
    mCounters = new Vector<Counter>();
    
    // loop through shared prefs loading each counter
    SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
    int idx = 1;
    // each counter name will be stored under key "Counter_<idx>"
    while(settings.getString(COUNTER_PREFIX + idx, null) != null)
    {
      String name = settings.getString(COUNTER_PREFIX + idx, null);
      int count = settings.getInt(VALUE_PREFIX + idx, 0);
      Counter tempCounter = new Counter(name, count);
      mCounters.add(tempCounter);
      idx++;
    }
    
    // TODO: Testing purposes, just clear the settings every time we load.
    Editor edit = settings.edit();
    edit.clear();
    edit.commit();
  }
}
