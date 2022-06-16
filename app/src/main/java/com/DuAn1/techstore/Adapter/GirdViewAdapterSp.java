package com.DuAn1.techstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.DuAn1.techstore.R;

public class GirdViewAdapterSp extends BaseAdapter {

    private final Context context;
    private final String[] TenLogo;
    private final int[] HinhLogo;

    public GirdViewAdapterSp(Context context, String[] tenLogo, int[] hinhLogo) {
        this.context = context;
        TenLogo = tenLogo;
        HinhLogo = hinhLogo;
    }


    @Override
    public int getCount() {
        return TenLogo.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        convertView = layoutInflater.inflate(R.layout.adapter_grid_theloai, null);
        TextView textView = convertView.findViewById(R.id.textView);
        ImageView imageView = convertView.findViewById(R.id.imageView);
        textView.setText(TenLogo[position]);
        imageView.setImageResource(HinhLogo[position]);
        return convertView;
    }
}
