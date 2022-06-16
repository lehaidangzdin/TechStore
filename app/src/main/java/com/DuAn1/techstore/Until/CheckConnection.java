package com.DuAn1.techstore.Until;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class CheckConnection {
    public static boolean haveNetworkConnection(Context context) {
        boolean wifi = false, mobile = false;
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo[] networkInfos = cm.getAllNetworkInfo();
        for (NetworkInfo info : networkInfos) {
            if (info.getTypeName().equals("WIFI"))
                if (info.isConnected())
                    wifi = true;
            if (info.getTypeName().equals("MOBILE"))
                if (info.isConnected())
                    mobile = true;

        }
        return wifi || mobile;
    }

}
