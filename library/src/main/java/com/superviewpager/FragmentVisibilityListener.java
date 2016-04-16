package com.superviewpager;

/**
 * The interface for receiving visibility changes for {@code Fragment}s inside {@code SuperViewPager}.
 *
 * @author Serhiy Brukhanda <http://lnkd.in/dMuBjh8>
 */
public interface FragmentVisibilityListener
{
    /**
     * Called when {@code Fragment} is visible to the user.
     */
    void onFragmentVisible();

    /**
     * Called when {@code Fragment} is invisible to the user.
     */
    void onFragmentInvisible();
}