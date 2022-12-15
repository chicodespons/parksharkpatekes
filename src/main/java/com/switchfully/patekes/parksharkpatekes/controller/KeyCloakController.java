package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.NewMemberDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.KeyCloakCantMakeUserException;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.service.KeyCloakService;
import com.switchfully.patekes.parksharkpatekes.service.MemberService;
import org.springframework.web.bind.annotation.*;


import static org.springframework.http.HttpStatus.CREATED;

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
    @ResponseStatus(CREATED)
    public void createUser( @RequestBody NewMemberDto newMemberDto) throws KeyCloakCantMakeUserException, MemberException {
        keyCloakService.addUser(newMemberDto);
        memberService.addUser(newMemberDto);
    }

//    @PutMapping(path = "/membershiplevel/{id}", consumes = "application/json")
//    public void updateMembershipLevel(@RequestBody UpdateMembershipLevelDto updateMembershipLevelDto, @PathVariable Long id, Authentication authentication) throws MemberException {
//        memberService.updateMembershipLevel(updateMembershipLevelDto, id, authentication);
//
//    }
}