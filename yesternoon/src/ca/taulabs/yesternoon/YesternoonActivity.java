package ca.taulabs.yesternoon;

import java.util.Vector;

import ca.taulabs.yesternoon.animations.SlideAnimation;
import ca.taulabs.yesternoon.picker.NumberPicker;
import ca.taulabs.yesternoon.picker.NumberPicker.OnChangedListener;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.ViewFlipper;

public class YesternoonActivity extends Activity
{    
  // our storage and counters
  private PreferencesStorage mStorage;
  private Vector<Counter> mCounters;
  
  // gesture detector for detecting flings
  private GestureDetector mGestureDetector;
  
  // the currently displayed counter
  private int mCurrentCounterIdx = 0;
  
  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState) 
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    
    // let's init our instances
    mGestureDetector = new GestureDetector(new MainGestureDetector());
    mStorage = PreferencesStorage.getPreferencesStorage(this);
    mCounters = mStorage.getCounters();
  }
  
  @Override
  public void onStart()
  {
    super.onStart();
    
    if (mCounters.isEmpty())
    {
      // We should bring up a UI that lets
      // the user create their first counter
      launchAddCounterActivity();
    }
    else
    {
      // we have at least 1 counter, let's init our main UI
      initialize();
    }
  }

  @Override
  public boolean onTouchEvent(MotionEvent event)
  {
    return mGestureDetector.onTouchEvent(event);
  }
  
  private void initialize()
  {
    // TODO: We should check what the last loaded counter was and go to it.
    
    // Get the main layout view flipper
    ViewFlipper vflipper = (ViewFlipper)findViewById(R.id.counterFlipperView);
    vflipper.removeAllViews();
    
    // For now just load the first counter
    mCurrentCounterIdx = 0;
    
    // go through each counter and generate a view for it.
    for (int nI = 0; nI < mCounters.size(); nI++)
    {
      Counter nextCounter = mCounters.elementAt(nI);
      
      // Inflate the layout
      LayoutInflater layout = (LayoutInflater)getSystemService(Context.LAYOUT_INFLATER_SERVICE);
      View inflatedView = layout.inflate(R.layout.single_counter_view, null);
            
      // fill the views
      TextView counterNameView = (TextView)inflatedView.findViewById(R.id.mainCounterName);
      counterNameView.setText(nextCounter.getName());
      NumberPicker mainCounterValue = (NumberPicker)inflatedView.findViewById(R.id.mainCounter);
      mainCounterValue.setCurrent(nextCounter.getCount());
      mainCounterValue.setOnChangeListener(new CounterValueChangedListener());
      
      vflipper.addView(inflatedView);
    }
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
        launchAddCounterActivity();
        return true;
      case R.id.deleteCounterMenuItem:
        // show the next view if possible
        ViewFlipper vflipper = (ViewFlipper)findViewById(R.id.counterFlipperView);
        vflipper.setInAnimation(SlideAnimation.inFromRightAnimation(300));
        vflipper.setOutAnimation(SlideAnimation.outToLeftAnimation(300));
        vflipper.showNext();
        vflipper.removeViewAt(mCurrentCounterIdx);
        
        // remove counter from the list
        mCounters.remove(mCurrentCounterIdx);
        
        // wrap around the end
        if (mCurrentCounterIdx >= mCounters.size())
          mCurrentCounterIdx = 0; 
        
        // Save the counters
        mStorage.saveCounters();
        
        // if there are 0 counters left, we will force them to go to Add counter screen
        if (mCounters.isEmpty())
          launchAddCounterActivity();
        
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
      if (mCounters.isEmpty())
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
  
  private void launchAddCounterActivity()
  {
    Intent addCounterIntent = new Intent(this, AddCounterActivity.class);
    this.startActivityForResult(addCounterIntent, 1);
  }
  
  private class CounterValueChangedListener implements OnChangedListener
  {
    @Override
    public void onChanged(NumberPicker picker, int oldVal, int newVal)
    {   
      // Get the current counter and set it's value
      Counter counter = mCounters.get(mCurrentCounterIdx);
      counter.setCount(newVal);
      
      // save the new value
      mStorage.saveCounter(counter);
    }
  }
  
  private class MainGestureDetector implements OnGestureListener
  {
    private static final int SWIPE_MIN_DISTANCE = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;
    
    @Override
    public boolean onDown(MotionEvent e)
    {
      return false;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
        float velocityY)
    {
      // base case, just 1 view so don't do anything
      if (mCounters.size() <= 1)
        return false;
      
      boolean bFlingDetected = false;
      
      // load our view flipper so we can set the animations appropriately.
      ViewFlipper flipper = (ViewFlipper)findViewById(R.id.counterFlipperView);
            
      // left-swipe
      if((e1.getX() - e2.getX()) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) 
      {
        flipper.setInAnimation(SlideAnimation.inFromRightAnimation(300));
        flipper.setOutAnimation(SlideAnimation.outToLeftAnimation(300));
        mCurrentCounterIdx++;
        flipper.showNext();
        bFlingDetected = true;
      }
      // right-swipe
      else if((e2.getX() - e1.getX()) > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) 
      {
        flipper.setInAnimation(SlideAnimation.inFromLeftAnimation(300));
        flipper.setOutAnimation(SlideAnimation.outToRightAnimation(300));
        mCurrentCounterIdx--;
        flipper.showPrevious();
        bFlingDetected = true;
      }
      
      // wrap around the end
      if (mCurrentCounterIdx < 0)
        mCurrentCounterIdx = mCounters.size() - 1;
      else if (mCurrentCounterIdx >= mCounters.size())
        mCurrentCounterIdx = 0; 
      
      return bFlingDetected;
    }

    @Override
    public void onLongPress(MotionEvent e)
    {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
        float distanceY)
    {
      return false;
    }

    @Override
    public void onShowPress(MotionEvent e)
    {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent e)
    {
      return false;
    }
  }
}