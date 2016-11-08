package com.r9software.infinitescrolling.dao;

import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import com.r9software.infinitescrolling.model.Album;
import com.r9software.infinitescrolling.model.Photo;

import java.security.Policy;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by asus on 05/11/2016.
 */

public class PhotosSqliteImpl extends AbstractBaseSqliteDao {
    public PhotosSqliteImpl(){

    }
    public void insertPhotos(List<Photo> photos)
    {
        int count=photos.size();
        for(final Photo photo:photos){
            count--;
            ContentValues vals= new ContentValues();
            vals.put("size_code",photo.getSizeCode());
            vals.put("url",photo.getURL());
            vals.put("width",photo.getWidth());
            vals.put("height",photo.getHeight());
            vals.put("quality",photo.getQuality());
            vals.put("local_path",photo.getLocalPath());
            vals.put("image_group",photo.getImageGroup());
            vals.put("album_id",photo.getAlbumID());
            insertMultiple("photos", vals, count,null, new DbInsertInterface() {
                @Override
                public void onInsert(long id) {
                    photo.setID(id);
                }
            });
        }

    }
    public void insertAlbum(Album album, DbInsertInterface dbInsert){
        ContentValues cont= new ContentValues();
        cont.put("album_id",album.getAlbumID());
        cont.put("name",album.getName());
        insert("album",cont,null,dbInsert);
    }
    public void updateLocalPathPhoto(String path,long id){
        ContentValues val= new ContentValues();
        val.put("local_path",path);
        update("photos",val,"id=?",new String[]{String.valueOf(id)});
    }
    public List<Photo> getMediumPhotos() {
        StringBuffer sql = new StringBuffer("Select id,size_code,url,width,height,quality,local_path,image_group from photos where size_code like \"%medium\"");
        String[] args = new String[]{

        };

        final List<Photo> result = new ArrayList<Photo>();
        query(sql, args, new DbQueryInterface() {
            @Override
            public void onCursor(Cursor cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Photo photo = new Photo();
                    photo.setID(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    photo.setSizeCode(cursor.getString(cursor.getColumnIndexOrThrow("size_code")));
                    photo.setURL(cursor.getString(cursor.getColumnIndexOrThrow("url")));
                    photo.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow("width")));
                    photo.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow("height")));
                    photo.setImageGroup(cursor.getInt(cursor.getColumnIndexOrThrow("image_group")));
                    photo.setQuality(cursor.getDouble(cursor.getColumnIndexOrThrow("quality")));
                    photo.setLocalPath(cursor.getString(cursor.getColumnIndexOrThrow("local_path")));
                    result.add(photo);
                    cursor.moveToNext();
                }
            }
        });

        return result;
    }
    public List<Photo> getMediumPhotos(int limit) {
        StringBuffer sql = new StringBuffer("Select id,size_code,url,width,height,quality,local_path,image_group from photos where size_code like \"%medium\"  ORDER BY id LIMIT ? ");
        String[] args = new String[]{
            String.valueOf(limit)
        };

        final List<Photo> result = new ArrayList<Photo>();
        query(sql, args, new DbQueryInterface() {
            @Override
            public void onCursor(Cursor cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Photo photo = new Photo();
                    photo.setID(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    photo.setSizeCode(cursor.getString(cursor.getColumnIndexOrThrow("size_code")));
                    photo.setURL(cursor.getString(cursor.getColumnIndexOrThrow("url")));
                    photo.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow("width")));
                    photo.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow("height")));
                    photo.setImageGroup(cursor.getInt(cursor.getColumnIndexOrThrow("image_group")));
                    photo.setQuality(cursor.getDouble(cursor.getColumnIndexOrThrow("quality")));
                    photo.setLocalPath(cursor.getString(cursor.getColumnIndexOrThrow("local_path")));
                    result.add(photo);
                    cursor.moveToNext();
                }
            }
        });

        return result;
    }
    public List<Photo> getMediumPhotos(long id,int limit) {
        StringBuffer sql = new StringBuffer("Select id,size_code,url,width,height,quality,local_path,image_group from photos where size_code like \"%medium\"  AND id > ?  ORDER BY id LIMIT ?");
        String[] args = new String[]{
                String.valueOf(id),
                String.valueOf(limit)
        };

        final List<Photo> result = new ArrayList<Photo>();
        query(sql, args, new DbQueryInterface() {
            @Override
            public void onCursor(Cursor cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Photo photo = new Photo();
                    photo.setID(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    photo.setSizeCode(cursor.getString(cursor.getColumnIndexOrThrow("size_code")));
                    photo.setURL(cursor.getString(cursor.getColumnIndexOrThrow("url")));
                    photo.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow("width")));
                    photo.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow("height")));
                    photo.setImageGroup(cursor.getInt(cursor.getColumnIndexOrThrow("image_group")));
                    photo.setQuality(cursor.getDouble(cursor.getColumnIndexOrThrow("quality")));
                    photo.setLocalPath(cursor.getString(cursor.getColumnIndexOrThrow("local_path")));
                    result.add(photo);
                    cursor.moveToNext();
                }
            }
        });

        return result;
    }
    public Photo getLargeVersionOF(int group) {
        StringBuffer sql = new StringBuffer("Select id,size_code,url,width,height,quality,local_path,image_group from photos where size_code like \"%large\" and image_group= ?");
        String[] args = new String[]{
                String.valueOf(group)
        };

        final List<Photo> result = new ArrayList<Photo>();
        query(sql, args, new DbQueryInterface() {
            @Override
            public void onCursor(Cursor cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    Photo photo = new Photo();
                    photo.setID(cursor.getInt(cursor.getColumnIndexOrThrow("id")));
                    photo.setSizeCode(cursor.getString(cursor.getColumnIndexOrThrow("size_code")));
                    photo.setURL(cursor.getString(cursor.getColumnIndexOrThrow("url")));
                    photo.setWidth(cursor.getInt(cursor.getColumnIndexOrThrow("width")));
                    photo.setHeight(cursor.getInt(cursor.getColumnIndexOrThrow("height")));
                    photo.setImageGroup(cursor.getInt(cursor.getColumnIndexOrThrow("image_group")));
                    photo.setQuality(cursor.getDouble(cursor.getColumnIndexOrThrow("quality")));
                    photo.setLocalPath(cursor.getString(cursor.getColumnIndexOrThrow("local_path")));
                    result.add(photo);
                    cursor.moveToNext();
                }
            }
        });

        return result.get(0);
    }
    public int getNumberofPictures(){
        StringBuffer sql = new StringBuffer("" +
                "Select count(1) AS number_of_photos from photos");
        String[] args = new String[]{

        };

        final int[] result = {0};
        query(sql, args, new DbQueryInterface() {
            @Override
            public void onCursor(Cursor cursor) {
                cursor.moveToFirst();
                while (!cursor.isAfterLast()) {
                    result[0] =cursor.getInt(cursor.getColumnIndexOrThrow("number_of_photos"));
                    cursor.moveToNext();
                }
            }
        });
        return result[0];
    }

}
