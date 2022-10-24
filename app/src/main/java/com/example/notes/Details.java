package com.example.notes;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.text.method.ScrollingMovementMethod;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;


public class Details extends AppCompatActivity {
    long id;
    TextView mDetails;
    NoteDatabase db = new NoteDatabase(this);
    Note note;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = findViewById(R.id.toolBar);
        setSupportActionBar(toolbar);

        mDetails = findViewById(R.id.detailsOfNote);

        Intent i = getIntent();
        id = i.getLongExtra("ID",0);

        note = db.getNote(id);


        getSupportActionBar().setTitle(note.getTitle());
        mDetails.setText((note.getContent()));
        mDetails.setMovementMethod(new ScrollingMovementMethod());

//        FloatingActionButton fab = findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                db.deleteNote(note.getID());
//                startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                Toast.makeText(getApplicationContext(), "Note Deleted!" , Toast.LENGTH_SHORT).show();
//            }
//        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.edit_menu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.edit) {
            Intent i = new Intent(this, Edit.class);
            i.putExtra("ID", note.getID());
            startActivity(i);
        }else if(item.getItemId() == R.id.delete){
            db.deleteNote(note.getID());
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            Toast.makeText(getApplicationContext(), "Note Deleted!" , Toast.LENGTH_SHORT).show();
        }

        return super.onOptionsItemSelected(item);
    }
    
    private void setSupportActionBar(Toolbar toolbar) {
    }
}