package com.company;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InventoryService {
    private Map<Ingredient, Long> stock;
    private Map<String, Ingredient> ingredientMap;

    public InventoryService(JSONObject stockJson) {
        stock = new ConcurrentHashMap<>();
        ingredientMap = new HashMap<>();
        for (Object o : stockJson.keySet()) {
            String key = (String) o;
            Ingredient ingredient = getIngredient(key);
            stock.put(ingredient, (Long) stockJson.get(key));
        }
    }

    public Ingredient getIngredient(String name){
        if(!ingredientMap.containsKey(name)){
            ingredientMap.put(name, new Ingredient(name));
        }

        return ingredientMap.get(name);
    }

    public void removeStocks(Map<Ingredient, Long> ingredients){
        ingredients.entrySet().parallelStream().forEach(entry -> stock.put(entry.getKey(), stock.get(entry.getKey()) - entry.getValue()));
    }

    public void addStock(String ingredientName, int quantity){
        Ingredient ingredient = getIngredient(ingredientName);
        stock.put(ingredient, stock.get(ingredient) + quantity);
    }

    public long getStock(Ingredient ingredient){
        return stock.get(ingredient);
    }

    @Override
    public String toString() {
        return "Inventory{" +
                "stock=" + stock +
                '}';
    }
}
