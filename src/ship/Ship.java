package ship;

import port.Berth;
import port.Port;
import port.ResourceException;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class Ship extends Thread{
    private int containersCapacity;

    private int containersCount;

    private Port port;

    public Ship(int containersCapacity, int containersCount, Port port) {
        this.containersCapacity = containersCapacity;
        this.containersCount = containersCount;
        this.port = port;
    }

    public void load(int loadedContainersCount) throws ResourceException {
        boolean possibleToLoad = containersCount + loadedContainersCount <= containersCapacity;
        if (possibleToLoad && loadedContainersCount > 0) {
            containersCount += loadedContainersCount;
        } else {
            throw new ResourceException("Impossible to load " + loadedContainersCount
                    + " containers to Ship #" + this.getId() + ", current count: " + containersCount + ", capacity: " + containersCapacity);
        }
    }

    public void unload(int unloadedContainersCount) throws ResourceException {
        boolean possibleToUnload =  containersCount - unloadedContainersCount >= 0;
        if (possibleToUnload && unloadedContainersCount > 0) {
            containersCount -= unloadedContainersCount;
        } else {
            throw new ResourceException("Impossible to unload " + unloadedContainersCount
                    + " containers from Ship #" + this.getId() + ", current count: " + containersCount + ", capacity: " + containersCapacity);
        }
    }

    public void run() {
        Berth berth = null;
        try {
            berth = port.getBerth(this, 500); // change to 100

            int action = ThreadLocalRandom.current().nextInt(1, 3);
            int containersCount = ThreadLocalRandom.current().nextInt(0, this.containersCapacity + 1);

            if (action == 1) {
                port.unload(containersCount);
                this.load(containersCount);
                System.out.println(containersCount + " containers loaded from Port to Ship #" + this.getId() +  ", " + berth);
            } else {
                this.unload(containersCount);
                port.load(containersCount);
                System.out.println(containersCount + " containers loaded from Ship #" + this.getId() + " to Port" + ", " + berth);
            }
        } catch (ResourceException e) {
            System.err.println("Ship #" + this.getId() + " lost ->" + e.getMessage());
        } finally {
            if (berth != null) {
                port.releaseResource(this, berth);
            }
        }
    }
}
