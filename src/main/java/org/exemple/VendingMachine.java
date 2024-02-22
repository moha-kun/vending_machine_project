package org.exemple;

import java.util.*;

public class VendingMachine {

    private final List<Integer> ACCEPTED_COINS = List.of(1, 2, 5, 10);

    private Map<Integer, Integer> coinsStock;
    private Map<String, Integer> products;
    private Map<Integer, Integer> userBalance;
    private String selectedProduct;
    private Map<Integer, Integer> changeCombination;

    public VendingMachine() {
        selectedProduct = "";
        products = new HashMap<>();
        coinsStock =  initializeCoinsMap();
        userBalance = initializeCoinsMap();
        changeCombination = initializeCoinsMap();
    }

    private Map<Integer, Integer> initializeCoinsMap() {
        Map<Integer, Integer> map = new HashMap<>();
        map.put(10, 0);
        map.put(5, 0);
        map.put(2, 0);
        map.put(1, 0);
        return map;
    }

    public void addCoinsStock(int coin, int number) {
        if (!coinsStock.containsKey(coin))
            coinsStock.put(coin, 0);
        coinsStock.put(coin, coinsStock.get(coin) + number);
    }

    public void addProduct(String product, int price) {
        products.put(product, price);
    }

    public String insertCoin(int coin) {
        if (!ACCEPTED_COINS.contains(coin))
            throw new CoinNotAcceptedException("This coin is not supported");
        userBalance.put(coin, userBalance.get(coin) + 1);
        return "Done";
    }

    public String selectProduct(String product) {
        if (!products.containsKey(product))
            throw new ProductOutOfStockException("Product out of stock");
        selectedProduct = product;
        return "Done";
    }

    public Map<Integer, Integer> cancelRequest() {
        Map<Integer, Integer> result = userBalance;
        selectedProduct = "";
        userBalance = initializeCoinsMap();
        return result;
    }

    public String submit() throws Exception {
        if (selectedProduct.isEmpty() && getUserBalance() == 0)
            throw new Exception("Select a product first");
        if (!isThereAChange())
            return "There is no coins for change";
        String product = selectedProduct;
        int productPrice = products.get(selectedProduct);
        int balance = getUserBalance();
        cleanAfterSubmitting();
        return "You bought a " + product + "\n" +
                "The remaining change: " + (balance - productPrice);
    }

    public boolean isThereAChange() {
        if (getUserBalance() == 0 || selectedProduct.isEmpty())
            return false;
        int change = getUserBalance() - products.get(selectedProduct);
        if (change < 0)
            return false;
        if (change == 0)
            return true;
        Map<Integer, Integer> combination = getChangeCombination();
        if (combination != null)
            return true;
        return false;
    }

    public Map<Integer, Integer> getChangeCombination() {
        int change = getUserBalance() - products.get(selectedProduct);
        int[] acceptedCoins = new int[]{10, 5, 2, 1};
        int acceptedCoinsIndex = 0;
        while (change > 0) {
            if (acceptedCoinsIndex > 3)
                return null;
            int currentCoinsStock = coinsStock.get(acceptedCoins[acceptedCoinsIndex]);
            int numberOfConsumedCoins = changeCombination.get(acceptedCoins[acceptedCoinsIndex]);
            if (currentCoinsStock - numberOfConsumedCoins < 1) {
                acceptedCoinsIndex++;
                continue;
            }
            if (change < acceptedCoins[acceptedCoinsIndex]) {
                acceptedCoinsIndex++;
                continue;
            }
            change -= acceptedCoins[acceptedCoinsIndex];
            changeCombination.put(currentCoinsStock, changeCombination.get(currentCoinsStock) + 1);
        }
        return changeCombination;
    }

    public void cleanAfterSubmitting() {
        for (int coin : ACCEPTED_COINS) {
            int currentCoinStock = coinsStock.get(coin) - changeCombination.get(coin) + userBalance.get(coin);
            coinsStock.put(coin, currentCoinStock);
        }

        products.remove(selectedProduct);
        selectedProduct = "";
        userBalance = initializeCoinsMap();
        changeCombination = initializeCoinsMap();
    }

    public int getUserBalance() {
        int balance = 0;
        for (Map.Entry<Integer, Integer> entry : userBalance.entrySet()) {
            balance += entry.getKey() * entry.getValue();
        }
        return balance;
    }

    @Override
    public String toString() {
        return "VendingMachine{" +
                "ACCEPTED_COINS=" + ACCEPTED_COINS +
                ", coinsStock=" + coinsStock +
                ", products=" + products +
                ", userBalance=" + userBalance +
                ", selectedProduct='" + selectedProduct + '\'' +
                ", changeCombination=" + changeCombination +
                '}';
    }
}
