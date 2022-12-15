package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.NewMemberDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.mapper.MemberMapper;
import com.switchfully.patekes.parksharkpatekes.model.*;
import com.switchfully.patekes.parksharkpatekes.repository.PostalCodeRepository;
import com.switchfully.patekes.parksharkpatekes.service.MemberService;
import io.restassured.RestAssured;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
class KeyCloakControllerTest {

    @LocalServerPort
    private int port;

    @Autowired
    private MemberService memberService;

    @Autowired
    private MemberMapper memberMapper;
    private static String bearerToken;
    private Name firstMemberName = new Name("firstname", "lastname");
    private LicensePlate licensePlate = new LicensePlate("1234567ss", "Belgium");
    private Address address = new Address("fristmemberstreet", 5, new PostalCode(8000, "BE"));
    private NewMemberDto firstNewMemberDto = new NewMemberDto("username", "username@email.com", "123456789",
            firstMemberName,"123456789", licensePlate, address);
    @Autowired
    private PostalCodeRepository postalCodeRepository;

    @BeforeAll
    public static void setUp() {
        Locale.setDefault(Locale.ENGLISH);
        JSONObject response = RestAssured
                .given()
                .contentType("application/x-www-form-urlencoded")
                .formParam("username", "admin")
                .formParam("password", "password")
                .formParam("grant_type", "password")
                .formParam("client_id", "parkshark-patekes")
                .formParam("client_secret", "9SqtwsMTNVNqYFG9eP1rGgcgkKGpWNIA")
                .when()
                .post("https://keycloak.switchfully.com/auth/realms/parksharkpatekes/protocol/openid-connect/token")
                .then()
                .extract().as(JSONObject.class);
        bearerToken = response.getAsString("access_token");
    }

    void setUpTestDivisionDatabase() throws MemberException {
        memberService.addUser(firstNewMemberDto);
    }

    @Test
    void createUser_whenGivenGoodDTO_addMemberToDatabase() throws MemberException {
        NewMemberDto newMemberDto = new NewMemberDto("test", "test@email.com", "123456789",
                new Name("testy", "testerson"),"123456789", new LicensePlate("testplate", "Belgium"),
                new Address("testmemberstreet", 10, new PostalCode(5555, "DE")));
        Member newMember = memberMapper.CreateMemberfromMemberDto(newMemberDto);
        RestAssured.given().port(port).header("Authorization", "Bearer " + bearerToken)
                .contentType("application/json").body(newMemberDto)
                .when().post("parksharkpatekes/user")
                .then().statusCode(201);

        assertTrue(memberService.getAllMembers().contains(newMember));

    }
}