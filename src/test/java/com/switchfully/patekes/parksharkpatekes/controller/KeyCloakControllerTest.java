package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.NewMemberDto;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.mapper.MemberMapper;
import com.switchfully.patekes.parksharkpatekes.model.*;
import com.switchfully.patekes.parksharkpatekes.repository.MemberRepository;
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
    private MemberRepository memberRepository;
    @Autowired
    private MemberMapper memberMapper;
    private static String tokenAsString;
    private Name firstMemberName = new Name("firstname", "lastname");
    private LicensePlate licensePlate = new LicensePlate("1234567ss", "Belgium");
    private Address address = new Address("fristmemberstreet", 5, new PostalCode(8000, "BE"));
    private NewMemberDto firstNewMemberDto = new NewMemberDto("username", "username@email.com", "123456789",
            firstMemberName,"123456789", licensePlate, address);
    @Autowired
    private PostalCodeRepository postalCodeRepository;

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
    void createUser_whenGivenGoodDTO_addMemberToDatabase() throws MemberException {
        NewMemberDto newMemberDto = new NewMemberDto("klootzak", "klootzak.test@email.com", "123456789",
                new Name("klooty", "klootzakson"),"123456789", new LicensePlate("testplate5656", "Lolaland"),
                new Address("lolateststreet", 150, new PostalCode(66666, "RO")),
                "BRONZE");

        Member newMember = memberMapper.CreateMemberfromMemberDto(newMemberDto);

        RestAssured.given().port(port)
                .contentType("application/json").body(newMemberDto)
                .when().post("parksharkpatekes/user")
                .then().statusCode(201);


                assertTrue(memberService.getAllMembersAsMembers().contains(newMember));

    }

//    @Test
//    @DirtiesContext
//    void createUser_whenGivenBadDtoEmail_doNotAddMemberToDatabase(){
//        NewMemberDto newMemberDto = new NewMemberDto("test", "", "123456789",
//                new Name("testy", "testerson"),"123456789", new LicensePlate("testplate", "Belgium"),
//                new Address("testmemberstreet", 10, new PostalCode(5555, "DE")),
//                "BRONZE");
//
//        Member newMember = new Member(new Name("testy", "testerson"), "123456789",
//                "","123456789",MembershipLvl.BRONZE,new LicensePlate("testplate", "Belgium"),
//                new Address("testmemberstreet", 10, new PostalCode(5555, "DE")));
//
//        RestAssured.given().port(port)
//                .contentType("application/json").body(newMemberDto)
//                .when().post("parksharkpatekes/user")
//                .then().statusCode(400);
//
//    }
}