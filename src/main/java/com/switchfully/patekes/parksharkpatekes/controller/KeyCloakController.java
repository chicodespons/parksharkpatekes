package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.MemberDto;
import com.switchfully.patekes.parksharkpatekes.service.KeyCloakService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "parksharkpatekes/user")
public class KeyCloakController {

    private final KeyCloakService keyCloakService;

    public KeyCloakController(KeyCloakService keyCloakService) {
        this.keyCloakService = keyCloakService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public void createUser(@RequestBody MemberDto memberDto) {
        keyCloakService.addUser(memberDto);
    }
}