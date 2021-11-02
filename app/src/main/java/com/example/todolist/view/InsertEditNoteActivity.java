package com.example.todolist.view;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.todolist.R;
import com.example.todolist.database.NoteEntity;
import com.example.todolist.viewmodel.NoteViewModel;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InsertEditNoteActivity extends AppCompatActivity {
    EditText mNoteTitleEdt, mNoteEdt;
    Button mSaveBtn;
    NoteViewModel noteViewModel;
    String noteType = "";
    long noteID = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_edit_note);

        mNoteTitleEdt = findViewById(R.id.idEdtNoteTitle);
        mNoteEdt = findViewById(R.id.idEdtNoteDesc);
        mSaveBtn = findViewById(R.id.idBtn);



        noteViewModel = new ViewModelProvider(this,
                new NoteViewModel.NoteViewModelFactory(getApplication())).get(NoteViewModel.class);


        Intent intent = getIntent();
        if (intent != null){
            noteType = intent.getStringExtra("noteType");
            if(noteType == null) noteType ="";
            if (noteType.equals("Edit")) {
                // on below line we are setting data to edit text.
                String noteTitle = intent.getStringExtra("noteTitle");
                String noteDescription = intent.getStringExtra("noteDescription");
                noteID = intent.getLongExtra("noteId", -1);
                mSaveBtn.setText("Update Note");
                mNoteTitleEdt.setText(noteTitle);
                mNoteEdt.setText(noteDescription);
            } else {
                mSaveBtn.setText("Save Note");
            }
        }

        mSaveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String noteTitle = mNoteTitleEdt.getText().toString();
                String noteDescription = mNoteEdt.getText().toString();

                if(noteType.equals("Edit")){
                    if((!noteTitle.isEmpty()) && !(noteDescription.isEmpty())){

                        NoteEntity noteEntity = new NoteEntity(noteTitle,
                                noteDescription,
                                new SimpleDateFormat("dd MMM, yyyy - HH:mm").format(new Date()) );
                        noteEntity.setId(noteID);
                        noteViewModel.updateNote(noteEntity);
                    }
                    Toast.makeText(InsertEditNoteActivity.this, noteTitle + " updated", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(InsertEditNoteActivity.this, MainActivity.class));
                    finish();

                } else {
                    if((!noteTitle.isEmpty()) && !(noteDescription.isEmpty())){
                        noteViewModel.insertNote(new NoteEntity(noteTitle,
                                noteDescription,
                                new SimpleDateFormat("dd MMM, yyyy - HH:mm").format(new Date())));
                    }
                    Toast.makeText(InsertEditNoteActivity.this, noteTitle + " added", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(InsertEditNoteActivity.this, MainActivity.class));
                    finish();
                }


            }
        });

    }
}
