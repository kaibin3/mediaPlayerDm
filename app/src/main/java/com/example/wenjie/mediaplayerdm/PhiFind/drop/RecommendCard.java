package com.example.wenjie.mediaplayerdm.PhiFind.drop;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenjie.mediaplayerdm.PhiFind.BaseCard;
import com.example.wenjie.mediaplayerdm.PhiFind.entry.FindCardInfo;
import com.example.wenjie.mediaplayerdm.R;

/**
 * Created by wen.jie on 2018/1/3.
 */

public class RecommendCard extends BaseCard {

    private ImageView imageView;
    private TextView textView1;
    private TextView textView2;

    private FindCardInfo mCardInfo;

    public RecommendCard(@NonNull Context context) {
        super(context);
        init();
    }

    public RecommendCard(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    private void init() {
        View.inflate(getContext(), R.layout.item_recommend_card, this);
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        imageView = findViewById(R.id.previous_img);
        textView1 = findViewById(R.id.description_text);
        textView2 = findViewById(R.id.label_text);
    }

    public void upDateCard(FindCardInfo findCardInfo){
        mCardInfo = findCardInfo;
        bindView(findCardInfo);
    }

    private void bindView(FindCardInfo findCardInfo) {

    }


}
