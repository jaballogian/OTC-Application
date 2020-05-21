package com.otc.application.item;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemVideoPembelajaran implements Parcelable {

    public String getSubBabVideo() {
        return subBabVideo;
    }

    public void setSubBabVideo(String subBabVideo) {
        this.subBabVideo = subBabVideo;
    }

    private String subBabVideo;

    public String getShowSubBabVideo() {
        return showSubBabVideo;
    }

    public void setShowSubBabVideo(String showSubBabVideo) {
        this.showSubBabVideo = showSubBabVideo;
    }

    private String showSubBabVideo;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.subBabVideo);
        dest.writeString(this.showSubBabVideo);
    }

    public ItemVideoPembelajaran(){

    }

    private ItemVideoPembelajaran(Parcel in){
        this.subBabVideo = in.readString();
        this.showSubBabVideo = in.readString();
    }

    public static final Parcelable.Creator<ItemVideoPembelajaran> CREATOR = new Parcelable.Creator<ItemVideoPembelajaran>() {
        @Override
        public ItemVideoPembelajaran createFromParcel(Parcel source) {
            return new ItemVideoPembelajaran(source);
        }
        @Override
        public ItemVideoPembelajaran[] newArray(int size) {
            return new ItemVideoPembelajaran[size];
        }
    };
}
