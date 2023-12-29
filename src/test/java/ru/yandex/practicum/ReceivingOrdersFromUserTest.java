package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.order.Ingredient;
import ru.yandex.practicum.order.IngredientListConstructor;
import ru.yandex.practicum.order.Ingredients;
import ru.yandex.practicum.order.UserOrders;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserGenerator;

import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.junit.Assert.assertEquals;

public class ReceivingOrdersFromUserTest {
    private User user;
    private List<Ingredient> ingredients;
    private int randomQuantityOrders = DataForTests.randomQuantityOrders();

    @Before
    public void setUp() {
        RestAssured.baseURI = DataForTests.BASE_URI;
        user = UserGenerator.randomUser();
        DataForTests.createUserInApi(user);
        ingredients = DataForTests.getIngredients().as(Ingredients.class).getData();
        for (int i = 0; i < randomQuantityOrders; i++) {
            DataForTests.createOrder(IngredientListConstructor.getRandomListForOrder(ingredients));
        }
    }

    @Test
    @DisplayName("Запрос списка заказов авторизованного пользователя")
    public void testGetOrderUserAutorizedReturnThreeOrdersAnd200() {
        Response response = DataForTests.getOrderUser();
        UserOrders userOrder = response.as(UserOrders.class);

        response.then().statusCode(HttpStatus.SC_OK);
        assertEquals(randomQuantityOrders, userOrder.orders.size());
    }

    @Test
    @DisplayName("Попытка запроса списка заказов не авторизованного пользователя")
    public void testGetOrderUserNoAutorizedReturnThreeOrdersAnd200() {
        Response response = DataForTests.getOrderUserNoAutoriz();

        response.then()
                .statusCode(HttpStatus.SC_UNAUTHORIZED)
                .assertThat().body("success", equalTo(false));

    }

    @After
    public void setDown() {
        DataForTests.deleteUser();
    }
}
