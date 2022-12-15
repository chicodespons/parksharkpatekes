package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.NewMemberDto;
import com.switchfully.patekes.parksharkpatekes.dto.UpdateMembershipLevelDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.KeyCloakCantMakeUserException;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.security.TokenDecoder;
import com.switchfully.patekes.parksharkpatekes.service.KeyCloakService;
import com.switchfully.patekes.parksharkpatekes.service.MemberService;
import net.minidev.json.parser.ParseException;
import org.springframework.security.core.Authentication;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;

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
    public void createUser( @RequestBody NewMemberDto newMemberDto) throws KeyCloakCantMakeUserException, MemberException {
        keyCloakService.addUser(newMemberDto);
        memberService.addUser(newMemberDto);
    }

//    @PutMapping(path = "/membershiplevel/{id}", consumes = "application/json")
//    @PreAuthorize("hasAnyAuthority('MEMBER')")
//    public void updateMembershipLevel(@RequestBody UpdateMembershipLevelDto updateMembershipLevelDto, @PathVariable Long id, @RequestHeader String token) throws MemberException, ParseException {
//        String email = TokenDecoder.tokenDecode(token);
//        memberService.updateMembershipLevel(updateMembershipLevelDto, id, email);
//    }
}