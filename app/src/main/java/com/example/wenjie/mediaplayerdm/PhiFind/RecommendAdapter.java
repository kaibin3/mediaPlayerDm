package com.example.wenjie.mediaplayerdm.PhiFind;


import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.wenjie.mediaplayerdm.PhiFind.entry.FindCardInfo;
import com.example.wenjie.mediaplayerdm.R;

import java.util.ArrayList;
import java.util.List;

public class RecommendAdapter extends BaseAdapter {
    private static final String TAG = "RecommendAdapter";
    private Context context;
    private List<FindCardInfo> mCards = new ArrayList<>();
    private LayoutInflater mLayoutInflater;

    public RecommendAdapter(Context context) {
        this.context = context;
        mLayoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<FindCardInfo> cards) {
        if (null != cards) {
            mCards.clear();
            mCards.addAll(cards);
            notifyDataSetChanged();
            Log.d(TAG, "setData: " + cards.size());
        }
    }

    @Override
    public int getCount() {
        Log.d(TAG, "getCount: " + mCards.size());
        return mCards.size();
    }

    @Override
    public FindCardInfo getItem(int position) {
        return mCards.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (null == convertView) {
            convertView = mLayoutInflater.inflate(R.layout.item_recommend_card, parent, false);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final FindCardInfo findCardInfo = mCards.get(position);
        bindView(findCardInfo, viewHolder);
        return convertView;
    }

    private void bindView(FindCardInfo findCardInfo, ViewHolder holder) {
        Log.d(TAG, "bindView: " + findCardInfo);
        if (!TextUtils.isEmpty(findCardInfo.getDescription())) {
            holder.textView1.setText(findCardInfo.getDescription());
        }

        if (!TextUtils.isEmpty(findCardInfo.getLabel())) {
            holder.textView2.setText(findCardInfo.getLabel());
        }

        if (!TextUtils.isEmpty(findCardInfo.getPhotoUrl())) {
            Glide.with(context).load(findCardInfo.getPhotoUrl()).into(holder.imageView);
        }
    }


    public class ViewHolder {
        View itemView;
        ImageView imageView;
        TextView textView1;
        TextView textView2;

        public ViewHolder(View view) {
            itemView = view;
            imageView = view.findViewById(R.id.previous_img);
            textView1 = view.findViewById(R.id.description_text);
            textView2 = view.findViewById(R.id.label_text);
        }
    }

}
