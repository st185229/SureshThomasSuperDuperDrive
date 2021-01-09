package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.CredentialsMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.Credentials;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;
import java.util.List;

@Service
public class CredentialsService {
    private final CredentialsMapper credentialsMapper;
    private final EncryptionService encryptionService;
    private final UserService userService;

    public CredentialsService(CredentialsMapper credentialsMapper, EncryptionService encryptionService, UserService userService) {
        this.credentialsMapper = credentialsMapper;
        this.encryptionService = encryptionService;
        this.userService = userService;
    }

    public boolean addCredential(Credentials credential) {
        var encryptedCredential = this.encryptCredential(credential);
        return credentialsMapper.create(encryptedCredential) != null;
    }

    private Credentials encryptCredential(Credentials credential) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        credential.setPassword(encryptionService.encryptValue(credential.getPassword(), encodedSalt));
        credential.setKey(encodedSalt);
        credential.setUserid(userService.getLoggedInUserId());
        return credential;
    }

    public boolean updateCredential(Credentials credentials) {
        //re-encrypt the same
        var encryptedCredential = encryptCredential(credentials);
        var updatedCount = credentialsMapper.update(encryptedCredential);
        return updatedCount > 0;
    }

    public List<Credentials> getCredentials() {
        //Load credentials from database
        return credentialsMapper.readAll(userService.getLoggedInUserId());
    }

    public void deleteCredential(int credId) {
        credentialsMapper.delete(credId, userService.getLoggedInUserId());
    }
}
