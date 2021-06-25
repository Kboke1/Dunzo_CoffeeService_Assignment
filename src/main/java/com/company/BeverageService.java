package com.company;

import org.json.simple.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class BeverageService {
    private final InventoryService inventoryService;
    private Map<String, Beverage> beverageMap;

    public BeverageService(InventoryService inventoryService, JSONObject beverageJson) {
        this.inventoryService = inventoryService;
        beverageMap = new HashMap<>();

        for(Object o : beverageJson.keySet()){
            String beverageName = (String) o;

            if(beverageJson.get(o) instanceof JSONObject){
                JSONObject ingredientsJson = (JSONObject) beverageJson.get(o);
                Map<Ingredient, Long> ingredients = new HashMap<>();
                for(Object obj : ingredientsJson.keySet()){
                    String ingredientName = (String) obj;
                    Ingredient ingredient = inventoryService.getIngredient(ingredientName);
                    ingredients.put(ingredient, (Long) ingredientsJson.get(obj));
                }

                Beverage beverage = new Beverage(beverageName, ingredients);
                beverageMap.put(beverageName, beverage);
            }

        }
    }

    public synchronized Beverage getBeverage(String name){
        return beverageMap.get(name);
    }

    public void makeBeverage(String beverageName){
        Beverage beverage = getBeverage(beverageName);
        Map<Ingredient, Long> ingredients = beverage.getRequiredIngredients();
        boolean canMakeBeverage = true;
        synchronized(inventoryService){
            for(Ingredient ingredient : ingredients.keySet()){
                long availableQuantity = inventoryService.getStock(ingredient);
                if(availableQuantity == 0){
                    System.out.printf("%s cannot be prepared because %s is not available.", beverage.getName(), ingredient.getName());
                    System.out.println();
                    canMakeBeverage = false;
                    break;
                }
                else if(availableQuantity < ingredients.get(ingredient)){
                    System.out.printf("%s cannot be prepared because item %s is not sufficient.", beverage.getName(), ingredient.getName());
                    System.out.println();
                    canMakeBeverage = false;
                    break;
                }
            }
            if(canMakeBeverage){
                inventoryService.removeStocks(ingredients);
                System.out.printf("%s is prepared.", beverage.getName());
                System.out.println();
            }
        }
    }

    public void addIngredient(String ingredientName, int quantity) {
        synchronized (inventoryService){
            inventoryService.addStock(ingredientName, quantity);
        }
        System.out.printf("%s - %d added to stock", ingredientName, quantity);
        System.out.println();
    }
}
