package com.DuAn1.techstore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.DuAn1.techstore.Activity.Activity_ChiTietSp;
import com.DuAn1.techstore.Model.SanPham;
import com.DuAn1.techstore.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class AdapterSP_Moi extends RecyclerView.Adapter<AdapterSP_Moi.ViewHolder> {

    private Context context;
    private final ArrayList<SanPham> list;

    public AdapterSP_Moi(Context context, ArrayList<SanPham> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_sp_banchay, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SanPham sanPham = list.get(position);
        if (sanPham != null) {
            Picasso.get().load(sanPham.getHinhAnh())
                    .placeholder(R.drawable.dongho)
                    .error(R.drawable.atvphone)
                    .into(holder.imgSp);
            holder.tvTenSp.setText(sanPham.getTenSanPham());
            DecimalFormat format = new DecimalFormat("###,###,###");
            holder.tvGiaSp.setText(format.format(sanPham.getGiaTien()) + "đ");
            holder.tvGiaCuSp.setText(format.format(sanPham.getGiaCu()) + "đ");
            holder.tvGiaCuSp.setPaintFlags(holder.tvGiaCuSp.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            holder.checkBox.setOnClickListener(view -> {
                if (holder.checkBox.isChecked()) {
                    Toast.makeText(context, "Đã thích!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Bỏ thích!", Toast.LENGTH_SHORT).show();
                }
            });
            holder.cardView.setOnClickListener(view -> {
                Intent intent = new Intent(context, Activity_ChiTietSp.class);
                Bundle bundle = new Bundle();
                bundle.putInt("maSP",sanPham.getMaSanPham());
                intent.putExtras(bundle);
                context.startActivity(intent);
            });
        }
    }

    public void fixMemoryLeak() {
        context = null;
    }

    @Override
    public int getItemCount() {
        if (list != null) {
            return list.size();
        }
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final ImageView imgSp;
        private final TextView tvTenSp;
        private final TextView tvGiaSp;
        private final TextView tvGiaCuSp;
        private final CheckBox checkBox;
        private final CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSp = itemView.findViewById(R.id.imgSp);
            tvTenSp = itemView.findViewById(R.id.tvTenSp);
            tvGiaSp = itemView.findViewById(R.id.tvGiaSp);
            tvGiaCuSp = itemView.findViewById(R.id.tvGiaCuSp);
            checkBox = itemView.findViewById(R.id.chkLike);
            cardView = itemView.findViewById(R.id.cardView);
        }
    }
}
