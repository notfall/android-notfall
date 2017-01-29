package com.mayassin.android.notfall.android;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import com.mayassin.android.notfall.R;

/**
 * Created by mohamed on 1/28/17.
 */

public class RegisterFragment extends Fragment {
    private View view;
    private int pageNum;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Bundle bundle = getArguments();
        pageNum = bundle.getInt("page");

        switch (pageNum) {
            case 1:
                view =  inflater.inflate(R.layout.fragment_registerone, container, false);
                break;
            case 2:
                view =  inflater.inflate(R.layout.fragment_registertwo, container, false);
                break;
            case 3:
                view =  inflater.inflate(R.layout.fragment_registerthree, container, false);
                break;
            default:
                view =  inflater.inflate(R.layout.fragment_registerone, container, false);
                break;
        }
        return view;

    }


}