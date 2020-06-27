import Models.Beverage;
import Models.CoffeeMachineDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

public class App {
    public static void main(String[] args) throws Exception{
        String jsonInput = new String(Files.readAllBytes(Paths.get("/Users/lenin.gangwal/Documents/Projects/DunzoAssignment/src/main/java/input.json")));
        CoffeeMachineDetails coffeeMachineDetails = new ObjectMapper().readValue(jsonInput, CoffeeMachineDetails.class);

        CoffeeMachine coffeeMachine = new CoffeeMachine(coffeeMachineDetails.getMachine().getOutlets().getCount());

        InventoryManager inventoryManager = InventoryManager.getInstance();

        Map<String, Integer> ingredients = coffeeMachineDetails.getMachine().getIngredientQuantityMap();

        for (String key : ingredients.keySet()) {
            inventoryManager.addInventory(key, ingredients.get(key));
        }

        HashMap<String, HashMap<String, Integer>> beverages = coffeeMachineDetails.getMachine().getBeverages();
        for (String key : beverages.keySet()) {
            Beverage beverage = new Beverage(key, beverages.get(key));
            coffeeMachine.addBeverageRequest(beverage);
        }
        sleep(1000);

//        coffeeMachine.stopMachine();
//        InventoryManager.getInstance().addInventory("hot_water", 600);
//        HashMap<String, Integer> ingredient = new HashMap<String, Integer>();
//        ingredient.put("hot_water", 500);
//        coffeeMachine.addBeverageRequest(new Beverage("rahul", ingredient));


    }

}
