package order;

import order.Ingredients;
import order.Order;
import order.OrderMethods;
import user.User;
import user.UserMethods;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Test;
import java.util.ArrayList;

@DisplayName("Получение заказа: GET /api/orders")
public class OrderGetTest {
    private String accessToken;
    @Test
    @DisplayName("Успешное получение заказа")
    public void createOrderWithAuthWithIngredients() {

        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        //создание заказа
        Ingredients ingredients = OrderMethods.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id());
        ingredient1.add(ingredients.getData().get(2).get_id());
        ingredient1.add(ingredients.getData().get(3).get_id());
        Order order = new Order(ingredient1);
        OrderMethods.createOrderWithToken(order, accessToken);

        response = OrderMethods.getOrders(accessToken);
        response.then().log().all()
                .assertThat().statusCode(200)
                .and().body("success", Matchers.is(true))
                .and().body("orders", Matchers.notNullValue())
                .and().body("total", Matchers.any(Integer.class))
                .and().body("totalToday", Matchers.any(Integer.class));
    }
    @Test
    @DisplayName("Неуспешное получение заказа")
    public void createOrderNoAuthWithIngredients() {

        User user = UserMethods.createNewUser();
        Response response = UserMethods.createUser(user);
        accessToken = response.then().log().all().extract().path("accessToken").toString();
        //создание заказа
        Ingredients ingredients = OrderMethods.getIngredient();
        ArrayList<String> ingredient1 = new ArrayList<>();
        ingredient1.add(ingredients.getData().get(1).get_id());
        ingredient1.add(ingredients.getData().get(2).get_id());
        ingredient1.add(ingredients.getData().get(3).get_id());
        Order order = new Order(ingredient1);
        OrderMethods.createOrderWithToken(order, accessToken);

        response = OrderMethods.getOrders();
        response.then().log().all()
                .assertThat().statusCode(401)
                .and().body("success", Matchers.is(false))
                .and().body("message", Matchers.is("You should be authorised"));
    }
    @After
    public void deleteUser() {
        if (accessToken != null) {
            UserMethods.deleteUser(accessToken);
        }
    }}