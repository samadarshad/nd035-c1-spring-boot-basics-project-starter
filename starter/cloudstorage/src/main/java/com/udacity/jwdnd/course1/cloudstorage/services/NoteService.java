package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NotesMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService {
    private final NotesMapper notesMapper;

    public NoteService(NotesMapper notesMapper) {
        this.notesMapper = notesMapper;
    }

   public void createNoteAndUpdateObject(Note note) {
        notesMapper.insertAndUpdateObjectThenGetNumberOfRowsAffected(note);
   }

   public Note getNote(Integer noteId) { return notesMapper.getNote(noteId); }

   public List<Note> getNotesByUserId(Integer userId) { return notesMapper.getAllNotesByUserId(userId); }

   public void deleteNote(Integer noteId) { }

   public void updateNote(Integer noteId) { }
}
