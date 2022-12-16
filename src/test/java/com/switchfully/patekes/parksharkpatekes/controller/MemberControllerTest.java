package com.switchfully.patekes.parksharkpatekes.controller;


import com.switchfully.patekes.parksharkpatekes.dto.MemberDto;
import com.switchfully.patekes.parksharkpatekes.dto.NewMemberDto;
import com.switchfully.patekes.parksharkpatekes.dto.UpdateMembershipLevelDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.model.*;
import com.switchfully.patekes.parksharkpatekes.repository.DivisionRepository;
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

    private NewMemberDto testMemberBronze = new NewMemberDto("test", "bronze@mail.com", "pwd",
            new Name("frany", "de vetttigaard"), "0478000000",
            new LicensePlate("123456", "BE"),
            new Address("MechelseSteenWeg", 78, new PostalCode(2000, "AN")), "bronze");
    private NewMemberDto testMemberGold = new NewMemberDto("test", "gold@mail.com", "pwd",
            new Name("frany", "de vetttigaard"), "0478000000",
            new LicensePlate("123", "BE"),
            new Address("MechelseSteenWeg", 78, new PostalCode(2000, "AN")), "gold");
    private static String tokenAsString;
    private static String bronzeToken;
    private static String goldToken;
    @Autowired
    private DivisionRepository divisionRepository;

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

    void createTestMembers() throws MemberException {
        RestAssured
                .given().port(port).header("Authorization", "Bearer " + tokenAsString).contentType("application/json").body(testMemberBronze)
                .when().post("parksharkpatekes/user")
                .then().statusCode(201);
        RestAssured
                .given().port(port).header("Authorization", "Bearer " + tokenAsString).contentType("application/json").body(testMemberGold)
                .when().post("parksharkpatekes/user")
                .then().statusCode(201);

        bronzeToken = RestAssured
                .given().baseUri("https://keycloak.switchfully.com")
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", "bronze@mail.com")
                .formParam("password", "pwd")
                .formParam("grant_type", "password")
                .formParam("client_id", "parkshark-patekes")
                .formParam("client_secret", "9SqtwsMTNVNqYFG9eP1rGgcgkKGpWNIA")
                .when()
                .post("/auth/realms/parksharkpatekes/protocol/openid-connect/token")
                .then()
                .extract().as(JSONObject.class).getAsString("access_token");
        goldToken = RestAssured
                .given().baseUri("https://keycloak.switchfully.com")
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", "gold@mail.com")
                .formParam("password", "pwd")
                .formParam("grant_type", "password")
                .formParam("client_id", "parkshark-patekes")
                .formParam("client_secret", "9SqtwsMTNVNqYFG9eP1rGgcgkKGpWNIA")
                .when()
                .post("/auth/realms/parksharkpatekes/protocol/openid-connect/token")
                .then()
                .extract().as(JSONObject.class).getAsString("access_token");

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

    @Test
    @DirtiesContext
    void updateMembershipLevelBronzeToSilver_HappyPath() throws MemberException {
        createTestMembers();
        System.out.println();
        UpdateMembershipLevelDto updateMembershipLevelDto = new UpdateMembershipLevelDto("silver");
        MemberDto result =
                RestAssured.given().port(port).header("Authorization", "Bearer " + bronzeToken).contentType("application/json").body(updateMembershipLevelDto)
                        .when().put("member/membershiplevel/1")
                        .then().statusCode(200).and().extract().as(MemberDto.class);

        assertEquals(MembershipLvl.SILVER, result.membershipLvl());
        assertEquals(MembershipLvl.SILVER,memberRepository.findAll().get(0).getMembershipLvl());
    }

    @Test
    @DirtiesContext
    void updateMembershipLevelBronzeToSilver_WhenWrongUser_ExceptionAndCustomMessage() throws MemberException {
        createTestMembers();
        System.out.println();
        UpdateMembershipLevelDto updateMembershipLevelDto = new UpdateMembershipLevelDto("silver");
        JSONObject result =
                RestAssured.given().port(port).header("Authorization", "Bearer " + goldToken).contentType("application/json").body(updateMembershipLevelDto)
                        .when().put("member/membershiplevel/1")
                        .then().statusCode(400).and().extract().as(JSONObject.class);

        assertEquals("Not authorized, tried to update wrong account", result.getAsString("message"));
    }
    @Test
    @DirtiesContext
    void updateMembershipLevel_WhenWrongInput_ExceptionAndCustomMessage() throws MemberException {
        createTestMembers();
        System.out.println();
        UpdateMembershipLevelDto updateMembershipLevelDto = new UpdateMembershipLevelDto("feznhgeyuf");
        JSONObject result =
                RestAssured.given().port(port).header("Authorization", "Bearer " + bronzeToken).contentType("application/json").body(updateMembershipLevelDto)
                        .when().put("member/membershiplevel/1")
                        .then().statusCode(400).and().extract().as(JSONObject.class);

        assertEquals("Wrong input given: please select a valid Membershiplevel (bronze, silver or gold).", result.getAsString("message"));
    }

    @Test
    @DirtiesContext
    void updateMembershipLevelBronzeToSilver_WhenIdNotInDatabase_ExceptionAndCustomMessage() throws MemberException {
        createTestMembers();
        System.out.println();
        UpdateMembershipLevelDto updateMembershipLevelDto = new UpdateMembershipLevelDto("feznhgeyuf");
        JSONObject result =
                RestAssured.given().port(port).header("Authorization", "Bearer " + bronzeToken).contentType("application/json").body(updateMembershipLevelDto)
                        .when().put("member/membershiplevel/77")
                        .then().statusCode(400).and().extract().as(JSONObject.class);

        assertEquals("Could not find specified member.", result.getAsString("message"));
    }


}
