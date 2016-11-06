package com.r9software.infinitescrolling.model;

/**
 * Created by asus on 05/11/2016.
 */

public class Photo {

    private long ID;
    private String sizeCode;
    private String URL;
    private int width;
    private int height;
    private double quality;
    private String localPath;
    private int imageGroup;
    private long albumID;

    public void setID(long ID) {
        this.ID = ID;
    }

    public long getID() {
        return ID;
    }

    public void setSizeCode(String sizeCode) {
        this.sizeCode = sizeCode;
    }

    public String getSizeCode() {
        return sizeCode;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    public String getURL() {
        return URL;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getWidth() {
        return width;
    }

    public void setHeight(int height) {
        this.height = height;
    }

    public int getHeight() {
        return height;
    }

    public void setQuality(double quality) {
        this.quality = quality;
    }

    public double getQuality() {
        return quality;
    }

    public void setLocalPath(String localPath) {
        this.localPath = localPath;
    }

    public String getLocalPath() {
        return localPath;
    }

    public int getImageGroup() {
        return imageGroup;
    }

    public void setImageGroup(int imageGroup) {
        this.imageGroup = imageGroup;
    }

    public long getAlbumID() {
        return albumID;
    }

    public void setAlbumID(long albumID) {
        this.albumID = albumID;
    }
}
