package com.switchfully.patekes.parksharkpatekes.service;

import com.switchfully.patekes.parksharkpatekes.dto.NewMemberDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.KeyCloakCantMakeUserException;
import com.switchfully.patekes.parksharkpatekes.security.KeyCloakConfig;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class KeyCloakService {

    public void  addUser(NewMemberDto newMemberDto) throws KeyCloakCantMakeUserException {
        UsersResource usersResource = KeyCloakConfig.getInstance().realm(KeyCloakConfig.realm).users();
        CredentialRepresentation credentialRepresentation = createPasswordCredentials(newMemberDto.getPassword());

        UserRepresentation kcUser = new UserRepresentation();
        kcUser.setUsername(newMemberDto.getEmail());
        kcUser.setCredentials(Collections.singletonList(credentialRepresentation));
        kcUser.setFirstName(newMemberDto.getName().getFirstname());
        kcUser.setLastName(newMemberDto.getName().getLastname());
        kcUser.setEmail(newMemberDto.getEmail());
        kcUser.setEnabled(true);
        kcUser.setEmailVerified(false);
        usersResource.create(kcUser);
//        if(usersResource.create(kcUser).getStatus() != 201){
//            throw new KeyCloakCantMakeUserException("not possible for keycloak to make user");
//        }

    }

    private static CredentialRepresentation  createPasswordCredentials(String password) {
        CredentialRepresentation passwordCredentials = new CredentialRepresentation();
        passwordCredentials.setTemporary(false);
        passwordCredentials.setType(CredentialRepresentation.PASSWORD);
        passwordCredentials.setValue(password);
        return passwordCredentials;
    }

}
