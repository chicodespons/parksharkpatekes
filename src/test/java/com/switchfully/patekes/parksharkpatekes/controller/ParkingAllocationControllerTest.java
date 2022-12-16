package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.*;
import com.switchfully.patekes.parksharkpatekes.exceptions.KeyCloakCantMakeUserException;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.mapper.ParkingLotMapper;
import com.switchfully.patekes.parksharkpatekes.model.*;
import com.switchfully.patekes.parksharkpatekes.repository.*;
import com.switchfully.patekes.parksharkpatekes.service.MemberService;
import com.switchfully.patekes.parksharkpatekes.service.ParkingLotService;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
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
public class ParkingAllocationControllerTest {
    @LocalServerPort
    private int port;
    @Autowired
    private ParkingLotService parkingLotService;
    @Autowired
    private DivisionRepository divisionRepository;
    @Autowired
    private LicensePlateRepository licensePlateRepository;
    @Autowired
    private MemberService memberService;
    @Autowired
    private ParkingLotMapper parkingLotMapper;
    private static String adminTokenAsString;
    private static String goldMemberTokenAsString;
    private LicensePlate testLicensePlate;

    @BeforeAll
    static void setUp() {
        JSONObject responseAdmin = RestAssured
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
        adminTokenAsString = responseAdmin.getAsString("access_token");

        JSONObject goldResponseMember = RestAssured
                .given().baseUri("https://keycloak.switchfully.com")
                .contentType("application/x-www-form-urlencoded; charset=utf-8")
                .formParam("username", "gold@email.com")
                .formParam("password", "password")
                .formParam("grant_type", "password")
                .formParam("client_id", "parkshark-patekes")
                .formParam("client_secret", "9SqtwsMTNVNqYFG9eP1rGgcgkKGpWNIA")
                .when()
                .post("/auth/realms/parksharkpatekes/protocol/openid-connect/token")
                .then()
                .extract().as(JSONObject.class);
        goldMemberTokenAsString = goldResponseMember.getAsString("access_token");
    }

    LicensePlate setUpLicensePlate() {
        return licensePlateRepository.save(new LicensePlate("1TES1", "Belgium"));
    }

    Division setUpTestDiv() {
        return divisionRepository.save(new Division("testDiv", "originalCompany", "testDir"));
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

    NewMemberDto setUpNewMemberDto(LicensePlate fakeLP) {
        return new NewMemberDto("gold@email.com", "gold@email.com", "password", new Name("test", "gold"), "040000000",
                fakeLP, new Address("teststraat", 201, new PostalCode(9111, "NKW")), "GOLD");
    }

    @Test
    @DirtiesContext
    void allocateParkingSpot_whenGoldMember_happyPath() throws MemberException {
        Division testDiv = setUpTestDiv();
        CreateParkingLotDTO testParkingLotDto = setUpCreateParkingLotDTO(testDiv);
        ParkingLotDTO testParkingLot = parkingLotService.addParkingLot(testParkingLotDto);
        testLicensePlate = setUpLicensePlate();
        memberService.addUser(setUpNewMemberDto(testLicensePlate));
        StartParkingAllocationRequestDto allocationRequestDto = new StartParkingAllocationRequestDto(testLicensePlate, testParkingLot.id());

        ParkingAllocationDto result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + goldMemberTokenAsString).contentType("application/json").body(allocationRequestDto)
                        .when().post("/parking/allocation")
                        .then().statusCode(201).and().extract().as(ParkingAllocationDto.class);

        assertEquals(result.getLicensePlate(), testLicensePlate);
        assertEquals(parkingLotMapper.toDTO(result.getParkingLot()), testParkingLot);
        assertTrue(result.isActive());
    }

