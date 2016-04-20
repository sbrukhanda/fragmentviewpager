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
package com.sbrukhanda.fragmentviewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.sbrukhanda.fragmentviewpager.adapters.FragmentPagerAdapter;
import com.sbrukhanda.fragmentviewpager.adapters.FragmentStatePagerAdapter;
import com.sbrukhanda.fragmentviewpager.utils.Logger;

import java.lang.ref.WeakReference;

/**
 * An extended {@link ViewPager} with added functionality for working with {@code Fragment}s.<p />
 *
 * The major feature of this {@code ViewPager} is that it provides callbacks for the visibility
 * state of its {@code Fragment} pages through the {@link FragmentVisibilityListener} interface.
 * Also, it is possible to nest a {@code FragmentViewPager} inside another {@code FragmentViewPager}
 * and still maintain this functionality.<p />
 *
 * Instructions on how to properly use {@code FragmentViewPager}:
 * <ol>
 *     <li>
 *         Attach {@code FragmentViewPager} programmatically or via XML to an {@code Activity} or
 *         {@code Fragment}, as you would with native {@code ViewPager}.
 *     </li>
 *     <li>
 *         Set {@code FragmentViewPager}'s adapter.<br />
 *         <b>Important: </b> The provided {@code PagerAdapter} should be an instance of
 *         {@link FragmentPagerAdapter} or {@link FragmentStatePagerAdapter},
 *         or else callbacks for the visibility state of {@code Fragment} pages wont work.
 *     </li>
 *     <li>
 *         Override {@code onResumeFragments()} method of the hosting {@code Activity} and
 *         call {@link #notifyPagerVisible()} inside it. Example:
 *         <pre>
 *             private FragmentViewPager mFragmentsPager;
 *
 *            {@literal @Override}
 *             public void onResumeFragments() {
 *                 super.onResumeFragments();
 *                 mFragmentsPager.notifyPagerVisible();
 *             }
 *         </pre>
 *     </li>
 *     <li>
 *         Override {@code onPause()} method of the hosting {@code Activity} and
 *         call {@link #notifyPagerInvisible()} inside it. Example:
 *         <pre>
 *             private FragmentViewPager mFragmentsPager;
 *
 *            {@literal @Override}
 *             public void onPause() {
 *                 super.onPause();
 *                 mFragmentsPager.notifyPagerInvisible();
 *             }
 *         </pre>
 *     </li>
 *     <li>
 *         Implement {@link FragmentVisibilityListener} on all {@code Fragment}s that you wish to
 *         receive callbacks for their visibility state.
 *     </li>
 * </ol>
 *
 * Additionally, this {@code FragmentViewPager}'s paging can be disabled, i.e. user swipe events will be
 * ignored, but all other means of changing its pages will still function as expected. This
 * functionality is exposed through {@link #isPagingEnabled()} and {@link #setPagingEnabled(boolean)}
 * methods.
 *
 * @author Serhiy Brukhanda <http://lnkd.in/dMuBjh8>
 */
public class FragmentViewPager extends ViewPager
{
    // =============================================================================================
    // Constants
    // =============================================================================================

    private static final String TAG = FragmentViewPager.class.getSimpleName();


    // =============================================================================================
    // Fields
    // =============================================================================================

    private InternalOnPageChangeListener mInternalOnPageChangeListener;

    /**
     * Indicates whether this {@code FragmentViewPager}'s is visible to the user or not.
     *
     * @see #notifyPagerVisible()
     * @see #notifyPagerInvisible()
     */
    private boolean mIsPagerVisible;

    /**
     * Indicates whether this {@code FragmentViewPager}'s paging capabilities (i.e. user swipe events
     * will cause a page change) are enabled or disabled.
     *
     * @see #isPagingEnabled()
     * @see #setPagingEnabled(boolean)
     */
    private boolean mIsPagingEnabled;


    // =============================================================================================
    // Constructors & Methods
    // =============================================================================================

    /**
     * Constructs a new {@code FragmentViewPager} with the specified {@code Context}.
     *
     * @param context The {@code Context} to use.
     */
    public FragmentViewPager(Context context) {
        super(context);
        init();
    }

    /**
     * Constructs a new {@code FragmentViewPager} with the specified {@code Context} and {@code AttributeSet}.
     *
     * @param context The {@code Context} to use.
     * @param attrs The {@code AttributeSet} to use.
     */
    public FragmentViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Initialized the state of this {@code FragmentViewPager}.
     */
    private void init() {
        mIsPagerVisible = false;
        mIsPagingEnabled = true;
    }

