package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserGenerator;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;

public class UserLoginTest {
    private User user;

    @Before
    public void setUp() {
        RestAssured.baseURI = DataForTests.BASE_URI;
        user = UserGenerator.randomUser();
    }

    @Test
    @DisplayName("Логин юзера")
    public void testLoginUserCorrectDataReturnTokenAnd200() {
        DataForTests.createUserInApi(user);
        Response response = DataForTests.loginUserInApi(user);
        response.then()
                .statusCode(SC_OK)
                .assertThat().body("accessToken", matchesRegex("^Bearer .*"));
    }

    @Test
    @DisplayName("Попытка логина юзера с некоректными данными")
    public void testLoginUserIncorrectDataReturnErrorAnd401() {
        DataForTests.createUserInApi(user);
        user = UserGenerator.randomUser();
        Response response = DataForTests.loginUserInApi(user);
        response.then()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false));
    }

    @After
    public void setDown() {
        DataForTests.deleteUser();
    }
}
