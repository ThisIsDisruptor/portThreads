package port;

import java.util.Random;
import java.util.concurrent.TimeUnit;

//причал
public class Berth {

    int berthId;

    private volatile boolean busy;

    public Berth(int berthId) {
        this.berthId = berthId;
    }

    public int getBerthId() {
        return berthId;
    }

    public boolean isBusy() {
        return busy;
    }

    public void setBusy(boolean busy) {
        this.busy = busy;
    }

    @Override
    public String toString() {
        return
                "berth #" + berthId +
                ", busy=" + busy;
    }
}
