package com.sky;

import java.io.ByteArrayOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Parcel;
import android.os.Parcelable;

public class Image extends Object implements Parcelable {
	public Bitmap bitmap = null;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		byte[] bytes = null;
		if (null != bitmap) {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();     
			bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
			bytes = baos.toByteArray();
		}
	    
		dest.writeByteArray(bytes);
	}
	
	public static final Parcelable.Creator<Image> CREATOR = new Parcelable.Creator<Image>() {
		@SuppressWarnings("null")
		@Override
		public Image createFromParcel(Parcel source) {
			byte[] bytes = null;
			source.readByteArray(bytes);
			Image image = new Image();
			image.bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
			
			return image;
		}

		@Override
		public Image[] newArray(int size) {
			return new Image[size];
		}
	};  

}
