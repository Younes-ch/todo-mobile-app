package com.example.todoapp.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.widget.Toast;

import com.example.todoapp.R;

public class Connectivity {

    public static boolean isNetworkConnected(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();

            if (activeNetwork != null && activeNetwork.isConnectedOrConnecting()) {
                return true;
            }
        }

        return false;
    }

    public static void showNoInternetMessage(Context context) {
        String toastMessage = "⛔ " + context.getResources().getString(R.string.no_internet_connection_message);
        Toast.makeText(context, toastMessage, Toast.LENGTH_SHORT).show();
    }
}
