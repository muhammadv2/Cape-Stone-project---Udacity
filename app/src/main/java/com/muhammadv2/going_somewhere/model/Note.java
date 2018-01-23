package com.muhammadv2.going_somewhere.model;

import org.parceler.Parcel;

@Parcel
public final class Note {

    String noteTitle;
    String noteBody;
    boolean toggleNote;

    public Note(String noteTitle, String noteBody, boolean toggleNote) {
        this.noteTitle = noteTitle;
        this.noteBody = noteBody;
        this.toggleNote = toggleNote;
    }

    public String getNoteTitle() {
        return noteTitle;
    }

    public String getNoteBody() {
        return noteBody;
    }

    public boolean isToggleNote() {
        return toggleNote;
    }
}