    /** {@inheritDoc}  */
    @Override
    public void setAdapter(PagerAdapter adapter) {
        if ((adapter instanceof FragmentPagerAdapter) || adapter instanceof FragmentStatePagerAdapter) {
            attachInternalOnPageChangeListener();
        } else {
            Logger.w(TAG, "Provided PagerAdapter does not support Fragment visibility events.");
            detachInternalOnPageChangeListener();
        }
        super.setAdapter(adapter);
    }

    /** @hide */
    private void attachInternalOnPageChangeListener() {
        if (mInternalOnPageChangeListener == null) {
            mInternalOnPageChangeListener = new InternalOnPageChangeListener();
            addOnPageChangeListener(mInternalOnPageChangeListener);
        }
    }

    /** @hide */
    private void detachInternalOnPageChangeListener() {
        if (mInternalOnPageChangeListener != null) {
            removeOnPageChangeListener(mInternalOnPageChangeListener);
            mInternalOnPageChangeListener = null;
        }
    }

    /**
     * Notifies this {@code FragmentViewPager} that it is visible to the user.<p />
     *
     * Most commonly called from {@code onResumeFragments()} method of the hosting {@code Activity}
     * or from {@code FragmentVisibilityListener.onFragmentVisible()} method of the hosting
     * {@code Fragment} in case this {@code FragmentViewPager} is nested inside another
     * {@code FragmentViewPager}.
     */
    public void notifyPagerVisible() {
        mIsPagerVisible = true;
        if (mInternalOnPageChangeListener != null) {
            mInternalOnPageChangeListener.updateCurrentFragmentState(true);
        }
    }

    /**
     * Notifies this {@code FragmentViewPager} that it is no longer visible to the user.<p />
     *
     * Most commonly called from {@code onPause()} method of the hosting {@code Activity} or from
     * {@code FragmentVisibilityListener.onFragmentInvisible()} method of the hosting
     * {@code Fragment} in case this {@code FragmentViewPager} is nested inside another
     * {@code FragmentViewPager}.
     */
    public void notifyPagerInvisible() {
        if (mInternalOnPageChangeListener != null) {
            mInternalOnPageChangeListener.updateCurrentFragmentState(false);
        }
        mIsPagerVisible = false;
    }

