package com.udacity.jwdnd.course1.cloudstorage.mapper;

import java.util.List;

public interface CrudMapper<T> {
    T get(Integer id);
    List<T> getAllByUserId(Integer userId);
    int insertAndUpdateObjectThenGetNumberOfRowsAffected(T obj);
    int updateThenGetNumberOfRowsAffected(T obj);
    int deleteThenGetNumberOfRowsAffected(Integer id);
}