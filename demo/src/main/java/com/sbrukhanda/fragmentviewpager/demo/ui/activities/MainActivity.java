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
package com.sbrukhanda.fragmentviewpager.demo.ui.activities;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.sbrukhanda.fragmentviewpager.adapters.FragmentPagerAdapter;
import com.sbrukhanda.fragmentviewpager.adapters.FragmentStatePagerAdapter;
import com.sbrukhanda.fragmentviewpager.FragmentViewPager;
import com.sbrukhanda.fragmentviewpager.demo.R;
import com.sbrukhanda.fragmentviewpager.demo.ui.fragments.CompoundFragment;
import com.sbrukhanda.fragmentviewpager.demo.ui.fragments.SimpleFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Serhiy Brukhanda <http://lnkd.in/dMuBjh8>
 */
public class MainActivity extends AppCompatActivity
{
    // =============================================================================================
    // Constants
    // =============================================================================================

    private static final int LOG_HISTORY_SIZE = 5;


    // =============================================================================================
    // Fields
    // =============================================================================================

    private FragmentViewPager mFragmentsPager;
    private TabLayout       mFragmentsTab;

    private TextView     mLogsLabel;
    private List<String> mLogsHistory;


    // =============================================================================================
    // Constructors & Methods
    // =============================================================================================

    // *********************************************************************************************
    public MainActivity() {
        mLogsHistory = new ArrayList<>(LOG_HISTORY_SIZE);
    }

    // *********************************************************************************************
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mLogsLabel = (TextView) findViewById(R.id.lbl_logs);

        mFragmentsPager = (FragmentViewPager) findViewById(R.id.pager_fragments);
        mFragmentsPager.setOffscreenPageLimit(1);

        PagerAdapter pagerAdapter = new FragmentStateAdapter(getSupportFragmentManager());
        mFragmentsPager.setAdapter(pagerAdapter);

        mFragmentsTab = (TabLayout) findViewById(R.id.tabs_main);
        mFragmentsTab.setupWithViewPager(mFragmentsPager);
    }

    // *********************************************************************************************
    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        mFragmentsPager.notifyPagerVisible();
    }

    // *********************************************************************************************
    @Override
    public void onPause() {
        super.onPause();
        mFragmentsPager.notifyPagerInvisible();
    }

    // *********************************************************************************************
    public void logMessage(String msg) {
        // Return as nothing to log
        if (TextUtils.isEmpty(msg)) return;

        mLogsHistory.add(msg);

        // Remove first item in order to keep history within predefined bounds
        if (mLogsHistory.size() > LOG_HISTORY_SIZE) mLogsHistory.remove(0);

        String separator = "";
        String text = "";

        // Assemble log
        for (String log : mLogsHistory) {
            text += separator + log;
            separator = "\n";
        }

        if (!isFinishing()) mLogsLabel.setText(text);
    }


    // =============================================================================================
    // Inner Classes
    // =============================================================================================

    // *********************************************************************************************
    private class FragmentAdapter extends FragmentPagerAdapter
    {
        public FragmentAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment instantiateFragment(int position) {
            String name = "Fragment " + position;
            switch (position) {
                case 2:
                case 7:
                    return CompoundFragment.newInstance(name);

                default:
                    return SimpleFragment.newInstance(name);
            }
        }

        @Override
        public int getCount() {
            return 8;
        }

        public CharSequence getPageTitle(int position) {
            return String.valueOf(position);
        }
    }

    // *********************************************************************************************
    private class FragmentStateAdapter extends FragmentStatePagerAdapter
    {
        public FragmentStateAdapter(FragmentManager fragmentManager) {
            super(fragmentManager);
        }

        @Override
        public Fragment instantiateFragment(int position) {
            String name = "Fragment " + position;
            switch (position) {
                case 2:
                case 7:
                    return CompoundFragment.newInstance(name);

                default:
                    return SimpleFragment.newInstance(name);
            }
        }

        @Override
        public int getCount() {
            return 8;
        }

        public CharSequence getPageTitle(int position) {
            return String.valueOf(position);
        }
    }
}