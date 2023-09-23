package org.egx.news.controlles;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import jakarta.transaction.Transactional;
import org.egx.news.entity.Equity;
import org.egx.news.entity.News;
import org.egx.news.repos.EquityRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;

import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@Transactional
class EquityControllerIntegrationTest {

    private final static String BASE_URI = "http://localhost";
    @LocalServerPort
    private int port;
    @Autowired
    private EquityRepository equityRepository;

    @BeforeEach
    void setUp() {
    }
    @Test
    void testFetchEquitiesAsList_whenNoParamsProvided_shouldReturnEquitiesWithSizeOfTen(){

        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        var response = RestAssured.get("/api/v1/equities");
        assertEquals(200,response.getStatusCode());
        List<Equity> equities = JsonPath.from(response.asString()).getList("content",Equity.class);
        assertEquals(10,equities.size());
    }
    @Test
    void testFetchEquitiesAsList_whenSectorFilterIsProvided_shouldReturnEquitiesWithGivenSector() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        String params="?sectorFilter=real Estate";
        var response = RestAssured.get("/api/v1/equities"+params);
        assertEquals(200, response.getStatusCode());
        List<Equity> equities = JsonPath.from(response.asString()).getList("content", Equity.class);
        List<String> real = new ArrayList<>();
        List<String> expected = new ArrayList<>();
        for(var item: equities){
            real.add(item.getSector());
            expected.add("Real Estate");
        }
        assertEquals(expected, real);
    }
    @Test
    void testFetchEquitiesAsList_whenSectorNameIsProvided_shouldReturnEquitiesWithGivenName() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        String params="?nameFilter=Arab Pharmac";
        var response = RestAssured.get("/api/v1/equities"+params);
        assertEquals(200, response.getStatusCode());
        List<Equity> equities = JsonPath.from(response.asString()).getList("content",Equity.class);
        List<Boolean> real = new ArrayList<>();
        List<Boolean> expected = new ArrayList<>();
        for(var item: equities){
            real.add(item.getName().contains("Arab Pharmac"));
            expected.add(true);
        }
        assertEquals(expected, real);
    }

    @Test
    void testGetEquity_whenReutersCodeProvided_shouldReturnRequiredEquity() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        var response = when().get("/api/v1/equities/{code}","MOIN")
                .then().statusCode(200)
                .body("id",equalTo(30));

    }

    @Test
    void testDeleteEquity_whenReutersCodeProvided_shouldDeleteRequiredEquity() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        var response = when().delete("/api/v1/equities/{code}","EXPA")
                .then().statusCode(200);
        var news = equityRepository.findByReutersCode("EXPA");
        assertTrue(news.isEmpty());
    }

    @Test
    void testGetNews_whenReutersCodeProvided_shouldReturnNewsForGivenEquity() {
        RestAssured.baseURI = BASE_URI;
        RestAssured.port = port;
        var response = when().get("/api/v1/equities/{code}/news","DCRC")
                .then().statusCode(200);
    }
}