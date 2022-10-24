package com.example.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.List;

public class NoteDatabase extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 2;
    private static final String DATABASE_NAME = "DBNote";
    private static final String DATABASE_TABLE = "Notes";

    // Columns for Notes table
    private static final String id = "id";
    private static final String title = "title";
    private static final String content = "content";
    private static final String date = "date";
    private static final String time = "time";

    public NoteDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
//        String query = "Create Table " + DATABASE_TABLE + "("+ id + " INT PRIMARY KEY,"+ title + " TEXT,"+ content + " TEXT," + date+ " TEXT,"+ time+ " TEXT" + ")";
        String query = "Create Table Notes(id INTEGER PRIMARY KEY, title TEXT, content TEXT, date TEXT, time TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if(oldVersion >= newVersion){
            return;
        }
        db.execSQL("drop Table if exists " + DATABASE_TABLE);
        onCreate(db);
    }

    public long addNote(@NonNull Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        c.put(title, note.getTitle());
        c.put(content, note.getContent());
        c.put(date, note.getDate());
        c.put(time, note.getTime());

        long ID = db.insert(DATABASE_TABLE, null, c);
        return ID;
    }

    public Note getNote(long ID){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(DATABASE_TABLE, new String[]{id, title, content, date, time}, id + "=?", new String[]{String.valueOf(ID)}, null, null, null, null);
        if(cursor != null ){
            cursor.moveToFirst();
        }
        return new Note(
                Long.parseLong(cursor.getString(0)),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4));
    }

    public List<Note> getNotes(){
        SQLiteDatabase db = this.getReadableDatabase();
        List<Note> allNotes = new ArrayList<>();
        String query = "Select * from " + DATABASE_TABLE +" ORDER BY "+ id +" DESC";
        Cursor cursor = db.rawQuery(query,null);
        if(cursor.moveToFirst()){
            do{
                Note note = new Note();
                note.setID(Long.parseLong(cursor.getString(0)));
                note.setTitle(cursor.getString(1));
                note.setContent(cursor.getString(2));
                note.setDate(cursor.getString(3));
                note.setTime(cursor.getString(4));
                allNotes.add(note);
            }while (cursor.moveToNext());
        }
        return allNotes;
    }

    public int editNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues c = new ContentValues();
        Log.d("Edited", "Edited Title: -> "+ note.getTitle() + "\n ID -> "+note.getID());
        c.put(title,note.getTitle());
        c.put(content,note.getContent());
        c.put(date,note.getDate());
        c.put(time,note.getTime());
        return db.update(DATABASE_TABLE,c,id+"=?",new String[]{String.valueOf(note.getID())});
    }
    
    void deleteNote(long ids){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(DATABASE_TABLE,id+"=?",new String[]{String.valueOf(ids)});
        db.close();
    }
}
