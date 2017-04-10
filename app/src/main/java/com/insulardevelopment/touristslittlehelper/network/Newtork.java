package com.insulardevelopment.touristslittlehelper.network;

import android.content.Context;
import android.net.ConnectivityManager;

/**
 * Created by Маргарита on 09.04.2017.
 */

public class Newtork {
    public static boolean isAvalaible(Context context){
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null &&
                cm.getActiveNetworkInfo().isConnectedOrConnecting();
    }
}
