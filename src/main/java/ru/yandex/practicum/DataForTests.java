package ru.yandex.practicum;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.order.Ingredient;
import ru.yandex.practicum.order.Order;
import ru.yandex.practicum.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static io.restassured.RestAssured.given;

public class DataForTests {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final String URI_FOR_ORDERS = "/api/orders";
    private static final String URI_FOR_REGISTER_USER = "/api/auth/register";
    private static final String URI_FOR_LOGIN_USER = "/api/auth/login";
    private static final String URI_FOR_UPDATE_OR_DELETE_USER = "/api/auth/user";
    private static final String URI_FOR_LOGOUT_USER = "/api/auth/logout";
    private static final String URI_FOR_UPDATE_TOKEN = "/api/auth/token";
    private static final String URI_FOR_GET_INGREDIENTS = "/api/ingredients";
    public static final int MAX_ORDER_IN_USER = 5;
    public static String accessToken;
    public static String refreshToken;
    private static Random random = new Random();

    @Step("Создание юзера")
    public static Response createUserInApi(User user) {
        Response response = given()
                .header("Content-Type", "application/json")
                .and()
                .body(user)
                .when()
                .post(URI_FOR_REGISTER_USER);
        accessToken = response.then().extract().path("accessToken");
        refreshToken = response.then().extract().path("refreshToken");

        return response;
    }

    @Step("Получение списка ингредиентов")
    public static Response getIngredients() {
        return given()
                .header("Content-Type", "application/json")
                .when()
                .get(URI_FOR_GET_INGREDIENTS);
    }

    @Step("Создание заказа с авторизацией")
    public static Response createOrder(List<Ingredient> ingredients) {
        List<String> ingredientsHash = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            ingredientsHash.add(ingredient._id);
        }
        Order order = new Order(ingredientsHash);

        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(order)
                .when()
                .post(URI_FOR_ORDERS);
    }

    @Step("Создание заказа без авторизации")
    public static Response createOrderWithoutAutoriz(List<Ingredient> ingredients) {
        List<String> ingredientsHash = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            ingredientsHash.add(ingredient._id);
        }
        Order order = new Order(ingredientsHash);

        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(order)
                .when()
                .post(URI_FOR_ORDERS);
    }

    @Step("Создание заказа с неправильным хешем ингредиентов")
    public static Response createInvalidHashOrder(List<Ingredient> ingredients) {
        List<String> ingredientsHash = new ArrayList<>();
        for (Ingredient ingredient : ingredients) {
            ingredientsHash.add(ingredient.type);
        }
        Order order = new Order(ingredientsHash);

        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(order)
                .when()
                .post(URI_FOR_ORDERS);
    }

    @Step("Авторизация юзера")
    public static Response loginUserInApi(User user) {
        return given()
                .header("Content-Type", "application/json")
                .body(user)
                .when()
                .post(URI_FOR_LOGIN_USER);
    }

    @Step("Обновление данных юзера")
    public static Response updateDataUserInApi(User user) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(user)
                .when()
                .patch(URI_FOR_UPDATE_OR_DELETE_USER);
    }

    @Step("Обновление данных юзера без авторизации")
    public static Response updateDataUserWithoutAutorizInApi(User user) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(URI_FOR_UPDATE_OR_DELETE_USER);
    }

    @Step("Удаление юзера")
    public static Response deleteUser() {
        if (accessToken != null) {
            return given()
                    .header("Content-Type", "application/json")
                    .header("Authorization", accessToken)
                    .when()
                    .delete(URI_FOR_UPDATE_OR_DELETE_USER);
        }
        return null;
    }

    @Step("Получение списка заказов юзера с авторизацией")
    public static Response getOrderUser() {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .when()
                .get(URI_FOR_ORDERS);
    }

    @Step("Получение списка заказов юзера без авторизации")
    public static Response getOrderUserNoAutoriz() {
        return given()
                .header("Content-Type", "application/json")
                .when()
                .get(URI_FOR_ORDERS);
    }

    public static int randomQuantityOrders() {
        return random.nextInt(MAX_ORDER_IN_USER);
    }
}
