package com.switchfully.patekes.parksharkpatekes.service;

import com.switchfully.patekes.parksharkpatekes.dto.MemberDto;
import com.switchfully.patekes.parksharkpatekes.security.KeyCloakConfig;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class KeyCloakService {

    public void addUser(MemberDto memberDto){
        UsersResource usersResource = KeyCloakConfig.getInstance().realm(KeyCloakConfig.realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(memberDto.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(memberDto.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(memberDto.getFirstname());
        kcUser.setLastName(memberDto.getLastname());
        kcUser.setEmail(memberDto.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        usersResource.create(kcUser);

    }

    private static CredentialRepresentation  createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

}
