package com.switchfully.patekes.parksharkpatekes.controller;


import com.switchfully.patekes.parksharkpatekes.dto.MemberDto;
import com.switchfully.patekes.parksharkpatekes.dto.NewMemberDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.model.*;
import com.switchfully.patekes.parksharkpatekes.repository.MemberRepository;
import com.switchfully.patekes.parksharkpatekes.service.MemberService;
import io.restassured.RestAssured;
import io.restassured.common.mapper.TypeRef;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;


@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class MemberControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private MemberService memberService;
    @Autowired
    private MemberRepository memberRepository;


    private static String tokenAsString;

    @BeforeAll
    static void setUp() {
        JSONObject response = RestAssured
                .given().baseUri("https://keycloak.switchfully.com")
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", "admin")
                .formParam("password", "password")
                .formParam("grant_type", "password")
                .formParam("client_id", "parkshark-patekes")
                .formParam("client_secret", "9SqtwsMTNVNqYFG9eP1rGgcgkKGpWNIA")
                .when()
                .post("/auth/realms/parksharkpatekes/protocol/openid-connect/token")
                .then()
                .extract().as(JSONObject.class);
        tokenAsString = response.getAsString("access_token");
    }

    @Test
    @DirtiesContext
    void createMember_whenAdminGetsListOfOne_HappyPath() throws MemberException {
        NewMemberDto newMemberDto = new NewMemberDto("test", "test101@mail.com", "pwd",
                                                    new Name("frany", "de vetttigaard"), "0478000000",
                                                    new LicensePlate("AAA-000", "BE"),
                                                    new Address("MechelseSteenWeg", 78, new PostalCode(2000, "AN")), "SILVER");
        MemberDto expectedMemberDTO = new MemberDto(1, new Name("frany", "de vetttigaard"), "0478000000", "test101@mail.com",
                MembershipLvl.SILVER,
                new LicensePlate("AAA-000", "BE"),
                LocalDateTime.now(),
                new Address("MechelseSteenWeg", 78, new PostalCode(2000, "AN")));
        RestAssured
                .given().port(port).header("Authorization", "Bearer " + tokenAsString).contentType("application/json").body(newMemberDto)
                .when().post("parksharkpatekes/user")
                .then().statusCode(201);
        List<MemberDto> result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + tokenAsString).contentType("application/json")
                        .when().get("/member/list")
                        .then().statusCode(200).and().extract().as(new TypeRef<List<MemberDto>>() {
                        });
        assertEquals(result.get(0), expectedMemberDTO);
    }
}
