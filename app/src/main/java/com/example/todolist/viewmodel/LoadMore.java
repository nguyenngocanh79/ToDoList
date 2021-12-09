package com.example.todolist.viewmodel;

import com.example.todolist.database.NoteEntity;


import java.util.ArrayList;
import java.util.List;

public class LoadMore {
    public Long mListNoteSize;
    public List<NoteEntity> mNoteEntities = new ArrayList<>();
    public int mCurrentPage = 1;
    public final int mNotePerPage = 3;
    public int mTotalPage = 4;
    public boolean mLoading = false;
//    public boolean mGetResultEnable = true;
    public boolean mLastPage = false;
    public static enum ChangeType {LOAD, DELETE, INSERT, UPDATE}
    public ChangeType changeType = ChangeType.LOAD;
    public static enum StartRotation {START_ROTATION, STOP_ROTATION}
    public StartRotation rotation = StartRotation.STOP_ROTATION;
    public int lastFirstVisiblePosition = 0;



}
