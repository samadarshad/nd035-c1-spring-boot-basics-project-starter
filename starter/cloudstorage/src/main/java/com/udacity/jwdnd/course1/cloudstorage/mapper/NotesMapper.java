package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Select;

import java.util.List;

@Mapper
public interface NotesMapper {
    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    Note getNote(Integer noteId);

    @Select("SELECT * FROM NOTES")
    List<Note> getAllNotes();

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getAllNotesByUserId(Integer userId);

    //returns number of rows affected (i.e. 1), and modifies the note object to have noteId
    @Insert("INSERT INTO NOTES (notetitle, notedescription, userId) VALUES(#{notetitle}, #{notedescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertAndUpdateObjectThenGetNumberOfRowsAffected(Note note);
}