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

import com.google.gson.Gson;
import com.r9software.infinitescrolling.dao.AbstractBaseSqliteDao;
import com.r9software.infinitescrolling.dao.PhotosSqliteImpl;
import com.r9software.infinitescrolling.dao.SQLiteDatabaseHelper;
import com.r9software.infinitescrolling.model.Album;
import com.r9software.infinitescrolling.model.Photo;
import com.r9software.infinitescrolling.util.Constants;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

    private Handler mHandler = new Handler();
    private static final String QUERY = Constants.QUERY;
    private final PhotosSqliteImpl dao = new PhotosSqliteImpl();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initParams();
    }

    private void initParams() {
        SQLiteDatabaseHelper.init(getApplicationContext());
        hideActionBar();
        mHandler.post(new Runnable() {
                          @Override
                          public void run() {
                              int number = dao.getNumberofPictures();
                              //validate if we have data from the server
                              if (number == 0) {
                                  //download the data
                                  downloadData();
                              } else {
                                  //we already have data
                                  Log.d(getClass().getSimpleName(), "Number of rows " + number);
                                  startMainActivity();

                              }
                          }
                      }
        );
    }

    private void startMainActivity() {
        Intent mIntent = new Intent(SplashActivity.this, MainActivity.class);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(mIntent);
    }

    /*
    * Single method to download data and process data from the server
    * */
    OkHttpClient client = new OkHttpClient().newBuilder().build();

    private void downloadData() {
        String url = Constants.BASE_URL_API + QUERY;
        Log.d(getClass().getSimpleName(), url);
        Request request = new Request.Builder()
                .addHeader("Cookie", Constants.COOKIE_VALUE)
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
        Log.d(getClass().getSimpleName(), jsonResponse);
        try {
            JSONObject mJson = new JSONObject(jsonResponse);
            final Album album = new Album();
            album.setName(mJson.getJSONObject("data").getJSONObject("album").getString("name"));
            album.setAlbumID(mJson.getJSONObject("data").getJSONObject("album").getString("id"));
            final JSONArray records = mJson.getJSONObject("data").getJSONObject("album").getJSONObject("photos").getJSONArray("records");
            mJson = null;
            dao.insertAlbum(album, new AbstractBaseSqliteDao.DbInsertInterface() {
                @Override
                public void onInsert(long id) {
                    //insert the pictures of the album

                    List<Photo> photos = new ArrayList<>();
                    try {
                        for (int x = 0; x < records.length(); x++) {
                            JSONObject record = records.getJSONObject(x);
                            JSONArray urlArray = record.getJSONArray("urls");
                            for(int y=0; y < urlArray.length();y++){
                                String size=urlArray.getJSONObject(y).getString("size_code");
                                String url=urlArray.getJSONObject(y).getString("url");
                                int width=urlArray.getJSONObject(y).getInt("width");
                                int height=urlArray.getJSONObject(y).getInt("height");
                                double quality=urlArray.getJSONObject(y).getDouble("quality");
                                Photo mPhoto= new Photo(size,url,width,height,quality,x,id);
                                photos.add(mPhoto);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dao.insertPhotos(photos);
                    startMainActivity();
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void hideActionBar() {
        // Hide UI first
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.hide();
        }
    }

}
