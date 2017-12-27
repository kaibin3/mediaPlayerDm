package com.example.wenjie.mediaplayerdm.base;

import android.content.Context;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by wen.jie on 2017/12/14.
 */

public class LinearForActivity extends LinearLayout {

    ActivityName activityName;
    private Context mContext;

    public LinearForActivity(Context context) {
        super(context);
        init(context);
    }

    public LinearForActivity(Context context, ActivityName activityName) {
        super(context);
        this.activityName = activityName;
        init(context);
        fillData();
    }


    public LinearForActivity(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }


    private void init(Context context) {
        mContext = context;
        setOrientation(VERTICAL);
    }

    public void fillData() {

        TextView activityTv = new TextView(mContext);
        activityTv.setPadding(0, 40, 0, 0);

        activityTv.setText(activityName.getTargetActivity().getSimpleName());

        activityTv.setTextSize(16);
        //       textView.setBackgroundColor(getResources().getColor(R.color.green_light));
        addView(activityTv);

        if (!TextUtils.isEmpty(activityName.getActivityDescribe())) {
            TextView describeTv = new TextView(mContext);
            describeTv.setTextSize(12);
            //           textView2.setBackgroundColor(getResources().getColor(R.color.yellow));
            describeTv.setText(activityName.getActivityDescribe());
            addView(describeTv);
        }

        setTag(activityName.getTargetActivity().getName());
        // setOnClickListener(this);


    }
}
