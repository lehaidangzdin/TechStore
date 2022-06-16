package com.DuAn1.techstore.Activity;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DuAn1.techstore.Adapter.Adapter_SP;
import com.DuAn1.techstore.DAO.Server;
import com.DuAn1.techstore.Model.SanPham;
import com.DuAn1.techstore.R;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.ArrayList;

public class Activity_Search extends AppCompatActivity {
    private SearchView searchView;
    private SanPham sanPham;
    private ArrayList<SanPham> lstSp;
    private RecyclerView rcv;
    private Toolbar toolbar;
    private Adapter_SP adapter_SP;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        AnhXa();
        ActionBar();
        SearchDL();
        getDLSP();
    }


    private void SearchDL() {
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
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
    }


    private void ActionBar() {
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_baseline_arrow_back_ios_24);
    }

    private void AnhXa() {
        searchView = findViewById(R.id.searchView);
        toolbar = findViewById(R.id.toolbar);
        rcv = findViewById(R.id.rcv);
        //
        searchView.setQueryHint("Tên sản phẩm...");
        searchView.setFocusable(true);
        searchView.setIconified(false);
        searchView.requestFocusFromTouch();
        //
        lstSp = new ArrayList<>();
        adapter_SP = new Adapter_SP(Activity_Search.this, lstSp);
        GridLayoutManager manager = new GridLayoutManager(getApplicationContext(), 2);
        rcv.setLayoutManager(manager);
        rcv.setAdapter(adapter_SP);
    }

    private void getDLSP() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());//khai bao context
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Server.getAllSp, response -> {
            if (response != null) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject jsonObject = response.getJSONObject(i);//lay doi tuong thu i
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
                        lstSp.add(sanPham);
                        adapter_SP.notifyDataSetChanged();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
        }, error -> Toast.makeText(getApplicationContext(), "Sever Err! " + error.getMessage(), Toast.LENGTH_SHORT).show());
        requestQueue.add(jsonArrayRequest);//add request vao xu ly
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            super.onBackPressed();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        adapter_SP.fixMemoryLeak();
    }

}