import Models.Beverage;
/**This class represents an atomic task to make any Beverage.
 * Uses Runnable interface to support multithreading */

public class BeverageTask implements Runnable {
    private Beverage beverage;

    BeverageTask(Beverage beverage) {
        this.beverage = beverage;
    }

    @Override
    public void run() {
        if (InventoryManager.getInstance().checkAndUpdateIfEnoughInventoryForBeverage(beverage)) {
            System.out.println(beverage.getName() + " is prepared");
        }

    }

    @Override
    public String toString() {
        return beverage.getName();
    }
}
