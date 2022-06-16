package com.DuAn1.techstore.Model;

import android.app.Activity;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;

import androidx.appcompat.app.AlertDialog;

import com.DuAn1.techstore.R;
import com.github.ybq.android.spinkit.sprite.Sprite;
import com.github.ybq.android.spinkit.style.ThreeBounce;

public class Loading {
    private final Activity activity;
    private final AlertDialog alertDialog;

    public Loading(Activity activity) {
        this.activity = activity;
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        View view = LayoutInflater.from(activity).inflate(R.layout.loading, null);
        builder.setView(view);
        alertDialog = builder.create();
        ProgressBar progressBar = view.findViewById(R.id.progressBar);
        Sprite threeBuonce = new ThreeBounce();
        progressBar.setIndeterminateDrawable(threeBuonce);
        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
    }

    public void LoadingDialog() {
        if (alertDialog.isShowing() == false) {
            alertDialog.show();
        }

    }

    public void DimissDialog() {
        if (alertDialog.isShowing() == true) {
            alertDialog.dismiss();
        }
    }
}
