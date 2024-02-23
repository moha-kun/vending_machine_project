package org.exemple;

import org.exemple.exceptions.CoinNotAcceptedException;
import org.exemple.exceptions.NoCoinsForChangeException;
import org.exemple.exceptions.ProductOutOfStockException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Map;
import java.util.stream.Stream;

public class VendingMachineTest {

    private VendingMachine vendingMachine;

    @BeforeEach
    public void setUp() {
        vendingMachine = new VendingMachine();
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

    @ParameterizedTest
    @MethodSource("getDataForShouldReturnSelectedProductAndRemainingChange")
    public void shouldReturnSelectedProductAndRemainingChange(String product, int budget, String expectedMessage) {
        vendingMachine.insertCoin(budget);
        vendingMachine.selectProduct(product);
        String message = vendingMachine.submit();
        Assertions.assertEquals(expectedMessage, message);
    }

    public static Stream<Object[]> getDataForShouldReturnSelectedProductAndRemainingChange() {
        return Stream.of(
          new Object[][] {
                  {"water", 10, "You bought a water\nThe remaining change: 3"},
                  {"coca", 10, "You bought a coca\nThe remaining change: 2"},
                  {"twix", 5, "You bought a twix\nThe remaining change: 0"},
          }
        );
    }

    @ParameterizedTest
    @MethodSource("getDataForShouldThrowNoCoinsException")
    public void shouldThrowNoCoinsException(String product, int budget, String expectedMessage) {
        vendingMachine.insertCoin(budget);
        vendingMachine.selectProduct(product);
        Exception exception = Assertions.assertThrows(
                NoCoinsForChangeException.class,
                () -> vendingMachine.submit()
        );
        String message = exception.getMessage();
        Assertions.assertEquals(expectedMessage, message);
    }

    public static Stream<Object[]> getDataForShouldThrowNoCoinsException() {
        return Stream.of(
                new Object[][] {
                        {"water", 5, "There is no coins for change"},
                        {"bueno", 10, "There is no coins for change"},
                        {"twix", 2, "There is no coins for change"},
                }
        );
    }

    @Test
    public void shouldReset() {
        vendingMachine.insertCoin(10);
        vendingMachine.selectProduct("water");
        vendingMachine.submit();

        vendingMachine.insertCoin(10);
        vendingMachine.selectProduct("coca");
        vendingMachine.submit();

        vendingMachine.reset();

        String expectedMessage = "VendingMachine{" +
                "ACCEPTED_COINS=[1, 2, 5, 10]," +
                " coinsStock={1=5, 2=2, 5=0, 10=0}," +
                " products={coca=8, twix=5, bueno=20, water=7}," +
                " userBalance={1=0, 2=0, 5=0, 10=0}," +
                " selectedProduct=''," +
                " changeCombination={1=0, 2=0, 5=0, 10=0}}";
        String actualMessage = vendingMachine.toString();
        Assertions.assertEquals(expectedMessage, actualMessage);
    }
}
