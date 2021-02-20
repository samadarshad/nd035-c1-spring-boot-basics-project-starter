package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.NoteMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NoteService implements CrudService<Note> {
    private final NoteMapper noteMapper;

    public NoteService(NoteMapper noteMapper) {
        this.noteMapper = noteMapper;
    }

    @Override
    public Class<Note> getObjectType() {
        return Note.class;
    }

    public void createAndUpdateObject(Note note) {
        int rows = noteMapper.insertAndUpdateObjectThenGetNumberOfRowsAffected(note);
        assert(rows == 1);
    }

    public Note get(Integer noteId) { return noteMapper.get(noteId); }

    public List<Note> getByUserId(Integer userId) { return noteMapper.getAllByUserId(userId); }

    public void delete(Integer noteId) {
        int rows = noteMapper.deleteThenGetNumberOfRowsAffected(noteId);
       assert(rows == 1);
    }

    public void update(Note note) {
        int rows = noteMapper.updateThenGetNumberOfRowsAffected(note);
        assert(rows == 1);
    }
}
