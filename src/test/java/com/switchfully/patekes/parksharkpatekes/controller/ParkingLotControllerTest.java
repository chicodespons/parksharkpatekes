package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.CreateParkingLotDTO;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingLotDTO;
import com.switchfully.patekes.parksharkpatekes.model.*;
import com.switchfully.patekes.parksharkpatekes.repository.DivisionRepository;
import com.switchfully.patekes.parksharkpatekes.repository.ParkingLotRepository;
import com.switchfully.patekes.parksharkpatekes.service.ParkingLotService;
import io.restassured.RestAssured;
import net.minidev.json.JSONObject;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.annotation.DirtiesContext;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ParkingLotControllerTest {
    @LocalServerPort
    private int port;
    private final static String URL = "https://keycloak.switchfully.com/auth/realms/parksharkpatekes/protocol/openid-connect/token";
    private static String response;

    @Autowired
    private ParkingLotService parkingLotService;
    @Autowired
    private ParkingLotRepository parkingLotRepository;
    @Autowired
    private DivisionRepository divisionRepository;

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
                .formParam("client_secret", "X4mrRxpRG1znHLSDbin6UW3BReYaX29f")
                .when()
                .post("/auth/realms/parksharkpatekes/protocol/openid-connect/token")
                .then()
                .extract().as(JSONObject.class);
        tokenAsString = response.getAsString("access_token");
    }

    Division setUpTestDiv() {
        return divisionRepository.save(new Division("testDiv", "originalCompany", "testDir"));
    }

    CreateParkingLotDTO setUpCreateParkingLotDTO(Division testDiv) {
        return new CreateParkingLotDTO(testDiv.getId(),
                "testLot2",
                new ContactPerson(new Name("franky","testman"), "0479586525", "01212121212", "vettige@frank.test",
                        new Address("flodderstraat", 60, new PostalCode(9100, "SNC"))),
                new Address("flodderstraat", 180, new PostalCode(9100, "SNC")),
                1000,
                Category.ABOVE_GROUND,
                5);
    }

    ParkingLotDTO setUpParkingLotDTO(Division testDiv) {
        return new ParkingLotDTO(1000,
                testDiv,
                "testLot2",
                new ContactPerson(new Name("franky","testman"), "0479586525", "01212121212", "vettige@frank.test",
                        new Address("flodderstraat", 60, new PostalCode(9100, "SNC"))),
                new Address("flodderstraat", 180, new PostalCode(9100, "SNC")),
                1000,
                Category.ABOVE_GROUND,
                5);
    }

    @Test
    @DirtiesContext
    void createParkingLot_whenAdmin_happyPath() {
        Division testDiv = setUpTestDiv();
        CreateParkingLotDTO createParkingLotDTO = setUpCreateParkingLotDTO(testDiv);
        ParkingLotDTO expectedParkingLotDTO = setUpParkingLotDTO(testDiv);

        ParkingLotDTO result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + tokenAsString).contentType("application/json").body(createParkingLotDTO)
                        .when().post("/parkinglot/add")
                        .then().statusCode(201).and().extract().as(ParkingLotDTO.class);

        assertEquals(result, expectedParkingLotDTO);
    }


}
