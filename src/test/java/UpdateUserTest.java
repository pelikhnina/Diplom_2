import io.restassured.response.ValidatableResponse;
import org.example.user.AuthService;
import org.example.user.User;
import org.example.user.UserAssertions;
import org.example.user.UserGenerator;
import org.junit.Test;
import static org.hamcrest.CoreMatchers.equalTo;

public class UpdateUserTest {

    private AuthService authService = new AuthService();
    private UserGenerator userGenerator = new UserGenerator();

    @Test
    public void updateUser() {
        User user = userGenerator.generateRandomUser();
        ValidatableResponse response = authService.createUser(user);
        UserAssertions.createdSuccessfully(response, user);

        String accessToken = response.extract().path("accessToken");

        User updatedUser = userGenerator.generateRandomUser();
        ValidatableResponse updateResponse = authService.updateUser(updatedUser, accessToken);

        updateResponse.assertThat()
                .statusCode(200)
                .body("success", equalTo(true))
                .body("user.email", equalTo(updatedUser.getEmail()))
                .body("user.name", equalTo(updatedUser.getName()));
    }

    @Test
    public void updateUserNoAuth() {
        User user = userGenerator.generateRandomUser();
        ValidatableResponse response = authService.createUser(user);
        UserAssertions.createdSuccessfully(response, user);

        User updatedUser = userGenerator.generateRandomUser();
        ValidatableResponse updateResponse = authService.updateUser(updatedUser, null);

        updateResponse.assertThat()
                .statusCode(401)
                .body("success", equalTo(false))
                .body("message", equalTo("You should be authorised"));
    }

    @Test
    public void updateUserSameEmail() {
        User user = userGenerator.generateRandomUser();
        ValidatableResponse response = authService.createUser(user);
        UserAssertions.createdSuccessfully(response, user);

        String accessToken = response.extract().path("accessToken");

        User updatedUser = userGenerator.generateRandomUser();
        updatedUser.setEmail(user.getEmail());
        ValidatableResponse updateResponse = authService.updateUser(updatedUser, accessToken);

        updateResponse.assertThat()
                .statusCode(403)
                .body("success", equalTo(false))
                .body("message", equalTo("User with such email already exists"));
    }

}
