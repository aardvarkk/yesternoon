package ca.taulabs.yesternoon;

import java.util.Vector;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class PreferencesStorage implements Storage 
{
  public static final String PREFS_NAME = "YesternoonPrefs";
  public static final String COUNTER_PREFIX = "Counter_";
  public static final String VALUE_PREFIX = "Value_";
  
  private Context mContext;
  
  public PreferencesStorage(Context context)
  {
    mContext = context;
  }
  
	@Override
	public Vector<Counter> loadCounters() 
	{
	  Vector<Counter> counters = new Vector<Counter>();
	  
	  // loop through shared prefs loading each counter
	  SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
	  int idx = 1;
	  // each counter name will be stored under key "Counter_<idx>"
	  while(settings.getString(COUNTER_PREFIX + idx, null) != null)
	  {
	    String name = settings.getString(COUNTER_PREFIX + idx, null);
	    int count = settings.getInt(VALUE_PREFIX + idx, 0);
	    Counter tempCounter = new Counter(name, count);
	    counters.add(tempCounter);
	    idx++;
	  }
	  
		return counters;
	}

	@Override
	public void saveCounters(Vector<Counter> counters) 
	{
	  SharedPreferences settings = mContext.getSharedPreferences(PREFS_NAME, 0);
	  Editor edit = settings.edit();
	  edit.clear(); // clear any previous counters
	  
	  // save each counter
	  for (int nI = 0; nI < counters.size(); nI++)
	  {
	    edit.putString(COUNTER_PREFIX + nI, counters.get(nI).getName());
	    edit.putInt(VALUE_PREFIX + nI, counters.get(nI).getCount());
	  }
	  
	  // commit the changes
	  edit.commit();
	}

	@Override
	public void saveCounter(Counter counter) 
	{
		// TODO Auto-generated method stub
	}
}
