package com.otc.application.activity.ui.pembelian;

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

public class PembelianFragment extends Fragment {

    private PembelianViewModel pembelianViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pembelianViewModel =
                ViewModelProviders.of(this).get(PembelianViewModel.class);
        View root = inflater.inflate(R.layout.fragment_pembelian, container, false);
        final TextView textView = root.findViewById(R.id.text_dashboard);
        pembelianViewModel.getText().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}
