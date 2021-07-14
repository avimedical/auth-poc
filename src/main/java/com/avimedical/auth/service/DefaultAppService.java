package com.avimedical.auth.service;

import com.avimedical.auth.firebase.service.FirebaseService;
import com.google.firebase.auth.FirebaseAuthException;
import io.quarkus.runtime.StartupEvent;

import javax.enterprise.event.Observes;
import javax.inject.Inject;
import java.io.IOException;

public class DefaultAppService {

    @Inject
    FirebaseService firebaseService;

    void onStart(@Observes StartupEvent event) throws IOException, FirebaseAuthException {

        firebaseService.initialize();

        //firebaseService.setClaims();

        firebaseService.createCustomToken();

        firebaseService.getUser();
    }

}
