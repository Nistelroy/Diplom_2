package ru.yandex.practicum.user;

import com.github.javafaker.Faker;

public class UserGenerator {
    private static Faker faker = new Faker();

    public static User randomUser() {
        User user = new User();

        user.withEmail(faker.internet().emailAddress());
        user.withPassword(faker.internet().password(6, 8));
        user.withName(faker.name().firstName());

        return user;
    }

    public static String getRandomEmail() {
        return faker.internet().emailAddress();
    }

    public static String getRandomPassword() {
        return faker.internet().password(6, 8);
    }

    public static String getRandomName() {
        return faker.name().firstName();
    }
}
