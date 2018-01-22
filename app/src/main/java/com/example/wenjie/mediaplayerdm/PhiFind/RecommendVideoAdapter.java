package com.example.wenjie.mediaplayerdm.PhiFind;


import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wenjie.mediaplayerdm.PhiFind.entry.VideoCardInfo;
import com.example.wenjie.mediaplayerdm.R;

import java.util.List;

public class RecommendVideoAdapter extends BaseQuickAdapter<VideoCardInfo, BaseViewHolder> {
    private static final String TAG = "RecommendAdapter";

    public RecommendVideoAdapter(int layoutResId, @Nullable List<VideoCardInfo> data) {
        super(R.layout.item_video_recommend, data);
        Log.d(TAG, "RecommendAdapterNew: ");
    }


    @Override
    protected void convert(BaseViewHolder helper, VideoCardInfo findCardInfo) {
        Log.d(TAG, "bindView: " + findCardInfo);
        helper.setText(R.id.description_text, findCardInfo.getDescription());
        helper.setText(R.id.label_text, findCardInfo.getLabel());
        ImageView imageView = helper.getView(R.id.previous_img);

        Glide.with(mContext).load(findCardInfo.getPhotoUrl()).into(imageView);

    }

}
