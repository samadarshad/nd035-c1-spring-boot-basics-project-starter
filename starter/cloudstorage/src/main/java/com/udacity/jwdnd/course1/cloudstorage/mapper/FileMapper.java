package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import org.apache.ibatis.annotations.*;

import java.util.List;

@Mapper
public interface FileMapper {
    @Select("SELECT * FROM FILES WHERE fileId = #{fileId}")
    File get(Integer fileId);

    @Select("SELECT * FROM FILES WHERE userId = #{userId}")
    List<File> getAllByUserId(Integer userId);

    //returns number of rows affected (i.e. 1), and modifies the file object to have fileId
    @Insert("INSERT INTO FILES (fileName, contentType, fileSize, userId, fileData) VALUES(#{fileName}, #{contentType}, #{fileSize}, #{userId}, #{fileData})")
    @Options(useGeneratedKeys = true, keyProperty = "fileId")
    int insertAndUpdateObjectThenGetNumberOfRowsAffected(File file);

    @Delete("DELETE FROM FILES WHERE fileId = #{fileId}")
    int deleteThenGetNumberOfRowsAffected(Integer fileId);
}
