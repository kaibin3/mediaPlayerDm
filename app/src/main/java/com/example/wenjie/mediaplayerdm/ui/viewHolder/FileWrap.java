package com.example.wenjie.mediaplayerdm.ui.viewHolder;

import android.view.View;
import android.widget.TextView;

import com.example.wenjie.mediaplayerdm.R;


public class FileWrap {
	private View view =null;
	private TextView tv_fileName = null;
	

	public FileWrap(View view){
		this.view=view;
	}


	public TextView getTv_fileName() {
		if(tv_fileName==null){
			tv_fileName=(TextView) view.findViewById(R.id.tv_fileName);
		}
		return tv_fileName; 
	}


}
