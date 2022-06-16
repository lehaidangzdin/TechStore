package com.DuAn1.techstore.Activity;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.DuAn1.techstore.R;
import com.DuAn1.techstore.fragment.FragmentHoaDon;
import com.DuAn1.techstore.fragment.FragmentManChinh;
import com.DuAn1.techstore.fragment.FragmentTaiKhoan;
import com.etebarian.meowbottomnavigation.MeowBottomNavigation;


public class ManChinhActivity extends AppCompatActivity {

    private MeowBottomNavigation bottomNavigation;
    private Toolbar toolbar;
    private ActionBar actionBar;
    private FragmentManager fragmentManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manchinh);
        fragmentManager = getSupportFragmentManager();
        AnhXa();
        // xu li actionBar
        ActionBar();
        // xu li bottom
        BottomNav();


    }

    private void AnhXa() {
        bottomNavigation = findViewById(R.id.BottomNav);
        toolbar = findViewById(R.id.toolbar);
    }

    private void ActionBar() {
        setSupportActionBar(toolbar);
        actionBar = getSupportActionBar();
    }


    private void BottomNav() {
        // add icon fragment
        bottomNavigation.add(new MeowBottomNavigation.Model(1, R.drawable.ic_baseline_home_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(2, R.drawable.ic_baseline_article_24));
        bottomNavigation.add(new MeowBottomNavigation.Model(3, R.drawable.ic_baseline_person_24));

        bottomNavigation.setOnShowListener(item -> {
            // replace fragment khi click item
            Fragment fragment = null;
            switch (item.getId()) {
                case 1: {
                    fragment = new FragmentManChinh(getApplicationContext());
                    actionBar.setTitle("Trang chủ");
                    break;
                }
                case 2: {
                    fragment = new FragmentHoaDon();
                    actionBar.setTitle("Hóa đơn");
                    break;
                }
                case 3: {
                    fragment = new FragmentTaiKhoan(getApplicationContext());
                    actionBar.setTitle("Tài khoản");
                    break;
                }
            }
            loadFragment(fragment);
        });

        // setCountNotification
        //bottomNavigation.setCount(2, String.valueOf(getCountNotification()));
        bottomNavigation.show(1, true);

        bottomNavigation.setOnClickMenuListener(item -> {
            // khi click tab
        });
        bottomNavigation.setOnReselectListener(item -> {
            // khi chọn lại tab
            //Toast.makeText(getApplicationContext(), "Reselect", Toast.LENGTH_SHORT).show();
        });

    }

    private int getCountNotification() {
        return 10;// số notification
    }

    private void loadFragment(Fragment fragment) {
        // replace fragmet
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.container, fragment)
                .commitNow();
    }


    public void Exit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View view = LayoutInflater.from(this).inflate(R.layout.dialog_thoat, null);
        builder.setView(view);
        AlertDialog alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        alertDialog.show();

        Button btnHuy = view.findViewById(R.id.btn_dialog_Huy);
        Button btnOut = view.findViewById(R.id.btn_dialog_Thoat);

        btnOut.setOnClickListener(v -> {
            alertDialog.dismiss();
            System.exit(0);

        });

        btnHuy.setOnClickListener(v -> alertDialog.dismiss());
    }


    public void replaceFragments(Class fragmentClass) {
        Fragment fragment = null;
        try {
            fragment = (Fragment) fragmentClass.newInstance();
        } catch (Exception e) {
            e.printStackTrace();
        }
        // Insert the fragment by replacing any existing fragment
        fragmentManager.beginTransaction()
                .setCustomAnimations(R.anim.enter_right_to_left, R.anim.exit_right_to_left,
                        R.anim.enter_left_to_right, R.anim.exit_left_to_right)
                .replace(R.id.container, fragment)
                .addToBackStack(null)
                .commit();
    }

    public void popBackFragments() {
        if (fragmentManager.getBackStackEntryCount() > 0) {
            fragmentManager.popBackStack();
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cart) {
            Intent intent = new Intent(getApplicationContext(), Activity_GioHang.class);
            startActivity(intent);
        }
        if (item.getItemId() == R.id.SearchActivity) {
            Intent intent = new Intent(getApplicationContext(), Activity_Search.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_manhinhchinh, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onBackPressed() {
        Exit();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
