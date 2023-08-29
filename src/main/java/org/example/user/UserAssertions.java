package org.example.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.CoreMatchers;

import static java.net.HttpURLConnection.HTTP_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.startsWith;
import static org.hamcrest.Matchers.emptyOrNullString;

public class UserAssertions {
    @Step
    public static void createdSuccessfully(ValidatableResponse response, User expectedUser) {
        response
                .statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(expectedUser.getEmail()))
                .body("user.name", equalTo(expectedUser.getName()))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", CoreMatchers.not(emptyOrNullString()));
    }

    @Step
    public static void loggedInSuccessfully(ValidatableResponse response, User expectedUser) {
        response
                .statusCode(HTTP_OK)
                .body("success", equalTo(true))
                .body("user.email", equalTo(expectedUser.getEmail()))
                .body("user.name", equalTo(expectedUser.getName()))
                .body("accessToken", startsWith("Bearer "))
                .body("refreshToken", CoreMatchers.not(emptyOrNullString()));
    }

}