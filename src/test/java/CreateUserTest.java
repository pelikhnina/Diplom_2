import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserAssertions;
import org.example.user.UserGenerator;
import org.example.user.AuthService;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {
    private static final String BASE_URI = "https://stellarburgers.nomoreparties.site/";
    private static final String API= "/api";
    private final UserGenerator userGenerator = new UserGenerator();
    private final AuthService authService = new AuthService();

    @Test
    public void createUser() {
        User user = userGenerator.generateRandomUser();
        ValidatableResponse response = authService.createUser(user);

        UserAssertions.createdSuccessfully(response, user);
    }
    @Test
    public void createAlreadyRegisteredUser() {
        User user = userGenerator.generateRandomUser();
        ValidatableResponse response = authService.createUser(user);
        UserAssertions.createdSuccessfully(response, user);

        ValidatableResponse invalidResponse = authService.createUser(user);
        invalidResponse.assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User already exists"));
    }
    @Test
    public void createUserWithMissingFields() {
        User user = userGenerator.generateRandomUser();
        user.setPassword("");
        ValidatableResponse response = authService.createUser(user);

        response.assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

}
