package com.example.wenjie.mediaplayerdm.base;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.example.wenjie.mediaplayerdm.Mp4Activity;
import com.example.wenjie.mediaplayerdm.R;

import java.util.ArrayList;
import java.util.List;


public class MediaShowFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "ShowFragment";

    private View mFragmentView;
    LinearLayout allActivityLayout = null;
    List<ActivityName> showActivity = new ArrayList<>();


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mFragmentView = inflater.inflate(R.layout.fragment_show, container, false);
        return mFragmentView;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        allActivityLayout = (LinearLayout) mFragmentView.findViewById(R.id.linear_layout);

        showActivity.add(new ActivityName(getActivity(), Mp4Activity.class, ""));


        for (int i = 0; i < showActivity.size(); i++) {
            LinearForActivity rawItem = new LinearForActivity(getActivity(), showActivity.get(i));
            rawItem.setOnClickListener(this);
            allActivityLayout.addView(rawItem);
        }
    }

    @Override
    public void onClick(View v) {
        for (int i = 0; i < showActivity.size(); i++) {
            if (v.getTag().equals(showActivity.get(i).getTargetActivity().getName())) {
                Intent intent = new Intent();
                intent.setClass(getActivity(), showActivity.get(i).getTargetActivity());
                startActivity(intent);
                break;
            }
        }
    }

}
