package com.switchfully.patekes.parksharkpatekes.security;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;

public class KeyCloakConfig {

    static Keycloak keycloak = null;
    final static String serverUrl = "https://keycloak.switchfully.com/auth/";
    public final static String realm = "parksharkpatekes";
    final static String clientId = "parkshark-patekes";
    final static String clientSecret = "9SqtwsMTNVNqYFG9eP1rGgcgkKGpWNIA";
    final static String userName = "therealadmin";
    final static String password = "password";

//    public KeyCloakConfig(){
//
//    }


    public static Keycloak getInstance(){
        if(keycloak == null){

            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.PASSWORD)
                    .username(userName)
                    .password(password)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .resteasyClient(new ResteasyClientBuilder()
                            .connectionPoolSize(10)
                            .build()
                                   )
                    .build();
        }
        return keycloak;
    }
}
