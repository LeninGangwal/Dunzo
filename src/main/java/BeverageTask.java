//import Models.Models.Beverage;

import Models.Beverage;

public class BeverageTask implements Runnable {

    private Beverage beverage;

    BeverageTask(Beverage beverage){
        this.beverage = beverage;
    }

    @Override
    public void run() {
        if (InventoryManager.getInstance().checkAndUpdateIfEnoughInventory(beverage)){
            System.out.println(beverage.getName() + " is prepared");
        }

    }

    @Override
    public String toString() {
        return beverage.getName();
    }
}
