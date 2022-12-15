package com.switchfully.patekes.parksharkpatekes.controller;


import com.switchfully.patekes.parksharkpatekes.dto.MemberDto;
import com.switchfully.patekes.parksharkpatekes.service.MemberService;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    public List<MemberDto> getAllMembers(){
        return memberService.getAllMembers();
    }

}
