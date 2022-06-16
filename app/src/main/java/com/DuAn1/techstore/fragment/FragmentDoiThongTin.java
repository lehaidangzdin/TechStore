package com.DuAn1.techstore.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.DuAn1.techstore.Activity.ManChinhActivity;
import com.DuAn1.techstore.R;

public class FragmentDoiThongTin extends Fragment {
    private ImageView img_change_info_back_change_user;
    public FragmentDoiThongTin () {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_doi_thong_tin,null,false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnhXa(view);
        img_change_info_back_change_user.setOnClickListener(v -> ((ManChinhActivity) getActivity()).popBackFragments());
    }
    public void AnhXa(View view) {
        img_change_info_back_change_user = view.findViewById(R.id.img_change_info_back_change_user);
    }
}
