package com.sbnnest.selectpic.bean;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


/**
 *
 * 图片相册
 *
 */
public class ImageAlbum implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 8612681963356956925L;
	private int postion;
	private List<ImageAgent> photoList;
	
	public ImageAlbum(){
		photoList = new ArrayList<ImageAgent>();
	}

	public int getPostion() {
		return postion;
	}

	public void setPostion(int mPostion) {
		this.postion = mPostion;
	}

	public List<ImageAgent> getPhotoList() {
		return photoList;
	}

	public void setPhotoList(List<ImageAgent> mPhotoList) {
		this.photoList = mPhotoList;
	}
	

}
