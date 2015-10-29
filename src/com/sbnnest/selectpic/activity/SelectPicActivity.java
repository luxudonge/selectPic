package com.sbnnest.selectpic.activity;

import java.io.File;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.GridView;
import android.widget.PopupWindow.OnDismissListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.sbnnest.selectpic.R;
import com.sbnnest.selectpic.bean.ImageAgent;
import com.sbnnest.selectpic.bean.ImageAlbum;
import com.sbnnest.selectpic.bean.ImageFloder;
import com.sbnnest.selectpic.imageloader.ListImageDirPopupWindow;
import com.sbnnest.selectpic.imageloader.ListImageDirPopupWindow.OnImageDirSelected;
import com.sbnnest.selectpic.imageloader.MyAdapter;

/**
 *
 * 图片选择器
 *
 */
public class SelectPicActivity extends Activity implements OnImageDirSelected , OnItemClickListener
{
	
	
	
	private ProgressDialog mProgressDialog;

	/**
	 * 存储文件夹中的图片数量
	 */
	private int mPicsSize;
	/**
	 * 图片数量最多的文件夹
	 */
	private File mImgDir;
	/**
	 * 所有的图片
	 */
	private List<ImageAgent> mImgs = new ArrayList<ImageAgent>();

	private GridView mGirdView;
	private MyAdapter mAdapter;
	
	/**
	 * 临时的辅助类，用于防止同一个文件夹的多次扫描
	 */
	private HashSet<String> mDirPaths = new HashSet<String>();

	/**
	 * 扫描拿到所有的图片文件夹
	 */
	private List<ImageFloder> mImageFloders = new ArrayList<ImageFloder>();

	private RelativeLayout mBottomLy;

	private TextView mChooseDir;
	private TextView mImageCount;
	int totalCount = 0;
	
	private int mScreenHeight;

	private ListImageDirPopupWindow mListImageDirPopupWindow;

	private Handler mHandler = new Handler()
	{
		public void handleMessage(android.os.Message msg)
		{
			mProgressDialog.dismiss();
			// 为View绑定数据
			data2View();
			// 初始化展示文件夹的popupWindw
			initListDirPopupWindw();
		}
	};

	/**
	 * 为View绑定数据
	 */
	private void data2View()
	{
		if (mImgDir == null)
		{
			Toast.makeText(getApplicationContext(), "擦，一张图片没扫描到",
					Toast.LENGTH_SHORT).show();
			return;
		}
		mImgs.clear();
		for(String path : mImgDir.list()){
			ImageAgent imageAgent = new ImageAgent();
			imageAgent.setOriginalImagePath(mImgDir + "/" +path);
			mImgs.add(imageAgent);
		}
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new MyAdapter(this, mImgs,
				R.layout.grid_item, mImgDir.getAbsolutePath());
		mGirdView.setAdapter(mAdapter);
		mImageCount.setText(totalCount + "张");
	};

