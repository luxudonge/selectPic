package com.king.photo.zoom;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

public class PhotoCanvas extends RelativeLayout{

	public PhotoCanvas(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
		init();
	}
	
	public PhotoCanvas(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
		init();
	}


	public PhotoCanvas(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		// TODO Auto-generated constructor stub
		init();
	}
	
	private PhotoView mPhoto;
	private ProgressBar mBar;
	
	private void init(){
		mPhoto = new PhotoView(getContext());
		mPhoto.setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		addView(mPhoto);

		mBar = new ProgressBar(getContext());
		mBar.setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT) );
		addView(mBar);
	}
	
	public PhotoView getPhotoView(){
		return mPhoto;
	}
	
	
	public ProgressBar getProgressBar(){
		return mBar;
	}

}
