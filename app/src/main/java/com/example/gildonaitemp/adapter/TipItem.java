package com.example.gildonaitemp.adapter;

public class TipItem {
    private String title;
    private String description;
    private int imageResId;
    private boolean hasImage;

    public TipItem(String title, String description, int imageResId, boolean hasImage) {
        this.title = title;
        this.description = description;
        this.imageResId = imageResId;
        this.hasImage = hasImage;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public int getImageResId() {
        return imageResId;
    }

    public boolean hasImage() {
        return hasImage;
    }
}
