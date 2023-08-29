import io.restassured.response.ValidatableResponse;
import org.example.user.User;
import org.example.user.UserAssertions;
import org.example.user.UserGenerator;
import org.example.user.AuthService;
import org.junit.After;
import org.junit.Test;
import java.util.*;

import static org.hamcrest.CoreMatchers.equalTo;

public class CreateUserTest {
    private final UserGenerator userGenerator = new UserGenerator();
    private final AuthService authService = new AuthService();
    private final List<String> accessTokens = new ArrayList<>();

    @Test
    public void createUser() {
        User user = userGenerator.generateRandomUser();
        ValidatableResponse response = authService.createUser(user);
        UserAssertions.createdSuccessfully(response, user);

        accessTokens.add(response.extract().path("accessToken"));
    }
    @Test
    public void createAlreadyRegisteredUser() {
        User user = userGenerator.generateRandomUser();
        ValidatableResponse response = authService.createUser(user);
        UserAssertions.createdSuccessfully(response, user);
        accessTokens.add(response.extract().path("accessToken"));

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
        accessTokens.add(response.extract().path("accessToken"));

        response.assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("Email, password and name are required fields"));
    }

    @After
    public void tearDown() {
        for (String accessToken : accessTokens) {
            authService.deleteUser(accessToken);
        }
        accessTokens.clear();
    }
}
