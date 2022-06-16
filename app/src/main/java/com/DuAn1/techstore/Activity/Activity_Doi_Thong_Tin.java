package com.DuAn1.techstore.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.DuAn1.techstore.DAO.Server;
import com.DuAn1.techstore.Model.KhachHang;
import com.DuAn1.techstore.Model.Loading;
import com.DuAn1.techstore.R;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class Activity_Doi_Thong_Tin extends AppCompatActivity {
    private ImageView img_change_info_back_change_user;
    private TextInputLayout textInputLayout1;
    private TextInputEditText edHoTen;
    private TextInputLayout textInputLayout2;
    private TextInputEditText edNamSinh;
    private TextInputLayout textInputLayout3;
    private TextInputEditText edSoDienThoai;
    private TextInputLayout textInputLayout4;
    private TextInputEditText edDiaChi;
    private AppCompatButton btnLogChangePass;
    //
    private KhachHang khachHang;
    private Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_thong_tin);

        AnhXa();
        loading.LoadingDialog();
        img_change_info_back_change_user.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Doi_Thong_Tin.this, Activity_Change_User.class);
            startActivity(intent);
            finish();
        });
        btnLogChangePass.setOnClickListener(view -> {
            if (validate() > 0) {
                CheckPass(khachHang);
            }
        });
    }

    private void CheckPass(KhachHang khachHang) {
        AlertDialog.Builder builder = new AlertDialog.Builder(Activity_Doi_Thong_Tin.this);
        View view = LayoutInflater.from(Activity_Doi_Thong_Tin.this).inflate(R.layout.dialog_checkpass, null);

        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        EditText edPass;
        TextView tvCheckPass;
        AppCompatButton btnOK;

        edPass = view.findViewById(R.id.edPass);
        tvCheckPass = view.findViewById(R.id.tvCheck);
        btnOK = view.findViewById(R.id.btnOK);
        btnOK.setOnClickListener(view1 -> {
            if (edPass.getText().toString().trim().equals(khachHang.getPassword())) {
                loading.LoadingDialog();
                updateThongTinKhachHang();
                alertDialog.dismiss();
            } else {
                tvCheckPass.setText("Mật khẩu không chính xác!");
                tvCheckPass.setTextColor(Color.RED);
                edPass.requestFocus();
            }
        });

    }


    public void AnhXa() {
        img_change_info_back_change_user = findViewById(R.id.img_change_info_back_change_user);
        textInputLayout1 = findViewById(R.id.textInputLayout1);
        edHoTen = findViewById(R.id.edHoTen);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        edNamSinh = findViewById(R.id.edNamSinh);
        textInputLayout3 = findViewById(R.id.textInputLayout3);
        edSoDienThoai = findViewById(R.id.edSoDienThoai);
        textInputLayout4 = findViewById(R.id.textInputLayout4);
        edDiaChi = findViewById(R.id.edDiaChi);
        btnLogChangePass = findViewById(R.id.btn_log_change_pass);
        //
        loading = new Loading(this);
        khachHang = new KhachHang();
        //
        getThongTinKH();
        //

    }

    private void updateThongTinKhachHang() {
        String hoTen = edHoTen.getText().toString().trim();
        String namSinh = edNamSinh.getText().toString().trim();
        String soDienThoai = edSoDienThoai.getText().toString().trim();
        String diaChi = edDiaChi.getText().toString().trim();
        if (!(hoTen.length() == 0) && !(namSinh.length() == 0) && !(soDienThoai.length() == 0) && !(diaChi.length() == 0)) {
            StringRequest request = new StringRequest(Request.Method.POST, Server.updateKhachHang,
                    response -> {
                        try {
                            JSONObject checkStatus = null;
                            checkStatus = new JSONObject(response);
                            String status = checkStatus.getString("status");
                            String message = checkStatus.getString("message");
                            switch (status) {
                                case "susccess": {
                                    loading.DimissDialog();
                                    Dialog("Đổi thông tin thành công!");
                                    btnLogChangePass.setEnabled(false);
                                    break;
                                }
                                case "failure": {
                                    loading.DimissDialog();
                                    Dialog("Đổi thông tin thất bại!");
                                    break;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();

                        }
                    }, error ->Toast.makeText(getApplicationContext(), "Sever Err! " + error.getMessage(), Toast.LENGTH_SHORT).show()){
                @NonNull
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> params = new HashMap<>();
                    params.put("maKH", String.valueOf(khachHang.getMaKhachHang()));
                    params.put("tenKH", hoTen);
                    params.put("namSinh", namSinh);
                    params.put("soDienThoai", soDienThoai);
                    params.put("diaChi", diaChi);
                    return params;
                }
            };
            RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
            queue.add(request);
        }
    }

    private void getThongTinKH() {
        SharedPreferences preferences = getApplicationContext().getSharedPreferences("Luu_dangNhap", Context.MODE_PRIVATE);
        String userName = preferences.getString("tenDangNhap", "");
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getKhachHang,
                response -> {
                    try {
                        loading.DimissDialog();
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        khachHang.setMaKhachHang(jsonObject.getInt("maKH"));
                        khachHang.setUsername(jsonObject.getString("tenDangNhap"));
                        khachHang.setPassword(jsonObject.getString("matKhau"));
                        khachHang.setTenKhachHang(jsonObject.getString("tenKH"));
                        khachHang.setNamSinh(jsonObject.getString("namSinh"));
                        khachHang.setSoDienThoai(jsonObject.getString("soDienThoai"));
                        khachHang.setDiaChi(jsonObject.getString("diaChi"));
//                        Log.e("pass", "getThongTinKH: " + khachHang.getPassword());
                        //
                        edHoTen.setText(khachHang.getTenKhachHang());
                        edDiaChi.setText(khachHang.getDiaChi());
                        edSoDienThoai.setText(khachHang.getSoDienThoai());
                        edNamSinh.setText(khachHang.getNamSinh());
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

    private void Dialog(String mess) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.dialog_thongbao_resigter, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();
        TextView tvThongbao = view.findViewById(R.id.tvMess);
        // dialog
        Button btnThongBao = view.findViewById(R.id.btnThongBao);
        tvThongbao.setText(mess);

        btnThongBao.setOnClickListener(view1 -> {
            loading.DimissDialog();
            alertDialog.dismiss();
        });
    }


    private int validate() {
        int check = 1;
        //
        String hoTen = Objects.requireNonNull(edHoTen.getText()).toString().trim();
        String namSinh = Objects.requireNonNull(edNamSinh.getText()).toString().trim();
        String soDienThoai = Objects.requireNonNull(edSoDienThoai.getText()).toString().trim();
        String diaChi = Objects.requireNonNull(edDiaChi.getText()).toString().trim();
        //
        if (hoTen.length() == 0) {
            textInputLayout1.setError("Không để trống họ tên!");
            edHoTen.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout1.setError(null);
        }
        String firstSt = String.valueOf(hoTen.charAt(0));
        if (!firstSt.matches("^[A-Z]")) {
            textInputLayout1.setError("Chữ cái đầu phải viết hoa!");
            edHoTen.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout1.setError(null);
        }
        //
        if (namSinh.length() == 0) {
            textInputLayout2.setError("Không để trống năm sinh!");
            edNamSinh.requestFocus();
            check = -1;
            return check;
        } else if (namSinh.length() < 4) {
            textInputLayout2.setError("Năm sinh không hợp lệ!");
            edNamSinh.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout2.setError(null);
        }
        if (soDienThoai.length() == 0) {
            textInputLayout3.setError("Không để trống số điện thoại!");
            edSoDienThoai.requestFocus();
            check = -1;
            return check;
        } else if (soDienThoai.length() < 10) {
            textInputLayout3.setError("Số điện thoại không hợp lệ!");
            edSoDienThoai.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout3.setError(null);
        }
        if (diaChi.length() == 0) {
            textInputLayout4.setError("không để trống địa chỉ!");
            edDiaChi.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout4.setError(null);
        }
        return check;
    }
}