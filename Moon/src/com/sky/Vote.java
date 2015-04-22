package com.sky;

import android.os.Parcel;
import android.os.Parcelable;

public class Vote extends Object implements Parcelable {
	
	public int voteId = -1;
	public String voteName = null;

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(voteId);
		dest.writeString(voteName);
	}
	
	public static final Parcelable.Creator<Vote> CREATOR = new Parcelable.Creator<Vote>() {

		@Override
		public Vote createFromParcel(Parcel source) {
			Vote vote = new Vote();
			vote.voteId = source.readInt();
			vote.voteName = source.readString();
			return vote;
		}

		@Override
		public Vote[] newArray(int size) {
			return new Vote[size];
		}
	};  

}
