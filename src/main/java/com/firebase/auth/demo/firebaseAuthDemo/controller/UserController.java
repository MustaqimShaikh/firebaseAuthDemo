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
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;


@RestController
@Validated
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;
    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    /**
     * To LogIn via Firebase.
     *
     * @param idToken - Firebase's idToken.
     * @return User Model.
     */
    @PostMapping("/social/login")
    public ResponseEntity<?> googleLogin(@RequestParam("idToken") String idToken) throws FirebaseAuthException {
        long startTime = System.currentTimeMillis();
        UserRecord userRecord = userService.socialLogin(createCustomToken());
        long endTime = System.currentTimeMillis();
        LOGGER.info("Time taken to log in via Google: {} Milliseconds", (endTime - startTime));
        return new ResponseEntity<>(userRecord, HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<User> get() throws Exception {
        long startTime = System.currentTimeMillis();
        User user = userService.get();
        long endTime = System.currentTimeMillis();
        LOGGER.info("Time taken to get User: {} Milliseconds", (endTime - startTime));
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<User> save(@RequestBody String bio) throws Exception {
        long startTime = System.currentTimeMillis();
        User savedUser = userService.save(bio);
        long endTime = System.currentTimeMillis();
        LOGGER.info("Time taken to save User: {} Milliseconds", (endTime - startTime));
        return new ResponseEntity<>(savedUser, HttpStatus.OK);
    }


    public String createCustomToken() throws FirebaseAuthException {
        String customToken = FirebaseAuth.getInstance().createCustomToken("Xf7sNv599RX2znmZlbYJ0iiaygJ3");
        System.out.println("Custom token created: " + customToken);
        return customToken;
    }
}
