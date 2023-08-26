package org.example;

import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;

public class BaseService {
    public static String BASE_URL = "https://stellarburgers.nomoreparties.site";
    public static String BASE_PATH = "/api";

    protected RequestSpecification spec() {
        return given().log().all()
                .contentType(ContentType.JSON)
                .baseUri(BASE_URL)
                .basePath(BASE_PATH);
    }
}
