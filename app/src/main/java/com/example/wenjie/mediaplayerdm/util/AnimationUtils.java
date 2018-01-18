package com.example.wenjie.mediaplayerdm.util;

import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;

/**
 * Created by wen.jie on 2018/1/18.
 */

public class AnimationUtils {

    /**插值器
     * LinearInterpolator为匀速效果，
     * Accelerateinterpolator为加速效果、
     * DecelerateInterpolator为减速效果，
     */


    /**
     * 匀速无限360度旋转 （效果不如属性动画）
     **/
    public static RotateAnimation rotationAnim(View view) {
        RotateAnimation rotateAnimation = new RotateAnimation(0f, 359f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotateAnimation.setInterpolator(new LinearInterpolator());
        rotateAnimation.setDuration(800);
        rotateAnimation.setRepeatCount(Animation.INFINITE);
        rotateAnimation.setFillAfter(true);
        view.startAnimation(rotateAnimation);
        return rotateAnimation;
    }

    /**
     * 匀速 无限360度旋转
     */
    public static void rotationAnimator(View view) {
        ObjectAnimator rotation = ObjectAnimator.ofFloat(view, "rotation", 0f, 359f);//最好是0f到359f，0f和360f的位置是重复的
        rotation.setRepeatCount(ObjectAnimator.INFINITE);
        rotation.setInterpolator(new LinearInterpolator());
        rotation.setDuration(500);
        rotation.start();
    }

}
