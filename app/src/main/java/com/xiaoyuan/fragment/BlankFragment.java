package com.xiaoyuan.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoyuan.longer.R;
import com.xiaoyuan.longer.Ts_Activity;
import com.xiaoyuan.tools.Toast;

/**
 * A simple {@link Fragment} subclass.
 */
public class BlankFragment extends Fragment {
    public BlankFragment() {
        // Required empty public constructor


    }





    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_blank, container, false);
    }

}
