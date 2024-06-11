package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;


@DisplayName("Создание пользователя: POST /api/auth/register")
public class UserCreateTest {
    String accessToken;

    @Test
    @DisplayName("Создание пользователя")
    public void createSuccessUser() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("accessToken", Matchers.notNullValue())
                .and().body("refreshToken", Matchers.notNullValue())
                .and().body("user.email", Matchers.equalTo(user.getEmail()))
                .and().body("user.name", Matchers.equalTo(user.getName()));
    }
    @Test
    @DisplayName("Cоздание пользователя, который уже зарегистрирован")
    public void createDoubleUser() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        response = UserMethods.createUser(user);
        response.then().log().all()
                .assertThat().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("User already exists"));
    }
    @Test
    @DisplayName("Cоздание пользователя без e-mail")
    public void createUserWithoutEmail() {
        User user = UserMethods.createNewUser();
        user.setEmail(null);
        Response response = UserMethods.createUser(user);
        response.then().log().all()
                .assertThat().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("Cоздание пользователя без поля password")
    public void createUserWithoutPassword() {
        User user = UserMethods.createNewUser();
        user.setPassword(null);
        Response response = UserMethods.createUser(user);
        response.then().log().all()
                .assertThat().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }
    @Test
    @DisplayName("Cоздание пользователя без поля имя")
    public void createUserWithoutName() {
        User user = UserMethods.createNewUser();
        user.setName(null);
        Response response = UserMethods.createUser(user);
        response.then().log().all()
                .assertThat().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Email, password and name are required fields"));
    }
    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserMethods.deleteUser(accessToken);
        }
    }}