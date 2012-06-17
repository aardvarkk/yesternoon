package ca.taulabs.yesternoon;

import java.util.Vector;

import ca.taulabs.yesternoon.picker.NumberPicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class YesternoonActivity extends Activity 
{
  private PreferencesStorage mStorage;
  private Vector<Counter> mCounters;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    
    // let's init our instances
    mStorage = new PreferencesStorage(this);
    mCounters = mStorage.loadCounters();
    
    if (mCounters.isEmpty())
    {
      // We should bring up a UI that lets
      // the user create their first counter
      setContentView(R.layout.add_counter);
      NumberPicker picker = (NumberPicker)findViewById(R.id.startingCounter);
      //picker.setRange(Integer.MIN_VALUE, Integer.MAX_VALUE);
      picker.setCurrent(0);
      Button btnAddCounterOK = (Button)findViewById(R.id.btnAddCounterOK);
      btnAddCounterOK.setOnClickListener(new AddCounterOKClicked());
    }
    else
    {
      // we have at least 1 counter, let's check what the
      // last counter was that was used and show that one
      setContentView(R.layout.main);
    }
  }
  
  private class AddCounterOKClicked implements OnClickListener
  {
    @Override
    public void onClick(View view)
    {
      // The add counter views "OK" button has been
      // clicked. We should validate the input and 
      // if everything is ok, add a counter and switch 
      // to the main view.
      EditText counterNameView = (EditText)YesternoonActivity.this.findViewById(R.id.nameEdit);
      String counterName = counterNameView.getText().toString();
      if (counterName == null || counterName.equals(""))
      {
        AlertDialog.Builder builder = new AlertDialog.Builder(YesternoonActivity.this);
        builder.setMessage("Please specify a name for the counter.");
        builder.setCancelable(false);
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
          public void onClick(DialogInterface dialog, int id) {
               dialog.cancel();
          }
        });
        AlertDialog alert = builder.create();
        alert.show();
      }
      else
      {
        // They have properly created a counter
        // Let's create a new counter
        NumberPicker picker = (NumberPicker)findViewById(R.id.startingCounter);
        Counter counter = new Counter(counterName, picker.getCurrent());
        mCounters.add(counter);
        
        // Add the counter to our list of counters
        mStorage.saveCounters(mCounters);
        
        // switch to the main view!
        YesternoonActivity.this.setContentView(R.layout.main);
      }
    } 
  }
}