/*
 * Copyright (C) 2016 Serhiy Brukhanda
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.sbrukhanda.fragmentviewpager.demo;

import com.sbrukhanda.fragmentviewpager.utils.Logger;

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