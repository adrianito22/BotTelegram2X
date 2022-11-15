package com.tiburela.telegramccc;

import static android.content.Context.MODE_PRIVATE;

import android.content.Context;
import android.content.SharedPreferences;

public class SharePref {
   public static  SharedPreferences sharedPreferences ;
     public static final String key="sdklfjjdsf";





 public static    void inshareANDaddData(Context context,boolean seEnviBotTelegramData) {

        sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)
        SharedPreferences.Editor myEdit = sharedPreferences.edit();

// Storing the key and its value as the data fetched from edittext
        myEdit.putBoolean(key, seEnviBotTelegramData);

// Once the changes have been made,
// we need to commit to apply those changes made,
// otherwise, it will throw an error
        myEdit.commit();
    }



    public static    boolean chekifSeenvio(Context context) {

        sharedPreferences = context.getSharedPreferences("MySharedPref",MODE_PRIVATE);

// Creating an Editor object to edit(write to the file)

// Storing the key and its value as the data fetched from edittext
// The value will be default as empty string because for
// the very first time when the app is opened, there is nothing to show
        boolean hayaData = sharedPreferences.getBoolean(key, false);
// Once the changes have been made,
// we need to commit to apply those changes made,
// otherwise, it will throw an error

        return hayaData;
    }



}
