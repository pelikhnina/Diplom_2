import io.restassured.response.ValidatableResponse;
import org.example.ingredient.Ingredient;
import org.example.ingredient.IngredientService;
import org.example.ingredient.Ingredients;
import org.example.order.Order;
import org.example.order.OrderAssertions;
import org.example.order.OrderService;
import org.example.user.AuthService;
import org.example.user.User;
import org.example.user.UserAssertions;
import org.example.user.UserGenerator;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.*;

public class OrdersListTest {
    private final UserGenerator userGenerator = new UserGenerator();
    private final AuthService authService = new AuthService();
    private final OrderService orderService = new OrderService();
    private final IngredientService ingredientService = new IngredientService();
    private final List<String> accessTokens = new ArrayList<>();


    @Test
    public void getUserOrders() {

        User user = userGenerator.generateRandomUser();
        ValidatableResponse response = authService.createUser(user);
        UserAssertions.createdSuccessfully(response, user);

        String accessToken = response.extract().path("accessToken");
        Ingredients ingredients = ingredientService.getIngredientsApi();

        List<String> ingredientIds = new ArrayList<>();

        for (Ingredient ingredient : ingredients.getIngredients()) {
            ingredientIds.add(ingredient.getId());
        }

        Order order = new Order();
        order.setIngredients(ingredientIds);
        ValidatableResponse createOrderResponse = orderService.createOrder(order, accessToken);
        OrderAssertions.createdSuccessfully(createOrderResponse);

        ValidatableResponse userOrdersResponse = orderService.getOrders(accessToken);

        userOrdersResponse.assertThat()
                        .statusCode(200)
                        .body("success", equalTo(true))
                        .body("orders.size()", greaterThan(0));

        accessTokens.add(accessToken);

    }
    @Test
    public void getUserOrdersNoAuth() {
        User user = userGenerator.generateRandomUser();
        ValidatableResponse response = authService.createUser(user);
        UserAssertions.createdSuccessfully(response, user);

        String accessToken = response.extract().path("accessToken");
        Ingredients ingredients = ingredientService.getIngredientsApi();

        List<String> ingredientIds = new ArrayList<>();

        for (Ingredient ingredient : ingredients.getIngredients()) {
            ingredientIds.add(ingredient.getId());
        }

        Order order = new Order();
        order.setIngredients(ingredientIds);
        ValidatableResponse createOrderResponse = orderService.createOrder(order, accessToken);
        OrderAssertions.createdSuccessfully(createOrderResponse);

        ValidatableResponse userOrdersResponse = orderService.getOrders("");

        userOrdersResponse.assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));

    }
}
