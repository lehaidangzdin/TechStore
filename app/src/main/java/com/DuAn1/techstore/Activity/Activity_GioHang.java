package com.DuAn1.techstore.Activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DuAn1.techstore.Adapter.Adapter_GioHang;
import com.DuAn1.techstore.DAO.Server;
import com.DuAn1.techstore.Model.GioHang;
import com.DuAn1.techstore.Model.Loading;
import com.DuAn1.techstore.Model.SanPham;
import com.DuAn1.techstore.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Activity_GioHang extends AppCompatActivity {

    private Toolbar toolbar;
    private Loading loading;
    private TextView tvSoLuongSPTrongGio;
    private RecyclerView recyclerView;
    private ImageView imgGHTrong;

    @SuppressLint("StaticFieldLeak")
    private static TextView tvTongTien;
    private Button btnThanhToan;

    private SanPham sanPham;
    private GioHang gioHang;
    private int maKH;
    private int tongTien = 0;

    private ArrayList<SanPham> lstSP;
    private ArrayList<GioHang> lstGH;
    private Adapter_GioHang adapter_gioHang;

    private JSONObject checkStatus = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gio_hang);
        Anhxa();
        ActionBar();
        getThongTinKH();

        btnThanhToan.setOnClickListener(view -> ThanhToanGioHang());
    }

    private void Anhxa() {
        toolbar = findViewById(R.id.toolbar);
        imgGHTrong = findViewById(R.id.imgGHTrong);
        tvSoLuongSPTrongGio = findViewById(R.id.tvSoLuongSPTrongGio);
        recyclerView = findViewById(R.id.rcv);
        tvTongTien = findViewById(R.id.tvTongTien);
        btnThanhToan = findViewById(R.id.btnThanhToan);
        imgGHTrong.setVisibility(View.INVISIBLE);

        loading = new Loading(this);
        loading.LoadingDialog();
        lstSP = new ArrayList<>();
        lstGH = new ArrayList<>();
        //
        adapter_gioHang = new Adapter_GioHang(Activity_GioHang.this, this, lstGH, lstSP);
        LinearLayoutManager manager = new LinearLayoutManager(Activity_GioHang.this);
        // ----
        RecyclerView.ItemDecoration itemDecoration = new DividerItemDecoration(this, DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        //----
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter_gioHang);
    }

    @SuppressLint("RestrictedApi")
    private void ActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Giỏ Hàng");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }

    }

    private void ThanhToanGioHang() {
        Intent intent = new Intent(Activity_GioHang.this, Activity_ThanhToan.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("lstSP", lstSP);
        bundle.putSerializable("lstGH", lstGH);
        bundle.putInt("tongTien", tongTien);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();
    }

    public void xuLiKhiGHRong() {
        imgGHTrong.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.INVISIBLE);
        tvSoLuongSPTrongGio.setText("0 đơn hàng trong giỏ");
        tvTongTien.setText("0đ");
        btnThanhToan.setEnabled(false);
    }

    private void getDLGioHang(int maKH) {
        StringRequest request = new StringRequest(Request.Method.POST, Server.getGioHang,
                response -> {
                    try {
//                        Log.e("res", "getDLGioHang: " + response);
                        checkStatus = new JSONObject(response);
                        String status = checkStatus.getString("status");
                        String message = checkStatus.getString("message");

                        if (status.equals("failure")) {
                            xuLiKhiGHRong();
                            loading.DimissDialog();
                        } else if (status.equals("susccess")) {
                            loading.DimissDialog();
                            imgGHTrong.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            JSONArray jsonArray = checkStatus.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                try {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);//lay doi tuong thu i
                                    sanPham = new SanPham();
                                    gioHang = new GioHang();
                                    sanPham.setMaSanPham(jsonObject.getInt("maSP"));
                                    sanPham.setMaLoai(jsonObject.getInt("maLoai"));
                                    sanPham.setTenSanPham(jsonObject.getString("tenSP"));
                                    sanPham.setSoLuongNhap(jsonObject.getInt("soLuongNhap"));
                                    sanPham.setHinhAnh(jsonObject.getString("hinhAnh"));
                                    sanPham.setGiaTien(jsonObject.getInt("giaTien"));
                                    sanPham.setGiaCu(jsonObject.getInt("giaCu"));
                                    sanPham.setNgayNhap(jsonObject.getString("ngayNhap"));
                                    sanPham.setThongTinSanPham(jsonObject.getString("thongTinSP"));
                                    //
                                    gioHang.setMaSanPham(sanPham.getMaSanPham());
                                    gioHang.setMaKH(jsonObject.getInt("maKH"));
                                    gioHang.setSoLuongMua(jsonObject.getInt("soLuong"));
                                    lstSP.add(sanPham);
                                    lstGH.add(gioHang);
                                    tvSoLuongSPTrongGio.setText(lstGH.size() + " đơn hàng!");
                                    adapter_gioHang.notifyDataSetChanged();
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }
                            setTongTien();

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }, error -> Toast.makeText(getApplicationContext(), "Sever Err! " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("maKH", String.valueOf(maKH));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(request);
    }

    private void getThongTinKH() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Luu_dangNhap", Context.MODE_PRIVATE);
        maKH = preferences.getInt("maKH", 0);
        getDLGioHang(maKH);
    }

    @SuppressLint("SetTextI18n")
    public void setTongTien() {
        tongTien = 0;
        if (lstSP != null && lstGH != null) {
            for (int i = 0; i < lstSP.size(); i++) {
                tongTien += lstSP.get(i).getGiaTien() * lstGH.get(i).getSoLuongMua();
            }
            DecimalFormat format = new DecimalFormat("###,###,###");
            tvTongTien.setText(String.valueOf(format.format(tongTien) + " đ"));
            tvSoLuongSPTrongGio.setText(String.valueOf(lstGH.size()) + " đơn hàng trong giỏ!");
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter_gioHang.fixMemoryLeak();
    }
}