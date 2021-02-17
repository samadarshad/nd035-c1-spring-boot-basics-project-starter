package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.File;
import com.udacity.jwdnd.course1.cloudstorage.model.Note;
import jdk.jshell.spi.ExecutionControl;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FileService implements CrudService<File> {
    private final FileMapper fileMapper;

    public FileService(FileMapper fileMapper) {
        this.fileMapper = fileMapper;
    }

    public void createAndUpdateObject(File file) {
        int rows = fileMapper.insertAndUpdateObjectThenGetNumberOfRowsAffected(file);
        assert(rows == 1);
    }

    public File get(Integer fileId) { return fileMapper.get(fileId); }

    public List<File> getByUserId(Integer userId) { return fileMapper.getAllByUserId(userId); }

    public void update(File obj) {
        throw new UnsupportedOperationException();
    }

    public void delete(Integer fileId) {
        int rows = fileMapper.deleteThenGetNumberOfRowsAffected(fileId);
        assert(rows == 1);
    }
}