    @Test
    @DirtiesContext
    void deAllocateParkingSpot_whenGoldMemberStopAlloc_happyPath() throws MemberException {
        Division testDiv = setUpTestDiv();
        CreateParkingLotDTO testParkingLotDto = setUpCreateParkingLotDTO(testDiv);
        ParkingLotDTO testParkingLot = parkingLotService.addParkingLot(testParkingLotDto);
        testLicensePlate = setUpLicensePlate();
        memberService.addUser(setUpNewMemberDto(testLicensePlate));
        StartParkingAllocationRequestDto allocationRequestDto = new StartParkingAllocationRequestDto(testLicensePlate, testParkingLot.id());


        ParkingAllocationDto resultOfAlloc = RestAssured
                .given().port(port).header("Authorization", "Bearer " + goldMemberTokenAsString).contentType("application/json").body(allocationRequestDto)
                .when().post("/parking/allocation")
                .then().statusCode(201).and().extract().as(ParkingAllocationDto.class);
        EndParkingAllocationRequestDto deAllocationRequestDto = new EndParkingAllocationRequestDto(resultOfAlloc.getAllocationId());
        ParkingAllocationDto result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + goldMemberTokenAsString).contentType("application/json").body(deAllocationRequestDto)
                        .when().put("/parking/allocation")
                        .then().statusCode(201).and().extract().as(ParkingAllocationDto.class);

