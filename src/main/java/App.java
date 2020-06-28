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
 * Assumptions:
 * 1) The input JSON will not have duplicate beverage names. (Workaround would be to use MultiMap, custom deserialiser. Skipping it as it might be out of scope given time constraints)
 * 2) The input JSON contains correct input. Keeping check on the range of values seems to be out of scope given time constraints(Workaround would be add  javax validations in the input Models itself).
 *
 * Algorithm:
 * A multithreaded system with n threads is invoked to represent n nozzled coffee machine.
 * It queues up all the requests from the input and tries to create the beverages. Importance has been given to thread safety to ensure two drinks do not use same ingredient.
 * Feature to add new ingredient in our inventory is given, as well as to add new Beverage Requests at any given point of time.
 */
public class App {
    /*Running test cases*/
    public static void main(String[] args) throws Exception {

        String jsonInput = "";
        //Test Case->Sample Input
        jsonInput = new String(Files.readAllBytes(Paths.get("src/test/input.json")));
        run(jsonInput);

        //Test Case -> No ingredients provided
        jsonInput = new String(Files.readAllBytes(Paths.get("src/test/input_2.json")));
        run(jsonInput);

        //Test Case -> Multiple Beverages competing for finish, also a beverage without any ingredient
        jsonInput = new String(Files.readAllBytes(Paths.get("src/test/input_3.json")));
        run(jsonInput);

        //Test Case -> After processing the input beverages, an inventory is added manually, and then another beverage request is added.
        jsonInput = new String(Files.readAllBytes(Paths.get("src/test/input.json")));
        runWithInventoryInwarding(jsonInput);


    }
//Runs the input beverage requests
    public static void run(String jsonInput) throws Exception {
        CoffeeMachine coffeeMachine = initialiseMachine(jsonInput);
        reset(coffeeMachine);
    }

//Adds inventory in the running machine, and then retries a beverage
    public static void runWithInventoryInwarding(String jsonInput) throws Exception {
       CoffeeMachine coffeeMachine = initialiseMachine(jsonInput);
       System.out.println("Adding request for pina_colada");

       //Creating new beverage
       HashMap<String, Integer> ingredient = new HashMap<>();
       ingredient.put("vodka", 5);
       Beverage beverage = new Beverage("pina_colada", ingredient);

       coffeeMachine.addBeverageRequest(beverage);

       sleep(2000);
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

/**Resetting inventory and stopping coffee machine. This is only used for testing. In real world, no need for resetting unless machine is stopped.*/
    public static void reset(CoffeeMachine coffeeMachine) throws Exception{
        sleep(3000);       //Sleeping to ensure threads are finished. Just for testing.
        System.out.println("Resetting\n\n");
        coffeeMachine.stopMachine();
        InventoryManager.getInstance().resetInventory();
    }
}
