package com.example.todolist.database;

import android.app.Application;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = {com.example.todolist.database.NoteEntity.class}, version = 1)
public abstract class NoteDatabase extends RoomDatabase {
    private static NoteDatabase instance = null;

    public abstract com.example.todolist.database.NoteDao noteDao();

    public synchronized static NoteDatabase getInstance(Application application) {
        if (instance == null) {
            instance = Room.databaseBuilder(
                    application,
                    NoteDatabase.class,
                    "note.sql"
            ).build();
        }
        return instance;
    }
}