import Models.Beverage;

import java.util.concurrent.*;

/**Represents a physical Coffee Machine, which can serve PARALLELY, using multi threading.
 *  Supports adding beverage requests, with a maximum pending queue size = MAX_QUEUED_REQUEST*/
public class CoffeeMachine {
    private static final int MAX_QUEUED_REQUEST = 100;
    private ThreadPoolExecutor executor;

    public CoffeeMachine(int outlet) {
        executor = new ThreadPoolExecutor(outlet, outlet, 5000L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>(MAX_QUEUED_REQUEST));
        executor.setRejectedExecutionHandler(new RejectedTaskHandler());
    }

    void addBeverageRequest(Beverage beverage) {
        BeverageTask task = new BeverageTask(beverage);
        executor.execute(task);
    }

    void stopMachine() {
        executor.shutdown();
    }

}
