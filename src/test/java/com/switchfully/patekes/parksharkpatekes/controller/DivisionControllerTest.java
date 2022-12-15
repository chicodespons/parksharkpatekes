package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.CreateDivisionDto;
import com.switchfully.patekes.parksharkpatekes.dto.DivisionDto;
import com.switchfully.patekes.parksharkpatekes.mapper.DivisionMapper;
import com.switchfully.patekes.parksharkpatekes.model.Division;
import com.switchfully.patekes.parksharkpatekes.repository.DivisionRepository;
import com.switchfully.patekes.parksharkpatekes.service.DivisionService;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.test.annotation.DirtiesContext;

import java.util.List;
import java.util.Locale;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class DivisionControllerTest {
    @LocalServerPort
    private int port;


    @Autowired
    private DivisionService divisionService;
    @Autowired
    private DivisionRepository divisionRepository;

    @Autowired
    private DivisionMapper divisionMapper;
    private static String bearerToken;
    private CreateDivisionDto firstDivision = new CreateDivisionDto("firstDivision", "firstDivision", "firstDivision");
    private CreateDivisionDto secondDivision = new CreateDivisionDto("secondDivision", "secondDivision", "secondDivision");
    private CreateDivisionDto thirdDivision = new CreateDivisionDto("thirdDivision", "thirdDivision", "thirdDivision");


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
                .formParam("client_secret", "X4mrRxpRG1znHLSDbin6UW3BReYaX29f")
                .when()
                .post("https://keycloak.switchfully.com/auth/realms/parksharkpatekes/protocol/openid-connect/token")
                .then()
                .extract().as(JSONObject.class);
        bearerToken = response.getAsString("access_token");
    }

    void setUpTestDivisionDatabase() {
        divisionService.createDivision(firstDivision);
        divisionService.createDivision(secondDivision);
        divisionService.createDivision(thirdDivision);
    }

    @Test
    void createDivision_whenAdmin_happyPath() {
        setUpTestDivisionDatabase();
        CreateDivisionDto createDivisionDto = new CreateDivisionDto("testdetest", "testtest", "test");
        DivisionDto result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + bearerToken)
                        .contentType("application/json").body(createDivisionDto)
                        .when().post("/divisions")
                        .then().statusCode(201).and().extract().as(DivisionDto.class);
        assertEquals("testdetest", result.name());
        assertTrue(divisionService.getAllDivisions().contains(result));
    }

    @Test
    void createDivision_whenAdminAndEmptyFields_thenExceptionAndCustomMessage() {
        setUpTestDivisionDatabase();
        CreateDivisionDto createDivisionDto = new CreateDivisionDto("", "", "");
        JSONObject message =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + bearerToken)
                        .contentType("application/json").body(createDivisionDto)
                        .when().post("/divisions")
                        .then().statusCode(400).and().extract().as(JSONObject.class);
        assertEquals("must not be empty", message.getAsString("name"));
        assertEquals("must not be empty", message.getAsString("originalName"));
        assertEquals("must not be empty", message.getAsString("director"));
    }
//    @Test
//    void createDivision_whenAdminAndNullFields_thenExceptionAndCustomMessage() {
//        CreateDivisionDto createDivisionDto = null;
//        JSONObject message =
//                RestAssured
//                        .given().port(port).header("Authorization", "Bearer " + bearerToken)
//                        .contentType("application/json").body(createDivisionDto)
//                        .when().post("/divisions")
//                        .then().statusCode(400).and().extract().as(JSONObject.class);
//        assertEquals("mag niet leeg zijn", message.getAsString("name"));
//        assertEquals("mag niet null zijn", message.getAsString("originalName"));
//        assertEquals("mag niet null zijn", message.getAsString("director"));
//    }

    @Test
    void getAllDivision_whenAdmin_happyPath() {
        setUpTestDivisionDatabase();
        List<DivisionDto> result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + bearerToken)
                        .contentType("application/json")
                        .when().get("/divisions")
                        .then().statusCode(200).and().extract().body().jsonPath().getList(".", DivisionDto.class);
        assertEquals("firstDivision", result.get(0).name());
        assertEquals("secondDivision", result.get(1).name());
        assertEquals("thirdDivision", result.get(2).name());
    }
    @Test
    void getDivisionById_whenAdmin_happyPath() {
        setUpTestDivisionDatabase();
        DivisionDto result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + bearerToken)
                        .contentType("application/json")
                        .when().get("/divisions?id=1")
                        .then().statusCode(200).and().extract().as(DivisionDto.class);
        assertEquals("firstDivision", result.name());
    }
    @Test
    void getDivisionById_whenAdminAndIdNotFound_thenExceptionAndCustomMessage() {
        setUpTestDivisionDatabase();
        JSONObject result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + bearerToken)
                        .contentType("application/json")
                        .when().get("/divisions?id=9")
                        .then().statusCode(400).and().extract().as(JSONObject.class);
        assertEquals("No division found with the id: 9", result.getAsString("message"));
    }
    @Test
    void getDivisionByName_whenAdminAndIdNotFound_happyPath() {
        setUpTestDivisionDatabase();
        List<DivisionDto> result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + bearerToken)
                        .contentType("application/json")
                        .when().get("/divisions?name=firstDivision")
                        .then().statusCode(200).and().extract().body().jsonPath().getList(".", DivisionDto.class);
        assertEquals("firstDivision" , result.get(0).name());
        assertEquals(1, result.size());
    }
    @Test
    void createSubdivision_whenAdmin_happyPath() {
        setUpTestDivisionDatabase();
        CreateDivisionDto createDivisionDto = new CreateDivisionDto("testdetest", "testtest", "test");
        DivisionDto testSubdivision =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + bearerToken)
                        .contentType("application/json").body(createDivisionDto)
                        .when().post("/divisions/2")
                        .then().statusCode(201).and().extract().as(DivisionDto.class);

        DivisionDto result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + bearerToken)
                        .contentType("application/json")
                        .when().get("/divisions?id=2")
                        .then().statusCode(200).and().extract().as(DivisionDto.class);
        assertTrue(result.subdivisions().contains(testSubdivision));
    }

    @Test
    void createSubdivision_whenAdminAndWrongParentId_thenExceptionAndCustomMessage() {
        setUpTestDivisionDatabase();
        CreateDivisionDto createDivisionDto = new CreateDivisionDto("testdetest", "testtest", "test");
        JSONObject testSubdivision =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + bearerToken)
                        .contentType("application/json").body(createDivisionDto)
                        .when().post("/divisions/9")
                        .then().statusCode(400).and().extract().as(JSONObject.class);

        DivisionDto result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + bearerToken)
                        .contentType("application/json")
                        .when().get("/divisions?id=2")
                        .then().statusCode(200).and().extract().as(DivisionDto.class);

        assertEquals("No division found with the id: 9 ,the subdivision creation is cancelled.", testSubdivision.getAsString("message"));
        assertTrue(result.subdivisions().isEmpty());
    }




}
