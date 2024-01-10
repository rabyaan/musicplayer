package com.example.simplemusicplayerv2;

public class Song {
    private String title;
    private String artist;
    private String filePath;

    public Song(String title, String filePath) {
        this.title = title;
        this.filePath = filePath;
    }

    public String getTitle() {
        return title;
    }

    public String getArtist() {
        return artist;
    }

    public String getFilePath() {
        return "src/main/songs/" + title;
    }

    @Override
    public String toString() {
        return title;
    }
}
