package ru.yandex.practicum;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import ru.yandex.practicum.user.User;

import static io.restassured.RestAssured.given;

public class DataForTests {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site";
    private static final String URI_FOR_ORDERS = "/api/orders";
    private static final String URI_FOR_REGISTER_USER = "/api/auth/register";
    private static final String URI_FOR_LOGIN_USER = "/api/auth/login";
    private static final String URI_FOR_UPDATE_OR_DELETE_USER = "/api/auth/user";
    private static final String URI_FOR_LOGOUT_USER = "/api/auth/logout";
    private static final String URI_FOR_UPDATE_TOKEN = "/api/auth/token";
    private static final String URI_FOR_GET_INGREDIENTS = "/api/auth/ingredients";
    public static String accessToken;
    public static String refreshToken;

    @Step("Create basic user for use in tests")
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

    @Step("Create basic user for use in tests")
    public static Response loginUserInApi(User user) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(user)
                .when()
                .post(URI_FOR_LOGIN_USER);
    }

    @Step("Create basic user for use in tests")
    public static Response updateDataUserInApi(User user) {
        return given()
                .header("Content-Type", "application/json")
                .header("Authorization", accessToken)
                .and()
                .body(user)
                .when()
                .patch(URI_FOR_UPDATE_OR_DELETE_USER);
    }

    @Step("Create basic user for use in tests")
    public static Response updateDataUserWithoutAutorizInApi(User user) {
        return given()
                .header("Content-Type", "application/json")
                .and()
                .body(user)
                .when()
                .patch(URI_FOR_UPDATE_OR_DELETE_USER);
    }

    @Step("Create basic user for use in tests")
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
}
