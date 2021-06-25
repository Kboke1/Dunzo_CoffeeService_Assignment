package com.company;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws FileNotFoundException {
        serve();
    }

    private static void serve() throws FileNotFoundException {
        BeverageService beverageService = initializeCoffeeMachine();

        if(beverageService != null){
            File folder = new File("resources/input");
            for(File file : folder.listFiles()){
                if(file.isFile()){
                    System.out.println();
                    System.out.println();
                    System.out.println("Running for " + file.getName());

                    InputStream is = new FileInputStream(file);
                    try (Scanner scanner = new Scanner(new BufferedReader(new InputStreamReader(is)))) {
                        int testCount = scanner.nextInt();
                        for (int testNumber = 1; testNumber <= testCount; testNumber++) {
                            String command = scanner.nextLine();
                            String[] splitCommand = command.split(" ");

                            if(splitCommand.length == 2){
                                if(splitCommand[0].equals("make")){
                                    beverageService.makeBeverage(splitCommand[1]);
                                }
                                else{
                                    beverageService.addIngredient(splitCommand[0], Integer.parseInt(splitCommand[1]));
                                }
                            }

                        }
                    }
                }
            }
        }
    }

    private static BeverageService initializeCoffeeMachine() {
        JSONParser jsonParser = new JSONParser();
        BeverageService beverageService = null;
        try (FileReader reader = new FileReader("resources/setup.json"))
        {
            Object obj = jsonParser.parse(reader);
            JSONObject coffeeMachineSetup = (JSONObject) ((JSONObject) obj).get("machine");

            JSONObject initialStock = (JSONObject) coffeeMachineSetup.get("total_items_quantity");
            InventoryService inventoryService = new InventoryService(initialStock);

            JSONObject beverages = (JSONObject) coffeeMachineSetup.get("beverages");
            beverageService = new BeverageService(inventoryService, beverages);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return beverageService;
    }
}
