package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface NoteMapper extends CrudMapper<Note> {
    @Select("SELECT * FROM NOTES WHERE noteId = #{noteId}")
    Note get(Integer noteId);

    @Select("SELECT * FROM NOTES WHERE userId = #{userId}")
    List<Note> getAllByUserId(Integer userId);

    @Insert("INSERT INTO NOTES (notetitle, notedescription, userId) VALUES(#{notetitle}, #{notedescription}, #{userId})")
    @Options(useGeneratedKeys = true, keyProperty = "noteId")
    int insertAndUpdateObjectThenGetNumberOfRowsAffected(Note note);

    @Update("UPDATE NOTES SET notetitle = #{notetitle}, notedescription = #{notedescription} WHERE noteId = #{noteId}")
    int updateThenGetNumberOfRowsAffected(Note note);

    @Delete("DELETE FROM NOTES WHERE noteId = #{noteId}")
    int deleteThenGetNumberOfRowsAffected(Integer noteId);
}