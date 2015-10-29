package com.sbnnest.selectpic.bean;

import java.io.Serializable;

/**
 *
 * 图片代理类
 *
 */
public class ImageAgent implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 5147966287764314506L;
	private String thumbPath;
	private String originalImagePath;

	public String getThumbPath() {
		return thumbPath;
	}

	public void setThumbPath(String thumbPath) {
		this.thumbPath = thumbPath;
	}

	public String getOriginalImagePath() {
		return originalImagePath;
	}

	public void setOriginalImagePath(String originalImagePath) {
		this.originalImagePath = originalImagePath;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		if (!(obj instanceof ImageAgent)) {
			return false;
		}
		ImageAgent myClass = (ImageAgent) obj;
		if(myClass.originalImagePath == null){
			return false;
		}
		return myClass.originalImagePath.equals(originalImagePath);
	}
}
