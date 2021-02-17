package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CredentialService implements CrudService<Credential> {
    private final CredentialMapper credentialMapper;

    public CredentialService(CredentialMapper credentialMapper) {
        this.credentialMapper = credentialMapper;
    }

    @Override
    public void createAndUpdateObject(Credential credential) {
        //encrypt and store key too
        int rows = credentialMapper.insertAndUpdateObjectThenGetNumberOfRowsAffected(credential);
        assert(rows == 1);
    }

    @Override
    public Credential get(Integer credentialId) {
        return credentialMapper.get(credentialId);
    }

    @Override
    public List<Credential> getByUserId(Integer userId) {
        return credentialMapper.getAllByUserId(userId);
    }

    @Override
    public void update(Credential credential) {
        int rows = credentialMapper.updateThenGetNumberOfRowsAffected(credential);
        assert(rows == 1);
    }

    @Override
    public void delete(Integer credentialId) {
        int rows = credentialMapper.deleteThenGetNumberOfRowsAffected(credentialId);
        assert(rows == 1);
    }
}
