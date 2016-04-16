package com.superviewpager.demo;

import com.superviewpager.utils.Logger;

import java.util.regex.Pattern;

/**
 * @author Serhiy Brukhanda <http://lnkd.in/dMuBjh8>
 */
public class WorkThread extends Thread
{
    // =============================================================================================
    // Constants
    // =============================================================================================

    private static final String TAG = WorkThread.class.getSimpleName();


    // =============================================================================================
    // Fields
    // =============================================================================================

    private String mName;


    // =============================================================================================
    // Constructors & Methods
    // =============================================================================================

    // *********************************************************************************************
    public WorkThread(String name) {
        mName = name;
    }

    // *********************************************************************************************
    @Override
    public void run() {
        try {
            Logger.i(TAG, "Started WorkThread [" + mName + "].");

            Thread.sleep(2000);

            while (!isInterrupted()) {
                Pattern pattern = Pattern.compile("[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+(?:[A-Z]{2}|com|org|net|edu|gov|mil|biz|info|mobi|name|aero|asia|jobs|museum)\\b");
            }
        } catch (InterruptedException e) {
            // Ignore
        } finally {
            Logger.i(TAG, "Stopped WorkThread [" + mName + "].");
        }
    }

    // *********************************************************************************************
    public void cancel() {
        interrupt();
    }
}