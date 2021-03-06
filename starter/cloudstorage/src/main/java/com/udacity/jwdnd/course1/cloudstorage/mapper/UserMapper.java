package com.udacity.jwdnd.course1.cloudstorage.mapper;

import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.apache.ibatis.annotations.*;

@Mapper
public interface UserMapper {
    @Select("SELECT * FROM USERS WHERE username = #{username}")
    User getByUsername(String username);

    @Select("SELECT * FROM USERS WHERE userId = #{userId}")
    User get(int userId);

    //returns number of rows affected (i.e. 1), and modifies the user object to have userId
    @Insert("INSERT INTO USERS (username, salt, password, firstname, lastname) VALUES(#{username}, #{salt}, #{password}, #{firstName}, #{lastName})")
    @Options(useGeneratedKeys = true, keyProperty = "userId")
    int insertAndUpdateObjectThenGetNumberOfRowsAffected(User user);

    @Delete("DELETE FROM USERS WHERE userId = #{userId}")
    void delete(Integer userId);
}