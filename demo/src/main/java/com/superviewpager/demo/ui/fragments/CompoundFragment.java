package com.superviewpager.demo.ui.fragments;

import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.superviewpager.adapters.FragmentPagerAdapter;
import com.superviewpager.adapters.FragmentStatePagerAdapter;
import com.superviewpager.FragmentVisibilityListener;
import com.superviewpager.SuperViewPager;
import com.superviewpager.demo.R;

/**
 * @author Serhiy Brukhanda <http://lnkd.in/dMuBjh8>
 */
public class CompoundFragment extends Fragment implements FragmentVisibilityListener
{
    // =============================================================================================
    // Constants
    // =============================================================================================

    private static final String EXTRA_FRAGMENT_NAME = "EXTRA_FRAGMENT_NAME";


    // =============================================================================================
    // Fields
    // =============================================================================================

    private SuperViewPager  mFragmentsPager;
    private TabLayout       mFragmentsTab;

    private String mName;


    // =============================================================================================
    // Constructors & Methods
    // =============================================================================================

    // *********************************************************************************************
    public CompoundFragment() {
        // Required empty public constructor
    }

    // *********************************************************************************************
    public static CompoundFragment newInstance(String fragmentName) {
        Bundle arguments = new Bundle();
        arguments.putString(EXTRA_FRAGMENT_NAME, fragmentName);

        CompoundFragment fragment = new CompoundFragment();
        fragment.setArguments(arguments);

        return fragment;
    }

    // *********************************************************************************************
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View fragment = inflater.inflate(R.layout.fragment_compound, container, false);

        Bundle arguments = getArguments();
        if (arguments != null) {
            mName = arguments.getString(EXTRA_FRAGMENT_NAME);
        }

        mFragmentsPager = (SuperViewPager) fragment.findViewById(R.id.pager_sub_fragments);
        mFragmentsPager.setOffscreenPageLimit(1);

        PagerAdapter pagerAdapter = new FragmentStateAdapter(getChildFragmentManager());
        mFragmentsPager.setAdapter(pagerAdapter);

        mFragmentsTab = (TabLayout) fragment.findViewById(R.id.tabs_sub);
        mFragmentsTab.setupWithViewPager(mFragmentsPager);

        return fragment;
    }

    // *********************************************************************************************
    @Override
    public void onFragmentVisible() {
        mFragmentsPager.notifyPagerVisible();
    }

    // *********************************************************************************************
    @Override
    public void onFragmentInvisible() {
        mFragmentsPager.notifyPagerInvisible();
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
            return SimpleFragment.newInstance(mName + " - " + position);
        }

        @Override
        public int getCount() {
            return 3;
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
            return SimpleFragment.newInstance(mName + " - " + position);
        }

        @Override
        public int getCount() {
            return 3;
        }

        public CharSequence getPageTitle(int position) {
            return String.valueOf(position);
        }
    }
}