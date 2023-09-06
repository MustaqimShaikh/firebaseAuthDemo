package com.firebase.auth.demo.firebaseAuthDemo.controller;

import com.firebase.auth.demo.firebaseAuthDemo.model.User;
import com.firebase.auth.demo.firebaseAuthDemo.service.UserService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.auth.UserRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
public class UserController {

    @Autowired
    private UserService userService;
    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @GetMapping("/auth-user/idtoken")
    public ResponseEntity<UserRecord> getOriginalUserByIdToken(@RequestParam("idToken") String idToken) {
        long startTime = System.currentTimeMillis();
        UserRecord userRecord = userService.getOriginalUserByIdToken(idToken);
        long endTime = System.currentTimeMillis();
        logger.info("Time taken to get loggedIn User: {} Milliseconds", (endTime - startTime));
        return new ResponseEntity<>(userRecord, HttpStatus.OK);
    }

    @GetMapping("/auth-user/uid")
    public ResponseEntity<UserRecord> getOriginalUserByUid(@RequestParam("uid") String uid) {
        long startTime = System.currentTimeMillis();
        UserRecord userRecord = userService.getOriginalUserRecordByUid(uid);
        long endTime = System.currentTimeMillis();
        logger.info("Time taken to get loggedIn User: {} Milliseconds", (endTime - startTime));
        return new ResponseEntity<>(userRecord, HttpStatus.OK);
    }


    @GetMapping("/custom/uid")
    public ResponseEntity<User> getCustomUserByUid(@RequestParam("uid") String uid) {
        long startTime = System.currentTimeMillis();
        User user = userService.getCustomUserByUid(uid);
        long endTime = System.currentTimeMillis();
        logger.info("Time taken to get User by uid: {} Milliseconds", (endTime - startTime));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/custom/email")
    public ResponseEntity<User> getCustomUserByEmail(@RequestParam("email") String email) {
        long startTime = System.currentTimeMillis();
        User user = userService.getCustomUserByEmail(email);
        long endTime = System.currentTimeMillis();
        logger.info("Time taken to get User by email: {} Milliseconds", (endTime - startTime));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @GetMapping("/custom/email/all")
    public ResponseEntity<List<User>> getCustomUserListByEmail(@RequestParam("email") String email) {
        long startTime = System.currentTimeMillis();
        List<User> userList = userService.getCustomUserListByEmail(email);
        long endTime = System.currentTimeMillis();
        logger.info("Time taken to get User by email: {} Milliseconds", (endTime - startTime));
        return new ResponseEntity<>(userList, HttpStatus.OK);
    }

    @PostMapping("/custom")
    public ResponseEntity<User> saveUserRecordInFireStore(@RequestParam("bio") String bio,
                                                          @RequestParam("uid") String uid) {
        long startTime = System.currentTimeMillis();
        User savedUser = userService.saveUserRecordInFireStore(bio, uid);
        long endTime = System.currentTimeMillis();
        logger.info("Time taken to save User: {} Milliseconds", (endTime - startTime));
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }

    @GetMapping ("/auth-user/custom")
    public ResponseEntity<Map<String, Object>> saveUserRecordInFireStore(@RequestParam("uid") String uid) {
        long startTime = System.currentTimeMillis();
        Map<String, Object> response = userService.getOriginalUserAndFireStoreUser(uid);
        long endTime = System.currentTimeMillis();
        logger.info("Time taken to save AuthUser and CustomUser: {} Milliseconds", (endTime - startTime));
        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    //TBD
    public String createCustomToken() throws FirebaseAuthException {
        String customToken = FirebaseAuth.getInstance()
                .createCustomToken("Xf7sNv599RX2znmZlbYJ0iiaygJ3");
        logger.info("Custom token created: {}", customToken);
        return customToken;
    }
}
