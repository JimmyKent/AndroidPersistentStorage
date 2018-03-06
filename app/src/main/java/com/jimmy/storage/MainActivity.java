package com.jimmy.storage;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        testSp();


    }


    private void testSp() {
        long timeMs = System.currentTimeMillis();
        testSpSave();

        Log.e("jgc", "sp time : " + (System.currentTimeMillis() - timeMs));
        timeMs = System.currentTimeMillis();

        testSpGet();

        Log.e("jgc", "sp time : " + (System.currentTimeMillis() - timeMs));
        timeMs = System.currentTimeMillis();

        testSpGetBigData();
        Log.e("jgc", "sp time : " + (System.currentTimeMillis() - timeMs));
    }

    private void testSpGet() {
        SharedPreferences sp = getSharedPreferences("test", Context.MODE_PRIVATE);
        String test = sp.getString("test", "test");


    }

    private void testSpGetBigData() {
        /*SharedPreferences sp = getSharedPreferences("test_big_data", Context.MODE_PRIVATE);
        // sp.edit().clear().commit();
        SharedPreferences.Editor editor = sp.edit();
        for (int i = 0; i < 100; i++){
            editor.putString("key_"+i, "value_" + i);
        }
        editor.commit();*/


        SharedPreferences sp1 = getSharedPreferences("test_big_data", Context.MODE_PRIVATE);
        sp1.getString("key_1","");

    }

    private void testSpSave() {
        SharedPreferences sp = getSharedPreferences("test", Context.MODE_PRIVATE);
        // sp.edit().clear().commit();
        sp.edit().putString("test","test").commit();

    }



}
