package com.example.todolist.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.database.NoteEntity;
import com.example.todolist.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {


    FloatingActionButton mInsertFAB;
    NoteViewModel noteViewModel;
    List<NoteEntity> mNoteEntities;
//    Button mBtnDelete,mBtnUpdate, mBtnInsert;
//    TextView mText;
//    EditText mEditTitle, mEditDescription;

    RecyclerView mRcvNote;
    NoteAdapter mNoteAdapter;
    int mCurrentPage = 1;
    int mTotalPage = 1;
    boolean mLoading = false;
    boolean mLastPage = false;


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

        mInsertFAB = findViewById(R.id.idFAB);

        mRcvNote = findViewById(R.id.recyclerView);
        mNoteEntities = new ArrayList<>();
//        mNoteEntities = getMock();
        mNoteAdapter = new NoteAdapter(mNoteEntities);
        mRcvNote.setLayoutManager(new LinearLayoutManager(this));
        mRcvNote.setHasFixedSize(true);
        mRcvNote.setAdapter(mNoteAdapter);


        noteViewModel = new ViewModelProvider(this,new NoteViewModel.NoteViewModelFactory(getApplication())).get(NoteViewModel.class);


        //observe data
        noteViewModel.getListNote().observe(this, new Observer<List<NoteEntity>>() {
            @Override
            public void onChanged(List<NoteEntity> noteEntities) {
//                mNoteAdapter.updateList(noteEntities);

                mNoteEntities.clear();
                mNoteEntities.addAll(noteEntities);
                if(mCurrentPage == 1) {addFooter();}
                mNoteAdapter.notifyDataSetChanged();
                Log.d("BBB", "Current page: "+ mCurrentPage + "; Số item: "+noteEntities.size());
            }
        });

        noteViewModel.getIdUpdate().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                Log.d("BBB","Id update " + integer);
            }
        });

        noteViewModel.getIdInsert().observe(this, new Observer<Long>() {
            @Override
            public void onChanged(Long aLong) {
                Log.d("BBB","Id insert " + aLong);
            }
        });

        // call data
        noteViewModel.queryGetListNote();

        //Insert Note
        mInsertFAB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, InsertEditNoteActivity.class);
                startActivity(intent);
//                finish();
            }
        });

        //Update note (click vào item)
        mNoteAdapter.setOnClickItemListener(new NoteClickInterface() {
            @Override
            public void onNoteClick(NoteEntity noteEntity) {
                Intent intent = new Intent(MainActivity.this, InsertEditNoteActivity.class);
                intent.putExtra("noteType", "Edit");
                intent.putExtra("noteTitle", noteEntity.getTitle());
                intent.putExtra("noteDescription", noteEntity.getDescription());
                intent.putExtra("noteId", noteEntity.getId());
                startActivity(intent);
            }
        });

        //Delete
        mNoteAdapter.setOnDeleteItemListener(new NoteClickDeleteInterface() {
            @Override
            public void onDeleteIconClick(NoteEntity noteEntity) {
                Toast.makeText(
                        MainActivity.this,
                        "Xóa thẻ " + noteEntity.getTitle(),
                        Toast.LENGTH_SHORT)
                        .show();
//                mListFood.remove(mListFood.get(position));
//                mFoodAdapter.notifyItemRemoved(position);
                noteViewModel.deletedNote(noteEntity);
            }
        });

        //Scroll recyclerView
        mRcvNote.addOnScrollListener(new PaginationScrollListener((LinearLayoutManager) mRcvNote.getLayoutManager()) {
            @Override
            public void loadMoreItem() {
                mLoading = true;
                mCurrentPage +=1;

                loadNextPage();
            }

            @Override
            public boolean isLoading() {
                return mLoading;
            }

            @Override
            public boolean isLastPage() {
                return mLastPage;
            }
        });

//        //Insert thủ công
//        noteViewModel.insertNote(new NoteEntity("Work 5",
//                        "Do 5",
//                new SimpleDateFormat("dd MMM, yyyy - HH:mm").format(new Date())));

//        //Insert data
//        mBtnInsert.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                noteViewModel.insertNote(new NoteEntity(mEditTitle.getText().toString(),
//                        mEditDescription.getText().toString(),
//                new SimpleDateFormat("dd MMM, yyyy - HH:mm").format(new Date())));
//            }
//        });
//
//        //Delete data
//        mBtnDelete.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                noteViewModel.deletedNote(mNoteEntities.get(mNoteEntities.size() - 1));
//            }
//        });
//
//        //Update data
//        mBtnUpdate.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NoteEntity noteEntity = mNoteEntities.get(mNoteEntities.size() - 1);
//                noteEntity.setDescription("Do something 3.1");
//                noteEntity.setTitle("Work 3.1");
//                noteEntity.setTimestamp(new SimpleDateFormat("dd MMM, yyyy - HH:mm").format(new Date()));
//                noteViewModel.updateNote(noteEntity);
//            }
//        });

    }

    private void addFooter(){
        if (mCurrentPage < mTotalPage){
            mNoteAdapter.addFooterLoading();
        }else{
            mLastPage = true;
        }
    }

    private void loadNextPage(){
//        if (mCurrentPage > 1){
//            mNoteAdapter.removeLoading();
//        }
//        mNoteEntities.addAll(getMock());
//        mNoteAdapter.notifyDataSetChanged();
//        mLoading = false;
//
//        addFooter();
    }

//    private void loadNextPage(){
//        new CountDownTimer(2000 , 2000) {
//            @Override
//            public void onTick(long l) {
//
//            }
//
//            @Override
//            public void onFinish() {
////                Log.d("BBB","Trang: " + mCurrentPage + "");
//                if (mCurrentPage > 1){
//                    mNoteAdapter.removeLoading();
//                }
//                mNoteEntities.addAll(getMock());
//                mNoteAdapter.notifyDataSetChanged();
//                mLoading = false;
//
//                addFooter();
//
//            }
//        }.start();
//    }

//    private List<NoteEntity> getMock(){
//        return new ArrayList<>(Arrays.asList(
//                new NoteEntity("W6", "D6","T6"),
//                new NoteEntity("W7", "D7","T7"),
//                new NoteEntity("W8", "D8","T8"),
//                new NoteEntity("W9", "D9","T9"),
//                new NoteEntity("W10", "D10","T10")
//        ));
//    }

}