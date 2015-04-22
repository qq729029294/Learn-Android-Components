package com.sky;
import com.sky.Vote; 

interface AIDLSkyService {
	void show(String showText);
	void insertVote(in Vote vote);
	void remoteAllVote();
	List<Vote> getAllVote();
	void setBitmap(in Bitmap bitmap);
	Bitmap getBitmap();
	Bitmap loadUrlBitmap(String strUrl);
}