package order;

import user.User;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;
import org.hamcrest.Matchers;
import java.util.ArrayList;
import user.UserMethods;
@DisplayName("Создание заказа: POST /api/orders")
public class OrderCreateTest {
    private String accessToken;


    @Test
    @DisplayName("Успешное создание заказа с ингредиентами без авторизации")
    public void createOrderNoAuthWithIngredients() {
        Ingredients ingredients = OrderMethods.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id());
        ingredient1.add(ingredients.getData().get(2).get_id());
        ingredient1.add(ingredients.getData().get(3).get_id());
        Order order = new Order(ingredient1);
        Response response = OrderMethods.createOrderWithoutToken(order);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.any(Integer.class));
    }
    @Test
    @DisplayName("Успешное создание заказа с ингредиентами без авторизации")
    public void createOrderWithAuthWithIngredients() {

        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();

        Ingredients ingredients = OrderMethods.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id());
        ingredient1.add(ingredients.getData().get(2).get_id());
        ingredient1.add(ingredients.getData().get(3).get_id());
        Order order = new Order(ingredient1);
        response = OrderMethods.createOrderWithToken(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("name", Matchers.notNullValue())
                .and().body("order.number", Matchers.any(Integer.class));
    }
    @Test
    @DisplayName("Создание заказа с ингредиентами без авторизации")
    public void createOrderWithAuthNoIngredients() {

        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();

        Order order = new Order(null);
        response = OrderMethods.createOrderWithToken(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(400)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Создание заказа без авторизации и без ингредиентов")
    public void createOrderNoAuthNoIngredients() {

        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();

        Order order = new Order(null);
        response = OrderMethods.createOrderWithoutToken(order);
        response.then().log().all()
                .assertThat().statusCode(400)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("Ingredient ids must be provided"));
    }
    @Test
    @DisplayName("Создание заказа с авторизацией и c невалидным ингредиентом")
    public void createOrderWithAuthIngredientsNotValid() {

        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();

        Ingredients ingredients = OrderMethods.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id() + "yandex");
        Order order = new Order(ingredient1);
        response = OrderMethods.createOrderWithToken(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(500);
    }
    @Test
    @DisplayName("Создание заказа с авторизацией и c одним невалидным ингредиентом")
    public void createOrderWithAuthOneOfIngredientsNotValid() {

        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();

        Ingredients ingredients = OrderMethods.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id() + "yandex");
        ingredient1.add(ingredients.getData().get(2).get_id());
        ingredient1.add(ingredients.getData().get(3).get_id());
        Order order = new Order(ingredient1);
        response = OrderMethods.createOrderWithToken(order, accessToken);
        response.then().log().all()
                .assertThat().statusCode(500);
    }

    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserMethods.deleteUser(accessToken);
        }
    }}