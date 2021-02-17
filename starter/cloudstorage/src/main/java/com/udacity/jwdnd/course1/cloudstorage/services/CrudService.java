package com.udacity.jwdnd.course1.cloudstorage.services;

import java.util.List;

public interface CrudService<T> {
    public void createAndUpdateObject(T obj);
    public T get(Integer id);
    public List<T> getByUserId(Integer userId);
    public void update(T obj);
    public void delete(Integer id);
}
