package org.example.order;

import io.restassured.response.ValidatableResponse;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.emptyOrNullString;

public class OrderAssertions {

    public static void createdSuccessfully(ValidatableResponse response) {
        response.assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", not(emptyOrNullString()))
                .body("name", not(emptyOrNullString()));
    }
}
