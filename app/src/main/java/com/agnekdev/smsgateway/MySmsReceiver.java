package com.agnekdev.smsgateway;

import android.annotation.TargetApi;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MySmsReceiver extends BroadcastReceiver {
    private static final String TAG = MySmsReceiver.class.getSimpleName();
    public static final String pdu_type = "pdus";

    @TargetApi(Build.VERSION_CODES.M)
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        SmsMessage[] msgs;
        String senderNumber="";
        String strMessage = "";
        String format = bundle.getString("format");
        // Retrieve the SMS message received.
        Object[] pdus = (Object[]) bundle.get(pdu_type);
        if (pdus != null) {
            // Check the Android version.
            boolean isVersionM = (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M);
            // Fill the msgs array.
            msgs = new SmsMessage[pdus.length];
            for (int i = 0; i < msgs.length; i++) {
                // Check Android version and use appropriate createFromPdu.
                if (isVersionM) {
                    // If Android version M or newer:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
                } else {
                    // If Android version L or older:
                    msgs[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                }
                // Build the message to show.
                senderNumber += msgs[i].getOriginatingAddress();
                strMessage += msgs[i].getMessageBody() + "\n";

                if(senderNumber.equals("+22891814773")){
                    sendrequest(context,strMessage,senderNumber);
                    Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();
                    showMessage(context,strMessage,senderNumber);
                }

            }
        }
    }

    void sendrequest(final Context context, String body,String sender){
        RequestQueue queue = Volley.newRequestQueue(context);
        String url=String.format("http://10.1.1.4:8000/message-sms?body=%s&sender=%s",body,sender);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Toast.makeText(context,"ok ok",Toast.LENGTH_LONG).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(context,"Error",Toast.LENGTH_LONG).show();
            }
        });

        queue.add(stringRequest);
    }

    void showMessage(Context context,String msg,String sendenumber){
        Intent intent= new Intent(context,MainActivity.class);
        intent.putExtra("msg",msg);
        intent.putExtra("sender",sendenumber);
        context.startActivity(intent);
    }

}
