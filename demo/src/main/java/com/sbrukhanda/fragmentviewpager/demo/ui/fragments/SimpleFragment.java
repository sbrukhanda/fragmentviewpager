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
package com.sbrukhanda.fragmentviewpager.demo.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.sbrukhanda.fragmentviewpager.FragmentVisibilityListener;
import com.sbrukhanda.fragmentviewpager.demo.R;
import com.sbrukhanda.fragmentviewpager.demo.WorkThread;
import com.sbrukhanda.fragmentviewpager.demo.ui.activities.MainActivity;

/**
 * @author Serhiy Brukhanda <http://lnkd.in/dMuBjh8>
 */
public class SimpleFragment extends Fragment implements FragmentVisibilityListener
{
    // =============================================================================================
    // Constants
    // =============================================================================================

    private static final String EXTRA_FRAGMENT_NAME = "EXTRA_FRAGMENT_NAME";


    // =============================================================================================
    // Fields
    // =============================================================================================

    private TextView mNameLabel;

    private String mName;
    //private WorkThread mWorkThread;


    // =============================================================================================
    // Constructors & Methods
    // =============================================================================================

    // *********************************************************************************************
    public SimpleFragment() {
        // Mandatory empty public constructor
    }

    // *********************************************************************************************
    public static SimpleFragment newInstance(String fragmentName) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_FRAGMENT_NAME, fragmentName);

        SimpleFragment fragment = new SimpleFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    // *********************************************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_simple, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mName = arguments.getString(EXTRA_FRAGMENT_NAME);
        }

        mNameLabel = (TextView) fragment.findViewById(R.id.lbl_name);
        mNameLabel.setText(mName);

        return fragment;
    }

    // *********************************************************************************************
    @Override
    public void onFragmentVisible() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).logMessage(mName + " is visible.");
        }

        //mWorkThread = new WorkThread(mName);
        //mWorkThread.start();
    }

    // *********************************************************************************************
    @Override
    public void onFragmentInvisible() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).logMessage(mName + " is invisible.");
        }

        //mWorkThread.cancel();
        //mWorkThread = null;
    }
}