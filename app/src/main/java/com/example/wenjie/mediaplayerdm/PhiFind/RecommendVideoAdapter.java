package com.example.wenjie.mediaplayerdm.PhiFind;


import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.wenjie.mediaplayerdm.PhiFind.entry.RecommendVideoEntry;
import com.example.wenjie.mediaplayerdm.R;

import java.util.List;

public class RecommendVideoAdapter extends BaseQuickAdapter<RecommendVideoEntry.CardData, BaseViewHolder> {
    private static final String TAG = "RecommendVideoAdapter";

    public RecommendVideoAdapter(int layoutResId, @Nullable List<RecommendVideoEntry.CardData> data) {
        super(R.layout.item_video_recommend, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, RecommendVideoEntry.CardData cardData) {
        helper.setText(R.id.title, cardData.getTitle());
        helper.setText(R.id.tag, "# " + cardData.getTags());
        ImageView imageView = helper.getView(R.id.image_view);

        Glide.with(mContext)
                .load(cardData.getPictureUrl())
                .placeholder(R.drawable.discover_video_bg_loading_related)
                .into(imageView);
    }

}
