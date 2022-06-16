package com.DuAn1.techstore.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.DuAn1.techstore.Adapter.Adapter_HoaDon;
import com.DuAn1.techstore.DAO.Server;
import com.DuAn1.techstore.Model.HoaDon;
import com.DuAn1.techstore.Model.KhachHang;
import com.DuAn1.techstore.Model.Loading;
import com.DuAn1.techstore.R;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;

public class FragmentHoaDon extends Fragment {
    private TextView tvSoLuongHD;
    private RecyclerView rcv;
    private ImageView imgBillNull;


    private KhachHang khachHang;
    private HoaDon hoaDon;
    private ArrayList<HoaDon> lstHD;
    //
    private Adapter_HoaDon adapter_hoaDon;
    private Loading loading;
    private JSONObject checkStatus = null;


    public FragmentHoaDon() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_hoadon, null, false);
        AnhXa(view);
        return view;
    }

    private void AnhXa(View view) {
        tvSoLuongHD = view.findViewById(R.id.tvSoLuongHD);
        rcv = view.findViewById(R.id.rcv);
        imgBillNull = view.findViewById(R.id.imgBillNull);
        imgBillNull.setVisibility(View.GONE);
        //
        loading = new Loading(getActivity());
        loading.LoadingDialog();
        lstHD = new ArrayList<>();
        khachHang = new KhachHang();
        getThongTinKH();
        //
        adapter_hoaDon = new Adapter_HoaDon(getActivity(), lstHD);
        rcv.setHasFixedSize(true);
        rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        rcv.setAdapter(adapter_hoaDon);

    }

    private void getThongTinHoaDon() {
        StringRequest request = new StringRequest(Request.Method.POST, Server.getHoaDon,
                response -> {
                    try {
                        checkStatus = new JSONObject(response);
                        String status = checkStatus.getString("status");
                        String message = checkStatus.getString("message");
                        if (status.equals("failure")) {
                            imgBillNull.setVisibility(View.VISIBLE);
                            loading.DimissDialog();
                        } else if (status.equals("susccess")) {
                            imgBillNull.setVisibility(View.GONE);
                            JSONArray jsonArray = checkStatus.getJSONArray("data");
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject jsonObject = jsonArray.getJSONObject(i);
                                hoaDon = new HoaDon();
                                hoaDon.setMaHoaDon(jsonObject.getInt("maHD"));
                                hoaDon.setMaKhachHang(jsonObject.getInt("maKH"));
                                hoaDon.setNgayBan(jsonObject.getString("ngayBan"));
                                hoaDon.setTongTien(jsonObject.getInt("tongTien"));
                                hoaDon.setIsPay(jsonObject.getInt("isPay"));
                                lstHD.add(hoaDon);
                                sapXepTheoMaHoaDon(lstHD);
                                adapter_hoaDon.notifyDataSetChanged();
                                loading.DimissDialog();
                            }
                            tvSoLuongHD.setText(lstHD.size() + " hóa đơn!");

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                },
                error -> Toast.makeText(getActivity(), "Lỗi kết nối!", Toast.LENGTH_SHORT).show()) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("maKH", String.valueOf(khachHang.getMaKhachHang()));
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        queue.add(request);
    }

    private ArrayList<HoaDon> sapXepTheoMaHoaDon(ArrayList<HoaDon> lstHD) {
        Collections.sort(lstHD, new Comparator<HoaDon>() {
            @Override
            public int compare(HoaDon hoaDon, HoaDon t1) {
                if (hoaDon.getMaHoaDon() < t1.getMaHoaDon()) {
                    return 1;
                } else {
                    if (hoaDon.getMaHoaDon() == t1.getMaHoaDon()) {
                        return 0;
                    } else return -1;
                }
            }
        });
        return lstHD;
    }

    private void getThongTinKH() {
        SharedPreferences preferences = getActivity().getApplicationContext().getSharedPreferences("Luu_dangNhap", Context.MODE_PRIVATE);
        khachHang.setMaKhachHang(preferences.getInt("maKH", 0));
        khachHang.setUsername(preferences.getString("tenDangNhap", ""));
        khachHang.setTenKhachHang(preferences.getString("tenKH", ""));
        khachHang.setNamSinh(preferences.getString("namSinh", ""));
        khachHang.setDiaChi(preferences.getString("diaChi", ""));
        khachHang.setSoDienThoai(preferences.getString("soDienThoai", ""));
        getThongTinHoaDon();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        adapter_hoaDon.fixMemoryLeak();
    }
}
