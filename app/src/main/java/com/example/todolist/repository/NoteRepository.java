package com.example.todolist.repository;

import android.app.Application;

import com.example.todolist.database.NoteDao;
import com.example.todolist.database.NoteDatabase;
import com.example.todolist.database.NoteEntity;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.Single;

public class NoteRepository {
    private static NoteRepository instance = null;
    private NoteDao noteDao;

    private NoteRepository(Application context){
        noteDao = NoteDatabase.getInstance(context).noteDao();
    }

    public static NoteRepository getInstance(Application context){
        if (instance == null){
            instance = new NoteRepository(context);
        }
        return instance;
    }

    public Observable<List<NoteEntity>> getListNote( int itemLimit, int itemOffset){
        return noteDao.getListNote(itemLimit, itemOffset);
    }
    public Single<Long> getListNoteSize(){return noteDao.getListNoteSize();}

    public Maybe<Long> insertNote(NoteEntity noteEntity){
        return noteDao.insertNote(noteEntity);
    }
    public Maybe<Integer> updateNote(NoteEntity noteEntity){
        return noteDao.updateNote(noteEntity);
    }
    public Completable delete(NoteEntity noteEntity){
        return noteDao.deleteNote(noteEntity);
    }

}