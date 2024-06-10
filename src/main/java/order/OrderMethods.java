package order;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import static constants.UrlApi.*;

public class OrderMethods {
    @Step("Получение данных об ингредиентах.")
    public static Ingredients getIngredient() {
        return spec()
                .when()
                .get(INGREDIENTS)
                .body()
                .as(Ingredients.class);
    }
    @Step("Создание заказа без токена авторизации")
    public static Response createOrderWithoutToken(Order order) {
        return spec()
                .body(order)
                .when()
                .post(ORDER);
    }
    @Step("Создание заказа с токеном авторизации")
    public static Response createOrderWithToken(Order order, String accessToken) {
        return spec()
                .header("Authorization",accessToken)
                .body(order)
                .when()
                .post(ORDER);
    }
    @Step("Получение заказа с токеном авторизации")
    public static Response getOrders(String accessToken) {
        return spec()
                .header("Authorization",accessToken)
                .when()
                .get(ORDER);
    }
    @Step("Получение заказа без токена авторизации в заголовке")
    public static Response getOrders() {
        return spec()
                .when()
                .get(ORDER);
    }
}