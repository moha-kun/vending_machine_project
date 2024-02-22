package org.exemple;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;

/*
 You need to design a Vending Machine which
     -Accepts coins of 1, 2, 5 and 10 dirham coins (not the 0.5 dirham coin)
     -Allow user to select products water(5), coca(7), twix(10), bueno(12)
     -Allow user to take refund by canceling the request.
     -Return the selected product and remaining change if any
     -Allow reset operation for vending machine supplier.
*/

public class VendingMachineTest {

    private VendingMachine vendingMachine;

    @BeforeEach
    public void setUp() {
        vendingMachine = new VendingMachine();
        vendingMachine.addProduct("water", 7);
        vendingMachine.addProduct("coca", 7);
        vendingMachine.addProduct("bueno", 7);
        vendingMachine.addProduct("twix", 7);
        vendingMachine.addCoinsStock(1, 10);
    }

    @ParameterizedTest
    @ValueSource(ints = {1, 2, 5, 10})
    public void shouldAcceptCoins(int coin) throws Exception {
        String message = vendingMachine.insertCoin(coin);
        String expectedMessage = "Done";
        Assertions.assertEquals(expectedMessage, message);
    }

    @ParameterizedTest
    @ValueSource(ints = {3, 7, 11, 20})
    public void shouldNotAcceptCoins(int coin) {
        Assertions.assertThrows(
                CoinNotAcceptedException.class,
                () -> vendingMachine.insertCoin(coin)
        );
    }

    @ParameterizedTest
    @ValueSource(strings = {"water", "coca", "twix", "bueno"})
    public void shouldSelectProduct(String product) {
        String message = vendingMachine.selectProduct(product);
        String expectedMessage = "Done";
        Assertions.assertEquals(expectedMessage, message);
    }

    @ParameterizedTest
    @ValueSource(strings = {"tacos", "cola", "phone"})
    public void shouldThrowWhenSelectProduct(String product) {
        Assertions.assertThrows(
                ProductOutOfStockException.class,
                () -> vendingMachine.selectProduct(product)
        );
    }

    @Test
    public void shouldReturnChangeWhenCancel() {
        vendingMachine.insertCoin(10);
        vendingMachine.insertCoin(5);
        Map<Integer, Integer> expected = Map.of(
                1, 0,
                2, 0,
                5, 1,
                10, 1
        );
        Map<Integer, Integer> map = vendingMachine.cancelRequest();
        Assertions.assertTrue(expected.equals(map));
    }

}
