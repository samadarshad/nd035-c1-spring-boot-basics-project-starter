package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
import org.apache.ibatis.jdbc.Null;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialService implements CrudService<Credential> {
    private final CredentialMapper credentialMapper;
    private final EncryptionService encryptionService;

    public CredentialService(CredentialMapper credentialMapper,
                             EncryptionService encryptionService) {
        this.credentialMapper = credentialMapper;
        this.encryptionService = encryptionService;
    }

    @Override
    public Class<Credential> getObjectType() {
        return Credential.class;
    }

    @Override
    public void createAndUpdateObject(Credential credential) {
        SecureRandom random = new SecureRandom();
        byte[] key = new byte[16];
        random.nextBytes(key);
        String encodedKey = Base64.getEncoder().encodeToString(key);
        credential.setKey(encodedKey);

        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), credential.getKey());
        credential.setPassword(encryptedPassword);

        int rows = credentialMapper.insertAndUpdateObjectThenGetNumberOfRowsAffected(credential);
        assert(rows == 1);
    }

    @Override
    public Credential get(Integer credentialId) {
        Credential credential = Credential.getInstance(credentialMapper.get(credentialId));
        if (credential == null) {
            return null;
        }
        String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());

        credential.setPassword(decryptedPassword);
        return credential;
    }

    @Override
    public List<Credential> getByUserId(Integer userId) {
        return credentialMapper.getAllByUserId(userId);
    }

    @Override
    public void update(Credential newPartialCredential) {
        Credential actualCredential = credentialMapper.get(newPartialCredential.getCredentialId());

        actualCredential.setUsername(newPartialCredential.getUsername());
        actualCredential.setUrl(newPartialCredential.getUrl());
        String encryptedPassword = encryptionService.encryptValue(newPartialCredential.getPassword(), actualCredential.getKey());
        actualCredential.setPassword(encryptedPassword);

        int rows = credentialMapper.updateThenGetNumberOfRowsAffected(actualCredential);
        assert(rows == 1);
    }

    @Override
    public void delete(Integer credentialId) {
        int rows = credentialMapper.deleteThenGetNumberOfRowsAffected(credentialId);
        assert(rows == 1);
    }
}
