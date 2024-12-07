package com.example.wallet.ui.models;

public class TipUI {
    private final String thumbnailUrl;
    private final String title;
    private final String description;
    private final String youtubeUrl;

    public TipUI(String thumbnailUrl, String title, String description, String youtubeUrl) {
        this.thumbnailUrl = thumbnailUrl;
        this.title = title;
        this.description = description;
        this.youtubeUrl = youtubeUrl;
    }

    // Getters
    public String getThumbnailUrl() { return thumbnailUrl; }
    public String getTitle() { return title; }
    public String getDescription() { return description; }
    public String getYoutubeUrl() { return youtubeUrl; }
}
