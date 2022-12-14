package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.NewMemberDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.KeyCloakCantMakeUserException;
import com.switchfully.patekes.parksharkpatekes.service.KeyCloakService;
import com.switchfully.patekes.parksharkpatekes.service.MemberService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(path = "parksharkpatekes/user")
public class KeyCloakController {

    private final KeyCloakService keyCloakService;
    private final MemberService memberService;

    public KeyCloakController(KeyCloakService keyCloakService, MemberService memberService) {
        this.keyCloakService = keyCloakService;
        this.memberService = memberService;
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json", produces = "application/json")
    public void createUser(@RequestBody NewMemberDto newMemberDto) throws KeyCloakCantMakeUserException {

        keyCloakService.addUser(newMemberDto);
        memberService.addUser(newMemberDto);
    }
}