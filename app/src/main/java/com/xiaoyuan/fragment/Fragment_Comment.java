package com.xiaoyuan.fragment;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.xiaoyuan.longer.R;

/**
 * Created by Axu on 2016/8/20.
 */
public class Fragment_Comment extends Fragment {
    public View view;
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_comment, null);
        return view;
    }
}
