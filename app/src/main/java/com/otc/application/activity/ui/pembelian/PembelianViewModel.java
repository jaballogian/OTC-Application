package com.otc.application.activity.ui.pembelian;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PembelianViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PembelianViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is dashboard fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}