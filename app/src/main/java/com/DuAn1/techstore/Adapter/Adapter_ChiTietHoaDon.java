package com.DuAn1.techstore.Adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.DuAn1.techstore.Activity.Activity_ChiTietSp;
import com.DuAn1.techstore.Model.ChiTietHoaDon;
import com.DuAn1.techstore.Model.SanPham;
import com.DuAn1.techstore.R;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class Adapter_ChiTietHoaDon extends RecyclerView.Adapter<Adapter_ChiTietHoaDon.ViewHolder> {
    private Context context;
    private ArrayList<ChiTietHoaDon> lstCTHD;
    private ArrayList<SanPham> lstSP;
    private static DecimalFormat format = new DecimalFormat("###,###,###");


    public Adapter_ChiTietHoaDon(Context context, ArrayList<SanPham> lstSP, ArrayList<ChiTietHoaDon> lstCTHD) {
        this.context = context;
        this.lstSP = lstSP;
        this.lstCTHD = lstCTHD;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_chitiethoadon, null, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ChiTietHoaDon chiTietHoaDon = lstCTHD.get(position);
        final SanPham sanPham = lstSP.get(position);
        if (chiTietHoaDon != null && sanPham != null) {
            Picasso.get().load(sanPham.getHinhAnh())
                    .placeholder(R.drawable.dongho)
                    .error(R.drawable.atvphone)
                    .into(holder.imgSP);
            holder.tvTenSP.setText(sanPham.getTenSanPham());
            holder.tvDonGia.setText(format.format(chiTietHoaDon.getDonGia()) + "đ");
            holder.tvSoLuongMua.setText("Số lượng mua: " + chiTietHoaDon.getSoLuongMua());

            holder.imgSP.setOnClickListener(view -> {
                Intent intent = new Intent(context, Activity_ChiTietSp.class);
                Bundle bundle = new Bundle();
                bundle.putInt("maSP",sanPham.getMaSanPham());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                intent.putExtras(bundle);
                context.startActivity(intent);
            });
        }
    }

    @Override
    public int getItemCount() {
        if (lstCTHD != null && lstSP != null) {
            return lstCTHD.size();
        }
        return 0;
    }

    public void fixMemoryLeak() {
        context = null;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgSP;
        private TextView tvTenSP;
        private TextView tvDonGia;
        private TextView tvSoLuongMua;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imgSP = itemView.findViewById(R.id.imgSP);
            tvTenSP = itemView.findViewById(R.id.tvTenSP);
            tvDonGia = itemView.findViewById(R.id.tvDonGia);
            tvSoLuongMua = itemView.findViewById(R.id.tvSoLuongMua);
        }
    }
}
