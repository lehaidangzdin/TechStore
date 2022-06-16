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
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.DuAn1.techstore.DAO.Server;
import com.DuAn1.techstore.Model.Loading;
import com.DuAn1.techstore.R;
import com.DuAn1.techstore.Until.CheckConnection;
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

public class LoginActivity extends AppCompatActivity {
    private ImageView imageView;
    private TextView tvDangNhap;
    private TextInputLayout textInputLayout1;
    private TextInputEditText edUsername;
    private TextInputLayout textInputLayout2;
    private TextInputEditText edPassword;
    private CheckBox chkRemember;
    private Button btnLogin;
    private TextView textView4;
    private TextView tvResigter;

    private Loading loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        //anh xa
        AnhXa();

        if (CheckConnection.haveNetworkConnection(getApplicationContext())) {
            // animation
            animationLogo();
            // chuyen sang activity Dang ki
            tvResigter.setOnClickListener(view -> Resigter());
            SharedPreferences sharedPreferences = getSharedPreferences("Accout_file", MODE_PRIVATE);

            edUsername.setText(sharedPreferences.getString("USER", ""));
            edPassword.setText(sharedPreferences.getString("PASS", ""));
            chkRemember.setChecked(sharedPreferences.getBoolean("REMEMBER", false));
            btnLogin.setOnClickListener(view -> {
                if (validate() > 0) {
                    loading.LoadingDialog();
                    Login(Objects.requireNonNull(edUsername.getText()).toString().trim(), Objects.requireNonNull(edPassword.getText()).toString().trim());
                }
            });
        } else {
            Dialog("Kiểm tra lại kết nối");
            btnLogin.setOnClickListener(view -> Dialog("Kiểm tra lại kết nối"));
        }
    }

    private void AnhXa() {
        imageView = findViewById(R.id.imgLogo);
        tvDangNhap = findViewById(R.id.tvDangNhap);
        textInputLayout1 = findViewById(R.id.textInputLayout1);
        edUsername = findViewById(R.id.edUsername);
        textInputLayout2 = findViewById(R.id.textInputLayout2);
        edPassword = findViewById(R.id.edPassword);
        chkRemember = findViewById(R.id.chkRemember);
        btnLogin = findViewById(R.id.btnLogin);
        textView4 = findViewById(R.id.textView4);
        tvResigter = findViewById(R.id.tvResigter);
        loading = new Loading(this);

    }

    // animation logo
    private void animationLogo() {
        //set vị trí ban đầu
        imageView.setTranslationY(550);
        imageView.setScaleX(1.5f);
        imageView.setScaleY(1.5f);
        textView4.setTranslationY(1000);
        tvResigter.setTranslationY(1000);
        //constraintLayout.setTranslationY(-900);
        //ẩn
        tvDangNhap.setAlpha(0);
        textInputLayout1.setAlpha(0);
        textInputLayout2.setAlpha(0);
        chkRemember.setAlpha(0);
        btnLogin.setAlpha(0);
        // animation
        imageView.animate().translationY(0).setDuration(1200).setStartDelay(300).start();
        imageView.animate().scaleX(1).setDuration(1000).setStartDelay(300).start();
        imageView.animate().scaleY(1).setDuration(1000).setStartDelay(300).start();
        tvDangNhap.animate().alpha(1).setDuration(1200).setStartDelay(900).start();
        textInputLayout1.animate().alpha(1).setDuration(1200).setStartDelay(1100).start();
        textInputLayout2.animate().alpha(1).setDuration(1200).setStartDelay(1300).start();
        chkRemember.animate().alpha(1).setDuration(1200).setStartDelay(1500).start();
        btnLogin.animate().alpha(1).setDuration(1200).setStartDelay(1700).start();
        textView4.animate().translationY(0).setDuration(1200).setStartDelay(1900).start();
        tvResigter.animate().translationY(0).setDuration(1200).setStartDelay(2100).start();
    }

    public void Login(String username, String password) {
        if (!username.equals("") && !password.equals("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.dangNhap, response -> {
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String message = jsonObject.getString("message");
                    Log.d("Response", status + "-" + message);
                    if (status.equals("susccess")) {
                        Intent intent = new Intent(LoginActivity.this, ManChinhActivity.class);
                        intent.putExtra("tenDangNhap", username);
                        startActivity(intent);
                        rememberAccount(username, password, chkRemember.isChecked());
                        luuThongTinKH(username);
                        loading.DimissDialog();
                        finish();

                    } else if (status.equals("failure")) {
                        loading.DimissDialog();
                        Toast.makeText(LoginActivity.this, message, Toast.LENGTH_SHORT).show();
                        rememberAccount("", "", false);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }, error -> {
                //Toast.makeText(LoginActivity.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
                Dialog("Lỗi kết nối!");
            }) {
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> data = new HashMap<>();
                    data.put("tenDangNhap", username);
                    data.put("matKhau", password);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        } else {
            Toast.makeText(this, "null user,pass", Toast.LENGTH_SHORT).show();
        }
    }

    private void luuThongTinKH(String username) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.getKhachHang,
                response -> {
                    try {
                        JSONArray jsonArray = new JSONArray(response);
                        JSONObject jsonObject = jsonArray.getJSONObject(0);
                        SharedPreferences sharedPreferences2 = LoginActivity.this.getSharedPreferences("Luu_dangNhap", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences2.edit();
                        editor.putBoolean("luuDangNhap", true);
                        editor.putString("tenDangNhap", username);
                        editor.putInt("maKH", jsonObject.getInt("maKH"));
                        editor.putString("tenKH", jsonObject.getString("tenKH"));
                        editor.putString("namSinh", jsonObject.getString("namSinh"));
                        editor.putString("soDienThoai", jsonObject.getString("soDienThoai"));
                        editor.putString("diaChi", jsonObject.getString("diaChi"));
                        editor.apply();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(getApplicationContext(), "Server Err! " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("tenDangNhap", username);
                return params;
            }
        };
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(stringRequest);
    }


    private int validate() {
        int check = 1;
        if (Objects.requireNonNull(edUsername.getText()).toString().length() == 0) {
            textInputLayout1.setError("Không để trống tên đăng nhập!");
            edUsername.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout1.setError(null);
        }
        if (Objects.requireNonNull(edPassword.getText()).toString().length() == 0) {
            textInputLayout2.setError("Không để trống mật khẩu!");
            edPassword.requestFocus();
            check = -1;
            return check;
        } else {
            textInputLayout2.setError(null);
        }

        return check;
    }

    private void Resigter() {
        Intent in = new Intent(LoginActivity.this, ResigterActivity.class);
        startActivity(in);
    }

    // nho tk, mk
    private void rememberAccount(String user, String pass, boolean status) {
        SharedPreferences sharedPreferences = getSharedPreferences("Accout_file", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        if (!status) {
            clearLuuDangNhap();
            editor.clear();
        } else {
            editor.putString("USER", user);
            editor.putString("PASS", pass);
            editor.putBoolean("REMEMBER", true);
        }
        editor.apply();
    }

    private void clearLuuDangNhap() {
        SharedPreferences sharedPreferences2 = getApplicationContext().getSharedPreferences("Luu_dangNhap", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences2.edit();
        editor.putBoolean("luuDangNhap", false);
        editor.putString("USER", "");
        editor.apply();
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

    // dialog thoat
    public void Exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
        View view = LayoutInflater.from(LoginActivity.this).inflate(R.layout.dialog_thoat, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();

        Button btnHuy = view.findViewById(R.id.btn_dialog_Huy);
        Button btnThoat = view.findViewById(R.id.btn_dialog_Thoat);

        btnThoat.setOnClickListener(v -> {
            alertDialog.dismiss();
            System.exit(0);

        });

        btnHuy.setOnClickListener(v -> alertDialog.dismiss());
    }

    @Override
    public void onBackPressed() {
        Exit();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }
}