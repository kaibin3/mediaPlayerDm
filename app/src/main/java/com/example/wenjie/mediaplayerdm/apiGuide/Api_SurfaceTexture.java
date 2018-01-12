package com.example.wenjie.mediaplayerdm.apiGuide;

import android.graphics.SurfaceTexture;

/**
 * Created by wen.jie on 2018/1/11.
 */

public class Api_SurfaceTexture {
    SurfaceTexture surfaceTexture;
    /**
     从Android 3.0(API level 11)加入。和SurfaceView不同的是，
     它对图像流的处理并不直接显示，而是转为GL外部纹理，
     因此可用于图像流数据的二次处理（如Camera滤镜，桌面特效等）。
     比如Camera的预览数据，变成纹理后可以交给GLSurfaceView直接显示，
     也可以通过SurfaceTexture交给TextureView作为View heirachy中的一个硬件加速层来显示。

     首先，SurfaceTexture从图像流（来自Camera预览，视频解码，GL绘制场景等）中获得帧数据，
     当调用updateTexImage()时，根据内容流中最近的图像更新SurfaceTexture对应的GL纹理对象，
     接下来，就可以像操作普通GL纹理一样操作它了。从下面的类图中可以看出，

     它核心管理着一个BufferQueue的Consumer和Producer两端。
     Producer端用于内容流的源输出数据，
     Consumer端用于拿GraphicBuffer并生成纹理。

     SurfaceTexture.OnFrameAvailableListener用于让SurfaceTexture的使用者知道有新数据到来。
     JNISurfaceTextureContext是OnFrameAvailableListener从Native到Java的JNI跳板。其中SurfaceTexture中的attachToGLContext()和detachToGLContext()可以让多个GL context共享同一个内容源。

     * */
}
