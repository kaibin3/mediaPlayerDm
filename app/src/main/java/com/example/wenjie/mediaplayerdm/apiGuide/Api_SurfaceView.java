package com.example.wenjie.mediaplayerdm.apiGuide;

import android.view.SurfaceView;

/**
 * Created by wen.jie on 2018/1/11.
 */

/**
 * http://www.jcodecraeer.com/a/anzhuokaifa/androidkaifa/2014/1213/2153.html
 */
public class Api_SurfaceView {
    SurfaceView surfaceView;

    /**
     1、应用程序的视频或者opengl内容往往是显示在一个特别的UI控件中：SurfaceView。SurfaceView的工作方式是创建一个
     置于应用窗口之后的新窗口。这种方式的效率非常高，因为SurfaceView窗口刷新的时候不需要重绘应用程序的窗口
     （android普通窗口的视图绘制机制是一层一层的，任何一个子元素或者是局部的刷新都会导致整个视图结构全部重绘一次，
     因此效率非常低下，不过满足普通应用界面的需求还是绰绰有余），但是SurfaceView也有一些非常不便的限制。

     因为SurfaceView的内容不在应用窗口上，所以不能使用变换（平移、缩放、旋转等）。也难以放在ListView或者ScrollView中，
     不能使用UI控件的一些特性比如View.setAlpha()。

     为了解决这个问题 Android 4.0中引入了TextureView。


     */
}
