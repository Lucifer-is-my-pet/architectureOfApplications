package main.modules;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/*
 * Создаёт валидный ИНН физического лица (12 цифр)
 */
class ITNGenerator {

    private ArrayList<Integer> ITN;

    private final int[] COEFFICIENTS_FOR_N11 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8}; // коэффициенты для вычисления 11 цифры
    private final int[] COEFFICIENTS_FOR_N12 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8}; // -//- 12 цифры

    ITNGenerator(int regionNumber) {
        this.ITN = new ArrayList<>();

        int[] n110 = {regionNumber / 10, regionNumber % 10, 0, 0, 0, 0, 0, 0, 0, 0}; // цифры от 1 до 10, первые две определены регионом
        for (int i = 2; i < 10; i++) {
            n110[i] = ThreadLocalRandom.current().nextInt(0, 10);
        }

        int[] n110withCoefficients = multiplyDigits(n110, COEFFICIENTS_FOR_N11);

        int n11 = calculateN(n110withCoefficients);

        int[] n111withCoefficients = multiplyDigits(n110, COEFFICIENTS_FOR_N12);
        n111withCoefficients = ArrayUtils.add(n111withCoefficients, n11 * COEFFICIENTS_FOR_N12[10]);

        int n12 = calculateN(n111withCoefficients);

        for (int digit : n110) {
            this.ITN.add(digit);
        }
        this.ITN.add(n11);
        this.ITN.add(n12);
    }

    String getString() {
        StringBuilder result = new StringBuilder();
        for (int digit : this.ITN) {
            result.append(digit);
        }
        return result.toString();
    }

    private int calculateN(int[] digitArray) {
        return (IntStream.of(digitArray).sum() % 11) % 10;
    }

    /*
    * Попарно перемножает элементы переданных массивов межу собой
    */
    private int[] multiplyDigits(int[] digits, int[] coefficients) {
        int[] result = {};
        for (int i = 0; i < digits.length; i++) {
            result = ArrayUtils.add(result, digits[i] * coefficients[i]);
        }
        return result;
    }
}
