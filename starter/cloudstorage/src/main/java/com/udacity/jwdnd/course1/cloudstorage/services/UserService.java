package com.udacity.jwdnd.course1.cloudstorage.services;

import com.udacity.jwdnd.course1.cloudstorage.mapper.UserMapper;
import com.udacity.jwdnd.course1.cloudstorage.model.User;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.Base64;

@Service
public class UserService {

    private final UserMapper userMapper;
    private final HashService hashService;

    public UserService(UserMapper userMapper, HashService hashService) {
        this.userMapper = userMapper;
        this.hashService = hashService;
    }

    public boolean isUsernameAvailable(String username) {
        return userMapper.getUser(username) == null;
    }

    public void createUserAndUpdateObject(User user) {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        String encodedSalt = Base64.getEncoder().encodeToString(salt);
        String hashedPassword = hashService.getHashedValue(user.getPassword(), encodedSalt);

        user.setSalt(encodedSalt);
        user.setPassword(hashedPassword);
        userMapper.insertAndUpdateObjectThenGetNumberOfRowsAffected(user);
    }

    // no requirement to delete user
//    public void deleteUser(Integer userId) {
//        userMapper.deleteUser(userId);
//    }

//    public User getUser(String username) {
//        return userMapper.getUser(username);
//    }

    public User getUserById(Integer userId) {
        return userMapper.getUserById(userId);
    }

}