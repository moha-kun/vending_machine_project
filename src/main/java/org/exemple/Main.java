package org.exemple;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        VendingMachine vendingMachine = new VendingMachine();
        vendingMachine.addProduct("tacos", 7);
        vendingMachine.addCoinsStock(1, 10);
        vendingMachine.insertCoin(10);
        vendingMachine.selectProduct("tacos");
        System.out.println(vendingMachine);
    }

}
