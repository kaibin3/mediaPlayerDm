package com.example.wenjie.mediaplayerdm.base;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.wenjie.mediaplayerdm.LocalMediasActivity;
import com.example.wenjie.mediaplayerdm.PhiFind.FindVideoActivity;
import com.example.wenjie.mediaplayerdm.R;
import com.example.wenjie.mediaplayerdm.netDm.TextureViewMediaDmActivity;
import com.example.wenjie.mediaplayerdm.onLineViedio.OnLinePlayActivity;

import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "MainActivity";

    LinearLayout allActivityLayout = null;

    List<ActivityName> showActivity = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        allActivityLayout = (LinearLayout) findViewById(R.id.activity_main);
        allActivityLayout.setOrientation(LinearLayout.VERTICAL);
        //  allActivityLayout.removeAllViews();

        showActivity.add(new ActivityName(this, LocalMediasActivity.class, "本地视频列表"));
        // showActivity.add(new ActivityName(this, Mp4Activity.class, "Mp4"));
        showActivity.add(new ActivityName(this, OnLinePlayActivity.class, "在线视频 Mp4"));
        showActivity.add(new ActivityName(this, FindVideoActivity.class, "phicomm Mp4"));
     //   showActivity.add(new ActivityName(this, VideoSurfaceDemo.class, "VideoSurfaceDemo Mp4"));
       // showActivity.add(new ActivityName(this, LiveCameraActivity.class, "TextureView  dm"));
        showActivity.add(new ActivityName(this, TextureViewMediaDmActivity.class, "TextureView Mp4"));


        for (int i = 0; i < showActivity.size(); i++) {
            LinearLayout rawItem = new LinearLayout(this);
            rawItem.setOrientation(LinearLayout.VERTICAL);

            TextView activityTv = new TextView(this);
            activityTv.setPadding(0, 40, 0, 0);

            activityTv.setText(showActivity.get(i).getTargetActivity().getSimpleName());

            activityTv.setTextSize(16);
            //       textView.setBackgroundColor(getResources().getColor(R.color.green_light));
            rawItem.addView(activityTv);

            if (!TextUtils.isEmpty(showActivity.get(i).getActivityDescribe())) {
                TextView describeTv = new TextView(this);
                describeTv.setTextSize(12);
                //           textView2.setBackgroundColor(getResources().getColor(R.color.yellow));
                describeTv.setText(showActivity.get(i).getActivityDescribe());
                rawItem.addView(describeTv);
            }

            rawItem.setTag(showActivity.get(i).getTargetActivity().getName());
            rawItem.setOnClickListener(this);

            allActivityLayout.addView(rawItem);
        }


        //  Log.d(TAG, "onCreate: " + RxjavaActivity3.class.getSimpleName());   //RxjavaActivity3
        //  Log.d(TAG, "onCreate: " + RxjavaActivity3.class.getCanonicalName());//com.example.wenjie.testrxjava.RxjavaActivity3
        // Log.d(TAG, "onCreate: " + RxjavaActivity3.class.getName());         //com.example.wenjie.testrxjava.RxjavaActivity3
        Log.d(TAG, "onCreate: -------------------------------------------------------------------------------------------");
        try {
            Log.d(TAG, "onCreate: " + showActivity.get(0).getClass().getSimpleName());   //ActivityName
            Log.d(TAG, "onCreate: " + showActivity.get(0).getClass().getName());         //com.example.wenjie.testrxjava.base.ActivityName
            Log.d(TAG, "onCreate: " + showActivity.get(0).getClass().getCanonicalName());//com.example.wenjie.testrxjava.base.ActivityName
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @Override
    public void onClick(View v) {

        for (int i = 0; i < showActivity.size(); i++) {
            if (v.getTag().equals(showActivity.get(i).getTargetActivity().getName())) {
                Intent intent = new Intent();
                intent.setClass(this, showActivity.get(i).getTargetActivity());
                startActivity(intent);
                break;
            }
        }


    }
}
