package com.king.photo.zoom;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sbnnest.selectpic.bean.ImageAgent;

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
	
	private ImageAgent mImageAgent;
	private PhotoView mPhoto;
	private ProgressBar mBar;
	
	public void setImageAgent(ImageAgent imageAgent){
		mImageAgent = imageAgent;
	}
	
	public ImageAgent getImageAgent(){
		return mImageAgent;
	}
	
	private void init(){
		mPhoto = new PhotoView(getContext());
		mPhoto.setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT));
		addView(mPhoto);

		mBar = new ProgressBar(getContext());
		mBar.setLayoutParams(new LayoutParams(RelativeLayout.LayoutParams.MATCH_PARENT, RelativeLayout.LayoutParams.MATCH_PARENT) );
		addView(mBar);
	}
	
	
	DisplayImageOptions options = new DisplayImageOptions.Builder()
    // .showImageOnLoading(R.drawable.ic_stub) // resource or drawable
    // .showImageForEmptyUri(R.drawable.ic_empty) // resource or drawable
    // .showImageOnFail(R.drawable.ic_error) // resource or drawable
		// .preProcessor(...)
     .resetViewBeforeLoading(true)  // default
     .delayBeforeLoading(0)
     .cacheInMemory(true) // default
     .cacheOnDisk(true) // default
    // .postProcessor(...)
    // .extraForDownloader(...)
     
     .considerExifParams(false) // default
     .imageScaleType(ImageScaleType.IN_SAMPLE_INT) // default
     .bitmapConfig(Bitmap.Config.RGB_565) // default
   //  .decodingOptions(...)
     .displayer(new SimpleBitmapDisplayer()) // default
     .handler(new Handler()) // default
     .build();


	boolean isLoading = false;
	public void startLoading(){
		if(isLoading){
			return;
		}
		isLoading = true;
		
		ImageLoader.getInstance().displayImage("file://"+mImageAgent.getOriginalImagePath(), mPhoto,options,
				//	BitmapC.image,
							new ImageLoadingListener() {
				
				@Override
				public void onLoadingStarted(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void onLoadingComplete(String arg0, View arg1, Bitmap arg2) {
					// TODO Auto-generated method stub
					mBar.setVisibility(View.GONE);
				}
				
				@Override
				public void onLoadingCancelled(String arg0, View arg1) {
					// TODO Auto-generated method stub
					
				}
			});

	}
	
	public PhotoView getPhotoView(){
		return mPhoto;
	}
	
	
	public ProgressBar getProgressBar(){
		return mBar;
	}

}
