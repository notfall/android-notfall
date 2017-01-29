package com.mayassin.android.notfall.android;

import android.content.Context;
import android.content.SharedPreferences;
import android.provider.Settings;

/**
 * Created by mohamed on 1/29/17.
 */

public class SessionManager {
    SharedPreferences pref;

    Context _context;

    SharedPreferences.Editor editor;

    private String android_id;

    public SessionManager(Context context){
        this._context = context;
        pref = _context.getSharedPreferences("sessionApp", Context.MODE_PRIVATE);
        editor = pref.edit();
        android_id = Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }

    public void createSessionFromKey(String key){
//        editor.putBoolean("loggedIn",true);
//        editor.putString("key",key);
//        editor.commit();
    }



    public void destroySession(){
        editor.clear();
        editor.commit();

        redirectToLogin();
    }




    public String getCurrentUser(){
        return pref.getString("user_id",null);
    }

    public boolean isLoggedIn(){
        return pref.getBoolean("loggedIn",false);
    }


    public void redirectToLogin(){
//        Intent i = new Intent(_context, LoginActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        _context.startActivity(i);
    }


}