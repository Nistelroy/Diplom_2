package ru.yandex.practicum;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserGenerator;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.Matchers.matchesRegex;

public class CreatingUserTest {
    private User user;

    @Before
    public void setUp() {
        RestAssured.baseURI = DataForTests.BASE_URI;
        user = UserGenerator.randomUser();
    }

    @Test
    public void testCreateUserCorrectDataReturnTokenAnd200() {
        Response response = DataForTests.createUserInApi(user);

        response.then()
                .statusCode(SC_OK)
                .assertThat().body("accessToken", matchesRegex("^Bearer .*"));
    }

    @Test
    public void testCreateUserAlreadyRegisteredDataReturnErrorAnd403() {
        DataForTests.createUserInApi(user);
        String tokenForDeleteUser = DataForTests.accessToken;
        Response response = DataForTests.createUserInApi(user);
        DataForTests.accessToken = tokenForDeleteUser;

        response.then()
                .statusCode(SC_FORBIDDEN)
                .assertThat().body("message", matchesRegex("User already exists"));
    }

    @Test
    public void testCreateUserNoPartDataReturnErrorAnd403() {
        user.withPassword("");
        Response response = DataForTests.createUserInApi(user);

        response.then()
                .statusCode(SC_FORBIDDEN)
                .assertThat().body("message", matchesRegex("Email, password and name are required fields"));
    }

    @After
    public void setDown() {
        DataForTests.deleteUser();
    }
}
