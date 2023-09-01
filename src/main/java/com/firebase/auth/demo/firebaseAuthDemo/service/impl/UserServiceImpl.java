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
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private UserRepository userRepository;

    /**
     * To LogIn via Google.
     *
     * @param idToken   - Firebase's Auth idToken.
     * @return User Model
     */
    @Override
    public UserRecord socialLogin(String idToken) {
        UserRecord userRecord;
        try {
            FirebaseToken decodedToken = FirebaseAuth.getInstance().verifyIdToken(idToken);
            userRecord = FirebaseAuth.getInstance().getUser(decodedToken.getUid());
            if (userRecord.getEmail() == null && userRecord.getPhoneNumber() == null) {
                LOGGER.debug("Firebase SignIn successful. LoggedIn User's Email: {}", userRecord.getEmail());
                throw new IllegalArgumentException("Firebase SignIn was successful but user has no phone or email specified");
            }
        } catch (FirebaseAuthException | IllegalArgumentException e) {
            e.printStackTrace();
            LOGGER.error("There was an exception while logging in via Firebase: {}", e.getMessage());
            throw new IllegalArgumentException(MessageEnum.FIREBASE_EXCEPTION.name());
        }
        return userRecord;
    }

    @Override
    public User get() throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User user = null;
        try {
            String uid = authentication.getName();
            user = userRepository.get(uid);
        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
        }
        return user;
    }

    @Override
    public User save(String bio) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User savedUser = null;
        try {
            String uid = authentication.getName();
            UserRecord userRecord = FirebaseAuth.getInstance().getUser(uid);
            User user = new User();
            user.setEmail(userRecord.getEmail());
            user.setName(userRecord.getDisplayName());
            user.setUid(uid);
            user.setLastLogin(userRecord.getUserMetadata().getLastSignInTimestamp());
            user.setBio(bio);
            savedUser = userRepository.save(user);
        } catch (Exception e) {
            LOGGER.error("Error: {}", e.getMessage());
        }
        return savedUser;
    }


}