    /** @hide */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return ((mIsPagingEnabled) ? super.onTouchEvent(event) : false);
    }

    /** @hide */
    @Override
    public boolean onInterceptTouchEvent(MotionEvent event) {
        return ((mIsPagingEnabled) ? super.onInterceptTouchEvent(event) : false);
    }

    /**
     * @return {@code True} if this {@code FragmentViewPager}'s paging capabilities (i.e. user swipe
     * events will cause a page change) are enabled, else {@code false}.
     */
    public boolean isPagingEnabled() {
        return mIsPagingEnabled;
    }

    /**
     * Enables/Disables this {@code FragmentViewPager}'s paging capabilities (i.e. user swipe events
     * will cause a page change).
     *
     * @param enabled Whether to enable or disable this {@code FragmentViewPager}'s paging capabilities.
     */
    public void setPagingEnabled(boolean enabled) {
        mIsPagingEnabled = enabled;
    }


    // =============================================================================================
    // Inner classes
    // =============================================================================================

    /**
     * A {@code ViewPager.SimpleOnPageChangeListener} that is responsible for notifying
     * {@code Fragment} pages about their visibility state.
     */
    private class InternalOnPageChangeListener extends ViewPager.SimpleOnPageChangeListener
    {
        // =========================================================================================
        // Fields
        // =========================================================================================

        /**
         * Contains the the state of the currently active {@code Fragment} page.
         */
        private FragmentContainer mFragmentContainer;


        // =========================================================================================
        // Constructors & Methods
        // =========================================================================================

        // *****************************************************************************************
        public InternalOnPageChangeListener() {
            mFragmentContainer = new FragmentContainer();
        }

        // *****************************************************************************************
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            // Return if page position has not changed or animating between pages
            if ((position == mFragmentContainer.getPosition()) || (positionOffset != 0)) return;

            // Notify Fragment pages if this {@code FragmentViewPager} is visible
            if (mIsPagerVisible) {
                // Try to notify previously visible Fragment page that is no longer visible
                Fragment currentFragment = mFragmentContainer.getFragment();
                if (currentFragment != null) {
                    notifyFragmentInvisible();
                }

                // Try to notify newly selected Fragment page that it is visible
                Fragment selectedFragment = getFragment(position);
                if (selectedFragment != null) {
                    // Update state with the newly selected Fragment page
                    mFragmentContainer.setFragment(selectedFragment);

                    if (selectedFragment.isAdded()) {
                        // Notify newly selected Fragment page only if it is still attached
                        notifyFragmentVisible();
                    } else {
                        Logger.e(TAG, "Fragment [" + selectedFragment.getClass().getSimpleName() + "] not added.");
                    }
                }
            }

            // Always update state with the latest valid Fragment page position
            mFragmentContainer.setPosition(position);
        }

        // *****************************************************************************************
        public void updateCurrentFragmentState(boolean isFragmentVisible) {

            // -------------------------------------------------------------------------------------
            // Try to retrieve currently active Fragment page if not already done from before. Might
            // happen when updateCurrentFragmentState() is called with newly initialized
            // InternalOnPageChangeListener.
            // -------------------------------------------------------------------------------------

            if (mFragmentContainer.getFragment() == null) {
                Fragment currentFragment = getFragment(mFragmentContainer.getPosition());
                mFragmentContainer.setFragment(currentFragment);
            }

            // -------------------------------------------------------------------------------------
            // Notify currently active Fragment page about its state.
            // -------------------------------------------------------------------------------------

            if (isFragmentVisible) {
                notifyFragmentVisible();
            } else {
                notifyFragmentInvisible();
            }
        }

        // *****************************************************************************************
        private Fragment getFragment(int position) {
            try {
                Fragment fragment = null;
                if (getAdapter() instanceof FragmentPagerAdapter) {
                    FragmentPagerAdapter adapter = (FragmentPagerAdapter) getAdapter();
                    fragment = adapter.getFragment(position);
                } else if (getAdapter() instanceof FragmentStatePagerAdapter) {
                    FragmentStatePagerAdapter adapter = (FragmentStatePagerAdapter) getAdapter();
                    fragment = adapter.getFragment(position);
                }
                return ((fragment instanceof FragmentVisibilityListener) ? fragment : null);
            } catch (Exception ex) {
                // Return to indicate that the Fragment page does comply with {@code FragmentViewPager}
                // rules.
                return null;
            }
        }

        // *****************************************************************************************
        private void notifyFragmentVisible() {
            Fragment currentFragment = mFragmentContainer.getFragment();
            if ((currentFragment != null) && !mFragmentContainer.isVisible()) {
                FragmentVisibilityListener listener = (FragmentVisibilityListener) currentFragment;
                mFragmentContainer.setVisible(true);
                listener.onFragmentVisible();
            }
        }

        // *****************************************************************************************
        private void notifyFragmentInvisible() {
            Fragment currentFragment = mFragmentContainer.getFragment();
            if ((currentFragment != null) && mFragmentContainer.isVisible()) {
                FragmentVisibilityListener listener = (FragmentVisibilityListener) currentFragment;
                mFragmentContainer.setVisible(false);
                listener.onFragmentInvisible();
            }
        }
    }

    /**
     * A container for keeping the state of currently active {@code Fragment} page.
     */
    private class FragmentContainer
    {
        // =========================================================================================
        // Fields
        // =========================================================================================

        private WeakReference<Fragment> mFragmentReference;
        private boolean                 mIsVisible;
        private int                     mPosition;


        // =========================================================================================
        // Constructors & Methods
        // =========================================================================================

        // *****************************************************************************************
        public FragmentContainer() {
            mIsVisible = false;
            mPosition = -1;
        }

        // *****************************************************************************************
        public Fragment getFragment() {
            return ((mFragmentReference != null) ? mFragmentReference.get() : null);
        }

        // *****************************************************************************************
        public void setFragment(Fragment fragment) {
            mFragmentReference = new WeakReference<>(fragment);
        }

        // *****************************************************************************************
        public int getPosition() {
            return mPosition;
        }

        // *****************************************************************************************
        public void setPosition(int position) {
            mPosition = position;
        }

        // *****************************************************************************************
        public boolean isVisible() {
            return mIsVisible;
        }

        // *****************************************************************************************
        public void setVisible(boolean isVisible) {
            mIsVisible = isVisible;
        }
    }
}