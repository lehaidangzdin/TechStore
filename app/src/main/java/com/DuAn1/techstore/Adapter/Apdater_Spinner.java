package com.DuAn1.techstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.DuAn1.techstore.R;

public class Apdater_Spinner extends ArrayAdapter {
    private Context context;
    private String[] list;
    private int[] listIcon;
    private ImageView tvIcon;
    private TextView tvChucNang;


    public Apdater_Spinner(@NonNull Context context, String[] list, int[] listIcon) {
        super(context, 0, list);
        this.context = context;
        this.list = list;
        this.listIcon = listIcon;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_spinner, null);
        }
        tvIcon = view.findViewById(R.id.tvIcon);
        tvChucNang = view.findViewById(R.id.tvChucNang);
        if (listIcon.length != 0 && listIcon.length != 0) {
            tvIcon.setImageResource(listIcon[position]);
            tvChucNang.setText(list[position]);
        }
        return view;
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.adapter_spinner_drop, null);
        }
        tvIcon = view.findViewById(R.id.tvIcon);
        tvChucNang = view.findViewById(R.id.tvChucNang);
        if (listIcon.length != 0 && listIcon.length != 0) {
            tvIcon.setImageResource(listIcon[position]);
            tvChucNang.setText(list[position]);
        }
        return view;
    }
}
