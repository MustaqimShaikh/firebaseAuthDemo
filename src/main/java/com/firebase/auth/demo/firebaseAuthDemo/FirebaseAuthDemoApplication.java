package com.firebase.auth.demo.firebaseAuthDemo;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class FirebaseAuthDemoApplication {

	public static void main(String[] args) throws FirebaseAuthException {
		SpringApplication.run(FirebaseAuthDemoApplication.class, args);
	}

}
