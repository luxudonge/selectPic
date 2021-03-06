package com.king.photo.util;


import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;
import android.os.Environment;

public class FileUtils {
	
	
	
	public static boolean checkDir(){
		File file = new File(SDPATH);
		boolean isOk = false;
		if(!file.exists()){
			isOk = file.mkdirs();
		}else{
			isOk = true;
		}
		return isOk;
	}
	
	public static File checkDir(String fileName){
		File destDir = new File(FileUtils.SDPATH);
		  if (!destDir.exists()) {
		   destDir.mkdirs();
		  }
		  
		  File file = new File(FileUtils.SDPATH + fileName);
			try {
				if(!file.exists()){
					file.createNewFile();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		return file;
	}
	
	
	
	public static String SDPATH = Environment.getExternalStorageDirectory()
			+ "/Photo_LJ/";

	public static String saveBitmap(Bitmap bm, String picName) {
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".png"); 
			if (f.exists()) {
				f.delete();
			}
			FileOutputStream out = new FileOutputStream(f);
			bm.compress(Bitmap.CompressFormat.PNG, 100, out);
			out.flush();
			out.close();
			return f.getPath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}
	
	

	
	public static String getPitmap( String picName){
		try {
			if (!isFileExist("")) {
				File tempf = createSDDir("");
			}
			File f = new File(SDPATH, picName + ".png"); 
			if (f.exists()) {
				f.delete();
			}
			return f.getPath();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public static File createSDDir(String dirName) throws IOException {
		File dir = new File(SDPATH + dirName);
		if (Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {

			System.out.println("createSDDir:" + dir.getAbsolutePath());
			System.out.println("createSDDir:" + dir.mkdir());
		}
		return dir;
	}

	public static boolean isFileExist(String fileName) {
		File file = new File(SDPATH + fileName);
		file.isFile();
		return file.exists();
	}
	
	public static void delFile(String fileName){
		File file = new File(SDPATH + fileName);
		if(file.isFile()){
			file.delete();
        }
		file.exists();
	}

	public static void deleteDir() {
		File dir = new File(SDPATH);
		if (dir == null || !dir.exists() || !dir.isDirectory())
			return;
		
		for (File file : dir.listFiles()) {
			if (file.isFile())
				file.delete(); 
			else if (file.isDirectory())
				deleteDir(); 
		}
		dir.delete();
	}

	public static boolean fileIsExists(String path) {
		try {
			File f = new File(path);
			if (!f.exists()) {
				return false;
			}
		} catch (Exception e) {

			return false;
		}
		return true;
	}

}
