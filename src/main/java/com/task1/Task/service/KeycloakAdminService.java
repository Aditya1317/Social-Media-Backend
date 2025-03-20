package com.task1.Task.service;

import jakarta.annotation.PostConstruct;
import lombok.extern.log4j.Log4j;
import lombok.extern.log4j.Log4j2;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RealmRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.ws.rs.core.Response;

@Service
@Log4j2
public class KeycloakAdminService
{
    @Value("${keycloak.auth-server-url}")
    private String serverUrl;

    @Value("${keycloak.admin-username}")
    private String adminUsername;

    @Value("${keycloak.admin-password}")
    private String adminPassword;

    @Value("${keycloak.client-id}")
    private String clientId;

    private Keycloak keycloak;

    @PostConstruct
    public void init() {
        keycloak = KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master")
                .clientId(clientId)
                .username(adminUsername)
                .password(adminPassword)
                .grantType(OAuth2Constants.PASSWORD)
                .build();

        createRealm("Testing");
        // Automatically create a test user
        createUser("Testing", "adi", "adi@avaali.com", "adi");
    }

    public void createRealm(String realmName) {
        RealmRepresentation realm = new RealmRepresentation();
        realm.setRealm(realmName);
        realm.setEnabled(true);
        keycloak.realms().create(realm);
        System.out.println("Realm created: " + realmName);
    }
    public void createUser(String realmName, String username, String email, String password) {
        // Create a user representation
        UserRepresentation user = new UserRepresentation();
        user.setUsername(username);
        user.setEmail(email);
        user.setEnabled(true);
        user.setFirstName("adi");
        user.setLastName("adi");
        user.setEmailVerified(true);// Activate the user

        // Send request to Keycloak
        UsersResource usersResource = keycloak.realm(realmName).users();
        Response response = usersResource.create(user);

        if (response.getStatus() == 201) { // 201 Created
            System.out.println("User created successfully: " + username);

            // Fetch the created user
            String userId = usersResource.search(username).get(0).getId();

            // Set password for the user
            setUserPassword(realmName, userId, password);
        } else {
            log.info("Failed to create user. Status: " + response.getStatus());
            log.info("Response details: " + response.readEntity(String.class)); // Print error details
        }
    }

    private void setUserPassword(String realmName, String userId, String password) {
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setTemporary(false);
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);

        keycloak.realm(realmName).users().get(userId).resetPassword(credential);
        System.out.println("Password set for user: " + userId);
    }
}