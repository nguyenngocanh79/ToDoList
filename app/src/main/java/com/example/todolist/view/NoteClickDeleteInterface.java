package com.example.todolist.view;

import com.example.todolist.database.NoteEntity;

public interface NoteClickDeleteInterface {
    void onDeleteIconClick(int position, NoteEntity noteEntity);
}

