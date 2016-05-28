package com.example.bettercharades;

/**
 * Created by Deb Banerji on 28-May-16.
 */
public class ResultPair {
    private String item;
    private boolean isCorrect;

    ResultPair(String item, boolean isCorrect) {
        this.item = item;
        this.isCorrect = isCorrect;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public String getItem() {
        return item;
    }
}