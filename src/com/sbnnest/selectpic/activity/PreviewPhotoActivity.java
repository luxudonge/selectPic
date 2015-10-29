package com.sbnnest.selectpic.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.king.photo.zoom.PhotoCanvas;
import com.king.photo.zoom.ViewPagerFixed;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.display.SimpleBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.sbnnest.selectpic.R;
import com.sbnnest.selectpic.bean.ImageAgent;
import com.sbnnest.selectpic.bean.ImageAlbum;
import com.sbnnest.selectpic.imageloader.MyAdapter;

/**
 *
 * 预览图片
 *
 */
public class PreviewPhotoActivity extends Activity implements OnCheckedChangeListener {
	
	
	public static void startActivity(Activity activity,ImageAlbum photoAlbum){
		Intent intent = new Intent();
		intent.setClass(activity, PreviewPhotoActivity.class);
		intent.putExtra(DYNAMIC, photoAlbum);
		activity.startActivityForResult(intent, 0);
	}
	
	private static String DYNAMIC = "DynamicData"; 
	
	private ImageAlbum mPhotoAlbum;
	private Intent intent;
	//顶部显示预览图片位置的textview
	private TextView positionTextView;
	//获取前一个activity传过来的position
	//当前的位置
	private int location = 0;
	
	private ArrayList<View> listViews = null;
	private ViewPagerFixed pager;
	private MyPageAdapter adapter;

	public List<String> drr = new ArrayList<String>();
	public List<String> del = new ArrayList<String>();
	
	private CheckBox mPicSelect;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_preview_photo);// 切屏到主界面
		intent = getIntent();
		// 为发送按钮设置文字
		pager = (ViewPagerFixed) findViewById(R.id.gallery01);
		pager.setOnPageChangeListener(pageChangeListener);
		mPhotoAlbum = (ImageAlbum)intent.getSerializableExtra(DYNAMIC);
		List<ImageAgent> pa = mPhotoAlbum.getPhotoList();
		for (int i = 0; i < pa.size(); i++) {
			initListViews( pa.get(i));
		}
		mPicSelect = (CheckBox)findViewById(R.id.pic_select);
		adapter = new MyPageAdapter(listViews);
		pager.setAdapter(adapter);
		pager.setCurrentItem(mPhotoAlbum.getPostion());
		location = mPhotoAlbum.getPostion();
	}
	
	private OnPageChangeListener pageChangeListener = new OnPageChangeListener() {

		public void onPageSelected(int arg0) {
			location = arg0;
			ImageAgent photo = mPhotoAlbum.getPhotoList().get(location);
			mPicSelect.setOnCheckedChangeListener(null);
			if(MyAdapter.mSelectedImage.contains( photo)){
				mPicSelect.setChecked(true);
			}else{
				mPicSelect.setChecked(false);
			}
			mPicSelect.setOnCheckedChangeListener(PreviewPhotoActivity.this);
		}

		public void onPageScrolled(int arg0, float arg1, int arg2) {

		}

		public void onPageScrollStateChanged(int arg0) {

		}
	};
	
	
	private void initListViews(ImageAgent bm) {
		if (listViews == null)
			listViews = new ArrayList<View>();
		PhotoCanvas canvas = new PhotoCanvas(this);
		ImageView image = canvas.getPhotoView();
		final ProgressBar bar = canvas.getProgressBar();
		//img.setBackgroundResource(R.drawable.ic_launcher);
		image.setImageResource(R.drawable.ic_launcher);
		//img.setImageBitmap(ImageLoader.getInstance().loadImageSync(CommUtil.getImagePath(bm.getThumbPath())));
		//img.setBackground(new BitmapDrawable(ImageLoader.getInstance().loadImageSync(CommUtil.getImagePath(bm.getThumbPath()))));;
		image.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,
				LayoutParams.MATCH_PARENT));
		
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

		ImageLoader.getInstance().displayImage("file://"+bm.getOriginalImagePath(), image,options,
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
				bar.setVisibility(View.GONE);
			}
			
			@Override
			public void onLoadingCancelled(String arg0, View arg1) {
				// TODO Auto-generated method stub
				
			}
		});
		listViews.add(canvas);
	}
	
	
	class MyPageAdapter extends PagerAdapter {

		private ArrayList<View> listViews;

		private int size;
		public MyPageAdapter(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public void setListViews(ArrayList<View> listViews) {
			this.listViews = listViews;
			size = listViews == null ? 0 : listViews.size();
		}

		public int getCount() {
			return size;
		}

		public int getItemPosition(Object object) {
			return POSITION_NONE;
		}

		public void destroyItem(View arg0, int arg1, Object arg2) {
			((ViewPagerFixed) arg0).removeView(listViews.get(arg1 % size));
		}

		public void finishUpdate(View arg0) {
		}

		public Object instantiateItem(View arg0, int arg1) {
			try {
				((ViewPagerFixed) arg0).addView(listViews.get(arg1 % size), 0);

			} catch (Exception e) {
			}
			return listViews.get(arg1 % size);
		}

		public boolean isViewFromObject(View arg0, Object arg1) {
			return arg0 == arg1;
		}

	}
	
	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		super.onPause();
		//ImageLoader.getInstance().clearMemoryCache();
	}

	
	@Override
	public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
		// TODO Auto-generated method stub
		ImageAgent imageAgent = mPhotoAlbum.getPhotoList().get(location);
		if(isChecked){
			MyAdapter.mSelectedImage.add(imageAgent);
		}else{
			MyAdapter.mSelectedImage.remove(imageAgent);
		}
		
		int size = MyAdapter.mSelectedImage.size();
		Log.e("Preview", "size:"+size);
	}

}
