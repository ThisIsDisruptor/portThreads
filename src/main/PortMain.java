package main;

import port.Berth;
import port.Port;
import ship.Ship;

import java.util.ArrayList;
import java.util.List;

public class PortMain {
    public static void main(String[] args) {
        List<Berth> berths = new ArrayList<Berth>() {
            {
                this.add(new Berth(771));
                this.add(new Berth(883));
                this.add(new Berth(550));
                this.add(new Berth(337));
                this.add(new Berth(442));
            }
        };
        Port port = new Port(berths);
        for (int i = 20; i < 40; i++) {
            new Ship(i, i / 2,  port).start();
        }
    }
}
