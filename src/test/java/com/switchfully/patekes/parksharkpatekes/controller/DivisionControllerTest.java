//package com.switchfully.patekes.parksharkpatekes.controller;
//
//import com.switchfully.patekes.parksharkpatekes.dto.CreateDivisionDto;
//import com.switchfully.patekes.parksharkpatekes.dto.DivisionDto;
//import com.switchfully.patekes.parksharkpatekes.model.Division;
//import com.switchfully.patekes.parksharkpatekes.repository.DivisionRepository;
//import com.switchfully.patekes.parksharkpatekes.service.DivisionService;
//import org.junit.jupiter.api.BeforeAll;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.boot.test.web.server.LocalServerPort;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
//@AutoConfigureTestDatabase
//public class DivisionControllerTest {
//    @LocalServerPort
//    private int port;
//    private final static String URL = "https://keycloak.switchfully.com/auth/realms/parksharkpatekes/protocol/openid-connect/token";
//    private static String response;
//
//    @Autowired
//    private DivisionService divisionService;
//    @Autowired
//    private DivisionRepository divisionRepository;
//
//    @BeforeAll
//    static void setUp() {
//
//        response = RestAssured
//                .given()
//                .contentType("application/x-www-form-urlencoded; charset=utf-8")
//                .formParam("grant_type", "password")
//                .formParam("username", "admin")
//                .formParam("password", "password")
//                .formParam("client_id", "parkShark")
//                .formParam("client_secret", "X4mrRxpRG1znHLSDbin6UW3BReYaX29f")
//                .when()
//                .post(URL)
//                .then()
//                .extract()
//                .path("access_token")
//                .toString();
//    }
//
//    @Test
//    void createDivision_whenAdmin_happyPath() {
//        CreateDivisionDto createItemDto = new CreateDivisionDto("test","test", "test");
//
//        DivisionDto result =
//                RestAssured
//                        .given().port(port).auth().preemptive().log().all().contentType("application/json").body(createItemDto)
//                        .when().post("/items")
//                        .then().statusCode(201).and().extract().as(ItemDto.class);
//        assertEquals("Laptop", result.name());
//    }
//
//
//
//}
