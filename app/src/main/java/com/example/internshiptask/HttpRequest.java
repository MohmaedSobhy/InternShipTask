package com.example.internshiptask;

import android.os.AsyncTask;

import com.example.internshiptask.Model.RequestHeader;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class HttpRequest extends AsyncTask<RequestHeader,Integer,String> {
    private static final String UTF_8 = "UTF-8";

    @Override
    protected String doInBackground(RequestHeader... params) {
        HttpURLConnection connection = null;
        RequestHeader httpCall = params[0];
        StringBuilder response = new StringBuilder();
        try{
            String dataParameters =(httpCall.getMethodtype()== RequestHeader.GET)?httpCall.getParams().get("Link"):getDataString(httpCall.getParams());
            URL url = new URL(httpCall.getMethodtype() == RequestHeader.GET ? httpCall.getUrl() +"/"+dataParameters : httpCall.getUrl());
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod(httpCall.getMethodtype() == RequestHeader.GET ? "GET":"POST");
            connection.setReadTimeout(10000 /* milliseconds */);
            connection.setConnectTimeout(15000 /* milliseconds */);
            if(httpCall.getParams() != null && httpCall.getMethodtype() == RequestHeader.POST){
                OutputStream os = connection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, UTF_8));
                writer.append(dataParameters);
                writer.flush();
                writer.close();
                os.close();
            }
            int responseCode = connection.getResponseCode();
            response.append("Response Code : ......"+responseCode+"\n");
            response.append("Headers : "+dataParameters+"\n");
            response.append("Response Body : \n");
            if(responseCode == HttpURLConnection.HTTP_OK){
                String line ;
                BufferedReader br = new BufferedReader( new InputStreamReader(connection.getInputStream()));
                while ((line = br.readLine()) != null){
                    response.append(line);
                }
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }   finally {
            connection.disconnect();
        }
        return response.toString();
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        onResponse(s);
    }

    public void onResponse(String response){

    }

    private String getDataString(HashMap<String,String> params) {
        StringBuilder result = new StringBuilder();
        boolean isFirst = true;
        for(Map.Entry<String,String> entry : params.entrySet()){
            if (isFirst){
                isFirst = false;
            }else{
                result.append("&");
            }
            result.append(entry.getKey());
            result.append("=");
            result.append(entry.getValue());
        }
        return result.toString();
    }
}
