package com.DuAn1.techstore.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.DuAn1.techstore.DAO.Server;
import com.DuAn1.techstore.Model.KhachHang;
import com.DuAn1.techstore.Model.Loading;
import com.DuAn1.techstore.R;
import com.android.volley.AuthFailureError;
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

public class Activity_Doi_Mat_Khau extends AppCompatActivity {
    private KhachHang khachHang;
    private ImageView img_change_pass_back_chang_user;
    private TextInputEditText edOldPass;
    private TextInputEditText edNewPass;
    private TextInputEditText edReNewPass;
    private TextInputLayout textInputLayout1;
    private TextInputLayout textInputLayout2;
    private TextInputLayout textInputLayout3;


    private AppCompatButton btnLogChangePass;
    //
    private String oldPass, newPass, reNewPass;
    //
    private Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doi_mat_khau);
        AnhXa();

        // back ve activity trc
        img_change_pass_back_chang_user.setOnClickListener(v -> {
            Intent intent = new Intent(Activity_Doi_Mat_Khau.this, Activity_Change_User.class);
            startActivity(intent);
            finish();
        });
        btnLogChangePass.setOnClickListener(view -> {
            if (validate() > 0) {
                loading.LoadingDialog();
                updateMatKhau();
            }
        });
    }

    public void AnhXa() {
        img_change_pass_back_chang_user = findViewById(R.id.img_change_pass_back_change_user);
        edOldPass = findViewById(R.id.edOldPass);
        edNewPass = findViewById(R.id.edNewPass);
        edReNewPass = findViewById(R.id.edReNewPass);
        btnLogChangePass = findViewById(R.id.btn_log_change_pass);
        textInputLayout1 = findViewById(R.id.textInputLayout1);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        textInputLayout3 = findViewById(R.id.textInputLayout3);
        //

        khachHang = new KhachHang();
        loading = new Loading(this);
        //
        getThongTinKH();
    }

    private void updateMatKhau() {
        StringRequest request = new StringRequest(Request.Method.POST, Server.updateMatKhau,
                response -> {
                    try {
                        JSONObject checkStatus = null;
                        checkStatus = new JSONObject(response);
                        String status = checkStatus.getString("status");
                        String message = checkStatus.getString("message");
                        Log.e("status", "updateMatKhau: " + status);
                        switch (status) {
                            case "susccess": {
                                loading.DimissDialog();
                                Dialog("Đổi mật khẩu thành công!");
                                clearText();
                                break;
                            }
                            case "failure": {
                                loading.DimissDialog();
                                Dialog("Đổi mật thất bại!");
                                break;
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }, error -> Toast.makeText(getApplicationContext(), "Sever Err! " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("maKH", String.valueOf(khachHang.getMaKhachHang()));
                params.put("matKhau", newPass);
                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
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

    private void clearText() {
        edOldPass.setText("");
        edNewPass.setText("");
        edReNewPass.setText("");
        //
        btnLogChangePass.setEnabled(false);
    }

    private int validate() {
        int check = 1;
        oldPass = Objects.requireNonNull(edOldPass.getText()).toString().trim();
        newPass = Objects.requireNonNull(edNewPass.getText()).toString().trim();
        reNewPass = Objects.requireNonNull(edReNewPass.getText()).toString().trim();
        //
        if (oldPass.length() == 0) {
            textInputLayout1.setError("Không để trống mật khẩu cũ!");
            edOldPass.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout1.setError(null);
        }
        if (!oldPass.equals(khachHang.getPassword())) {
            textInputLayout1.setError("Mật khẩu cũ không chính xác!");
            edOldPass.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout1.setError(null);
        }
        if (newPass.length() == 0) {
            textInputLayout2.setError("Không để trống mật khẩu mới!");
            edNewPass.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout2.setError(null);
        }
        if (reNewPass.length() == 0) {
            textInputLayout3.setError("Không để trống nhập lại mật khẩu!");
            edReNewPass.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout3.setError(null);
        }
        if (!newPass.equals(reNewPass)) {
            textInputLayout3.setError("Nhập lại mật khẩu không trùng!");
            edReNewPass.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout3.setError(null);
        }
        return check;
    }

}