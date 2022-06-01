package com.example.internshiptask.Network;

import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import com.example.internshiptask.R;

public class NetworkChangeListener extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if (!Common.IsConnectedToInternet(context)) {
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            View Layout_Dialog = LayoutInflater.from(context).inflate(R.layout.internet_connect_dialoge, null, false);
            builder.setView(Layout_Dialog);


            AlertDialog dialog = builder.create();
            dialog.show();
            ;
            dialog.setCancelable(false);

            dialog.getWindow().setGravity(Gravity.CENTER);

            Button button = Layout_Dialog.findViewById(R.id.btn_retry);
            button.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    dialog.dismiss();
                    onReceive(context, intent);
                }
            });

        }


    }
}
