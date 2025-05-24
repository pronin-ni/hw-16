package tests;

import io.restassured.RestAssured;
import org.junit.jupiter.api.BeforeAll;

public class BaseTest {
    private static final String baseUri = "https://reqres.in/";
    private static final String basePath = "/api";

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = baseUri;
        RestAssured.basePath = basePath;
    }
}
