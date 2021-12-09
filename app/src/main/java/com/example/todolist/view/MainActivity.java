package com.example.todolist.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.database.NoteEntity;
import com.example.todolist.viewmodel.LoadMore;
import com.example.todolist.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    FloatingActionButton mInsertFAB;
    NoteViewModel noteViewModel;
//    Long mListNoteSize;
//    List<NoteEntity> mNoteEntities;
//    Button mBtnDelete,mBtnUpdate, mBtnInsert;
//    TextView mText;
//    EditText mEditTitle, mEditDescription;

    RecyclerView mRcvNote;
    NoteAdapter01 mNoteAdapter;
//    int mCurrentPage = 1;
    final int mNotePerPage = 5;
//    int mTotalPage = 4;
//    boolean mLoading = false;
//    boolean mLastPage = false;

    enum ChangeType {LOAD, DELETE, INSERT, UPDATE}

    ChangeType changeType = ChangeType.LOAD;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        mBtnDelete = findViewById(R.id.buttonDelete);
//        mBtnUpdate = findViewById(R.id.buttonUpdate);
//        mBtnInsert = findViewById(R.id.buttonInsert);
//        mText = findViewById(R.id.textDatabase);
//        mEditTitle = findViewById(R.id.editTitle);
//        mEditDescription = findViewById(R.id.editDescription);

        Log.d("BBB", "onCreate: ");
        noteViewModel = new ViewModelProvider(this, new NoteViewModel.NoteViewModelFactory(getApplication())).get(NoteViewModel.class);
        mInsertFAB = findViewById(R.id.idFAB);

        mRcvNote = findViewById(R.id.recyclerView);
        mNoteAdapter = new NoteAdapter01();
        mNoteAdapter.updateListNote(noteViewModel.loadMore.mNoteEntities,noteViewModel.loadMore.mNoteEntities);
        mRcvNote.setLayoutManager(new LinearLayoutManager(this));
        mRcvNote.setHasFixedSize(true);
        mRcvNote.setAdapter(mNoteAdapter);
        mRcvNote.scrollToPosition(noteViewModel.loadMore.lastFirstVisiblePosition);

        //observe data
        noteViewModel.getListNote().observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
                switch (changeType) {
                    case LOAD:
                        //Nếu mới xoay màn hình thì không dùng phần dữ liệu có sẵn trong noteEntities
                        if(noteViewModel.loadMore.rotation == LoadMore.StartRotation.START_ROTATION ){
                            noteViewModel.loadMore.rotation = LoadMore.StartRotation.STOP_ROTATION;
                        } else {
//                            if (noteViewModel.loadMore.mCurrentPage > 1) {//Không cần kiểm tra, đã kiểm tra ở hàm mNoteAdapter.removeLoading();
//                                mNoteAdapter.removeLoading();
//                            }
//                            mNoteAdapter.removeLoading();
                            removeLoading();
                            noteViewModel.loadMore.mNoteEntities.addAll(noteEntities);
                            mNoteAdapter.updateListNote(noteViewModel.loadMore.mNoteEntities,noteEntities);
                            noteViewModel.loadMore.mLoading = false;
                            if(noteEntities.size()==0) {//Nếu hết data
                                Toast.makeText(MainActivity.this, "No more data", Toast.LENGTH_SHORT).show();
                            }
                        }

                        break;
                    case DELETE:
                        break;
                }
            }
        });

        // call data nếu chưa có dữ liệu (chỉ gọi lúc mới mở app)
        if(noteViewModel.loadMore.mNoteEntities.size()==0){
            noteViewModel.queryGetListNote(mNotePerPage, 0);
        }


        //Scroll recyclerView
        mRcvNote.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) mRcvNote.getLayoutManager()) {
            @Override
            public void loadMoreItem() {
                noteViewModel.loadMore.mLoading = true;
                noteViewModel.loadMore.mCurrentPage += 1;
                addFooter();
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        changeType = ChangeType.LOAD;
                        noteViewModel.queryGetListNote(mNotePerPage, (noteViewModel.loadMore.mCurrentPage-1)*mNotePerPage);
                    }
                },3000);
//                loadNextPage();
            }

            @Override
            public boolean isLoading() {
                return noteViewModel.loadMore.mLoading;
            }

            @Override
            public boolean isLastPage() {
                return noteViewModel.loadMore.mLastPage;
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        noteViewModel.loadMore.lastFirstVisiblePosition = ((LinearLayoutManager)mRcvNote.getLayoutManager()).findFirstCompletelyVisibleItemPosition();
        noteViewModel.loadMore.rotation = LoadMore.StartRotation.START_ROTATION;

        Log.d("BBB", "onDestroy: ");
    }



    private void addFooter() {
        if (!noteViewModel.loadMore.mLastPage) {
//            mNoteAdapter.addFooterLoading();
            addFooterLoading();
        }
    }

    private void loadNextPage() {
//        new CountDownTimer(1000, 800) {
//            @Override
//            public void onTick(long l) {
//
//            }
//
//            @Override
//            public void onFinish() {
//                Long idMax;
//                if (mCurrentPage == 1) {
//                    idMax = Long.MAX_VALUE;
//                } else {
//                    idMax = mNoteEntities
//                            .get(mNoteEntities.size() - 2)
//                            .getId() - 1;
//                }

//                changeType = ChangeType.LOAD;
//                noteViewModel.queryGetListNote(mNotePerPage, 0);
//            }
//        }.start();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                changeType = ChangeType.LOAD;
                noteViewModel.queryGetListNote(mNotePerPage, 0);
            }
        },1500);

//        changeType = ChangeType.LOAD;
//        noteViewModel.queryGetListNote(mNotePerPage, 0);

    }


    public void addFooterLoading() {
        noteViewModel.loadMore.mNoteEntities.add(null);
        mNoteAdapter.updateListNote(noteViewModel.loadMore.mNoteEntities,null);
    }

    //Nếu có item_loading thì xóa
    public void removeLoading() {
        if (noteViewModel.loadMore.mNoteEntities.size() > 0) {
            int position = noteViewModel.loadMore.mNoteEntities.size() - 1;
            if (noteViewModel.loadMore.mNoteEntities.get(position) == null) {
                noteViewModel.loadMore.mNoteEntities.remove(position);
            }
        }
    }


}