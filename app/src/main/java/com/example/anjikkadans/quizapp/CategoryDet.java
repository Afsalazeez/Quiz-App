package com.example.anjikkadans.quizapp;

/**
 * Created by Anjikkadan's on 4/24/2018.
 */

public class CategoryDet {
    private int imageUrl;
    private String buttonText;

    public CategoryDet(int imageUrl, String buttonText) {
        this.imageUrl = imageUrl;
        this.buttonText = buttonText;
    }

    public int getImageUrl() {
        return imageUrl;
    }

    public String getButtonText() {
        return buttonText;
    }
}
