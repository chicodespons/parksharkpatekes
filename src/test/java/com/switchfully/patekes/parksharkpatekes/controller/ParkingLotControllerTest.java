package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.CreateParkingLotDTO;
import com.switchfully.patekes.parksharkpatekes.dto.ParkingLotDTO;
import com.switchfully.patekes.parksharkpatekes.model.*;
import com.switchfully.patekes.parksharkpatekes.repository.DivisionRepository;
import com.switchfully.patekes.parksharkpatekes.repository.ParkingLotRepository;
import com.switchfully.patekes.parksharkpatekes.service.ParkingLotService;
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

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ParkingLotControllerTest {
    @LocalServerPort
    private int port;

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
                .formParam("client_secret", "9SqtwsMTNVNqYFG9eP1rGgcgkKGpWNIA")
                .when()
                .post("/auth/realms/parksharkpatekes/protocol/openid-connect/token")
                .then()
                .extract().as(JSONObject.class);
        tokenAsString = response.getAsString("access_token");
    }

    Division setUpTestDiv() {
        return divisionRepository.save(new Division("testDiv", "originalCompany", "testDir"));
    }

    Division setUpTestDiv2() {
        return divisionRepository.save(new Division("testDiv2", "originalCompany2", "testDir2"));
    }

    CreateParkingLotDTO setUpCreateParkingLotDTO(Division testDiv) {
        return new CreateParkingLotDTO(testDiv.getId(),
                "testLot1",
                new ContactPerson(new Name("franky","testman"), "0479586525", "01212121212", "vettige@frank.test",
                        new Address("flodderstraat", 60, new PostalCode(9100, "SNC"))),
                new Address("flodderstraat", 180, new PostalCode(9100, "SNC")),
                233,
                Category.ABOVE_GROUND,
                5);
    }

    CreateParkingLotDTO setUpCreateParkingLotDTO2(Division testDiv) {
        return new CreateParkingLotDTO(testDiv.getId(),
                "testLot2",
                new ContactPerson(new Name("frankert","testman"), "04479586525", "012312121212", "vettige2@frank.test",
                        new Address("flodderstraat", 61, new PostalCode(9100, "SNC"))),
                new Address("flodderstraat", 181, new PostalCode(9100, "SNC")),
                1000,
                Category.ABOVE_GROUND,
                5);
    }

    CreateParkingLotDTO setUpCreateParkingLotDTO3(Division testDiv) {
        return new CreateParkingLotDTO(testDiv.getId(),
                "testLot3",
                new ContactPerson(new Name("frankyster","testman"), "04579586525", "0123412121212", "vettige3@frank.test",
                        new Address("flodderstraat", 62, new PostalCode(9100, "SNC"))),
                new Address("flodderstraat", 182, new PostalCode(9100, "SNC")),
                100,
                Category.ABOVE_GROUND,
                100);
    }


    ParkingLotDTO setUpParkingLotDTO(Division testDiv) {
        return new ParkingLotDTO(1,
                testDiv,
                "testLot1",
                new ContactPerson(new Name("franky","testman"), "0479586525", "01212121212", "vettige@frank.test",
                        new Address("flodderstraat", 60, new PostalCode(9100, "SNC"))),
                new Address("flodderstraat", 180, new PostalCode(9100, "SNC")),
                233,
                Category.ABOVE_GROUND,
                5);
    }
    ParkingLotDTO setUpParkingLotDTO2(Division testDiv) {
        return new ParkingLotDTO(2,
                testDiv,
                "testLot2",
                new ContactPerson(new Name("frankert","testman"), "04479586525", "012312121212", "vettige2@frank.test",
                        new Address("flodderstraat", 61, new PostalCode(9100, "SNC"))),
                new Address("flodderstraat", 181, new PostalCode(9100, "SNC")),
                1000,
                Category.ABOVE_GROUND,
                5);
    }
    ParkingLotDTO setUpParkingLotDTO3(Division testDiv) {
        return new ParkingLotDTO(3,
                testDiv,
                "testLot3",
                new ContactPerson(new Name("frankyster","testman"), "04579586525", "0123412121212", "vettige3@frank.test",
                        new Address("flodderstraat", 62, new PostalCode(9100, "SNC"))),
                new Address("flodderstraat", 182, new PostalCode(9100, "SNC")),
                100,
                Category.ABOVE_GROUND,
                100);
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

    @Test
    @DirtiesContext
    void createParkingLot_whenAdminGetsListOfOne_HappyPath() {
        Division testDiv = setUpTestDiv();
        CreateParkingLotDTO createParkingLotDTO = setUpCreateParkingLotDTO(testDiv);
        ParkingLotDTO expectedParkingLotDTO = setUpParkingLotDTO(testDiv);


        RestAssured
                .given().port(port).header("Authorization", "Bearer " + tokenAsString).contentType("application/json").body(createParkingLotDTO)
                .when().post("/parkinglot/add")
                .then().statusCode(201);
        List<ParkingLotDTO> result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + tokenAsString).contentType("application/json")
                        .when().get("/parkinglot/list")
                        .then().statusCode(200).and().extract().as(new TypeRef<List<ParkingLotDTO>>() {
                        });
        assertEquals(result.get(0), expectedParkingLotDTO);
    }

    @Test
    @DirtiesContext
    void createParkingLot_whenAdminGetsListOfTwo_HappyPath() {
        Division testDiv = setUpTestDiv();
        CreateParkingLotDTO createParkingLotDTO = setUpCreateParkingLotDTO(testDiv);
        ParkingLotDTO expectedParkingLotDTO = setUpParkingLotDTO(testDiv);

        Division testDiv2 = setUpTestDiv2();
        CreateParkingLotDTO createParkingLotDTO2 = setUpCreateParkingLotDTO2(testDiv2);
        ParkingLotDTO expectedParkingLotDTO2 = setUpParkingLotDTO2(testDiv2);

        RestAssured
                .given().port(port).header("Authorization", "Bearer " + tokenAsString).contentType("application/json").body(createParkingLotDTO)
                .when().post("/parkinglot/add")
                .then().statusCode(201);
        RestAssured
                .given().port(port).header("Authorization", "Bearer " + tokenAsString).contentType("application/json").body(createParkingLotDTO2)
                .when().post("/parkinglot/add")
                .then().statusCode(201);
        List<ParkingLotDTO> result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + tokenAsString).contentType("application/json")
                        .when().get("/parkinglot/list")
                        .then().statusCode(200).and().extract().as(new TypeRef<List<ParkingLotDTO>>() {
                        });
        assertEquals(result.get(0), expectedParkingLotDTO);
        assertEquals(result.get(1), expectedParkingLotDTO2);
    }

    @Test
    @DirtiesContext
    void GetParkingLotList_whenAdminGetsListOfMore_HappyPath() {
        Division testDiv = setUpTestDiv();
        CreateParkingLotDTO createParkingLotDTO = setUpCreateParkingLotDTO(testDiv);
        ParkingLotDTO expectedParkingLotDTO = setUpParkingLotDTO(testDiv);

        Division testDiv2 = setUpTestDiv2();
        CreateParkingLotDTO createParkingLotDTO2 = setUpCreateParkingLotDTO2(testDiv2);
        ParkingLotDTO expectedParkingLotDTO2 = setUpParkingLotDTO2(testDiv2);

        CreateParkingLotDTO createParkingLotDTO3 = setUpCreateParkingLotDTO3(testDiv2);
        ParkingLotDTO expectedParkingLotDTO3 = setUpParkingLotDTO3(testDiv2);

        RestAssured
                .given().port(port).header("Authorization", "Bearer " + tokenAsString).contentType("application/json").body(createParkingLotDTO)
                .when().post("/parkinglot/add")
                .then().statusCode(201);
        RestAssured
                .given().port(port).header("Authorization", "Bearer " + tokenAsString).contentType("application/json").body(createParkingLotDTO2)
                .when().post("/parkinglot/add")
                .then().statusCode(201);
        RestAssured
                .given().port(port).header("Authorization", "Bearer " + tokenAsString).contentType("application/json").body(createParkingLotDTO3)
                .when().post("/parkinglot/add")
                .then().statusCode(201);
        List<ParkingLotDTO> result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + tokenAsString).contentType("application/json")
                        .when().get("/parkinglot/list")
                        .then().statusCode(200).and().extract().as(new TypeRef<List<ParkingLotDTO>>() {
                        });
        assertEquals(result.get(0), expectedParkingLotDTO);
        assertEquals(result.get(1), expectedParkingLotDTO2);
        assertEquals(result.get(2), expectedParkingLotDTO3);
    }
}
