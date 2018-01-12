package com.example.wenjie.mediaplayerdm.apiGuide;

import android.graphics.SurfaceTexture;
import android.view.TextureView;

/**
 * Created by wen.jie on 2018/1/11.
 */

/**
 https://www.cnblogs.com/wytiger/p/5693569.html
  **/
public class Api_TextureView {
    TextureView textureView;
    SurfaceTexture surfaceTexture;
    /**
     1、TextureView可以用来显示内容流。这样一个内容流例如可以视频或者OpenGL的场景。内容流可以来自本应用程序以及其他进程。

     2、Textureview必须在硬件加速开启的窗口中。

     3、与SurfaceView相比，TextureView不会创建一个单独的窗口，这使得它可以像一般的View一样执行一些变换操作，比如移动、动画等等，
        例如,你可以通过调用myView.setAlpha(0.5f)将TextureView设置成半透明。

     4、使用TextureView很简单：你需要使用的就是SurfaceTexture，SurfaceTexture可以用于呈现内容。
     */

    /**
     1 TextureView 在4.0(API level 14)中引入。它可以将内容流直接投影到View中，可以用于实现Live preview等功能。
     和SurfaceView不同，它 不会  在WMS中单独创建窗口，而是作为View hierachy中的一个普通View，
     因此可以和其它普通View一样进行移动，旋转，缩放，动画等变化。
     值得注意的是TextureView必须在  硬件加速 的窗口中。它显示的内容流数据可以来自App进程或是远端进程。

     从类图中可以看到，TextureView继承自View，
     它与其它的View一样在View hierachy中管理与绘制。
     TextureView重载了draw()方法，其中主要把SurfaceTexture中收到的图像数据作为纹理更新到对应的HardwareLayer中。
     SurfaceTexture.OnFrameAvailableListener用于通知TextureView内容流有新图像到来。
     SurfaceTextureListener接口用于让TextureView的使用者知道SurfaceTexture已准备好，
     这样就可以把SurfaceTexture交给相应的内容源。Surface为BufferQueue的Producer接口实现类，使生产者可以通过它的软件或硬件渲染接口为SurfaceTexture内部的BufferQueue提供graphic buffer。


     使用
     TextureView的使用非常简单，你唯一要做的就是获取用于渲染内容的SurfaceTexture。
     具体做法是先创建TextureView对象，然后实现SurfaceTextureListener接口，代码如下：

     */

}