        assertEquals(result.getLicensePlate(), testLicensePlate);
        assertEquals(parkingLotMapper.toDTO(result.getParkingLot()), testParkingLot);
        assertFalse(result.isActive());
    }

    @Test
    @DirtiesContext
    void allocateParkingSpotWithDiffrentPlate_whenGoldMember_happyPath() throws MemberException, KeyCloakCantMakeUserException {
        Division testDiv = setUpTestDiv();
        CreateParkingLotDTO testParkingLotDto = setUpCreateParkingLotDTO(testDiv);
        ParkingLotDTO testParkingLot = parkingLotService.addParkingLot(testParkingLotDto);
        testLicensePlate = setUpLicensePlate();
        NewMemberDto newMemberDto = setUpNewMemberDto(testLicensePlate);
        LicensePlate testPlate2 = licensePlateRepository.save(new LicensePlate("BBB-222", "NL"));

        RestAssured
                .given().port(port).contentType("application/json").body(newMemberDto)
                .when().post("parksharkpatekes/user")
                .then().statusCode(201);
        StartParkingAllocationRequestDto allocationRequestDto = new StartParkingAllocationRequestDto(new LicensePlate("BBB-222", "NL"), testParkingLot.id());

        ParkingAllocationDto result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + goldMemberTokenAsString).contentType("application/json").body(allocationRequestDto)
                        .when().post("/parking/allocation")
                        .then().statusCode(201).and().extract().as(ParkingAllocationDto.class);

        assertEquals(result.getLicensePlate(), testPlate2);
        assertEquals(parkingLotMapper.toDTO(result.getParkingLot()), testParkingLot);
        assertTrue(result.isActive());
    }

    @Test
    @DirtiesContext
    void getAllMember_whenAdmin_happyPath() throws MemberException {
        Division testDiv = setUpTestDiv();
        CreateParkingLotDTO testParkingLotDto = setUpCreateParkingLotDTO(testDiv);
        ParkingLotDTO testParkingLot = parkingLotService.addParkingLot(testParkingLotDto);
        testLicensePlate = setUpLicensePlate();
        memberService.addUser(setUpNewMemberDto(testLicensePlate));
        StartParkingAllocationRequestDto allocationRequestDto = new StartParkingAllocationRequestDto(testLicensePlate, testParkingLot.id());

        ParkingAllocationDto parkingAllocation =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + goldMemberTokenAsString).contentType("application/json").body(allocationRequestDto)
                        .when().post("/parking/allocation")
                        .then().statusCode(201).extract().as(ParkingAllocationDto.class);

        ParkingAllocationOverviewDto[] result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + adminTokenAsString).contentType("application/json")
                        .when().get("/parking/allocation")
                        .then().statusCode(200).and().extract().as(ParkingAllocationOverviewDto[].class);

        assertEquals(testLicensePlate, result[0].getLicensePlate());
        assertEquals(parkingAllocation.getAllocationId(), result[0].getAllocationId());
    }

    @Test
    @DirtiesContext
    void getAllMember_whenUser_happyPath() throws MemberException {
        testLicensePlate = setUpLicensePlate();
        memberService.addUser(setUpNewMemberDto(testLicensePlate));

        ValidatableResponse result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + goldMemberTokenAsString).contentType("application/json")
                        .when().get("/parking/allocation")
                        .then().statusCode(403);
    }

    @Test
    @DirtiesContext
    void getAllMember_whenAdmin_isActiveFilter() throws MemberException {
        Division testDiv = setUpTestDiv();
        CreateParkingLotDTO testParkingLotDto = setUpCreateParkingLotDTO(testDiv);
        ParkingLotDTO testParkingLot = parkingLotService.addParkingLot(testParkingLotDto);
        testLicensePlate = setUpLicensePlate();
        memberService.addUser(setUpNewMemberDto(testLicensePlate));
        StartParkingAllocationRequestDto allocationRequestDto = new StartParkingAllocationRequestDto(testLicensePlate, testParkingLot.id());

        ParkingAllocationDto parkingAllocation =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + goldMemberTokenAsString).contentType("application/json").body(allocationRequestDto)
                        .when().post("/parking/allocation")
                        .then().statusCode(201).extract().as(ParkingAllocationDto.class);

        EndParkingAllocationRequestDto endAllocationRequestDto = new EndParkingAllocationRequestDto(parkingAllocation.getAllocationId());
        ParkingAllocationDto endParkingAllocation =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + goldMemberTokenAsString).contentType("application/json").body(endAllocationRequestDto)
                        .when().put("/parking/allocation")
                        .then().statusCode(201).extract().as(ParkingAllocationDto.class);

        ParkingAllocationOverviewDto[] result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + adminTokenAsString).contentType("application/json").param("isActive", true)
                        .when().get("/parking/allocation")
                        .then().statusCode(200).and().extract().as(ParkingAllocationOverviewDto[].class);

        assertEquals(0, result.length);
    }

    @Test
    @DirtiesContext
    void getAllMember_whenAdmin_sortingFilter() throws MemberException {
        Division testDiv = setUpTestDiv();
        CreateParkingLotDTO testParkingLotDto = setUpCreateParkingLotDTO(testDiv);
        ParkingLotDTO testParkingLot = parkingLotService.addParkingLot(testParkingLotDto);
        testLicensePlate = setUpLicensePlate();
        memberService.addUser(setUpNewMemberDto(testLicensePlate));
        StartParkingAllocationRequestDto allocationRequestDto = new StartParkingAllocationRequestDto(testLicensePlate, testParkingLot.id());

        ParkingAllocationDto parkingAllocation1 =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + goldMemberTokenAsString).contentType("application/json").body(allocationRequestDto)
                        .when().post("/parking/allocation")
                        .then().statusCode(201).extract().as(ParkingAllocationDto.class);

        ParkingAllocationDto parkingAllocation2 =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + goldMemberTokenAsString).contentType("application/json").body(allocationRequestDto)
                        .when().post("/parking/allocation")
                        .then().statusCode(201).extract().as(ParkingAllocationDto.class);

        ParkingAllocationOverviewDto[] result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + adminTokenAsString).contentType("application/json").param("ascending", false)
                        .when().get("/parking/allocation")
                        .then().statusCode(200).and().extract().as(ParkingAllocationOverviewDto[].class);

        assertEquals(2, result[0].getAllocationId());
        assertEquals(1, result[1].getAllocationId());
    }

    @Test
    @DirtiesContext
    void getAllMember_whenAdmin_limitFilter() throws MemberException {
        Division testDiv = setUpTestDiv();
        CreateParkingLotDTO testParkingLotDto = setUpCreateParkingLotDTO(testDiv);
        ParkingLotDTO testParkingLot = parkingLotService.addParkingLot(testParkingLotDto);
        testLicensePlate = setUpLicensePlate();
        memberService.addUser(setUpNewMemberDto(testLicensePlate));
        StartParkingAllocationRequestDto allocationRequestDto = new StartParkingAllocationRequestDto(testLicensePlate, testParkingLot.id());

        ParkingAllocationDto parkingAllocation1 =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + goldMemberTokenAsString).contentType("application/json").body(allocationRequestDto)
                        .when().post("/parking/allocation")
                        .then().statusCode(201).extract().as(ParkingAllocationDto.class);

        ParkingAllocationDto parkingAllocation2 =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + goldMemberTokenAsString).contentType("application/json").body(allocationRequestDto)
                        .when().post("/parking/allocation")
                        .then().statusCode(201).extract().as(ParkingAllocationDto.class);

        ParkingAllocationOverviewDto[] result =
                RestAssured
                        .given().port(port).header("Authorization", "Bearer " + adminTokenAsString).contentType("application/json").param("limit", 1)
                        .when().get("/parking/allocation")
                        .then().statusCode(200).and().extract().as(ParkingAllocationOverviewDto[].class);

        assertEquals(1, result.length);
    }
}
