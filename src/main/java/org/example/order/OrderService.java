package org.example.order;
import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import org.example.BaseService;
import java.util.List;

public class OrderService extends BaseService {
    @Step
    public ValidatableResponse createOrder(Order order, String token) {
        return spec()
                .header("Authorization", token)
                .body(order)
                .post("/orders")
                .then();
    }

    public ValidatableResponse getOrders(String token) {
        return spec()
                .header("Authorization", token)
                .get("/orders")
                .then();
    }
}
