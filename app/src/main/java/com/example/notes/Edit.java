package com.example.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;
import java.util.Calendar;

public class Edit extends AppCompatActivity {
    Toolbar toolbar;
    EditText noteTitle, noteDetails;
    Calendar c;
    String todaysDate, currentTime;
    Note note;
    NoteDatabase db = new NoteDatabase(this);
    Long id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);

        Intent i = getIntent();
        id = i.getLongExtra("ID",0);
//        NoteDatabase db = new NoteDatabase(this);
        note = db.getNote(id);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(note.getTitle());
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        noteTitle = (EditText) findViewById(R.id.noteTitle);
        noteDetails = (EditText) findViewById(R.id.noteDetails);

        noteTitle.setText(note.getTitle());
        noteDetails.setText(note.getContent());

        noteTitle.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0){
                    getSupportActionBar().setTitle(s);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        // get current date and time

        c = Calendar.getInstance();
        todaysDate = c.get(Calendar.DAY_OF_MONTH) + "/" + (c.get(Calendar.MONTH)+1) + "/" + c.get(Calendar.YEAR);
        currentTime = pad(c.get(Calendar.HOUR_OF_DAY)) + ":" + pad(c.get(Calendar.MINUTE)) + ":" + pad(c.get(Calendar.SECOND));

        Log.d("Calendar", "Date and Time : " + todaysDate + " and " + currentTime);

    }

    private String pad(int i) {
        if(i < 10){
            return "0" + i;
        }
        else{
            return String.valueOf(i);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.save_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if(item.getItemId() == R.id.save) {
            if (noteTitle.getText().length() != 0) {
                note = new Note(id, noteTitle.getText().toString(), noteDetails.getText().toString(), todaysDate, currentTime);
                note.setTitle(noteTitle.getText().toString());
                note.setContent(noteDetails.getText().toString());
                int id = db.editNote(note);
                Toast.makeText(this, "Note Updated.", Toast.LENGTH_SHORT).show();

//                if(id==note.getID()){
//                    Toast.makeText(this, "Note Updated.", Toast.LENGTH_SHORT).show();
//                }
//                else{
//                    Toast.makeText(this, "Error Occurred!.", Toast.LENGTH_SHORT).show();
//                }
                Intent i = new Intent(getApplicationContext(), Details.class);
                i.putExtra("ID", note.getID());
                startActivity(i);

            } else {
                noteTitle.setError("Title Can not be Blank.");
            }
        }
        else if(item.getItemId() == R.id.delete){
            Toast.makeText(this, "Note Deleted!", Toast.LENGTH_SHORT).show();
            onBackPressed();

        }

        return super.onOptionsItemSelected(item);
    }

    private void goToMain() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }

    private void setSupportActionBar(Toolbar toolbar) {
    }
}