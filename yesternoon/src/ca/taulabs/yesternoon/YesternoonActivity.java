package ca.taulabs.yesternoon;

import ca.taulabs.yesternoon.animations.SlideAnimation;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.NumberPicker.OnValueChangeListener;
import android.widget.TextView;
import android.widget.ViewFlipper;
import android.widget.NumberPicker;

public class YesternoonActivity extends Activity
{
  // gesture detector for detecting flings
  private GestureDetector    mGestureDetector;
  private AppState           mState;

  public static final String TAG = "Yesternoon";

  @Override
  protected void onPause()
  {
    super.onPause();

    // Save our state persistently
    PreferencesStorage.getPreferencesStorage(this).saveCounters();
  }

  /** Called when the activity is first created. */
  @Override
  public void onCreate(Bundle savedInstanceState)
  {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);

    // let's init our instances
    mState = PreferencesStorage.getPreferencesStorage(this).getState();
    mGestureDetector = new GestureDetector(new MainGestureDetector());
  }

  @Override
  public void onStart()
  {
    super.onStart();

    if (mState.mCounters.isEmpty())
    {
      // We should bring up a UI that lets
      // the user create their first counter
      launchAddCounterActivity();
    } else
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
    // Get the main layout view flipper
    ViewFlipper vflipper = (ViewFlipper) findViewById(R.id.counterFlipperView);
    vflipper.removeAllViews();

    // go through each counter and generate a view for it.
    for (int nI = 0; nI < mState.mCounters.size(); nI++)
    {
      Counter this_counter = mState.mCounters.elementAt(nI);

      // Inflate the layout
      View inflatedView = getLayoutInflater().inflate(
          R.layout.single_counter_view, null);

      // set the counter name
      TextView counterNameView = (TextView) inflatedView
          .findViewById(R.id.mainCounterName);
      counterNameView.setText(this_counter.getName());

      // set the counter value
      NumberPicker mainCounterValue = (NumberPicker) inflatedView
          .findViewById(R.id.mainCounter);
      mainCounterValue.setMinValue(0);
      mainCounterValue.setMaxValue(999);
      mainCounterValue.setValue(this_counter.getCount());
      mainCounterValue.setWrapSelectorWheel(false);
      mainCounterValue
          .setDescendantFocusability(NumberPicker.FOCUS_BLOCK_DESCENDANTS);

      // add a listener to update our model
      mainCounterValue.setOnValueChangedListener(new OnValueChangeListener()
      {
        public void onValueChange(NumberPicker picker, int oldVal, int newVal)
        {
          setCounterValue(mState.mCurrentCounterIdx, newVal);
        }
      });

      vflipper.addView(inflatedView);
    }

    // Set the current one
    vflipper.setDisplayedChild(mState.mCurrentCounterIdx);
  }

  private void setCounterValue(int idx, int newVal)
  {
    Log.d(TAG, "Updating counter " + idx + " to value " + newVal);
    
    // Update the visual
    ViewFlipper vflipper = (ViewFlipper) findViewById(R.id.counterFlipperView);
    View singleCounterView = (View) vflipper
        .getChildAt(idx);
    NumberPicker picker = (NumberPicker) singleCounterView.findViewById(R.id.mainCounter);
    picker.setValue(newVal);
    
    // Get the current counter and set it's value
    mState.mCounters.get(idx).setCount(picker.getValue());
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu)
  {
    MenuInflater inflater = getMenuInflater();
    inflater.inflate(R.menu.main_menu, menu);
    return true;
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event)
  {
    // decrement the current active counter
    if (keyCode == KeyEvent.KEYCODE_VOLUME_DOWN)
    {
      setCounterValue(mState.mCurrentCounterIdx, mState.mCounters.get(mState.mCurrentCounterIdx).getCount()-1);
      return true;
    }
    // increment the current active counter
    else if (keyCode == KeyEvent.KEYCODE_VOLUME_UP)
    {
      setCounterValue(mState.mCurrentCounterIdx, mState.mCounters.get(mState.mCurrentCounterIdx).getCount()+1);
      return true;
    }
    return super.onKeyDown(keyCode, event);
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
        ViewFlipper vflipper = (ViewFlipper) findViewById(R.id.counterFlipperView);
        vflipper.setInAnimation(SlideAnimation.inFromRightAnimation(300));
        vflipper.setOutAnimation(SlideAnimation.outToLeftAnimation(300));
        vflipper.showNext();
        vflipper.removeViewAt(mState.mCurrentCounterIdx);

        // remove counter from the list
        mState.mCounters.remove(mState.mCurrentCounterIdx);

        // wrap around the end
        if (mState.mCurrentCounterIdx >= mState.mCounters.size())
          mState.mCurrentCounterIdx = 0;

        // if there are 0 counters left, we will force them to go to Add counter
        // screen
        if (mState.mCounters.isEmpty())
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
    if (requestCode == 1)
    {
      // check if they added the counter or if they just cancelled
      if (mState.mCounters.isEmpty())
      {
        YesternoonActivity.this.finish(); // they just cancelled or went back,
                                          // close the app
      } else
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

  private class MainGestureDetector implements OnGestureListener
  {
    private static final int SWIPE_MIN_DISTANCE       = 120;
    private static final int SWIPE_THRESHOLD_VELOCITY = 200;

    public boolean onDown(MotionEvent e)
    {
      return false;
    }

    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
        float velocityY)
    {
      // base case, just 1 view so don't do anything
      if (mState.mCounters.size() <= 1)
        return false;

      boolean bFlingDetected = false;

      // load our view flipper so we can set the animations appropriately.
      ViewFlipper flipper = (ViewFlipper) findViewById(R.id.counterFlipperView);

      // left-swipe
      if ((e1.getX() - e2.getX()) > SWIPE_MIN_DISTANCE
          && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
      {
        flipper.setInAnimation(SlideAnimation.inFromRightAnimation(300));
        flipper.setOutAnimation(SlideAnimation.outToLeftAnimation(300));
        mState.mCurrentCounterIdx++;
        flipper.showNext();
        bFlingDetected = true;
      }
      // right-swipe
      else if ((e2.getX() - e1.getX()) > SWIPE_MIN_DISTANCE
          && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY)
      {
        flipper.setInAnimation(SlideAnimation.inFromLeftAnimation(300));
        flipper.setOutAnimation(SlideAnimation.outToRightAnimation(300));
        mState.mCurrentCounterIdx--;
        flipper.showPrevious();
        bFlingDetected = true;
      }

      // wrap around the end
      if (mState.mCurrentCounterIdx < 0)
        mState.mCurrentCounterIdx = mState.mCounters.size() - 1;
      else if (mState.mCurrentCounterIdx >= mState.mCounters.size())
        mState.mCurrentCounterIdx = 0;

      Log.d(TAG, "Current Counter Index: " + mState.mCurrentCounterIdx);
      return bFlingDetected;
    }

    public void onLongPress(MotionEvent e)
    {
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
        float distanceY)
    {
      return false;
    }

    public void onShowPress(MotionEvent e)
    {
    }

    public boolean onSingleTapUp(MotionEvent e)
    {
      return false;
    }
  }
}