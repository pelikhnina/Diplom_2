package org.example.user;

import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.BaseService;

public class AuthService extends BaseService {
    @Step
    public ValidatableResponse createUser(User user) {
        return spec()
                .body(user)
                .post("/auth/register")
                .then();
    }

    @Step
    public ValidatableResponse loginUser(User user) {
        return spec()
                .body(user)
                .post("/auth/login")
                .then();
    }

    @Step
    public ValidatableResponse updateUser(User user, String token) {
        return spec()
                .header("Authorization", "Bearer" + token)
                .body(user)
                .patch("/auth/user")
                .then();
    }

    @Step
    public ValidatableResponse deleteUser(String token) {
        return spec()
                .header("Authorization", "Bearer" + token)
                .delete("/auth/user")
                .then();
    }
}
