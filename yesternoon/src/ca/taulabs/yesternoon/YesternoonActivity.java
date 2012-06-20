package ca.taulabs.yesternoon;

import java.util.Vector;

import ca.taulabs.yesternoon.picker.NumberPicker;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class YesternoonActivity extends Activity 
{    
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    // let's init our instances
    PreferencesStorage storage = PreferencesStorage.getPreferencesStorage(this);
    Vector<Counter> counters = storage.getCounters();
    
    if (counters.isEmpty())
    {
      // We should bring up a UI that lets
      // the user create their first counter
      Intent addCounterIntent = new Intent(this, AddCounterActivity.class);
      this.startActivityForResult(addCounterIntent, 1);
    }
    else
    {
      // we have at least 1 counter, let's init our main UI
      initialize();
    }
  }
  
  private void initialize()
  {
    // TODO: We should check what the last loaded counter was and go to it.
    
    // Get the main layout view flipper
    ViewFlipper vflipper = (ViewFlipper)findViewById(R.id.counterFlipperView);
    vflipper.removeAllViews();
    
    // Inflate the layout
    LayoutInflater layout = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    View inflatedView = layout.inflate(R.layout.single_counter_view, vflipper);
    
    // For now just load the first counter
    Vector<Counter> counters = PreferencesStorage.getPreferencesStorage(YesternoonActivity.this).getCounters();
    Counter nextCounter = counters.elementAt(0);
    
    // fill the views
    TextView counterNameView = (TextView)inflatedView.findViewById(R.id.mainCounterName);
    counterNameView.setText(nextCounter.getName());
    NumberPicker mainCounterValue = (NumberPicker)inflatedView.findViewById(R.id.mainCounter);
    mainCounterValue.setCurrent(nextCounter.getCount());
  }
  
  @Override
  public boolean onCreateOptionsMenu(Menu menu) 
  {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }
  
  @Override
  public boolean onOptionsItemSelected(MenuItem item) 
  {
    // Handle item selection
    switch (item.getItemId()) 
    {
      case R.id.addCounterMenuItem:
        Intent addCounterIntent = new Intent(this, AddCounterActivity.class);
        this.startActivityForResult(addCounterIntent, 1);
        return true;
      case R.id.deleteCounterMenuItem:
        return true;
      case R.id.editCounterMenuItem:
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }
  
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) 
  {
    super.onActivityResult(requestCode, resultCode, data);
    
    // the Add Counter ACtivity has returned
    if(requestCode == 1)
    {
      // check if they added the counter or if they just cancelled
      Vector<Counter> counters = PreferencesStorage.getPreferencesStorage(YesternoonActivity.this).getCounters();
      if (counters.isEmpty())
      {
        YesternoonActivity.this.finish(); // they just cancelled or went back, close the app
      }
      else
      {
        // Yay! They added a counter! Let's init
        initialize();
      }
    }
  }
}