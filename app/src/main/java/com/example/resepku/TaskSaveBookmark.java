package com.example.resepku;

import android.os.AsyncTask;

public class TaskSaveBookmark extends AsyncTask<Bookmark, Void, Void> {
    AppDatabase db;

    public TaskSaveBookmark(AppDatabase db) {
        this.db = db;
    }

    @Override
    protected Void doInBackground(Bookmark... bookmarks) {
        db.bookmarkDao().insert(bookmarks[0]);
        return null;
    }
}