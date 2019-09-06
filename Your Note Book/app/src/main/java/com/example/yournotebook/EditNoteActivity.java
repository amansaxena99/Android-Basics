package com.example.yournotebook;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.inputmethod.InputMethod;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashSet;

import static com.example.yournotebook.MainActivity.notes;
import static com.example.yournotebook.MainActivity.notesAdapter;
import static com.example.yournotebook.MainActivity.saveData;

public class EditNoteActivity extends AppCompatActivity {

    Intent intent;
    int postion;
    EditText noteEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_note);

        noteEditText = findViewById(R.id.noteEditText);

        intent = getIntent();
        postion = intent.getIntExtra("notePosition", -1);

        if (postion == -1){
            postion = notes.size();
            notes.add("");
            notesAdapter.notifyDataSetChanged();
            saveData(notes);
        }

        noteEditText.setText(notes.get(postion));

        noteEditText.requestFocus();

        InputMethodManager methodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        methodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        noteEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                notes.set(postion, String.valueOf(s));
                notesAdapter.notifyDataSetChanged();
                saveData(notes);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}
