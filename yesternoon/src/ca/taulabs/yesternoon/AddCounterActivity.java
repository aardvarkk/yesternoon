package ca.taulabs.yesternoon;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

public class AddCounterActivity extends Activity
{
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.add_counter);
    Button btnAddCounterOK = (Button)findViewById(R.id.btnAddCounterOK);
    Button btnAddCounterCancel = (Button)findViewById(R.id.btnAddCounterCancel);
    btnAddCounterOK.setOnClickListener(new AddCounterOKClicked());
    btnAddCounterCancel.setOnClickListener(new AddCounterCancelClicked());
  }
  
  private class AddCounterCancelClicked implements OnClickListener
  {
    public void onClick(View view)
    {
      // TODO: Do a check if there are counters, if they cancel we should warn them 
      finish();
    }
  }
  
  private class AddCounterOKClicked implements OnClickListener
  {
    public void onClick(View view)
    {
      // The add counter views "OK" button has been
      // clicked. We should validate the input and 
      // if everything is ok, add a counter and switch 
      // to the main view.
      EditText counterNameView = (EditText)AddCounterActivity.this.findViewById(R.id.nameEdit);
      String counterName = counterNameView.getText().toString();
      if (counterName == null || counterName.equals(""))
      {
        AlertDialog.Builder builder = new AlertDialog.Builder(AddCounterActivity.this);
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
        // Let's create a new counter, and add it to our list of counters!
        AppState state = PreferencesStorage.getPreferencesStorage(AddCounterActivity.this).getState();

        // Convert the text to a number
        EditText startingCountEdit = (EditText) findViewById(R.id.startingCountEdit);
        Integer startingCount = Integer.parseInt(startingCountEdit.getText().toString());
        
        state.mCounters.add(new Counter(counterName, startingCount));
        state.mCurrentCounterIdx = state.mCounters.size() - 1;
        setResult(RESULT_OK);
        finish();
      }
    } 
  }
}
