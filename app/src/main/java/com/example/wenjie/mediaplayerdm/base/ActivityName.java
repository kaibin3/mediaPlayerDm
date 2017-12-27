package com.example.wenjie.mediaplayerdm.base;

import android.content.Context;

/**
 * Created by wen.jie on 2017/3/15.
 */

public class ActivityName {

    private Context context;
    private Class targetActivity;
    private String activityDescribe;

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }

    public Class getTargetActivity() {
        return targetActivity;
    }

    public void setTargetActivity(Class targetActivity) {
        this.targetActivity = targetActivity;
    }

    public String getActivityDescribe() {
        return activityDescribe;
    }

    public void setActivityDescribe(String activityDescribe) {
        this.activityDescribe = activityDescribe;
    }

    public ActivityName(Context context, Class aClasslass) {
        this.context = context;
        this.targetActivity = aClasslass;
    }

    public ActivityName(Context context, Class targetActivity, String activityDescribe) {
        this.context = context;
        this.targetActivity = targetActivity;
        this.activityDescribe = activityDescribe;
    }
}
