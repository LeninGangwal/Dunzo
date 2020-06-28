import Models.Beverage;
import Models.CoffeeMachineDetails;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Thread.sleep;

/**
 * Assumptions ->
 * 1) The input JSON will not have duplicate beverage names. (Workaround would be to use MultiMap, custom deserialiser. Skipping it as it might be out of scope given time constraints)
 * 2) The input JSON contains correct input. Keeping check on the range of values seems to be out of scope given time constraints(Workaround would be add  javax validations in the input Models itself).
 * 3)
 */
public class App {
    /*Running test cases*/
    public static void main(String[] args) throws Exception {

        String jsonInput = "";

        jsonInput = new String(Files.readAllBytes(Paths.get("/Users/lenin.gangwal/Documents/Projects/DunzoAssignment/src/test/input.json")));
        run(jsonInput);

        jsonInput = new String(Files.readAllBytes(Paths.get("/Users/lenin.gangwal/Documents/Projects/DunzoAssignment/src/test/input_2.json")));
        run(jsonInput);

        jsonInput = new String(Files.readAllBytes(Paths.get("/Users/lenin.gangwal/Documents/Projects/DunzoAssignment/src/test/input_3.json")));
        run(jsonInput);

        jsonInput = new String(Files.readAllBytes(Paths.get("/Users/lenin.gangwal/Documents/Projects/DunzoAssignment/src/test/input.json")));
        runWithInventoryInwarding(jsonInput);


    }
//Runs the input beverage requests
    public static void run(String jsonInput) throws Exception {
        CoffeeMachine coffeeMachine = initialiseMachine(jsonInput);
        reset(coffeeMachine);
    }

//Adds inventory in the running machine, and then retires a beverage
    public static void runWithInventoryInwarding(String jsonInput) throws Exception {
       CoffeeMachine coffeeMachine = initialiseMachine(jsonInput);
       System.out.println("Adding request for pina_colada");

       //Creating new beverage
       HashMap<String, Integer> ingredient = new HashMap<>();
       ingredient.put("vodka", 5);
       Beverage beverage = new Beverage("pina_colada", ingredient);

       coffeeMachine.addBeverageRequest(beverage);

       sleep(1000);
       System.out.println("Adding vodka inventory");
       InventoryManager.getInstance().addInventory("vodka", 100);
       coffeeMachine.addBeverageRequest(beverage);

       reset(coffeeMachine);

    }
//parses the json into our input model. Creates a coffeeMachine using it.
    public static CoffeeMachine initialiseMachine(String jsonInput) throws Exception{
        System.out.println("New Machine");
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
        return coffeeMachine;
    }

//Resetting inventory and stopping coffee machine.
    public static void reset(CoffeeMachine coffeeMachine) throws Exception{
        sleep(3000);       //Sleeping to ensure threads are finished. Just for testing. S
        System.out.println("Resetting\n\n");
        coffeeMachine.stopMachine();
        InventoryManager.getInstance().resetInventory();
    }
}
