package com.DuAn1.techstore.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;
import android.widget.Toast;

import com.DuAn1.techstore.Adapter.Adapter_SP;
import com.DuAn1.techstore.Adapter.Apdater_Spinner;
import com.DuAn1.techstore.DAO.Server;

import com.DuAn1.techstore.Model.Loading;
import com.DuAn1.techstore.Model.SanPham;
import com.DuAn1.techstore.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;

import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class Activity_MayTinhBang extends AppCompatActivity {
    private static final int MATL = 4;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private ArrayList<SanPham> lstSP;
    private Adapter_SP adapter_SP;
    private SanPham sanPham;
    private Loading loading;
    //
    private Spinner spnLoc;
    private Apdater_Spinner apdater_spinner;
    private final String[] lstCn = {"Tất cả", "Giá cao đến thấp", "Giá thấp đến cao", "Sắp xếp theo tên"};
    private final int[] listIcon = {
            R.drawable.ic_baseline_phone_android_24,
            R.drawable.ic_baseline_trending_down_24,
            R.drawable.ic_baseline_trending_up_24,
            R.drawable.ic_baseline_text_rotation_down_24,
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_may_tinh_bang);
        AnhXa();
        ActionBar();
        getDL_DT();
        SapXepSpinner();
    }
    private void AnhXa() {
        loading = new Loading(this);
        toolbar = findViewById(R.id.toolbar);
        recyclerView = findViewById(R.id.rcv);
        spnLoc = findViewById(R.id.spnLoc);
        apdater_spinner = new Apdater_Spinner(getApplicationContext(), lstCn, listIcon);
        spnLoc.setAdapter(apdater_spinner);
        lstSP = new ArrayList<>();
        adapter_SP = new Adapter_SP(getApplicationContext(), lstSP);
        loading.LoadingDialog();
        //
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter_SP);
    }

    private void getDL_DT() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.sanPhamTheoTL,
                response -> {
                    try {
                        loading.DimissDialog();
                        JSONArray jsonArray = new JSONArray(response);
                        for (int i = 0; i < jsonArray.length(); i++) {
                            try {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);//lay doi tuong thu i
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
                                lstSP.add(sanPham);
                                adapter_SP.notifyDataSetChanged();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                },
                error -> Toast.makeText(getApplicationContext(), "Sever Err! " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("maLoai", String.valueOf(MATL));
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }

    @SuppressLint("RestrictedApi")
    private void ActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setTitle("Máy tính bảng");
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setDefaultDisplayHomeAsUpEnabled(true);
        }

    }

    private void SapXepSpinner() {
        spnLoc.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0: {
//
                        break;
                    }
                    case 1: {
                        sapXepGiamDanTheoGia(lstSP);
                        adapter_SP.notifyDataSetChanged();
                        break;
                    }
                    case 2: {
                        sapXepTangDanTheoGia(lstSP);
                        adapter_SP.notifyDataSetChanged();
                        break;
                    }
                    case 3: {
                        sapXepTheoChuCai(lstSP);
                        adapter_SP.notifyDataSetChanged();
                        break;
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
    }

    public ArrayList<SanPham> MacDinh(ArrayList<SanPham> list) {
        return list;
    }

    public ArrayList<SanPham> sapXepGiamDanTheoGia(ArrayList<SanPham> list) {
        Collections.sort(list, (sanPham, t1) -> {
            if (sanPham.getGiaTien() < t1.getGiaTien()) {
                return 1;
            } else {
                if (sanPham.getGiaTien() == t1.getGiaTien()) {
                    return 0;
                } else return -1;
            }
        });
        return list;
    }

    public ArrayList<SanPham> sapXepTangDanTheoGia(ArrayList<SanPham> list) {
        Collections.sort(list, (sanPham, t1) -> {
            if (sanPham.getGiaTien() < t1.getGiaTien()) {
                return -1;
            } else {
                if (sanPham.getGiaTien() == t1.getGiaTien()) {
                    return 0;
                } else return 1;
            }
        });
        return list;
    }

    public ArrayList<SanPham> sapXepTheoChuCai(ArrayList<SanPham> list) {
        Collections.sort(lstSP, new Comparator<SanPham>() {
            @Override
            public int compare(SanPham sanPham, SanPham t1) {
                return (sanPham.getTenSanPham().compareTo(t1.getTenSanPham()));
            }
        });
        return list;
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
        getMenuInflater().inflate(R.menu.search_view, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.Search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                adapter_SP.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                adapter_SP.getFilter().filter(newText);
                return false;
            }
        });
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter_SP.fixMemoryLeak();
    }
}