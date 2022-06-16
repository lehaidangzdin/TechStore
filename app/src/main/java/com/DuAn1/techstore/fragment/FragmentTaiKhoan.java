package com.DuAn1.techstore.fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DuAn1.techstore.Activity.Activity_Change_User;
import com.DuAn1.techstore.Activity.LoginActivity;
import com.DuAn1.techstore.Activity.ManChinhActivity;
import com.DuAn1.techstore.Adapter.Adapter_SP;
import com.DuAn1.techstore.DAO.Server;
import com.DuAn1.techstore.Model.KhachHang;
import com.DuAn1.techstore.Model.Loading;
import com.DuAn1.techstore.Model.SanPham;
import com.DuAn1.techstore.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class FragmentTaiKhoan extends Fragment {
    private ImageView img_chang_user;
    private TextView tvHoTen;
    private TextView tvTenDangNhap;
    private TextView tvSpDaMua;
    private TextView tvChuaMuaSP;
    private Button btnLogOut;


    private RecyclerView recyclerView;
    private Context context;
    private Intent intent;
    private Loading loading;
    private KhachHang khachHang;
    private SanPham sanPham;
    private ArrayList<SanPham> lstSP;
    private Adapter_SP adapter_sp;

    private JSONObject checkStatus = null;


    public FragmentTaiKhoan(Context context) {
        this.context = context;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_taikhoan, null, false);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        AnhXa(view);
        img_chang_user.setOnClickListener(v -> {
            intent = new Intent((ManChinhActivity) getActivity(), Activity_Change_User.class);
            startActivity(intent);
        });
        btnLogOut.setOnClickListener(view1 -> DialogDX());

    }

    private void AnhXa(View view) {
        img_chang_user = view.findViewById(R.id.img_change_user);
        tvHoTen = view.findViewById(R.id.tvHoTen);
        tvSpDaMua = view.findViewById(R.id.tvSpDaMua);
        tvChuaMuaSP = view.findViewById(R.id.tvChuaMuaSP);
        tvChuaMuaSP.setVisibility(View.GONE);
        recyclerView = view.findViewById(R.id.rcv);
        tvTenDangNhap = view.findViewById(R.id.tvTenDangNhap);
        btnLogOut = view.findViewById(R.id.btn_log_out);
        //
        lstSP = new ArrayList<>();
        khachHang = new KhachHang();

        loading = new Loading(getActivity());
        loading.LoadingDialog();
        //
        getThongTinKH();
        //
        adapter_sp = new Adapter_SP(context, lstSP);
        recyclerView.setLayoutManager(new GridLayoutManager(context, 2));
        recyclerView.setHasFixedSize(true);
        recyclerView.setAdapter(adapter_sp);

    }

    private void getThongTinKH() {
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("Luu_dangNhap", Context.MODE_PRIVATE);
        khachHang.setMaKhachHang(preferences.getInt("maKH", 0));
        khachHang.setUsername(preferences.getString("tenDangNhap", ""));
        khachHang.setTenKhachHang(preferences.getString("tenKH", ""));
        khachHang.setNamSinh(preferences.getString("namSinh", ""));
        khachHang.setDiaChi(preferences.getString("diaChi", ""));
        khachHang.setSoDienThoai(preferences.getString("soDienThoai", ""));
        // set thong tin
        tvHoTen.setText(khachHang.getTenKhachHang());
        tvTenDangNhap.setText("@" + khachHang.getUsername());
        getSanPhamDaMua();
    }


    private void getSanPhamDaMua() {
        RequestQueue queue = Volley.newRequestQueue(context);
        StringRequest request = new StringRequest(Request.Method.POST, Server.getSPDaMua,
                response -> {
                    try {
                        checkStatus = new JSONObject(response);
                        String status = checkStatus.getString("status");
                        String message = checkStatus.getString("message");
                        if (status.equals("failure")) {
                            tvSpDaMua.setText("Sản phẩm đã mua: (0)");
                            tvChuaMuaSP.setVisibility(View.VISIBLE);
                            loading.DimissDialog();
                        } else if (status.equals("susccess")) {
                            tvChuaMuaSP.setVisibility(View.GONE);
                            JSONArray jsonArray = checkStatus.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                sanPham = new SanPham();
                                sanPham.setMaSanPham(jsonObject.getInt("maLoai"));
                                sanPham.setMaSanPham(jsonObject.getInt("maSP"));
                                sanPham.setTenSanPham(jsonObject.getString("tenSP"));
                                sanPham.setSoLuongNhap(jsonObject.getInt("soLuongNhap"));
                                sanPham.setHinhAnh(jsonObject.getString("hinhAnh"));
                                sanPham.setGiaTien(jsonObject.getInt("giaTien"));
                                sanPham.setGiaCu(jsonObject.getInt("giaCu"));
                                sanPham.setNgayNhap(jsonObject.getString("ngayNhap"));
                                sanPham.setThongTinSanPham(jsonObject.getString("thongTinSP"));
                                lstSP.add(sanPham);
                                tvSpDaMua.setText("Sản phẩm đã mua: (" + lstSP.size() + " sản phẩm)");
                                adapter_sp.notifyDataSetChanged();
                                loading.DimissDialog();
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }, error -> {

        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("maKH", String.valueOf(khachHang.getMaKhachHang()));
                return params;
            }
        };
        queue.add(request);
    }

    private void DialogDX() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = LayoutInflater.from(getActivity()).inflate(R.layout.dialog_dangxuat, null, false);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Button btnDx = view.findViewById(R.id.btnDangXuat);
        Button btnHuy = view.findViewById(R.id.btnHuy);
        btnDx.setOnClickListener(view1 -> {
            alertDialog.dismiss();
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            clearLuuDangNhap();
            startActivity(intent);
            getActivity().finish();
        });
        btnHuy.setOnClickListener(view12 -> alertDialog.dismiss());
        alertDialog.show();
    }

    private void clearLuuDangNhap() {
        SharedPreferences sharedPreferences2 = getActivity().getSharedPreferences("Luu_dangNhap", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putBoolean("luuDangNhap", false);
        editor.putString("tenDangNhap", "");
        editor.putInt("maKH", 0);
        editor.putString("tenKH", "");
        editor.putString("namSinh", "");
        editor.putString("soDienThoai", "");
        editor.putString("diaChi", "");
        editor.apply();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (adapter_sp == null) {
            adapter_sp.fixMemoryLeak();
        }
    }
}
