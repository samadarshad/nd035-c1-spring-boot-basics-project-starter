package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

public interface CrudService<T> {
    Class<T> getObjectType();
    void createAndUpdateObject(T obj);
    T get(Integer id);
    List<T> getByUserId(Integer userId);
    void update(T obj);
    void delete(Integer id);
}
