package com.DuAn1.techstore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.DuAn1.techstore.Activity.Activity_ChiTietHoaDon;
import com.DuAn1.techstore.Model.HoaDon;
import com.DuAn1.techstore.R;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter_HoaDon extends RecyclerView.Adapter<Adapter_HoaDon.ViewHolder> {
    private Context context;
    private ArrayList<HoaDon> lstHD;
    static DecimalFormat format = new DecimalFormat("###,###,###");

    public Adapter_HoaDon(Context context, ArrayList<HoaDon> lstHD) {
        this.context = context;
        this.lstHD = lstHD;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_hoadon, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HoaDon hoaDon = lstHD.get(position);
        if (hoaDon != null) {
            holder.tvMaHD.setText("Mã hóa đơn: " + hoaDon.getMaHoaDon());
            holder.tvTongTien.setText("Tổng tiền: " + format.format(hoaDon.getTongTien()) + "đ");
            holder.tvNgayBan.setText(hoaDon.getNgayBan());
            holder.cardView.setOnClickListener(view -> {
                Intent intent = new Intent(context, Activity_ChiTietHoaDon.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                Bundle bundle = new Bundle();
                bundle.putSerializable("hoaDon", hoaDon);
                intent.putExtras(bundle);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (lstHD != null) {
            return lstHD.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMaHD;
        private TextView tvTongTien;
        private TextView tvNgayBan;
        private CardView cardView;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMaHD = itemView.findViewById(R.id.tvMaHD);
            tvTongTien = itemView.findViewById(R.id.tvTongTien);
            cardView = itemView.findViewById(R.id.cardView);
            tvNgayBan = itemView.findViewById(R.id.tvNgayBan);
        }
    }

    public void fixMemoryLeak() {
        context = null;
    }
}
