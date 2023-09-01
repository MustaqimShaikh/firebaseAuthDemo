package com.firebase.auth.demo.firebaseAuthDemo.service;

import com.firebase.auth.demo.firebaseAuthDemo.model.User;
import com.google.firebase.auth.UserRecord;

public interface UserService {
    UserRecord socialLogin(String idToken);

    User get() throws Exception;

    User save(String bio);
}