	/**
	 * 初始化展示文件夹的popupWindw
	 */
	private void initListDirPopupWindw()
	{
		mListImageDirPopupWindow = new ListImageDirPopupWindow(
				LayoutParams.MATCH_PARENT, (int) (mScreenHeight * 0.7),
				mImageFloders, LayoutInflater.from(getApplicationContext())
						.inflate(R.layout.list_dir, null));

		mListImageDirPopupWindow.setOnDismissListener(new OnDismissListener()
		{

			@Override
			public void onDismiss()
			{
				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = 1.0f;
				getWindow().setAttributes(lp);
			}
		});
		// 设置选择文件夹的回调
		mListImageDirPopupWindow.setOnImageDirSelected(this);
	}

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		
		DisplayMetrics outMetrics = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(outMetrics);
		mScreenHeight = outMetrics.heightPixels;
		mGirdView = (GridView) findViewById(R.id.id_gridView);
		mGirdView.setOnItemClickListener(this);
		mChooseDir = (TextView) findViewById(R.id.id_choose_dir);
		mImageCount = (TextView) findViewById(R.id.id_total_count);
		mBottomLy = (RelativeLayout) findViewById(R.id.id_bottom_ly);
		findViewById(R.id.back).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		mOkBtn = (Button)findViewById(R.id.ok_but);
		mOkBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
		});
		mBottomLy.setOnClickListener(new OnClickListener()
		{
			@Override
			public void onClick(View v)
			{
				mListImageDirPopupWindow
						.setAnimationStyle(R.style.anim_popup_dir);
				mListImageDirPopupWindow.showAsDropDown(mBottomLy, 0, 0);

				// 设置背景颜色变暗
				WindowManager.LayoutParams lp = getWindow().getAttributes();
				lp.alpha = .3f;
				getWindow().setAttributes(lp);
			}
		});
		getImages();
		update();

	}

	/**
	 * 利用ContentProvider扫描手机中的图片，此方法在运行在子线程中 完成图片的扫描，最终获得jpg最多的那个文件夹
	 */
	private void getImages()
	{
		if (!Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED))
		{
			Toast.makeText(this, "暂无外部存储", Toast.LENGTH_SHORT).show();
			return;
		}
		// 显示进度条
		mProgressDialog = ProgressDialog.show(this, null, "正在加载...");

		new Thread(new Runnable()
		{
			@Override
			public void run()
			{

				String firstImage = null;

				Uri mImageUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
				ContentResolver mContentResolver = SelectPicActivity.this
						.getContentResolver();

				// 只查询jpeg和png的图片
				Cursor mCursor = mContentResolver.query(mImageUri, null,
						MediaStore.Images.Media.MIME_TYPE + "=? or "
								+ MediaStore.Images.Media.MIME_TYPE + "=?",
						new String[] { "image/jpeg", "image/png" },
						MediaStore.Images.Media.DATE_MODIFIED);

				while (mCursor.moveToNext())
				{
					// 获取图片的路径
					String path = mCursor.getString(mCursor.getColumnIndex(MediaStore.Images.Media.DATA));

					Log.e("TAG", path);
					// 拿到第一张图片的路径
					if (firstImage == null)
						firstImage = path;
					// 获取该图片的父路径名
					File parentFile = new File(path).getParentFile();
					if (parentFile == null)
						continue;
					String dirPath = parentFile.getAbsolutePath();
					ImageFloder imageFloder = null;
					// 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
					if (mDirPaths.contains(dirPath))
					{
						continue;
					} else
					{
						mDirPaths.add(dirPath);
						// 初始化imageFloder
						imageFloder = new ImageFloder();
						imageFloder.setDir(dirPath);
						imageFloder.setFirstImagePath(path);
					}

					int picSize = parentFile.list(new FilenameFilter()
					{
						@Override
						public boolean accept(File dir, String filename)
						{
							return isImage(filename);
						}
					}).length;
					totalCount += picSize;

					imageFloder.setCount(picSize);
					mImageFloders.add(imageFloder);

					if (picSize > mPicsSize)
					{
						mPicsSize = picSize;
						mImgDir = parentFile;
					}
				}
				mCursor.close();

				// 扫描完成，辅助的HashSet也就可以释放内存了
				mDirPaths = null;

				// 通知Handler扫描图片完成
				mHandler.sendEmptyMessage(0x110);

			}
		}).start();

	}

	private Button mOkBtn;
	
	
	public void update(){
		mOkBtn.setText("完成（"+MyAdapter.mSelectedImage.size()+"）");
	}


	private boolean isImage(String filename){
		if(filename.toLowerCase().endsWith(".jpg") || filename.toLowerCase().endsWith(".png")
		|| filename.toLowerCase().endsWith(".jpeg") || filename.toLowerCase().endsWith("gif")){
			return true;
		}
		return false;
	}
	
	
	@Override
	public void selected(ImageFloder floder)
	{

		mImgDir = new File(floder.getDir());
		
		FilenameFilter file = new FilenameFilter() {
			
			@Override
			public boolean accept(File dir, String filename) {
				// TODO Auto-generated method stub
				return isImage(filename);
			}
		};
		mImgs.clear();
		for(String path : mImgDir.list(file)){
			ImageAgent imageAgent = new ImageAgent();
			imageAgent.setOriginalImagePath(mImgDir + "/" +path);
			mImgs.add(imageAgent);
		}
		
		/**
		 * 可以看到文件夹的路径和图片的路径分开保存，极大的减少了内存的消耗；
		 */
		mAdapter = new MyAdapter(this, mImgs,
				R.layout.grid_item, mImgDir.getAbsolutePath());
		mGirdView.setAdapter(mAdapter);
		// mAdapter.notifyDataSetChanged();
		mImageCount.setText(floder.getCount() + "张");
		mChooseDir.setText(floder.getName());
		mListImageDirPopupWindow.dismiss();

	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// TODO Auto-generated method stub
		super.onActivityResult(requestCode, resultCode, data);
		update();
		mAdapter.notifyDataSetChanged();
	}
	
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		// TODO Auto-generated method stub
		ImageAlbum photoAlbum = new ImageAlbum();
		photoAlbum.setPostion(position);
		for(ImageAgent pa :mImgs){
			photoAlbum.getPhotoList().add(pa);
		}
		PreviewPhotoActivity.startActivity(this, photoAlbum);
	}

}
