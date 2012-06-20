package ca.taulabs.yesternoon.animations;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Provides a set of static animations for doing a "sliding" animation.
 * 
 * @author Dinko Harbinja
 */
public class SlideAnimation 
{
  /**
   * Provides an animation that will move from off the screen into the screen
   * from the right hand side.
   * 
   * @param slideDuration
   *            How long it will take to slide it in/out of the view.
   * @return Animation An animation object that can be run to show a view
   *         coming into the parent view from the ride side.
   */
  public static Animation inFromRightAnimation(int slideDuration) 
  {
    Animation inFromRight = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, +1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f);
    inFromRight.setDuration(slideDuration);
    inFromRight.setInterpolator(new AccelerateInterpolator());
    return inFromRight;
  }

  /**
   * Provides an animation that will move from off the screen into the screen
   * from the left hand side.
   * 
   * @param slideDuration
   *            How long it will take to slide it in/out of the view.
   * @return Animation An animation object that can be run to show a view
   *         coming into the parent view from the left side.
   */
  public static Animation inFromLeftAnimation(int slideDuration) 
  {
    Animation inFromLeft = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, -1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f);
    inFromLeft.setDuration(slideDuration);
    inFromLeft.setInterpolator(new AccelerateInterpolator());
    return inFromLeft;
  }

  /**
   * Provides an animation that will move a current view that is on the screen
   * off the screen to the right side.
   * 
   * @param slideDuration
   *            How long it will take to slide it in/out of the view.
   * @return Animation An animation object that can be run to show a view
   *         leaving the parent view to the right side.
   */
  public static Animation outToRightAnimation(int slideDuration) 
  {
    Animation outtoRight = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, +1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f);
    outtoRight.setDuration(slideDuration);
    outtoRight.setInterpolator(new AccelerateInterpolator());
    return outtoRight;
  }

  /**
   * Provides an animation that will move a current view that is on the screen
   * off the screen to the left side.
   * 
   * @param slideDuration
   *            How long it will take to slide it in/out of the view.
   * @return Animation An animation object that can be run to show a view
   *         leaving the parent view to the left side.
   */
  public static Animation outToLeftAnimation(int slideDuration) 
  {
    Animation outtoLeft = new TranslateAnimation(
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, -1.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f,
            Animation.RELATIVE_TO_PARENT, 0.0f);
    outtoLeft.setDuration(slideDuration);
    outtoLeft.setInterpolator(new AccelerateInterpolator());
    return outtoLeft;
  }
}
