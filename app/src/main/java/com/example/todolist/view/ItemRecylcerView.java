package com.example.todolist.view;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class ItemRecylcerView<T,V> {
    private RecyclerView mRcv;
    private List<T> mList;
    private V mAdapter;
    private int mCurrentPage = 1;
    private int mNotePerPage = 3;
    private int mTotalPage = 1;
    private boolean mLoading = false;
    private boolean mLastPage = false;
    private Long mListNoteSize;

    public ItemRecylcerView(RecyclerView mRcv, List<T> mList, V mAdapter, int mCurrentPage,
                            int mNotePerPage, int mTotalPage, boolean mLoading, boolean mLastPage,
                            Long mListNoteSize) {
        this.mRcv = mRcv;
        this.mList = mList;
        this.mAdapter = mAdapter;
        this.mCurrentPage = mCurrentPage;
        this.mNotePerPage = mNotePerPage;
        this.mTotalPage = mTotalPage;
        this.mLoading = mLoading;
        this.mLastPage = mLastPage;
        this.mListNoteSize = mListNoteSize;
    }


}
