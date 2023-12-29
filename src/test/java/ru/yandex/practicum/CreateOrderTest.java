package ru.yandex.practicum;

import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import ru.yandex.practicum.order.Ingredient;
import ru.yandex.practicum.order.IngredientListConstructor;
import ru.yandex.practicum.order.Ingredients;
import ru.yandex.practicum.user.User;
import ru.yandex.practicum.user.UserGenerator;

import java.util.List;

import static org.apache.http.HttpStatus.SC_BAD_REQUEST;
import static org.apache.http.HttpStatus.SC_INTERNAL_SERVER_ERROR;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.equalTo;

public class CreateOrderTest {
    private User user;
    private List<Ingredient> ingredients;

    @Before
    public void setUp() {
        RestAssured.baseURI = DataForTests.BASE_URI;
        user = UserGenerator.randomUser();
        ingredients = DataForTests.getIngredients().as(Ingredients.class).getData();
    }

    @Test
    @DisplayName("Создание заказа")
    public void testOrderCorrectOrderReturnTrueAnd200() {
        DataForTests.createUserInApi(user);
        Response response = DataForTests.createOrder(IngredientListConstructor.getRandomListForOrder(ingredients));

        response.then()
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Создание заказа без авторизации")
    public void testOrderСorrectOrderWithoutAutorizReturnTrueAnd200() {
        Response response = DataForTests.createOrderWithoutAutoriz(IngredientListConstructor.getRandomListForOrder(ingredients));

        response.then()
                .statusCode(SC_OK)
                .assertThat().body("success", equalTo(true));
    }

    @Test
    @DisplayName("Попытка создания заказа без игредиентов")
    public void testOrderWithoutIngredientsReturnFalseAnd400() {
        DataForTests.createUserInApi(user);
        int quantityBunForBurger = 0;
        int quantityMainForBurger = 0;
        int quantitySauceForBurger = 0;

        Response response = DataForTests.createOrder(IngredientListConstructor.ingredientLisConstructor(ingredients,
                quantityBunForBurger, quantityMainForBurger, quantitySauceForBurger));
        response.then()
                .statusCode(SC_BAD_REQUEST)
                .assertThat().body("success", equalTo(false));
    }

    @Test
    @DisplayName("Попытка создания заказа с неправильным хешем ингредиентов")
    public void testOrderInvalidHashIngredientReturn500() {
        DataForTests.createUserInApi(user);
        int quantityBunForBurger = 3;
        int quantityMainForBurger = 2;
        int quantitySauceForBurger = 2;

        Response response = DataForTests.createInvalidHashOrder(IngredientListConstructor.ingredientLisConstructor(ingredients,
                quantityBunForBurger, quantityMainForBurger, quantitySauceForBurger));
        response.then()
                .statusCode(SC_INTERNAL_SERVER_ERROR)
                .assertThat().body(containsString("Internal Server Error"));
    }

    @After
    public void setDown() {
        DataForTests.deleteUser();
    }
}
