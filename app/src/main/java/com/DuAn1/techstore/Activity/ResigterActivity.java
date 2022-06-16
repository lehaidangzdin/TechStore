package com.DuAn1.techstore.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.DuAn1.techstore.DAO.Server;
import com.DuAn1.techstore.Model.Loading;
import com.DuAn1.techstore.R;
import com.DuAn1.techstore.Until.CheckConnection;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ResigterActivity extends AppCompatActivity {
    private TextView tvDangKi;
    private TextInputLayout textInputLayout1;
    private TextInputEditText edTenDangNhap;
    private TextInputLayout textInputLayout2;
    private TextInputEditText edHoTen;
    private TextInputLayout textInputLayout3;
    private TextInputEditText edNamSinh;
    private TextInputLayout textInputLayout4;
    private TextInputEditText edSoDienThoai;
    private TextInputLayout textInputLayout5;
    private TextInputEditText edDiaChi;
    private TextInputLayout textInputLayout6;
    private TextInputEditText edMatKhau;
    private TextInputLayout textInputLayout7;
    private TextInputEditText edReMatKhau;
    private Button btnResigter;
    private TextView tvDangNhap;

    // loading
    private Loading loading;
    private String tenDangNhap, hoTen, namSinh, matKhau, sdt, diaChi;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dangki);
        Anhxa();
        Animation();
        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            // chuyen sang man dang nhap
            btnResigter.setOnClickListener(view -> {
                if (validate() > 0) {
                    loading.LoadingDialog();
                    tenDangNhap = Objects.requireNonNull(edTenDangNhap.getText()).toString().trim();
                    hoTen = Objects.requireNonNull(edHoTen.getText()).toString().trim();
                    namSinh = Objects.requireNonNull(edNamSinh.getText()).toString().trim();
                    matKhau = Objects.requireNonNull(edMatKhau.getText()).toString().trim();
                    sdt = Objects.requireNonNull(edSoDienThoai.getText()).toString().trim();
                    diaChi = Objects.requireNonNull(edDiaChi.getText()).toString().trim();
                    Resigter();
                }
            });
            tvDangNhap.setOnClickListener(view -> DangNhap());
        } else {
            Dialog("Kiểm tra lại kết nối");
        }

    }

    private void Resigter() {
//        Log.e("zzzzz", Server.dangKy + " getParams: " + tenDangNhap + " " + matKhau + " " + hoTen + " " + namSinh + " " + sdt + " " + diaChi);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.dangKy, response -> {
            try {
                JSONObject jsonObject = null;
                jsonObject = new JSONObject(response);
                String status = jsonObject.getString("status");
                String message = jsonObject.getString("message");

                switch (status) {
                    case "susccess": {
                        loading.DimissDialog();
                        String thongBao = "Đăng kí thành công!";
                        Dialog(thongBao);
                        Toast.makeText(getApplicationContext(), "Successfully", Toast.LENGTH_SHORT).show();
                        ClearText();
                        btnResigter.setClickable(false);
                        break;
                    }
                    case "failure": {
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
                        loading.DimissDialog();
                        String thongBao = "Thất bại!";
                        Dialog(thongBao);
                        break;
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }, error -> Toast.makeText(getApplicationContext(), "Sever Err! " + error.getMessage(), Toast.LENGTH_SHORT).show()) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> data = new HashMap<>();
                data.put("tenDangNhap", tenDangNhap);
                data.put("matKhau", matKhau);
                data.put("tenKH", hoTen);
                data.put("namSinh", namSinh);
                data.put("soDienThoai", sdt);
                data.put("diaChi", diaChi);
                return data;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);

    }


    private int validate() {
        int check = 1;
        if (Objects.requireNonNull(edTenDangNhap.getText()).toString().length() == 0) {
            textInputLayout1.setError("Không để trống tên đăng nhập!");
            edTenDangNhap.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout1.setError(null);
        }
        if (Objects.requireNonNull(edTenDangNhap.getText()).toString().length() < 4) {
            textInputLayout1.setError("Tên đăng nhập tối thiểu 4 kí tự!");
            edTenDangNhap.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout1.setError(null);
        }
        //
        if (Objects.requireNonNull(edHoTen.getText()).toString().length() == 0) {
            textInputLayout2.setError("Không để trống họ tên!");
            edHoTen.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout2.setError(null);
        }

        String hoTen = edHoTen.getText().toString();
        String firstSt = String.valueOf(hoTen.charAt(0));
        if (!firstSt.matches("^[A-Z]")) {
            textInputLayout2.setError("Chữ cái đầu phải viết hoa!");
            edHoTen.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout2.setError(null);
        }

        //
        if (Objects.requireNonNull(edNamSinh.getText()).toString().length() == 0) {
            textInputLayout3.setError("Không để trống năm sinh!");
            edNamSinh.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout3.setError(null);
        }
        if (Objects.requireNonNull(edNamSinh.getText()).toString().length() < 4) {
            textInputLayout3.setError("Năm sinh không hợp lệ!");
            edNamSinh.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout3.setError(null);
        }

        //
        if (Objects.requireNonNull(edSoDienThoai.getText()).toString().length() == 0) {
            textInputLayout4.setError("Không để trống số điện thoại!");
            edSoDienThoai.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout4.setError(null);
        }
        if (Objects.requireNonNull(edSoDienThoai.getText()).toString().length() < 10) {
            textInputLayout4.setError("Số điện thoại không hợp lệ!");
            edSoDienThoai.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout4.setError(null);
        }

        //
        if (Objects.requireNonNull(edDiaChi.getText()).toString().length() == 0) {
            textInputLayout5.setError("Không để trống địa chỉ!");
            edDiaChi.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout5.setError(null);
        }

        if (Objects.requireNonNull(edMatKhau.getText()).toString().length() == 0) {
            textInputLayout6.setError("Không để trống mật khẩu!");
            edMatKhau.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout6.setError(null);
        }
        if (Objects.requireNonNull(edMatKhau.getText()).toString().length() < 4) {
            textInputLayout6.setError("Mật khẩu tối thiểu 4 kí tự!");
            edMatKhau.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout6.setError(null);
        }
        if (Objects.requireNonNull(edReMatKhau.getText()).toString().length() == 0) {
            textInputLayout7.setError("Không để trống nhập lại mật khẩu!");
            edReMatKhau.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout7.setError(null);
        }
        if (!edReMatKhau.getText().toString().equals(edMatKhau.getText().toString())) {
            textInputLayout7.setError("Mật khẩu nhập lại không trùng khớp!");
            edReMatKhau.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout7.setError(null);
        }

        return check;
    }

    private void ClearText() {
        edTenDangNhap.setText("");
        edMatKhau.setText("");
        edReMatKhau.setText("");
        edDiaChi.setText("");
        edHoTen.setText("");
        edSoDienThoai.setText("");
        edNamSinh.setText("");
    }

    private void DangNhap() {
        Intent intent = new Intent(ResigterActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }

    private void Anhxa() {
        tvDangKi = findViewById(R.id.tvDangKi);
        textInputLayout1 = findViewById(R.id.textInputLayout1);
        edTenDangNhap = findViewById(R.id.edTenDangNhap);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        edHoTen = findViewById(R.id.edHoTen);
        textInputLayout3 = findViewById(R.id.textInputLayout3);
        edNamSinh = findViewById(R.id.edNamSinh);
        textInputLayout4 = findViewById(R.id.textInputLayout4);
        edSoDienThoai = findViewById(R.id.edSoDienThoai);
        textInputLayout5 = findViewById(R.id.textInputLayout5);
        edDiaChi = findViewById(R.id.edDiaChi);
        textInputLayout6 = findViewById(R.id.textInputLayout6);
        edMatKhau = findViewById(R.id.edMatKhau);
        textInputLayout7 = findViewById(R.id.textInputLayout7);
        edReMatKhau = findViewById(R.id.edReMatKhau);
        btnResigter = findViewById(R.id.btnReigter);
        tvDangNhap = findViewById(R.id.tvDangNhap);

        loading = new Loading(this);
    }

    private void Animation() {
        tvDangKi.setTranslationX(1000);
        textInputLayout1.setTranslationX(1000);
        textInputLayout2.setTranslationX(1000);
        textInputLayout3.setTranslationX(1000);
        textInputLayout4.setTranslationX(1000);
        textInputLayout5.setTranslationX(1000);
        textInputLayout6.setTranslationX(1000);
        textInputLayout7.setTranslationX(1000);
        btnResigter.setTranslationX(1000);
        tvDangNhap.setTranslationX(1000);

        //
        tvDangKi.animate().translationX(0).setDuration(900).start();
        textInputLayout1.animate().translationX(0).setDuration(900).setStartDelay(200).start();
        textInputLayout2.animate().translationX(0).setDuration(900).setStartDelay(400).start();
        textInputLayout3.animate().translationX(0).setDuration(900).setStartDelay(600).start();
        textInputLayout4.animate().translationX(0).setDuration(900).setStartDelay(800).start();
        textInputLayout5.animate().translationX(0).setDuration(900).setStartDelay(1000).start();
        textInputLayout6.animate().translationX(0).setDuration(900).setStartDelay(1200).start();
        textInputLayout7.animate().translationX(0).setDuration(900).setStartDelay(1400).start();
        btnResigter.animate().translationX(0).setDuration(900).setStartDelay(1600).start();
        tvDangNhap.animate().translationX(0).setDuration(900).setStartDelay(1800).start();
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


    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }
}
