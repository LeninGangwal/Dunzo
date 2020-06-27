//import Models.Models.Beverage;
import Models.Beverage;

import java.util.HashMap;
import java.util.Map;

public class InventoryManager {
    //singleton
    static InventoryManager inventoryManager;
    private HashMap <String, Integer> inventory = new HashMap<>();
    private InventoryManager(){

    }

    public static InventoryManager getInstance(){
        if (inventoryManager == null) {
           inventoryManager = new InventoryManager();
        }
        return inventoryManager;
    }

     synchronized boolean checkAndUpdateIfEnoughInventory(Beverage beverage){
        System.out.println(Thread.currentThread().getId());
        Map<String, Integer> requiredIngredientMap = beverage.getIngredientQuantityMap();
        boolean isPossible = true;
        for (String ingredient : requiredIngredientMap.keySet()) {
            int ingredientInventoryCount = inventory.getOrDefault(ingredient, -1);
            if (ingredientInventoryCount == -1 || ingredientInventoryCount == 0) {
                System.out.println(beverage.getName() + " cannot be prepared because " + ingredient + " is not available");
                isPossible = false;
                break;
            }
             else if(requiredIngredientMap.get(ingredient) > ingredientInventoryCount){
                 System.out.println(beverage.getName() + " cannot be prepared because " + ingredient + " is not sufficient");
                 isPossible = false;
                 break;
            }
         }

        if (isPossible){
             for (String ingredient : requiredIngredientMap.keySet()) {
                 int existingInventory = inventory.getOrDefault(ingredient, 0);
                 inventory.put(ingredient,  existingInventory - requiredIngredientMap.get(ingredient));
             }
        }
         return isPossible;
     }

     void addInventory(String ingredient, int quantity){
        int existingInventory = inventory.getOrDefault(ingredient, 0);
        inventory.put(ingredient,  existingInventory + quantity);
     }


}
