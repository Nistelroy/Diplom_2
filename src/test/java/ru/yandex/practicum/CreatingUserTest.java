package ru.yandex.practicum;

import io.restassured.RestAssured;
import org.junit.Before;

public class CreatingUserTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = DataForTests.BASE_URI;
    }
}
