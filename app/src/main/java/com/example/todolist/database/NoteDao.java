package com.example.todolist.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

import io.reactivex.rxjava3.core.Completable;
import io.reactivex.rxjava3.core.Maybe;
import io.reactivex.rxjava3.core.Observable;

@Dao
public interface NoteDao {

//    @Query("Select * from note limit 2")
    @Query("Select * from note order by id desc")
    Observable<List<com.example.todolist.database.NoteEntity>> getListNote();

    @Insert
    Maybe<Long> insertNote(com.example.todolist.database.NoteEntity noteEntity);

    @Update
    Maybe<Integer> updateNote(com.example.todolist.database.NoteEntity noteEntity);

    @Delete
    Completable deleteNote(com.example.todolist.database.NoteEntity noteEntity);

}