package com.example.internshiptask.Network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

public class Common {
    public static boolean IsConnectedToInternet(Context context)
    {
        ConnectivityManager connectivityManager=
                (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);

        if(connectivityManager!=null)
        {
            NetworkInfo[] infos=connectivityManager.getAllNetworkInfo();
            if(infos!=null)
            {
                int size=infos.length;
                for(int i=0;i<size;i++)
                    if(infos[i].getState()==NetworkInfo.State.CONNECTED)
                        return true;
            }
        }
        return false;
    }
}
