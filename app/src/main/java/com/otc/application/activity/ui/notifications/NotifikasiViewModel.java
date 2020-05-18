package com.otc.application.activity.ui.notifications;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NotifikasiViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NotifikasiViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("notifikasi");
    }

    public LiveData<String> getText() {
        return mText;
    }
}