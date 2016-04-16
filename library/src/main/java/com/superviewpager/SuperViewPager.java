package com.superviewpager;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.superviewpager.adapters.FragmentPagerAdapter;
import com.superviewpager.adapters.FragmentStatePagerAdapter;
import com.superviewpager.utils.Logger;

import java.lang.ref.WeakReference;

/**
 * An extended {@link ViewPager} with added functionality for working with {@code Fragment}s.<p />
 *
 * The major feature of this {@code ViewPager} is that it provides callbacks for the visibility
 * state of its {@code Fragment} pages through the {@link FragmentVisibilityListener} interface.
 * Also, it is possible to nest a {@code SuperViewPager} inside another {@code SuperViewPager} and
 * still maintain this functionality.<p />
 *
 * Instructions on how to properly use {@code SuperViewPager}:
 * <ol>
 *     <li>
 *         Attach {@code SuperViewPager} programmatically or via XML to an {@code Activity} or
 *         {@code Fragment}, as you would with stock {@code ViewPager}.
 *     </li>
 *     <li>
 *         Set {@code SuperViewPager}'s adapter.<br />
 *         <b>Important: </b> The provided {@code PagerAdapter} should be an instance of
 *         {@link FragmentPagerAdapter} or {@link FragmentStatePagerAdapter},
 *         or else callbacks for the visibility state of {@code Fragment} pages wont work.
 *     </li>
 *     <li>
 *         Override {@code onResume()} method of the hosting {@code Activity} or {@code Fragment} and
 *         call {@link #notifyPagerVisible()} inside it. Example:
 *         <pre>
 *             private SuperViewPager mFragmentsPager;
 *
 *            {@literal @Override}
 *             public void onResume() {
 *                 super.onResume();
 *                 mFragmentsPager.notifyPagerVisible();
 *             }
 *         </pre>
 *     </li>
 *     <li>
 *         Override {@code onPause()} method of the hosting {@code Activity} or {@code Fragment} and
 *         call {@link #notifyPagerInvisible()} inside it. Example:
 *         <pre>
 *             private SuperViewPager mFragmentsPager;
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
 * Additionally, this {@code SuperViewPager}'s paging can be disabled, i.e. user swipe events will be
 * ignored, but all other means of changing its pages will still function as expected. This
 * functionality is exposed through {@link #isPagingEnabled()} and {@link #setPagingEnabled(boolean)}
 * methods.
 *
 * @author Serhiy Brukhanda <http://lnkd.in/dMuBjh8>
 */
public class SuperViewPager extends ViewPager
{
    // =============================================================================================
    // Constants
    // =============================================================================================

    private static final String TAG = SuperViewPager.class.getSimpleName();


    // =============================================================================================
    // Fields
    // =============================================================================================

    private InternalOnPageChangeListener mInternalOnPageChangeListener;

    /**
     * Indicates whether this {@code SuperViewPager}'s is visible to the user or not.
     *
     * @see #notifyPagerVisible()
     * @see #notifyPagerInvisible()
     */
    private boolean mIsPagerVisible;

    /**
     * Indicates whether this {@code SuperViewPager}'s paging capabilities (i.e. user swipe events
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
     * Constructs a new {@code SuperViewPager} with the specified {@code Context}.
     *
     * @param context The {@code Context} to use.
     */
    public SuperViewPager(Context context) {
        super(context);
        init();
    }

    /**
     * Constructs a new {@code SuperViewPager} with the specified {@code Context} and {@code AttributeSet}.
     *
     * @param context The {@code Context} to use.
     * @param attrs The {@code AttributeSet} to use.
     */
    public SuperViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    /**
     * Initialized the state of this {@code SuperViewPager}.
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
     * Notifies this {@code SuperViewPager} that it is visible to the user.<p />
     *
     * Most commonly called from {@code onResume()} method of the hosting {@code Activity} or
     * {@code Fragment}, or from {@code FragmentVisibilityListener.onFragmentVisible()} method of
     * the hosting {@code Fragment} in case this {@code SuperViewPager} is nested inside another
     * {@code SuperViewPager}.
     */
    public void notifyPagerVisible() {
        mIsPagerVisible = true;
        if (mInternalOnPageChangeListener != null) {
            mInternalOnPageChangeListener.updateCurrentFragmentState(true);
        }
    }

    /**
     * Notifies this {@code SuperViewPager} that it is no longer visible to the user.<p />
     *
     * Most commonly called from {@code onPause()} method of the hosting {@code Activity} or
     * {@code Fragment}, or from {@code FragmentVisibilityListener.onFragmentInvisible()} method of
     * the hosting {@code Fragment} in case this {@code SuperViewPager} is nested inside another
     * {@code SuperViewPager}.
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
     * @return {@code True} if this {@code SuperViewPager}'s paging capabilities (i.e. user swipe
     * events will cause a page change) are enabled, else {@code false}.
     */
    public boolean isPagingEnabled() {
        return mIsPagingEnabled;
    }

    /**
     * Enables/Disables this {@code SuperViewPager}'s paging capabilities (i.e. user swipe events
     * will cause a page change).
     *
     * @param enabled Whether to enable or disable this {@code SuperViewPager}'s paging capabilities.
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

            // Notify Fragment pages if this {@code SuperViewPager} is visible
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

                    if (selectedFragment.isAdded() || selectedFragment.getUserVisibleHint()) {
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
                // Return to indicate that the Fragment page does comply with {@code SuperViewPager}
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