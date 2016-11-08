package com.r9software.infinitescrolling.adapters;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.r9software.infinitescrolling.R;
import com.r9software.infinitescrolling.dao.PhotosSqliteImpl;
import com.r9software.infinitescrolling.model.Photo;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.List;

/**
 * Created by asus on 06/11/2016.
 */

public class ImageGalleryAdapter extends RecyclerView.Adapter<ImageViewHolder> {
    List<Photo> photos;
    Context context;
    public ImageGalleryAdapter(List<Photo> photos) {
        this.photos = photos;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.image_item, parent, false);
        return new ImageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ImageViewHolder holder, final int position) {
        if (!TextUtils.isEmpty(photos.get(position).getLocalPath())) {
            holder.imageView.setImageBitmap(BitmapFactory.decodeFile(photos.get(position).getLocalPath()));
        }
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //implement the use of the large image of this group of Images

                PhotosSqliteImpl dao= new PhotosSqliteImpl();
                Photo photoLarge= dao.getLargeVersionOF(photos.get(position).getImageGroup());
                new MyDownloadImageTask(photoLarge).execute();
            }
        });
    }

    ProgressDialog pDialog;

    private class MyDownloadImageTask extends AsyncTask<Void, String, Void> {

        Photo localPhoto;

        public MyDownloadImageTask(Photo photos) {
            this.localPhoto = photos;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(context);
            pDialog.setMessage("Loading Image in full size ...");
            pDialog.show();
        }

        protected Void doInBackground(Void... args) {


            InputStream input = null;
            FileOutputStream output = null;

            try {
                String outputName = localPhoto.getID() + "_" + localPhoto.getImageGroup() + "_fullSize.png";
                File file = new File(context.getCacheDir(), outputName);
                if (!file.exists()) {
                    file.getParentFile().mkdirs();
                    URL url = new URL(localPhoto.getURL());
                    input = url.openConnection().getInputStream();
                    output = new FileOutputStream(file);
                    int read;
                    byte[] data = new byte[1024];
                    while ((read = input.read(data)) != -1)
                        output.write(data, 0, read);
                    localPhoto.setLocalPath(file.getAbsolutePath());
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


        return null;
    }

    protected void onPostExecute(Void voi) {
        pDialog.dismiss();
        if (!TextUtils.isEmpty(localPhoto.getLocalPath())) {
            PhotosSqliteImpl dao= new PhotosSqliteImpl();
            dao.updateLocalPathPhoto(localPhoto.getLocalPath(), localPhoto.getID());
            AlertDialog.Builder alertadd = new AlertDialog.Builder(
                    context);
            LayoutInflater factory = LayoutInflater.from(context);
            final View localView = factory.inflate(R.layout.image_large_item, null);
            ((ImageView) localView.findViewById(R.id.imageView)).setImageBitmap(BitmapFactory.decodeFile(localPhoto.getLocalPath()));
            alertadd.setView(localView);
            alertadd.show();
        }else{
            Toast.makeText(context,"We couldn't retrieve the image try again later",Toast.LENGTH_SHORT).show();
        }
    }

}

    @Override
    public int getItemCount() {
        return photos.size();
    }
}
