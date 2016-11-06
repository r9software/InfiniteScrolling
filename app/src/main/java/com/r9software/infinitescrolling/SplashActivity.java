package com.r9software.infinitescrolling;

import android.annotation.SuppressLint;
import android.content.ContextWrapper;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.r9software.infinitescrolling.dao.PhotosSqliteImpl;
import com.r9software.infinitescrolling.dao.SQLiteDatabaseHelper;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class SplashActivity extends AppCompatActivity {

    private static final String AUTH_COOKIE = "__dev.waldo.auth__";
    private Handler mHandler = new Handler();
    private String DB_PATH;
    private static final String QUERY = "query={" +
            " album(id: \"YWxidW06ZjNjNWE4ZTQtMzRhNy00NWI0LWFmZGQtOTIxNTJhZmNmZTgz\") {\n" +
            "    id\n" +
            "    name\n" +
            "    photos {\n" +
            "      records {\n" +
            "        urls {\n" +
            "          size_code\n" +
            "          url\n" +
            "          width\n" +
            "          height\n" +
            "          quality\n" +
            "          mime\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}\n";
    private String DB_NAME = "database.db";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initParams();
    }

    private void initParams() {
        SQLiteDatabaseHelper.init(getApplicationContext());
        final PhotosSqliteImpl dao = new PhotosSqliteImpl();
        DB_PATH = getFilesDir().getAbsolutePath() + "/databases/";
        hideActionBar();
        mHandler.post(new Runnable() {
                          @Override
                          public void run() {
                              //validate if we have data from the server
                              if (dao.getNumberofPictures() == 0) {
                                  //download the data
                                  downloadData();
                              }
                              else{
                                  //we already have data
                                  startActivity(new Intent(SplashActivity.this,MainActivity.class));
                              }
                          }
                      }
        );
    }

    /*
    * Single method to download data and process data from the server
    * */
    OkHttpClient client = new OkHttpClient().newBuilder().build();

    private void downloadData() {
        String url = "https://core-graphql.dev.waldo.photos/gql?" + QUERY;
        Log.d(getClass().getSimpleName(), url);
        Request request = new Request.Builder()
                .addHeader("Cookie", "__dev.waldo.auth__=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJhY2NvdW50X2lkIjoiZTBmYWRkODQtNGE3Ny00MzZkLWE0MmUtNWRmNzFlNTJlNTYxIiwicm9sZXMiOlsiYWRtaW5pc3RyYXRvciJdLCJpc3MiOiJ3YWxkbzpjb3JlIiwiZ3JhbnRzIjpbImFsYnVtczpkZWxldGU6KiIsImFsYnVtczpjcmVhdGU6KiIsImFsYnVtczplZGl0OioiLCJhbGJ1bXM6dmlldzoqIl0sImV4cCI6MTQ4MDk5MzQ1MywiaWF0IjoxNDc4NDAxNDUzfQ.AxYbPEtDtlTeSgS2MQMe_vK_1mXoLR6DkWmr-E9CXHU")
                .url(url)
                .build();
        Call call = client.newCall(request);
        call.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.d(getClass().getSimpleName(), "error " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String body = response.body().string();
                if (response.code() == 200) {
                    processData(body);
                }
            }
        });
    }

    private void processData(String jsonResponse) {

    }

    private void hideActionBar() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

}
