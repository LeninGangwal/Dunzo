import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ThreadPoolExecutor;

/**Implementation to handle scenarios when our pending beverage requests has already reached threshold*/
class RejectedTaskHandler implements RejectedExecutionHandler {
    @Override
    public void rejectedExecution(Runnable r, ThreadPoolExecutor executor) {
        System.out.printf("RejectedTaskHandler: The beverage request %s has been rejected by executor service", r.toString());
    }
}