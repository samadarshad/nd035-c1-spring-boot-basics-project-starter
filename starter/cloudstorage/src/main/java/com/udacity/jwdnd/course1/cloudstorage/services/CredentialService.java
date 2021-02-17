package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialMapper;
import com.udacity.jwdnd.course1.cloudstorage.mapper.FileMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credential;
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
        Credential credential = credentialMapper.get(credentialId);

        String decryptedPassword = encryptionService.decryptValue(credential.getPassword(), credential.getKey());

        credential.setPassword(decryptedPassword);
        return credential;
    }

    @Override
    public List<Credential> getByUserId(Integer userId) {
        return credentialMapper.getAllByUserId(userId);
    }

    @Override
    public void update(Credential credential) {
        Credential newCredential = credentialMapper.get(credential.getCredentialId());
        String encryptedPassword = encryptionService.encryptValue(credential.getPassword(), newCredential.getKey());
        newCredential.setPassword(encryptedPassword);
        int rows = credentialMapper.updateThenGetNumberOfRowsAffected(newCredential);
        assert(rows == 1);
    }

    @Override
    public void delete(Integer credentialId) {
        int rows = credentialMapper.deleteThenGetNumberOfRowsAffected(credentialId);
        assert(rows == 1);
    }
}
