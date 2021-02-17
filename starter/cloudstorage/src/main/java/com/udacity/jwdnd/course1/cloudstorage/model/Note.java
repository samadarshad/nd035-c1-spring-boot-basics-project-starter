package com.udacity.jwdnd.course1.cloudstorage.model;

public class Note {


    public Note(Integer noteId, String notetitle, String notedescription, Integer userId) {
        this.noteId = noteId;
        this.notetitle = notetitle;
        this.notedescription = notedescription;
        this.userId = userId;
    }

    private Integer noteId;
    private String notetitle;
    private String notedescription;
    private Integer userId;

    public Integer getNoteId() {
        return noteId;
    }

    public void setNoteId(Integer noteId) {
        this.noteId = noteId;
    }

    public String getNotetitle() {
        return notetitle;
    }

    public void setNotetitle(String notetitle) {
        this.notetitle = notetitle;
    }

    public String getNotedescription() {
        return notedescription;
    }

    public void setNotedescription(String notedescription) {
        this.notedescription = notedescription;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }


}
