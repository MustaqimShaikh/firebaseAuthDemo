package com.firebase.auth.demo.firebaseAuthDemo.config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;

@Service
public class FirebaseAuthConfig {

    private static final Logger logger = LoggerFactory.getLogger(FirebaseAuthConfig.class);

    @Value("classpath:firebaseConfig.json")
    Resource serviceAccount;

    @PostConstruct
    public void initialize() throws IOException {
        FirebaseOptions options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount.getInputStream()))
                .build();
        FirebaseApp.initializeApp(options);
        logger.info("Firebase App Initiated..!");
    }

}
