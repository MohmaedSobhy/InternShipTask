package com.example.internshiptask;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.internshiptask.Model.RequestHeader;
import com.example.internshiptask.Network.NetworkChangeListener;

import java.util.ArrayDeque;
import java.util.HashMap;
import java.util.Queue;

public class MainActivity extends AppCompatActivity {


    String URL_website;
    EditText ED_HeaderRequest, ED_URLWebsit, Ed_Key, Ed_value;
    Spinner HeaderMethodes;
    Button AddRequest, CheckResultes, AddValues,NextRequest;
    TextView Text_results;
    LinearLayout Post_Fields;
    int MethodType = 0;
    HashMap<String,String> parameters=new HashMap<>();
    Queue<RequestHeader> requestHeaders = new ArrayDeque<>(); // to hold all request headers using Queue
    boolean IsInshilized=false;
    NetworkChangeListener networkChangeListener =new NetworkChangeListener();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Iniliazation(); // assign each view to his Id

        HeaderMethodes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) // Post Method
                {
                    ED_HeaderRequest.setVisibility(View.VISIBLE);
                    Post_Fields.setVisibility(View.GONE);
                } else {
                    ED_HeaderRequest.setVisibility(View.GONE);
                    Post_Fields.setVisibility(View.VISIBLE);
                }
                MethodType = i;
                NextRequest.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        }); // when you Select item from Spinner

        AddRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VerfiyFieldsGet()  && MethodType+1==1)
                {
                    // this is for Get Method
                    AddGetRequest();
                    ClearGetFieldes();
                }
                else if(!ED_URLWebsit.getText().toString().isEmpty()){
                    // for Post method
                     if(VerfiyFieldsPost()) {
                        // i write this  condition may be user add just one key and one value
                        // and user  forget to click Add Values  it will case an error so i call method Add Values here
                        AddKey_Values();
                    }
                     AddPostRequest();

                }

            }
        });

        CheckResultes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Get Each Request from Queue
                GetResultes();
            }
        });

        NextRequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GetResultes();
            }
        });

        AddValues.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (VerfiyFieldsPost()) {
                    AddKey_Values();
                }
            }
        });


    }

    private void Iniliazation() {
        ED_URLWebsit = findViewById(R.id.url_website);
        ED_HeaderRequest = findViewById(R.id.headerRequest);
        HeaderMethodes = findViewById(R.id.headermethodes);
        AddRequest = findViewById(R.id.btn_addrequest);
        CheckResultes = findViewById(R.id.btn_checkResults);
        Text_results = findViewById(R.id.results);
        Post_Fields = findViewById(R.id.linear_post_fields);
        Ed_value = findViewById(R.id.value_parmater);
        Ed_Key = findViewById(R.id.key_parmater);
        AddValues = findViewById(R.id.addvalues);
        NextRequest=findViewById(R.id.next_request);
    }

    private boolean VerfiyFieldsGet() {
        /*
        Verfiy EditText Get Request Header
         this methodes to check fieldes of Get Method
        if any one of them is empty return false else return true
        */
        if (ED_URLWebsit.getText().toString().isEmpty()) {
            ED_URLWebsit.setError(getString(R.string.error_websiturl));
            ED_URLWebsit.requestFocus();
            return false;
        }
        URL_website=ED_URLWebsit.getText().toString();

        if (ED_HeaderRequest.getText().toString().isEmpty()) {
            ED_HeaderRequest.setError(getString(R.string.error_requestheader));
            ED_HeaderRequest.requestFocus();
            return false;
        }

        return true;
    }

    private boolean VerfiyFieldsPost() {

        // Verify Edittext Posts
        if (Ed_Key.getText().toString().isEmpty()) {
            Ed_Key.setError("Enter Key");
            Ed_Key.requestFocus();
            return false;
        }

        if (Ed_value.getText().toString().isEmpty()) {
            Ed_value.setError("Enter Value URl");
            Ed_value.requestFocus();
            return false;
        }

        return true;
    }

    private void ClearGetFieldes() {
        ED_HeaderRequest.setText("");
    }

    private void ClearPostFields() {
        Ed_Key.setText("");
        Ed_value.setText("");
    }

    private void AddGetRequest() {
        // Request From Type Get
        RequestHeader requestHeader=new RequestHeader();
        requestHeader.setMethodtype(RequestHeader.GET);
        HashMap<String,String> Parmeter=new HashMap<>();
        requestHeader.setUrl(ED_URLWebsit.getText().toString());
        Parmeter.put("Link",ED_HeaderRequest.getText().toString());
        requestHeader.setParams(Parmeter);
        requestHeaders.add(requestHeader);
    }

    private void AddPostRequest() {
        // Request From Type Post
        RequestHeader requestHeader=new RequestHeader();
        HashMap<String,String> Parmeter=new HashMap<>();
        Parmeter.putAll(parameters);
        requestHeader.setUrl(ED_URLWebsit.getText().toString());
        requestHeader.setMethodtype(RequestHeader.POST);
        requestHeader.setParams(Parmeter);
        requestHeaders.add(requestHeader);
        IsInshilized=false;
        System.out.println(requestHeader);
        parameters.clear();

    }

    private void AddKey_Values() {
        // Add Key Values like name=mohamed or email="usermail"
        if(!IsInshilized)
        {
            parameters=new HashMap<>();
            IsInshilized=true;
        }
        parameters.put(Ed_Key.getText().toString(),Ed_value.getText().toString());
        ClearPostFields();
    }

    private void GetResultes(){
        // Get Each Request from queue one by one
        RequestHeader requestHeader;
        if(!requestHeaders.isEmpty())
        {
            requestHeader=requestHeaders.poll();
            new HttpRequest(){
                @Override
                public void onResponse(String response) {
                    super.onResponse(response);
                    Text_results.setVisibility(View.VISIBLE);
                    Text_results.setText(response);
                }
            }.execute(requestHeader);
            NextRequest.setVisibility(View.VISIBLE);

        }
        else{
            // show Dialoge Message
            AlertDialog.Builder builder=new AlertDialog.Builder(MainActivity.this);
            builder.setMessage("There is no More Request  ?");
            builder.setPositiveButton("AddMore", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {

                }
            });
            builder.create().show();

        }

    }

    @Override
    protected void onStart() {
        IntentFilter filter=new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(networkChangeListener,filter);
        super.onStart();
    }

    @Override
    protected void onStop() {
        unregisterReceiver(networkChangeListener);
        super.onStop();
    }




}