package com.xiaoyuan.fragment;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.SpannableString;
import android.text.Spanned;
import android.text.style.ClickableSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.xiaoyuan.longer.R;
import com.xiaoyuan.tools.ShuoMClickableSpan;

/**
 * A simple {@link Fragment} subclass.
 */
public class Fragment_message_my extends Fragment {


    public Fragment_message_my() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_message_my, container, false);
        TextView textView1 = (TextView) view.findViewById(R.id.message_tv);
        TextView textView2 = (TextView) view.findViewById(R.id.message_tv2);
        String ttt = " 想看最美丽的风景吗？快来关注我的摄影作品星球吧";
        SpannableString spanttt = new SpannableString(ttt);
        ClickableSpan clickttt = new ShuoMClickableSpan(ttt, getActivity());
        spanttt.setSpan(clickttt, 0, ttt.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);

        textView1.append(spanttt);
        textView2.append(spanttt);
        return view;
    }

}
