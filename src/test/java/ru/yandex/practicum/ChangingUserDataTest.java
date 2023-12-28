package ru.yandex.practicum;

import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserGenerator;

import static org.apache.http.HttpStatus.SC_ACCEPTED;
import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static org.hamcrest.CoreMatchers.equalTo;

public class ChangingUserDataTest {
    private User user;

    @Before
    public void setUp() {
        RestAssured.baseURI = DataForTests.BASE_URI;
        user = UserGenerator.randomUser();
    }

    @Test
    public void testUpdateUserNewEmailReturnTrueAnd200() {
        DataForTests.createUserInApi(user);
        user.withEmail(UserGenerator.getRandomEmail());

        DataForTests.updateDataUserInApi(user).then()
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));
    }

    @Test
    public void testUpdateUserNewPasswordReturnTrueAnd200() {
        DataForTests.createUserInApi(user);
        user.withEmail(UserGenerator.getRandomPassword());

        DataForTests.updateDataUserInApi(user).then()
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));
    }

    @Test
    public void testUpdateUserNewNameReturnTrueAnd200() {
        DataForTests.createUserInApi(user);
        user.withEmail(UserGenerator.getRandomName());

        DataForTests.updateDataUserInApi(user).then()
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));
    }

    @Test
    public void testUpdateUserBusyEmailReturnFalseAnd403() {
        DataForTests.createUserInApi(user);
        String tokenForDeleteFirstUser = DataForTests.accessToken;
        User user2 = UserGenerator.randomUser();
        DataForTests.createUserInApi(user2);
        DataForTests.deleteUser();

        DataForTests.updateDataUserInApi(user).then()
                .statusCode(SC_FORBIDDEN)
                .assertThat().body("success", equalTo(false));

        DataForTests.accessToken = tokenForDeleteFirstUser;
    }

    @Test
    public void testUpdateUserNewDataWithoutAutorizReturnFalseAnd401() {
        DataForTests.createUserInApi(user);
        user = UserGenerator.randomUser();

        DataForTests.updateDataUserWithoutAutorizInApi(user).then()
                .statusCode(SC_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false));
    }

    @Test
    public void testDeleteUserCreateUserAndDeleteReturnTrueAnd202() {
        DataForTests.createUserInApi(user);

        DataForTests.deleteUser().then()
                .statusCode(SC_ACCEPTED)
                .assertThat().body("success", equalTo(true));
    }

    @After
    public void setDown() {
        DataForTests.deleteUser();
    }
}
