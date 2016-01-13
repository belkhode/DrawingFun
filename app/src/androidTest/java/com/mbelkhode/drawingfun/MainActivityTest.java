package com.mbelkhode.drawingfun;

import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

/**
 * The automated test cases for the MainActivity that runs the main activity, color selection
 * fragment and alert fragment to check for the exceptions and so on.
 */
public class MainActivityTest extends android.test.ActivityInstrumentationTestCase2<MainActivity>{
    // TEST CLASS

    private MainActivity mainActivity;
    private DrawingFragment drawingFragment;
    private ColorSelectionFragment colorSelectionFragment;
    private AlertFragment alertFragment;

    public MainActivityTest() {
        super(MainActivity.class);
    }

    @Override
    protected void setUp() throws Exception {
        super.setUp();
        mainActivity = getActivity();
        drawingFragment = new DrawingFragment();
        colorSelectionFragment = ColorSelectionFragment.newInstance();
        alertFragment = AlertFragment.newInstance("Test string");
    }

    public void testPreConditions() {
        assertNotNull(mainActivity);
        assertNotNull(drawingFragment);
        assertNotNull(colorSelectionFragment);
        assertNotNull(alertFragment);
    }

    public void testDrawingFragment () {
        FragmentManager fragmentManager = mainActivity.getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(drawingFragment, null);
        fragmentTransaction.commit();

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().executePendingTransactions();
            }
        });

        getInstrumentation().waitForIdleSync();
    }

    public void testColorSelectFragment () {

        colorSelectionFragment.show(mainActivity.getFragmentManager(), null);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().executePendingTransactions();
            }
        });

        getInstrumentation().waitForIdleSync();
    }

    public void testAlertFragment () {

        alertFragment.show(mainActivity.getFragmentManager(), null);

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                getActivity().getSupportFragmentManager().executePendingTransactions();
            }
        });

        getInstrumentation().waitForIdleSync();
    }
}

