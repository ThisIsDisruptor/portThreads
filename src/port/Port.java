package port;

import ship.Ship;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

public class Port {
    private final static int BERTH_COUNT = 5;
    private final static int CONTAINERS_CAPACITY = 100;

    private Semaphore semaphore = new Semaphore(BERTH_COUNT, true);
    private ArrayList<Berth> berths = new ArrayList<>();

    private volatile int containersCount = 75;

    public void load(int loadedContainersCount) throws ResourceException {
        boolean possibleToLoad = containersCount + loadedContainersCount <= CONTAINERS_CAPACITY;
        if (possibleToLoad && loadedContainersCount > 0) {
            containersCount += loadedContainersCount;
        } else {
            throw new ResourceException("Impossible to load " + loadedContainersCount
                    + " containers to Port, current count: " + containersCount + ", capacity: " + CONTAINERS_CAPACITY );
        }
    }

    public void unload(int unloadedContainersCount) throws ResourceException {
        boolean possibleToUnload =  containersCount - unloadedContainersCount >= 0;
        if (possibleToUnload && unloadedContainersCount > 0) {
            containersCount -= unloadedContainersCount;
        } else {
            throw new ResourceException("Impossible to unload " + unloadedContainersCount
                    + " containers from Port, current count: " + containersCount + ", capacity: " + CONTAINERS_CAPACITY);
        }

    }

    public Port(List<Berth> source) {
        berths.addAll(source);
    }

    public Berth getBerth(Ship ship, long maxWaitMillis) throws ResourceException {
        try {
            if (semaphore.tryAcquire(maxWaitMillis, TimeUnit.MILLISECONDS)) {
                for (Berth berth : berths) {
                    if (!berth.isBusy()) {
                        berth.setBusy(true);
//                        System.out.println("Ship #" + ship.getId()
//                                + " took berth " + berth);
                        return berth;
                    }
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        throw new ResourceException("timed out " + maxWaitMillis);
    }
    public void releaseResource(Ship ship, Berth berth) {
        berth.setBusy(false); // change the status of the resource
//        System.out.println("Ship #" + ship.getId() + ": " + berth + " --> released");
        semaphore.release();
    }

}
