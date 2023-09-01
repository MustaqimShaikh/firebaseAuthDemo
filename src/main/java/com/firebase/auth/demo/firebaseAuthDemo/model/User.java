package com.firebase.auth.demo.firebaseAuthDemo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class User {

    private String uid;
    private String name;
    private String email;
    private long lastLogin;
    private String bio;


}	