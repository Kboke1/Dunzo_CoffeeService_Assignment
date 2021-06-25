package com.company;

import java.util.Map;

public class Beverage {
    private final String name;
    private final Map<Ingredient, Long> requiredIngredients;

    public Beverage(String name, Map<Ingredient, Long> requiredIngredients) {
        this.name = name;
        this.requiredIngredients = requiredIngredients;
    }

    public String getName() {
        return name;
    }

    public Map<Ingredient, Long> getRequiredIngredients() {
        return requiredIngredients;
    }

    @Override
    public String toString() {
        return "Beverage{" +
                "name='" + name + '\'' +
                ", requiredIngredients=" + requiredIngredients +
                '}';
    }
}
