import io.restassured.response.ValidatableResponse;
import org.example.user.AuthService;
import org.example.user.User;
import org.example.user.UserAssertions;
import org.example.user.UserGenerator;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class LoginUserTest {
    private final UserGenerator userGenerator = new UserGenerator();
    private final AuthService authService = new AuthService();
    @Test
    public void loginUser() {
        User user = userGenerator.generateRandomUser();
        ValidatableResponse response = authService.createUser(user);
        UserAssertions.createdSuccessfully(response, user);

        ValidatableResponse loginResponse = authService.loginUser(user);
        UserAssertions.loggedInSuccessfully(loginResponse, user);
    }

    @Test
    public void loginInvalidCredentials() {
        User user = userGenerator.generateRandomUser();
        ValidatableResponse response = authService.createUser(user);
        UserAssertions.createdSuccessfully(response, user);

        ValidatableResponse loginResponse = authService.loginUser(user);
        UserAssertions.loggedInSuccessfully(loginResponse, user);

        user.setPassword("");
        user.setEmail("");

        ValidatableResponse invalidLoginResponse = authService.loginUser(user);
        invalidLoginResponse.assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("email or password are incorrect"));
    }
}
