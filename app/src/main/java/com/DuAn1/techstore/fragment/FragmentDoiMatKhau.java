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

public class FragmentDoiMatKhau extends Fragment {
    private ImageView img_change_pass_back_chang_user;
    public FragmentDoiMatKhau () {
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_doi_mat_khau,null,false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnhXa(view);
        img_change_pass_back_chang_user.setOnClickListener(v -> ((ManChinhActivity) getActivity()).popBackFragments());
    }

    public void AnhXa(View view) {
        img_change_pass_back_chang_user = view.findViewById(R.id.img_change_pass_back_change_user);
    }
}
