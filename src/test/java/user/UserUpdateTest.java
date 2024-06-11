package user;

import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;

@DisplayName("Создание пользователя: PATCH /api/auth/user")
public class UserUpdateTest {
    String accessToken;
    @Test
    @DisplayName("Имя и e-mail обновлены")
    public void updateSuccessUser() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setName("name" + user.getName());
        user.setEmail("email" + user.getEmail());
        response = UserMethods.updateUser(user, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("user.email", Matchers.equalTo(user.getEmail()))
                .and().body("user.name", Matchers.equalTo(user.getName()));
    }
    @Test
    @DisplayName("Имя и e-mail необновлены при некорректном токене")
    public void updateUserWithIncorrectToken() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setName("name" + user.getName());
        user.setEmail("email" + user.getEmail());
        String incorrectToken = accessToken+"abc";
        response = UserMethods.updateUser(user, incorrectToken);
        response.then().log().all()
                .assertThat().statusCode(403)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("invalid signature"));
    }
    @Test
    @DisplayName("Имя и e-mail обновлены без Токена")
    public void updateUserWithoutToken() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setName("name" + user.getName());
        user.setEmail("email" + user.getEmail());
        response = UserMethods.updateUserWithoutToken(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }
    @Test
    @DisplayName("Пароль пользователя обновлен")
    public void updatePasswordUser() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setPassword("password" + user.getPassword());
        response = UserMethods.updateUser(user, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200);
        response = UserMethods.loginUser(user);
        response.then().log().all().assertThat().statusCode(200);
    }
    @Test
    @DisplayName("Пароль пользователя обновлен без токена")
    public void updatePasswordUserWithoutToken() {
        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        user.setPassword("password" + user.getPassword());
        response = UserMethods.updateUserWithoutToken(user);
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
        response = UserMethods.loginUser(user);
        response.then().log().all().assertThat().statusCode(401);
    }
    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserMethods.deleteUser(accessToken);
        }
    }}