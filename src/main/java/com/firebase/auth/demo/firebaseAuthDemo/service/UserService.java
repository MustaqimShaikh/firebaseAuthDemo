package com.firebase.auth.demo.firebaseAuthDemo.service;

import com.firebase.auth.demo.firebaseAuthDemo.model.User;
import com.google.firebase.auth.UserRecord;

import java.util.List;
import java.util.Map;

public interface UserService {
    UserRecord getOriginalUserByIdToken(String idToken);

    UserRecord getOriginalUserRecordByUid(String uid);

    User getCustomUserByUid(String uid);

    User getCustomUserByEmail(String uid);

    List<User> getCustomUserListByEmail(String email);

    User saveUserRecordInFireStore(String bio, String uid);

    Map<String, Object> getOriginalUserAndFireStoreUser(String uid);

}
