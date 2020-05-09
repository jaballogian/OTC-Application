package com.otc.application.item;

import android.os.Parcel;
import android.os.Parcelable;

public class ItemProgram implements Parcelable {

    private String namaProgram;

    public String getNamaProgram() {
        return namaProgram;
    }

    public void setNamaProgram(String namaProgram) {
        this.namaProgram = namaProgram;
    }

    public String getGambarProgram() {
        return gambarProgram;
    }

    public void setGambarProgram(String gambarProgram) {
        this.gambarProgram = gambarProgram;
    }

    private String gambarProgram;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.namaProgram);
        dest.writeString(this.gambarProgram);
    }

    public ItemProgram(){

    }

    private ItemProgram(Parcel in){
        this.gambarProgram = in.readString();
        this.namaProgram = in.readString();
    }

    public static final Parcelable.Creator<ItemProgram> CREATOR = new Parcelable.Creator<ItemProgram>() {
        @Override
        public ItemProgram createFromParcel(Parcel source) {
            return new ItemProgram(source);
        }
        @Override
        public ItemProgram[] newArray(int size) {
            return new ItemProgram[size];
        }
    };
}
