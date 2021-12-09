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
import io.reactivex.rxjava3.core.Single;

@Dao
public interface NoteDao {

//    @Query("Select * from note limit 2")
//    @Query("Select * from note " +
//            "Where id <= :idMax " +
//            "order by id desc limit :itemLimit")
//    Observable<List<com.example.todolist.database.NoteEntity>> getListNote(Long idMax, int itemLimit);

    @Query("Select * from note " +
            "limit :itemLimit offset :itemOffset")
    Observable<List<com.example.todolist.database.NoteEntity>> getListNote(int itemLimit, int itemOffset);

    @Query("SELECT COUNT(*) FROM note")
    Single<Long> getListNoteSize();

    @Insert
    Maybe<Long> insertNote(com.example.todolist.database.NoteEntity noteEntity);

    @Update
    Maybe<Integer> updateNote(com.example.todolist.database.NoteEntity noteEntity);

    @Delete
    Completable deleteNote(com.example.todolist.database.NoteEntity noteEntity);

}