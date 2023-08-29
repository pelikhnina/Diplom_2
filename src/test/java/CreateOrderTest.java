import io.restassured.response.ValidatableResponse;
import org.example.ingredient.Ingredient;
import org.example.ingredient.IngredientService;
import org.example.ingredient.Ingredients;
import org.example.order.Order;
import org.example.order.OrderService;
import org.example.user.AuthService;
import org.example.user.User;
import org.example.user.UserAssertions;
import org.example.user.UserGenerator;
import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.Matchers.emptyOrNullString;

public class CreateOrderTest {
    private final UserGenerator userGenerator = new UserGenerator();
    private final AuthService authService = new AuthService();
    private final OrderService orderService = new OrderService();
    private final IngredientService ingredientService = new IngredientService();
    private final List<String> accessTokens = new ArrayList<>();

    @Test
    public void createOrder(){
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
        createOrderResponse.assertThat()
                        .statusCode(200)
                        .body("success", equalTo(true))
                        .body("order.number", not(emptyOrNullString()))
                        .body("name", not(emptyOrNullString()));

        accessTokens.add(accessToken);

    }

    @Test
    public void createOrderNoAuth(){
        Ingredients ingredients = ingredientService.getIngredientsApi();

        List<String> ingredientIds = new ArrayList<>();

        for (Ingredient ingredient : ingredients.getIngredients()) {
            ingredientIds.add(ingredient.getId());
        }

        Order order = new Order();
        order.setIngredients(ingredientIds);
        ValidatableResponse createOrderResponse = orderService.createOrder(order, "");
        createOrderResponse.assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("order.number", not(emptyOrNullString()))
                .body("name", not(emptyOrNullString()));
    }

    @Test
    public void createOrderNoIngredients(){
        User user = userGenerator.generateRandomUser();
        ValidatableResponse response = authService.createUser(user);
        UserAssertions.createdSuccessfully(response, user);

        String accessToken = response.extract().path("accessToken");

        List<String> ingredientIds = new ArrayList<>();

        Order order = new Order();
        order.setIngredients(ingredientIds);
        ValidatableResponse createOrderResponse = orderService.createOrder(order, accessToken);
        createOrderResponse.assertThat()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));


        accessTokens.add(accessToken);
    }

    @Test
    public void createOrderInvalidIngredients(){
        User user = userGenerator.generateRandomUser();
        ValidatableResponse response = authService.createUser(user);
        UserAssertions.createdSuccessfully(response, user);

        String accessToken = response.extract().path("accessToken");
        Ingredients ingredients = ingredientService.getIngredientsApi();

        List<String> ingredientIds = new ArrayList<>();

        for (Ingredient ingredient : ingredients.getIngredients()) {
            ingredientIds.add(ingredient.getId() + "ff");
        }

        Order order = new Order();
        order.setIngredients(ingredientIds);
        ValidatableResponse createOrderResponse = orderService.createOrder(order, accessToken);
        createOrderResponse.assertThat()
                .statusCode(500)
                .body(containsString("Internal Server Error"));

        accessTokens.add(accessToken);
    }


    @Test
    public void createOrderNoAuthAndIngredients(){
        List<String> ingredientIds = new ArrayList<>();

        Order order = new Order();
        order.setIngredients(ingredientIds);
        ValidatableResponse createOrderResponse = orderService.createOrder(order, "");
        createOrderResponse.assertThat()
                .statusCode(400)
                .body("success", equalTo(false))
                .body("message", equalTo("Ingredient ids must be provided"));
    }

    @After
    public void tearDown() {
        for (String accessToken : accessTokens) {
            authService.deleteUser(accessToken);
        }
        accessTokens.clear();
    }
}
