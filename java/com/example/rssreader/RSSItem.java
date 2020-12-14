package com.example.rssreader;

import android.graphics.Bitmap;

public class RSSItem {

    private String title;
    private String date;
    private String preview;
    private String link;
    private String imageUrl;
    private Bitmap bitmap;

    public RSSItem(String title, String date, String preview, String link, String imageurl) {
        this.title = title;
        this.date = date;
        this.preview = preview;
        this.link = link;
        this.imageUrl = imageurl;
    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPreview() {
        return preview;
    }

    public void setPreview(String preview) {
        this.preview = preview;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
