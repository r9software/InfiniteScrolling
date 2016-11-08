package com.r9software.infinitescrolling.fragments;


import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.URLUtil;

import com.r9software.infinitescrolling.R;
import com.r9software.infinitescrolling.adapters.ImageGalleryAdapter;
import com.r9software.infinitescrolling.adapters.NumberedAdapter;
import com.r9software.infinitescrolling.dao.PhotosSqliteImpl;
import com.r9software.infinitescrolling.listeners.InifniteScrollListener;
import com.r9software.infinitescrolling.model.Photo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MyPicturesFragment extends Fragment {
    PhotosSqliteImpl dao = new PhotosSqliteImpl();
    Handler handler = new Handler();
    private ImageGalleryAdapter adapter;
    private List<Photo> photos = new ArrayList<>();
    private static int LIMIT = 12;

    public MyPicturesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        List<Photo> copy = dao.getMediumPhotos(LIMIT);
        processAndDownloadPhotos(copy);
        View mView = inflater.inflate(R.layout.fragment_my_pictures, container, false);
        RecyclerView recyclerView = (RecyclerView) mView.findViewById(R.id.recyclerView);
        GridLayoutManager mGrid = new GridLayoutManager(getActivity().getApplicationContext(), 3);
        recyclerView.setLayoutManager(mGrid);
        adapter = new ImageGalleryAdapter(photos);
        recyclerView.setAdapter(adapter);
        InifniteScrollListener scrollListener = new InifniteScrollListener(mGrid) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                //Log.d(getClass().getSimpleName(), "Total items " + totalItemsCount + " current page " + page);
                List<Photo> nextPage = dao.getMediumPhotos(photos.get(photos.size()-1).getID(), LIMIT);
                processAndDownloadPhotos(nextPage);
            }
        };
        recyclerView.addOnScrollListener(scrollListener);
        return mView;
    }

    private void processAndDownloadPhotos(final List<Photo> photos) {
        new MyDownloadImageTask(photos).execute();
    }


    private class MyDownloadImageTask extends AsyncTask<Void, String, Void> {

        List<Photo> localPhotos;

        public MyDownloadImageTask(List<Photo> photos) {
            this.localPhotos = photos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        protected Void doInBackground(Void... args) {


            InputStream input = null;
            FileOutputStream output = null;

            for (Photo mPhoto : localPhotos) {
                try {
                    String outputName = URLUtil.guessFileName(mPhoto.getURL(), null, null);
                    File file = new File(getActivity().getApplicationContext().getCacheDir(), outputName);
                    if (!file.exists()) {
                        file.getParentFile().mkdirs();
                        URL url = new URL(mPhoto.getURL());
                        input = url.openConnection().getInputStream();
                        output = new FileOutputStream(file);
                        int read;
                        byte[] data = new byte[1024];
                        while ((read = input.read(data)) != -1)
                            output.write(data, 0, read);
                        mPhoto.setLocalPath(file.getAbsolutePath());
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (output != null)

                            output.close();
                        if (input != null)
                            input.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            for (Photo photo : localPhotos) {
                photos.add(photo);
            }
            for (Photo photo : localPhotos) {
                if (!TextUtils.isEmpty(photo.getLocalPath()))
                    dao.updateLocalPathPhoto(photo.getLocalPath(), photo.getID());
            }
            return null;
        }

        protected void onPostExecute(Void voi) {
            adapter.notifyDataSetChanged();

        }
    }
}
