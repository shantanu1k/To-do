package com.cowday.todo.data;

import com.google.firebase.firestore.PropertyName;

public class Note{
    String note;
    public Note(){

    }
    public Note(String note) {
        this.note = note;
    }
    @PropertyName("note")
    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}