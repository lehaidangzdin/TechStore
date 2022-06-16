package com.DuAn1.techstore.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.Toolbar;

import com.DuAn1.techstore.DAO.Server;
import com.DuAn1.techstore.Model.Loading;
import com.DuAn1.techstore.Model.SanPham;
import com.DuAn1.techstore.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;

public class Activity_ChiTietSp extends AppCompatActivity {
    private Toolbar toolbar;
    private ImageView imageView;
    private ImageView imgHetHang;
    private TextView tvTen;
    private TextView tvGia;
    private TextView tvGiaCu;
    private TextView tvThongTin;
    private AppCompatButton btnMuaNgay;
    private AppCompatButton btnThemGioHang;
    private SanPham sanPham;
    private int maKH;
    private int sl;
    private int tongTien;
    //
    private Loading loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_sp);
        Anhxa();
        ActionBar();
        xuLiDl();
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbar);
        imageView = findViewById(R.id.img);
        imgHetHang = findViewById(R.id.imgHetHang);
        imgHetHang.setVisibility(View.GONE);
        tvTen = findViewById(R.id.tvTen);
        tvGia = findViewById(R.id.tvGia);
        tvGiaCu = findViewById(R.id.tvGiaCu);
        tvThongTin = findViewById(R.id.tvThongTin);
        btnMuaNgay = findViewById(R.id.btnMuaNgay);
        btnThemGioHang = findViewById(R.id.btnThemGioHang);
        loading = new Loading(this);
        sanPham = new SanPham();
        loading.LoadingDialog();
    }


    private void ActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Chi tiết sản phẩm");
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

    }

    @SuppressLint("SetTextI18n")
    private void xuLiDl() {
        Bundle bundle = getIntent().getExtras();
        int _maSP = bundle.getInt("maSP");
        getDlSp(_maSP);

    }

    private void getDlSp(int _maSP) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.spTheoMaSP,
                response -> {
                    if (response.equals("failure")) {
                        Toast.makeText(getApplicationContext(), "Lỗi k thể get thông tin sản phẩm!", Toast.LENGTH_SHORT).show();
                    } else {
                        try {
                            JSONArray jsonArray = new JSONArray(response);
                            JSONObject jsonObject = jsonArray.getJSONObject(0);
                            sanPham = new SanPham();
                            sanPham.setMaSanPham(jsonObject.getInt("maSP"));
                            sanPham.setMaLoai(jsonObject.getInt("maLoai"));
                            sanPham.setTenSanPham(jsonObject.getString("tenSP"));
                            sanPham.setSoLuongNhap(jsonObject.getInt("soLuongNhap"));
                            sanPham.setHinhAnh(jsonObject.getString("hinhAnh"));
                            sanPham.setGiaTien(jsonObject.getInt("giaTien"));
                            sanPham.setGiaCu(jsonObject.getInt("giaCu"));
                            sanPham.setNgayNhap(jsonObject.getString("ngayNhap"));
                            sanPham.setThongTinSanPham(jsonObject.getString("thongTinSP"));
                            setData(sanPham);
                            loading.DimissDialog();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                },
                error -> Toast.makeText(getApplicationContext(), "Sever Err! " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("maSP", String.valueOf(_maSP));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }

    private void setData(SanPham sanPham) {
        if (sanPham.getSoLuongNhap() > 1) {
            imgHetHang.setVisibility(View.GONE);
            Picasso.get().load(sanPham.getHinhAnh())
                    .placeholder(R.drawable.dongho)
                    .error(R.drawable.atvphone)
                    .into(imageView);
            tvTen.setText(sanPham.getTenSanPham());
            DecimalFormat format = new DecimalFormat("###,###,###");
            tvGia.setText(format.format(sanPham.getGiaTien()) + "đ");
            tvGiaCu.setText(format.format(sanPham.getGiaCu()) + "đ");
            tvGiaCu.setPaintFlags(tvGiaCu.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvThongTin.setText(sanPham.getThongTinSanPham());
            btnMuaNgay.setOnClickListener(view -> MuaNgay());
            maKH = getThongTinKH();
            btnThemGioHang.setOnClickListener(view -> ThemGioHang("Thêm vào giỏ hàng", maKH));
        } else {
            imgHetHang.setVisibility(View.VISIBLE);
            Picasso.get().load(sanPham.getHinhAnh())
                    .placeholder(R.drawable.dongho)
                    .error(R.drawable.atvphone)
                    .into(imageView);
            tvTen.setText(sanPham.getTenSanPham());
            DecimalFormat format = new DecimalFormat("###,###,###");
            tvGia.setText(format.format(sanPham.getGiaTien()) + "đ");
            tvGiaCu.setText(format.format(sanPham.getGiaCu()) + "đ");
            tvGiaCu.setPaintFlags(tvGiaCu.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            tvThongTin.setText(sanPham.getThongTinSanPham());
            btnMuaNgay.setOnClickListener(view -> DialogHetHang());
            btnThemGioHang.setOnClickListener(view -> DialogHetHang());
        }
    }

    private int getThongTinKH() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Luu_dangNhap", Context.MODE_PRIVATE);
        maKH = preferences.getInt("maKH", 0);
        return maKH;
    }

    private void DialogHetHang() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_ChiTietSp.this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_hethang, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Button btnHetHang;
        btnHetHang = view.findViewById(R.id.btnHetHang);
        btnHetHang.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            Activity_ChiTietSp.super.onBackPressed();
        });
        alertDialog.show();

    }


    private void MuaNgay() {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_mua_, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();


        ImageView imgDialogSp;
        TextView tvGia;
        TextView tvDialogSoLuong;
        AppCompatButton btnDialogGiam;
        TextView tvDialogSoLuongMua;
        AppCompatButton btnDialogTang;
        AppCompatButton btnDialogMua;

        imgDialogSp = view.findViewById(R.id.img_dialog_sp);
        tvGia = view.findViewById(R.id.tvGia);
        tvDialogSoLuong = view.findViewById(R.id.tv_dialog_soLuong);
        btnDialogGiam = view.findViewById(R.id.btn_dialog_Giam);
        tvDialogSoLuongMua = view.findViewById(R.id.tv_dialog_SoLuongMua);
        btnDialogTang = view.findViewById(R.id.btn_dialog_Tang);
        btnDialogMua = view.findViewById(R.id.btn_dialog_Mua);

        Picasso.get().load(sanPham.getHinhAnh())
                .placeholder(R.drawable.dongho)
                .error(R.drawable.atvphone)
                .into(imgDialogSp);

        tvDialogSoLuong.setText("Kho: " + sanPham.getSoLuongNhap());

        tvDialogSoLuongMua.setText(String.valueOf(1));
        btnDialogGiam.setOnClickListener(view1 -> {
            if (Integer.parseInt(tvDialogSoLuongMua.getText().toString()) > 1) {
                tvDialogSoLuongMua.setText(String.valueOf(Integer.parseInt(tvDialogSoLuongMua.getText().toString()) - 1));
                btnDialogGiam.setEnabled(true);
            } else {
                btnDialogGiam.setEnabled(false);
            }
            btnDialogTang.setEnabled(true);
        });
        btnDialogTang.setOnClickListener(view1 -> {
            if (Integer.parseInt(tvDialogSoLuongMua.getText().toString()) < 10 && Integer.parseInt(tvDialogSoLuongMua.getText().toString()) < sanPham.getSoLuongNhap()) {
                tvDialogSoLuongMua.setText(String.valueOf(Integer.parseInt(tvDialogSoLuongMua.getText().toString()) + 1));
                btnDialogTang.setEnabled(true);
            } else {
                btnDialogTang.setEnabled(false);
            }

            btnDialogGiam.setEnabled(true);
        });
        DecimalFormat format = new DecimalFormat("###,###,###");
        tvGia.setText(format.format(sanPham.getGiaTien()) + " đ");

        btnDialogMua.setText("Đặt hàng");
        btnDialogMua.setOnClickListener(view13 -> {
            bottomSheetDialog.dismiss();
            Intent intent = new Intent(Activity_ChiTietSp.this, Activity_ThanhToan.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("sanPham", sanPham);
            sl = Integer.parseInt(tvDialogSoLuongMua.getText().toString());
            bundle.putInt("slMua", sl);
            tongTien = sanPham.getGiaTien() * sl;
            bundle.putInt("sl", sl);
            bundle.putInt("tongTien", tongTien);
            intent.putExtras(bundle);
            startActivity(intent);
        });

    }

    private void ThemGioHang(String textBtn, int maKH) {
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_mua_, null);
        BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(this, R.style.BottomSheetDialog);
        bottomSheetDialog.setContentView(view);
        bottomSheetDialog.show();


        ImageView imgDialogSp;
        TextView tvGia;
        TextView tvDialogSoLuong;
        AppCompatButton btnDialogGiam;
        TextView tvDialogSoLuongMua;
        AppCompatButton btnDialogTang;
        AppCompatButton btnDialogMua;

        imgDialogSp = view.findViewById(R.id.img_dialog_sp);
        tvGia = view.findViewById(R.id.tvGia);
        tvDialogSoLuong = view.findViewById(R.id.tv_dialog_soLuong);
        btnDialogGiam = view.findViewById(R.id.btn_dialog_Giam);
        tvDialogSoLuongMua = view.findViewById(R.id.tv_dialog_SoLuongMua);
        btnDialogTang = view.findViewById(R.id.btn_dialog_Tang);
        btnDialogMua = view.findViewById(R.id.btn_dialog_Mua);


        Picasso.get().load(sanPham.getHinhAnh())
                .placeholder(R.drawable.dongho)
                .error(R.drawable.atvphone)
                .into(imgDialogSp);

        tvDialogSoLuong.setText("Kho: " + sanPham.getSoLuongNhap());

        tvDialogSoLuongMua.setText(String.valueOf(1));
        btnDialogGiam.setOnClickListener(view1 -> {
            if (Integer.parseInt(tvDialogSoLuongMua.getText().toString()) > 1) {
                tvDialogSoLuongMua.setText(String.valueOf(Integer.parseInt(tvDialogSoLuongMua.getText().toString()) - 1));
                btnDialogGiam.setEnabled(true);
            } else {
                btnDialogGiam.setEnabled(false);
            }
            btnDialogTang.setEnabled(true);
        });
        btnDialogTang.setOnClickListener(view1 -> {
            if (Integer.parseInt(tvDialogSoLuongMua.getText().toString()) < 10 && Integer.parseInt(tvDialogSoLuongMua.getText().toString()) < sanPham.getSoLuongNhap()) {
                tvDialogSoLuongMua.setText(String.valueOf(Integer.parseInt(tvDialogSoLuongMua.getText().toString()) + 1));
                btnDialogTang.setEnabled(true);
            } else {
                btnDialogTang.setEnabled(false);
            }

            btnDialogGiam.setEnabled(true);
        });
        DecimalFormat format = new DecimalFormat("###,###,###");
        tvGia.setText(format.format(sanPham.getGiaTien()) + " đ");

        btnDialogMua.setText(textBtn);
        btnDialogMua.setOnClickListener(view13 -> {

            loading.LoadingDialog();
            int sl = Integer.parseInt(tvDialogSoLuongMua.getText().toString());
            int maSp = sanPham.getMaSanPham();
            bottomSheetDialog.dismiss();
            AddGioHang(maSp, maKH, sl);
        });
    }

    private void AddGioHang(int maSp, int maKH, int sl) {
        StringRequest request = new StringRequest(Request.Method.POST, Server.insertGioHang,
                response -> {
                    try {
                        JSONObject checkStatus = null;
                        checkStatus = new JSONObject(response);
                        String status = checkStatus.getString("status");
                        String message = checkStatus.getString("message");
                        switch (status) {
                            case "susccess": {
                                loading.DimissDialog();
                                Toast.makeText(Activity_ChiTietSp.this, "Đã thêm vào giỏ hàng!", Toast.LENGTH_SHORT).show();
                                break;
                            }
                            case "failure": {
                                loading.DimissDialog();
                                Toast.makeText(Activity_ChiTietSp.this, "Không thể thêm vào giỏ hàng! "+message, Toast.LENGTH_SHORT).show();
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }, error -> Toast.makeText(Activity_ChiTietSp.this, "Sever Err! " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("maSP", String.valueOf(maSp));
                params.put("maKH", String.valueOf(maKH));
                params.put("soLuongMua", String.valueOf(sl));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        if (item.getItemId() == R.id.cart) {
            Intent intent = new Intent(getApplicationContext(), Activity_GioHang.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.cart, menu);
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}