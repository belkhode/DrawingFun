package com.mbelkhode.drawingfun;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

/**
 * The application's main activity that runs when the app is launched. Uses 3 fragments
 *
 *    1. DrawingFragment - This fragment sets up the drawing canvas and enables to user to
 *           set the fragment in brush mode, erase mode, set the paint color and erase the entire
 *           canvas
 *    2. ColorSelectionFragment - This dialog fragment pops up the predefined colors palette and
 *           lets the user choose the color. The invoking activity implements the callback to get
 *           back the color chosen by the user
 *    3. AlertFragment - This dialog fragment pops up the alert dialog when the user selects to
 *           erase the whole canvas. The invoking activity implements the callback to receive the
 *           response back from the user. The fragment accepts the title as the input of the dialog
 *           to keep the fragment generic and reusable.
 *
 */

public class MainActivity extends ActionBarActivity
        implements ColorSelectionFragment.OnColorSelectedListener, AlertFragment.OnResponseSelectedListener {

    private static final String LOG_TAG = MainActivity.class.getSimpleName();
    private static final String FIRST_RUN_KEY = "first run key";
    private boolean mFirstRun = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupToolBar();

        if (savedInstanceState != null) {
            mFirstRun = savedInstanceState.getBoolean(FIRST_RUN_KEY);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (mFirstRun) {
            ColorSelectionFragment fragment = ColorSelectionFragment.newInstance();
            if (fragment != null) {
                fragment.show(getFragmentManager(), "color selection dialog");
            } else {
                Log.e(LOG_TAG, "onStart: color selection fragment is null");
            }
        }
        mFirstRun = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id) {
            case R.id.brush:
                DrawingFragment drawFragment = getDrawingFragment();
                if (drawFragment != null) {
                    drawFragment.setBrushMode();
                } else {
                    Log.e(LOG_TAG, "onOptionsItemSelected: brush: Drawing fragment is null");
                }
                return true;
            case R.id.erase:
                DrawingFragment drawingFragment = getDrawingFragment();
                if (drawingFragment != null) {
                    drawingFragment.setEraseMode();
                } else {
                    Log.e(LOG_TAG, "onOptionsItemSelected: erase: Drawing fragment is null");
                }
                return true;
            case R.id.select_color:
                ColorSelectionFragment fragment = ColorSelectionFragment.newInstance();
                if (fragment != null) {
                    fragment.show(getFragmentManager(), "color selection dialog");
                } else {
                    Log.e(LOG_TAG, "onOptionsItemSelected: color selection fragment is null");
                }
                return true;
            case R.id.erase_all:
                AlertFragment alertFragment = AlertFragment.newInstance(getString(R.string.erase_question));
                if (alertFragment != null) {
                    alertFragment.show(getFragmentManager(), "alert dialog");
                } else {
                    Log.e(LOG_TAG, "onOptionsItemSelected: alert fragment is null");
                }
                return true;
            case R.id.save:
                DrawingFragment dFragment = getDrawingFragment();
                if (dFragment != null) {
                    dFragment.saveDrawing();
                } else {
                    Log.e(LOG_TAG, "onOptionsItemSelected: save: Drawing fragment is null");
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBoolean(FIRST_RUN_KEY, mFirstRun);
    }

    /**
     * This function sets up the toolbar.
     */
    private void setupToolBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setTitle(getText(R.string.app_name));
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.BLUE));
    }

    /**
     * This is the callback implemented by this activity to receive the selected color from the
     * ColorSelectionFragment. The result from ColorSelectionFragment is then passed to the
     * DrawingFragment to set the selected color.
     *
     * @param color The color selected by the user
     */
    @Override
    public void onColorSelected(int color) {
        DrawingFragment fragment = getDrawingFragment();
        if (fragment != null) {
            fragment.setPaintColor(color);
        } else {
            Log.e(LOG_TAG, "onColorSelected: Drawing fragment is null");
        }
    }

    /**
     * This is the callback implemented by this activity to receive the user response from the
     * AlertFragment. Based on the response from the AlertFragment, it is decided whether the
     * DrawingFragment should be called to erase all the contents.
     *
     * @param isOk The response from the user. true if the user selected ok, false otherwise
     */
    @Override
    public void onResponseSelected(boolean isOk) {
        if (isOk) {
            DrawingFragment drawingFragment = getDrawingFragment();
            if (drawingFragment != null) {
                drawingFragment.eraseAllAndSetDefaultColor();
            } else {
                Log.e(LOG_TAG, "onResponseSelected: Drawing fragment is null");
            }
        }
    }

    /**
     * Helper function to find the DrawingFragment.
     *
     */
    private DrawingFragment getDrawingFragment() {
        DrawingFragment fragment = (DrawingFragment)getSupportFragmentManager().findFragmentById(R.id.drawing_fragment);
        if (fragment == null) {
            Log.e(LOG_TAG, "Drawing fragment is null");
        }
        return fragment;
    }
}
