package com.avimedical.auth.firebase.service.impl;

import com.avimedical.auth.config.ApplicationProperties;
import com.avimedical.auth.firebase.service.FirebaseService;
import com.google.api.client.util.Value;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthException;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

@ApplicationScoped
public class DefaultFirebaseServiceImpl implements FirebaseService {

    private static final Logger LOGGER = Logger.getLogger("DefaultFirebaseServiceImpl");

    private static final String EMAIL = "test@avimedical.com";

    private static final String PHONE = "+4915217002904";

    private static final String CLAIM = "groups";

    private static final String USER_ID = "tYvM9AWdpbWvH9lcUC9REXxiHVQ2"; //me
    private static final String USER_ID = "9xTj6qRAPUgPsYPAq3bS8NpgRn92"; //riccardo

    private FirebaseAuth defaultAuth;

    @Inject
    ApplicationProperties applicationProperties;

    public void initialize() throws IOException {
        LOGGER.info("Initializing  Firebase Auth");
        FirebaseOptions defaultOptions = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.getApplicationDefault())
                .setServiceAccountId(
                        applicationProperties.getFirebase().getServiceKey()) //"my-client-id@my-project-id.iam.gserviceaccount.com")
                .build();

        FirebaseApp firebaseApp = FirebaseApp.initializeApp(defaultOptions);
        defaultAuth = FirebaseAuth.getInstance(firebaseApp);

        LOGGER.info("Default Auth: [" + defaultAuth + "]");
    }

    @Override
    public void setClaims() throws FirebaseAuthException {

        defaultAuth.setCustomUserClaims(USER_ID, getClaims()); //myUser.getUid()

        LOGGER.info("Custom claims set");

        //deleteUser();
    }

    private void deleteUser() throws FirebaseAuthException {

        var user = defaultAuth.getUserByEmail(EMAIL);

        defaultAuth.deleteUser(user.getUid());

        LOGGER.info("Deleted user with id : [" + user.getUid() + "]");

    }

    public void getUser() throws FirebaseAuthException {
        var authUser = defaultAuth.getUser(USER_ID);
        LOGGER.info("User: [" + authUser.getUid() + ", " + authUser.getEmail() + ", " + authUser.getPhoneNumber() + "]");
        LOGGER.info("My User custom claims: [" + authUser.getCustomClaims() + "]");
    }


    public void createCustomToken() throws FirebaseAuthException {

        String customToken = defaultAuth.createCustomToken(USER_ID, getClaims());
        LOGGER.info("Custom Token: [" + customToken + "]");


        // Decode token
        String[] chunks = customToken.split("\\.");
        Base64.Decoder decoder = Base64.getDecoder();
        String header = new String(decoder.decode(chunks[0]));
        String payload = new String(decoder.decode(chunks[1]));

        LOGGER.info("Token Header: [" + header + "]");
        LOGGER.info("Token Payload: [" + payload + "]");
    }

    private Map<String, Object> getClaims(){
        Map<String, Object> additionalClaims = new HashMap<String, Object>();
        additionalClaims.put(CLAIM, List.of(ROLES.STAFF));
        return additionalClaims;
    }

    enum ROLES {@Value ADMIN, @Value STAFF;}
}
