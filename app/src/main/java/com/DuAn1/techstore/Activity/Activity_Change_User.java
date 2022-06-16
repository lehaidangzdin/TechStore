package com.DuAn1.techstore.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.DuAn1.techstore.DAO.Server;
import com.DuAn1.techstore.Model.KhachHang;
import com.DuAn1.techstore.Model.Loading;
import com.DuAn1.techstore.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Activity_Change_User extends AppCompatActivity {
    private ImageView img_back_tai_khoan;
    private Button btn_doi_thong_tin, btn_doi_mat_khau;
    private Intent intent;
    private KhachHang khachHang;
    private Loading loading;
    private TextView tvHoTen;
    private TextView tvNamSinh;
    private TextView tvSoDienThoai;
    private TextView tvDiaChi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_user);

        AnhXa();
        img_back_tai_khoan.setOnClickListener(v -> {
            super.onBackPressed();
        });
        btn_doi_thong_tin.setOnClickListener(v -> {
            intent = new Intent(Activity_Change_User.this, Activity_Doi_Thong_Tin.class);
            startActivity(intent);
            finish();
        });
        btn_doi_mat_khau.setOnClickListener(v -> {
            intent = new Intent(Activity_Change_User.this, Activity_Doi_Mat_Khau.class);
            startActivity(intent);
            finish();
        });
    }

    public void AnhXa() {
        img_back_tai_khoan = findViewById(R.id.img_back_tai_khoan);
        btn_doi_thong_tin = findViewById(R.id.btn_doi_thong_tin);
        btn_doi_mat_khau = findViewById(R.id.btn_doi_mat_khau);
        tvHoTen = findViewById(R.id.tvHoTen);
        tvNamSinh = findViewById(R.id.tvNamSinh);
        tvSoDienThoai = findViewById(R.id.tvSoDienThoai);
        tvDiaChi = findViewById(R.id.tvDiaChi);
        //
        khachHang = new KhachHang();
        loading = new Loading(this);
        loading.LoadingDialog();
        //
        getThongTinKH();
    }

    private void getThongTinKH() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Luu_dangNhap", Context.MODE_PRIVATE);
        String userName = preferences.getString("tenDangNhap", "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getKhachHang,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        khachHang.setMaKhachHang(jsonObject.getInt("maKH"));
                        khachHang.setUsername(jsonObject.getString("tenDangNhap"));
                        khachHang.setPassword(jsonObject.getString("matKhau"));
                        khachHang.setTenKhachHang(jsonObject.getString("tenKH"));
                        khachHang.setNamSinh(jsonObject.getString("namSinh"));
                        khachHang.setSoDienThoai(jsonObject.getString("soDienThoai"));
                        khachHang.setDiaChi(jsonObject.getString("diaChi"));
                        //
                        tvHoTen.setText(khachHang.getTenKhachHang());
                        tvNamSinh.setText("Năm sinh: " + khachHang.getNamSinh());
                        tvSoDienThoai.setText("Số điện thoại:  " + khachHang.getSoDienThoai());
                        tvDiaChi.setText("Địa chỉ: " + khachHang.getDiaChi());
                        loading.DimissDialog();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> Toast.makeText(getApplicationContext(), "Sever Err! " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tenDangNhap", userName);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }
}