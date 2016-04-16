package com.superviewpager.demo.ui.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.superviewpager.FragmentVisibilityListener;
import com.superviewpager.demo.R;
import com.superviewpager.demo.WorkThread;
import com.superviewpager.demo.ui.activities.MainActivity;

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
    private WorkThread mWorkThread;


    // =============================================================================================
    // Constructors & Methods
    // =============================================================================================

    // *********************************************************************************************
    public SimpleFragment() {
        // Required empty public constructor
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

        mWorkThread = new WorkThread(mName);
        mWorkThread.start();
    }

    // *********************************************************************************************
    @Override
    public void onFragmentInvisible() {
        if (getActivity() != null) {
            ((MainActivity) getActivity()).logMessage(mName + " is invisible.");
        }

        mWorkThread.cancel();
        mWorkThread = null;
    }
}