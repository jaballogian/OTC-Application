package com.otc.application.activity.ui.notifications;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.otc.application.R;

//import com.otc.application.activity.R;

public class NotifikasiFragment extends Fragment {

    private NotifikasiViewModel notifikasiViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        notifikasiViewModel =
                ViewModelProviders.of(this).get(NotifikasiViewModel.class);
        View root = inflater.inflate(R.layout.fragment_notifikasi, container, false);
        final TextView textView = root.findViewById(R.id.text_notifications);
        notifikasiViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
