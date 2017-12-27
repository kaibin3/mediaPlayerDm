package com.example.wenjie.mediaplayerdm.ui.viewHolder;


import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.wenjie.mediaplayerdm.R;


public class ViewWrap {
	private View view =null;
	private TextView tv_name = null;
	private TextView tv_size = null;
	private TextView tv_time = null;
	private ImageView iv_show=null;
	private ImageView iv_isplay=null;

	public ViewWrap(View view){
		this.view=view;
	}


	/**
	 * ��ȡtv_name
	 * @return
	 */
	public TextView getTv_name() {
		if(tv_name==null){
			tv_name = (TextView) view.findViewById(R.id.tv_name);
		}
		return tv_name; 
	}


	/**
	 * ��ȡtv_size
	 * @return
	 */
	public TextView getTv_size() {
		if(tv_size==null){
			tv_size = (TextView) view.findViewById(R.id.tv_size);
		}
		return tv_size; 
	}

	
	/**
	 * ��ȡtv_time
	 * @return
	 */
	public TextView getTv_time() {
		if(tv_time==null){
			tv_time = (TextView) view.findViewById(R.id.tv_time);
		}
		return tv_time; 
	}


	/**
	 * ��ȡiv_show
	 * @return
	 */
	public ImageView getIv_show() {
		if(iv_show==null){
			iv_show = (ImageView) view.findViewById(R.id.iv_show);
		}
		return iv_show; 
	}


	/**
	 * ��ȡiv_isplay
	 * @return
	 */
	public ImageView getIv_isplay() {
		if(iv_isplay==null){
			iv_isplay = (ImageView) view.findViewById(R.id.iv_isplay);
		}
		return iv_isplay; 
	}


}
