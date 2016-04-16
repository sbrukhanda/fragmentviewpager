/*
 * Copyright (C) 2011 The Android Open Source Project
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

package com.superviewpager.adapters;

import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

/**
 * A modified copy of the {@link android.support.v4.app.FragmentPagerAdapter}, which retains its
 * original behaviour, but incorporates some additions and changes to its methods. <p />
 *
 * Firstly, method {@link android.support.v4.app.FragmentPagerAdapter#getItem(int)} was replaced
 * with a more appropriately named {@link #instantiateFragment(int)} method. <p />
 *
 * Secondly, method {@link #getFragment(int)} was added in order to allow access to the underlying
 * mechanism of stored {@code Fragment}s. <p />
 *
 * Lastly, method {@link #makeFragmentName(FragmentPagerAdapter, long)} was modified in order to
 * not interfere with the original {@link android.support.v4.app.FragmentPagerAdapter} and simplify
 * its parameters (mainly for being able to call it from {@link #getFragment(int)}). <p />
 *
 * <b>Note:</b> The Support-v4 v23.1.1 {@link android.support.v4.app.FragmentPagerAdapter}'s source
 * was used as a base.
 *
 * @author Serhiy Brukhanda <http://lnkd.in/dMuBjh8>
 */
public abstract class FragmentPagerAdapter extends PagerAdapter
{
    // =============================================================================================
    // Constants
    // =============================================================================================

    private static final String  TAG   = FragmentPagerAdapter.class.getSimpleName();
    private static final boolean DEBUG = true;


    // =============================================================================================
    // Fields
    // =============================================================================================

    private final FragmentManager mFragmentManager;
    private FragmentTransaction mCurTransaction     = null;
    private Fragment            mCurrentPrimaryItem = null;


    // =============================================================================================
    // Constructors & Methods
    // =============================================================================================

    // *********************************************************************************************
    public FragmentPagerAdapter(FragmentManager fm) {
        mFragmentManager = fm;
    }

    // *********************************************************************************************
    @Override
    public void startUpdate(ViewGroup container) {
        // Nothing needed
    }

    /**
     * Returns the {@code Fragment} associated with the specified position.
     *
     * @param position The position of the {@code Fragment} to fetch.
     * @return The {@code Fragment} associated with the specified position, else {@code null}.
     */
    public Fragment getFragment(int position) {
        long itemId = getItemId(position);
        String name = makeFragmentName(this, itemId);
        Fragment fragment = mFragmentManager.findFragmentByTag(name);
        return fragment;
    }

    /**
     * Create the {@code Fragment} for the specified position.
     *
     * @param position The position of the {@code Fragment} to create.
     * @return The newly created {@code Fragment}.
     */
    public abstract Fragment instantiateFragment(int position);

    // *********************************************************************************************
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }

        final long itemId = getItemId(position);

        Fragment fragment = getFragment(position);
        if (fragment != null) {
            if (DEBUG) Log.v(TAG, "Attaching item #" + itemId + ": f=" + fragment);
            mCurTransaction.attach(fragment);
        } else {
            fragment = instantiateFragment(position);
            if (DEBUG) Log.v(TAG, "Adding item #" + itemId + ": f=" + fragment);
            mCurTransaction.add(container.getId(), fragment,
                makeFragmentName(this, itemId));
        }
        if (fragment != mCurrentPrimaryItem) {
            fragment.setMenuVisibility(false);
            fragment.setUserVisibleHint(false);
        }

        return fragment;
    }

    // *********************************************************************************************
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        if (mCurTransaction == null) {
            mCurTransaction = mFragmentManager.beginTransaction();
        }
        if (DEBUG) Log.v(TAG, "Detaching item #" + getItemId(position) + ": f=" + object
            + " v=" + ((Fragment)object).getView());
        mCurTransaction.detach((Fragment)object);
    }

    // *********************************************************************************************
    @Override
    public void setPrimaryItem(ViewGroup container, int position, Object object) {
        Fragment fragment = (Fragment)object;
        if (fragment != mCurrentPrimaryItem) {
            if (mCurrentPrimaryItem != null) {
                mCurrentPrimaryItem.setMenuVisibility(false);
                mCurrentPrimaryItem.setUserVisibleHint(false);
            }
            if (fragment != null) {
                fragment.setMenuVisibility(true);
                fragment.setUserVisibleHint(true);
            }
            mCurrentPrimaryItem = fragment;
        }
    }

    // *********************************************************************************************
    @Override
    public void finishUpdate(ViewGroup container) {
        if (mCurTransaction != null) {
            mCurTransaction.commitAllowingStateLoss();
            mCurTransaction = null;
            mFragmentManager.executePendingTransactions();
        }
    }

    // *********************************************************************************************
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return ((Fragment)object).getView() == view;
    }

    // *********************************************************************************************
    @Override
    public Parcelable saveState() {
        return null;
    }

    // *********************************************************************************************
    @Override
    public void restoreState(Parcelable state, ClassLoader loader) {
        // Nothing needed
    }

    /**
     * Return a unique identifier for the item at the given position.
     *
     * <p>The default implementation returns the given position.
     * Subclasses should override this method if the positions of items can change.</p>
     *
     * @param position Position within this adapter
     * @return Unique identifier for the item at position
     */
    public long getItemId(int position) {
        return position;
    }

    // *********************************************************************************************
    private static String makeFragmentName(FragmentPagerAdapter adapter, long id) {
        return "android:super-switcher:" + adapter.hashCode() + ":" + id;
    }
}