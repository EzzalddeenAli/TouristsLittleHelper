package com.insulardevelopment.touristslittlehelper.network;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Вспомогательный класс для работы с интернетом
 */

public class Network {
    public static boolean isAvailable(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
