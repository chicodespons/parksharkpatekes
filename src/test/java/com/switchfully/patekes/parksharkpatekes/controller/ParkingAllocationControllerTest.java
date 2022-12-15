package com.switchfully.patekes.parksharkpatekes.controller;

import com.switchfully.patekes.parksharkpatekes.dto.*;
import com.switchfully.patekes.parksharkpatekes.exceptions.MemberException;
import com.switchfully.patekes.parksharkpatekes.mapper.ParkingLotMapper;
import com.switchfully.patekes.parksharkpatekes.model.*;
import com.switchfully.patekes.parksharkpatekes.repository.*;
import com.switchfully.patekes.parksharkpatekes.service.MemberService;
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
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureTestDatabase
public class ParkingAllocationControllerTest {
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
    @Autowired
    private LicensePlateRepository licensePlateRepository;
    @Autowired
    private AddressRepository addressRepository;
    @Autowired
    private PostalCodeRepository postalCodeRepository;
    @Autowired
    private ContactPersonRepository contactPersonRepository;
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MemberService memberService;
    @Autowired
    private ParkingLotMapper parkingLotMapper;

    private static String adminTokenAsString;
    private static String goldMemberTokenAsString;
    private static String bronzeMemberTokenAsString;

//    private Member testMember;
//    private PostalCode testPostalCode;
//    private Address testAddress;
    private LicensePlate testLicensePlate;
    private ParkingLot testParkingLot;
//    private ContactPerson testContactPerson;
//    private Division testDivision;

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

//        JSONObject bronzeResponseMember = RestAssured
//                .given().baseUri("https://keycloak.switchfully.com")
//                .contentType("application/x-www-form-urlencoded; charset=utf-8")
//                .formParam("username", "hfffry@email.com")
//                .formParam("password", "password")
//                .formParam("grant_type", "password")
//                .formParam("client_id", "parkshark-patekes")
//                .formParam("client_secret", "9SqtwsMTNVNqYFG9eP1rGgcgkKGpWNIA")
//                .when()
//                .post("/auth/realms/parksharkpatekes/protocol/openid-connect/token")
//                .then()
//                .extract().as(JSONObject.class);
//        bronzeMemberTokenAsString = bronzeResponseMember.getAsString("access_token");
    }

//    void setUpVariables() {
//        testPostalCode = setUpPostalcode();
//        testAddress = setupAddress();
//        testMember = setUpMember();
//        testLicensePlate = setUpLicensePlate();
//        testDivision = setUpDivision();
//        testParkingLot = setUpParkingLot();
//    }
//
//    Division setUpDivision() {
//        return divisionRepository.save(new Division("testDiv", "originalCompany", "testDir"));
//    }
//
    LicensePlate setUpLicensePlate() {
        return licensePlateRepository.save(new LicensePlate("1TES1", "Belgium"));
    }
//
//    Address setupAddress() {
//        return addressRepository.save(new Address("flodderstraat", 180, postalCodeRepository.save(new PostalCode(9100, "SNC"))));
//    }
//
//    PostalCode setUpPostalcode() {
//        return postalCodeRepository.save(new PostalCode(9100, "SNC"));
//    }
//
//    ContactPerson setupContactPerson() {
//        return contactPersonRepository.save(new ContactPerson(new Name("franky","testman"), "0479586525", "01212121212", "vettige@frank.test", addressRepository.save(new Address("flodderstraat", 180, postalCodeRepository.save(new PostalCode(9100, "SNC"))))));
//    }
//
//    ParkingLot setUpParkingLot() {
//        return parkingLotRepository.save(
//                new ParkingLot(testDivision, "testLot2", contactPersonRepository.save(new ContactPerson(new Name("franky","testman"), "0479586525", "01212121212", "vettige@frank.test", addressRepository.save(new Address("flodderstraat", 180, postalCodeRepository.save(new PostalCode(9100, "SNC")))))), addressRepository.save(new Address("flodderstraat", 180, postalCodeRepository.save(new PostalCode(9100, "SNC")))), 1000, Category.ABOVE_GROUND, 5)
//        );
//    }
//
    Member setUpMember() {
        return
                new Member(new Name("Abraham", "Lincoln"), "123", "gold@email.com", "password", MembershipLvl.GOLD, testLicensePlate, addressRepository.save(new Address("flodderstraat", 180, postalCodeRepository.save(new PostalCode(9100, "SNC")))))
        ;
    }
//
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
                fakeLP, new Address("teststraat", 201, new PostalCode(9111, "NKW")), "BRONZE");
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

//    @Test
//    @DirtiesContext
//    void allocateParkingSpotWithDiffrentPlate_whenGoldMember_happyPath() {
//        Division testDiv = setUpTestDiv();
//        CreateParkingLotDTO testParkingLotDto = setUpCreateParkingLotDTO(testDiv);
//        ParkingLotDTO testParkingLot = parkingLotService.addParkingLot(testParkingLotDto);
//        testLicensePlate = setUpLicensePlate();
//        LicensePlate testPlate2 = licensePlateRepository.save(new LicensePlate("BBB-222", "NL"));
//        memberService.addUser(setUpNewMemberDto(testLicensePlate));
//        StartParkingAllocationRequestDto allocationRequestDto = new StartParkingAllocationRequestDto(testPlate2, testParkingLot.id());
//
//        ParkingAllocationDto result =
//                RestAssured
//                        .given().port(port).header("Authorization", "Bearer " + goldMemberTokenAsString).contentType("application/json").body(allocationRequestDto)
//                        .when().post("/parking/allocation")
//                        .then().statusCode(201).and().extract().as(ParkingAllocationDto.class);
//
//        assertEquals(result.getLicensePlate(), testPlate2);
//        assertEquals(parkingLotMapper.toDTO(result.getParkingLot()), testParkingLot);
//        assertTrue(result.isActive());
//    }
}
