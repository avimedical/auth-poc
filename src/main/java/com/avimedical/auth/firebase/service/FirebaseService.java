package com.avimedical.auth.firebase.service;

import com.google.firebase.auth.FirebaseAuthException;

import java.io.IOException;

public interface FirebaseService {

    void initialize() throws IOException;

    void setClaims() throws IOException, FirebaseAuthException;

    void getUser() throws FirebaseAuthException;

    void createCustomToken() throws FirebaseAuthException;
}