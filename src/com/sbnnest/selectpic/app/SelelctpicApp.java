package com.sbnnest.selectpic.app;

import android.app.Application;

import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.WeakMemoryCache;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;

public class SelelctpicApp extends Application{

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		
		ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(
				this)
		//.defaultDisplayImageOptions(options)
		//.memoryCacheExtraOptions(480, 800) // default = device screen dimensions
       // .taskExecutor()
       // .taskExecutorForCachedImages()
        .threadPoolSize(5) // default
        .threadPriority(Thread.NORM_PRIORITY - 2) // default
        .tasksProcessingOrder(QueueProcessingType.FIFO) // default
        //.diskCache(new UnlimitedDiscCache(cacheDir)) // default
        .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
        //.imageDownloader(new BaseImageDownloader(context)) // default
        //.imageDecoder(new BaseImageDecoder()) // default
       /* .writeDebugLogs()*/
        .memoryCache(new WeakMemoryCache())
        .build();
		ImageLoader.getInstance().init(config);
	}
}
