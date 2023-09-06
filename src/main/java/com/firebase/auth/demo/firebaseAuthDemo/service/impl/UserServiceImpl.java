package com.firebase.auth.demo.firebaseAuthDemo.service.impl;

import com.firebase.auth.demo.firebaseAuthDemo.enums.MessageEnum;
import com.firebase.auth.demo.firebaseAuthDemo.model.User;
import com.firebase.auth.demo.firebaseAuthDemo.repository.UserRepository;
import com.firebase.auth.demo.firebaseAuthDemo.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.FirebaseToken;
import com.google.firebase.auth.UserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ExecutionException;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * To LogIn via Google.
     *
     * @param idToken - Firebase's Auth idToken.
     * @return userRecord - Original userRecord model of Firebase's Auth.
     */
    @Override
    public UserRecord getOriginalUserByIdToken(String idToken) {
        UserRecord userRecord;
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            userRecord = FirebaseAuth.getInstance().getUser(decodedToken.getUid());
            logger.info("Fetched UserRecord: {}", userRecord);
            if (userRecord.getEmail() == null && userRecord.getPhoneNumber() == null) {
                logger.debug("Firebase SignIn successful. LoggedIn User's Email: {}", userRecord.getEmail());
                throw new IllegalArgumentException("Firebase SignIn was successful but user has no phone or email specified");
            }
        } catch (FirebaseAuthException | IllegalArgumentException e) {
            e.printStackTrace();
            logger.error("There was an exception while logging in via Firebase: {}", e.getMessage());
            throw new IllegalArgumentException(MessageEnum.FIREBASE_EXCEPTION.name());
        }
        return userRecord;
    }


    /**
     * To get loggedIn user by uid
     *
     * @param uid - Unique id generated by Firebase's while user SignIn first time.
     * @return userRecord - Original userRecord model of Firebase's Auth.
     */
    @Override
    public UserRecord getOriginalUserRecordByUid(String uid) {
        UserRecord userRecord;
        try {
            userRecord = FirebaseAuth.getInstance().getUser(uid);
            logger.info("Fetched UserRecord: {}", userRecord);
            if (userRecord.getEmail() == null && userRecord.getPhoneNumber() == null) {
                logger.debug("Firebase SignIn successful. LoggedIn User's Email: {}", userRecord.getEmail());
                throw new IllegalArgumentException("Firebase SignIn was successful but user has no phone or email specified");
            }
        } catch (FirebaseAuthException | IllegalArgumentException e) {
            e.printStackTrace();
            logger.error("There was an exception while logging in via Firebase: {}", e.getMessage());
            throw new IllegalArgumentException(MessageEnum.FIREBASE_EXCEPTION.name());
        }
        return userRecord;
    }

    /**
     * To get AuthUser and Fire store User.
     *
     * @param uid - Unique id generated by Firebase's while user SignIn first time.
     * @return map - Response.
     */
    @Override
    public Map<String, Object> getOriginalUserAndFireStoreUser(String uid) {
        Map<String, Object> response = new HashMap<>();
        logger.info("Fetching UserRecord for uid: {}", uid);
        UserRecord userRecord = getOriginalUserRecordByUid(uid);
        User customUser = getCustomUserByUid(uid);
        if (Objects.nonNull(userRecord)) {
            logger.info("UserRecord found for uid: {}", uid);
            response.put("Original User", userRecord);
            if (Objects.nonNull(customUser)) {
                logger.info("User found for uid: {}", uid);
                response.put("Custom/FireStore User", customUser);
            } else {
                response.put("Error", MessageEnum.USER_NOT_FOUND.name());
            }
        } else {
            response.put("Error", MessageEnum.USER_RECORD_NOT_FOUND.name());
        }
        return response;
    }

    /**
     * ------------------> Below methods are related to FireStore Database.
     */

    @Override
    public User getCustomUserByUid(String uid) {
        logger.info("Fetching Custom User by given uid: {}", uid);
        User user = null;
        try {
            user = userRepository.getByUid(uid);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error: {}", e.getMessage());
        }
        return user;
    }

    @Override
    public User getCustomUserByEmail(String email) {
        logger.info("Fetching Custom User by given email: {}", email);
        User user = null;
        try {
            user = userRepository.getByEmail(email);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error: {}", e.getMessage());
        }
        return user;
    }

    @Override
    public List<User> getCustomUserListByEmail(String email) {
        logger.info("Fetching Custom User by given email: {}", email);
       List<User> userList = new ArrayList<>();
        try {
            userList = userRepository.getUserListByEmail(email);
        } catch (ExecutionException | InterruptedException e) {
            logger.error("Error: {}", e.getMessage());
        }
        return userList;
    }

    @Override
    public User saveUserRecordInFireStore(String bio, String uid) {
        User savedUser = null;
        try {
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            if (Objects.nonNull(userRecord)) {
                User user = null;
                user = userRepository.getByUid(uid);
                if (Objects.nonNull(user)) {
                    logger.info("User already exist in Fire store for given uid: {}", uid);
                } else {
                    user = new User();
                    user.setEmail(userRecord.getEmail());
                    user.setName(userRecord.getDisplayName());
                    user.setProfilePhoto(userRecord.getPhotoUrl());
                    user.setUid(uid);
                    user.setLastLogin(userRecord.getUserMetadata().getLastSignInTimestamp());
                    user.setBio(bio);
                    savedUser = userRepository.save(user);
                }
            } else {
                logger.info("UserRecord not found for given uid: {}", uid);
                throw new IllegalArgumentException(MessageEnum.USER_RECORD_NOT_FOUND.name());
            }
        } catch (Exception e) {
            logger.error("Error: {}", e.getMessage());
        }
        return savedUser;
    }

}
