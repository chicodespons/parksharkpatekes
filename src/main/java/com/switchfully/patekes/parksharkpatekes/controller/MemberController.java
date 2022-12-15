package com.switchfully.patekes.parksharkpatekes.controller;


import com.switchfully.patekes.parksharkpatekes.dto.MemberDto;
import com.switchfully.patekes.parksharkpatekes.dto.UpdateMembershipLevelDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.security.TokenDecoder;
import com.switchfully.patekes.parksharkpatekes.service.MemberService;
import net.minidev.json.parser.ParseException;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(path = "member")
public class MemberController {
    private final MemberService memberService;

    public MemberController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping(path = "list", produces = MediaType.APPLICATION_JSON_VALUE)
    public List<MemberDto> getAllMembers() {
        return memberService.getAllMembers();
    }


    @PreAuthorize("hasAuthority('MEMBER')")
    @PutMapping(path = "/membershiplevel/{id}", consumes = "application/json")
    public MemberDto updateMembershipLevel(@RequestBody UpdateMembershipLevelDto updateMembershipLevelDto, @PathVariable Long id, @RequestHeader String authorization ) throws MemberException, ParseException {
        String email = TokenDecoder.tokenDecode(authorization);
        return memberService.updateMembershipLevel(updateMembershipLevelDto, id, email);
    }

}
