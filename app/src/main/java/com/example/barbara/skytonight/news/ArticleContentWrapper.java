package com.example.barbara.skytonight.news;

import android.graphics.Bitmap;

public class ArticleContentWrapper {

    private String imageUrl;
    private String text;
    private boolean image;

    public ArticleContentWrapper(String string, boolean image) {
        if (image)
            this.imageUrl = string;
        else
            this.text = string;
    }
    public boolean hasImage() {
        return imageUrl != null;
    }

    public boolean hasText() {
        return text != null;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getText() {
        return text;
    }
}
