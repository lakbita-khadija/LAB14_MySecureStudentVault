package com.example.mysecurestudentvault.models;

public class ModuleRecord {

    public final int id;
    public final String title;
    public final int progress;
    public final int score;

    public ModuleRecord(int id, String title, int progress, int score) {
        this.id = id;
        this.title = title;
        this.progress = progress;
        this.score = score;
    }
}