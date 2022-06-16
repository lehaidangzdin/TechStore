package com.DuAn1.techstore.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
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
import java.util.Locale;

public class Adapter_SP extends RecyclerView.Adapter<Adapter_SP.ViewHolder> {
    private ArrayList<SanPham> lstSP;
    private final ArrayList<SanPham> lstSP_old;
    private Context context;

    public Adapter_SP(Context context, ArrayList<SanPham> lstSP) {
        this.context = context;
        this.lstSP = lstSP;
        this.lstSP_old = lstSP;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_sp, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final SanPham sanPham = lstSP.get(position);
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
                bundle.putInt("maSP", sanPham.getMaSanPham());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
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

        if (lstSP != null) {
            return lstSP.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        private final CardView cardView;
        private final ImageView imgSp;
        private final TextView tvTenSp;
        private final TextView tvGiaSp;
        private final TextView tvGiaCuSp;
        private final CheckBox checkBox;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = itemView.findViewById(R.id.cardView);
            imgSp = itemView.findViewById(R.id.imgSp);
            tvTenSp = itemView.findViewById(R.id.tvTenSp);
            tvGiaSp = itemView.findViewById(R.id.tvGiaSp);
            tvGiaCuSp = itemView.findViewById(R.id.tvGiaCuSp);
            checkBox = itemView.findViewById(R.id.chkLike);
        }
    }


    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String search = charSequence.toString();
                if (search.isEmpty()) {
                    lstSP = lstSP_old;
                } else {
                    ArrayList<SanPham> glist = new ArrayList<>();
                    for (SanPham sanPham : lstSP_old) {
                        if (sanPham.getTenSanPham().toLowerCase(Locale.ROOT).contains(search.toLowerCase(Locale.ROOT))) {
                            glist.add(sanPham);
                        }
                    }
                    lstSP = glist;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = lstSP;
                return filterResults;
            }

            @SuppressLint("NotifyDataSetChanged")
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                lstSP = (ArrayList<SanPham>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

}
