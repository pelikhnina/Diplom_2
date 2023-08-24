package org.example.user;

import io.qameta.allure.Step;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ValidatableResponse;

import static io.restassured.RestAssured.given;

public class AuthService {
    static {
        RestAssured.baseURI = "https://stellarburgers.nomoreparties.site";
        RestAssured.basePath = "/api/auth";
        RestAssured.requestSpecification = given()
                .contentType(ContentType.JSON)  // Выставляем contentType по умолчанию
                .accept(ContentType.JSON);      // Устанавливаем accept заголовок по умолчанию
    }
    @Step
    public ValidatableResponse createUser(User user) {
        return given()
                .body(user)
                .post("/register")
                .then();
    }

    @Step
    public ValidatableResponse loginUser(User user) {
        return given()
                .body(user)
                .post("/login")
                .then();
    }
}
