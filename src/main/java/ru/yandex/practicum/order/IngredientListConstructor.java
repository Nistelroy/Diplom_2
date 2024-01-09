package ru.yandex.practicum.order;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class IngredientListConstructor {
    public static List<Ingredient> ingredientLisConstructor(List<Ingredient> ingredients, int quantityBunForBurger, int quantityMainForBurger, int quantitySauceForBurger) {
        List<Ingredient> ingredientsForOrder = new ArrayList<>();

        if (quantityBunForBurger > 0) {
            for (Ingredient ingredient : ingredients) {
                if (ingredient.type.equals("bun")) {
                    ingredientsForOrder.add(ingredient);
                    quantityBunForBurger -= 1;
                }
            }
        }
        if (quantityMainForBurger > 0) {
            for (Ingredient ingredient : ingredients) {
                if (ingredient.type.equals("main")) {
                    ingredientsForOrder.add(ingredient);
                    quantityBunForBurger -= 1;
                }
            }
        }
        if (quantitySauceForBurger > 0) {
            for (Ingredient ingredient : ingredients) {
                if (ingredient.type.equals("sauce")) {
                    ingredientsForOrder.add(ingredient);
                    quantityBunForBurger -= 1;
                }
            }
        }
        return ingredientsForOrder;
    }

    public static List<Ingredient> getRandomListForOrder(List<Ingredient> ingredients) {
        Random random = new Random();
        int quantityBunForBurger = random.nextInt(2) + 1;
        int quantityMainForBurger = random.nextInt(2 + 1);
        int quantitySauceForBurger = random.nextInt(2) + 1;
        return ingredientLisConstructor(ingredients, quantityBunForBurger, quantityMainForBurger, quantitySauceForBurger);
    }
}
