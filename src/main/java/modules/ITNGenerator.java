package main.java.modules;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/*
 * Создаёт валидный ИНН физического лица (12 цифр)
 */
class ITNGenerator {

    private ArrayList<Integer> ITN;

    private final int[] COEFFICIENTS_FOR_DIGIT11 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8}; // коэффициенты для вычисления 11 цифры
    private final int[] COEFFICIENTS_FOR_DIGIT12 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8}; // -//- 12 цифры

    ITNGenerator(int regionNumber) {
        this.ITN = new ArrayList<>();

        int[] digits1to10 = {regionNumber / 10, regionNumber % 10, 0, 0, 0, 0, 0, 0, 0, 0}; // цифры от 1 до 10, первые две определены регионом
        for (int i = 2; i < 10; i++) {
            digits1to10[i] = ThreadLocalRandom.current().nextInt(0, 10);
        }

        int[] digits1to10withCoefficients = multiplyDigits(digits1to10, COEFFICIENTS_FOR_DIGIT11);

        int digit11 = calculateN(digits1to10withCoefficients);

        int[] digits1to11withCoefficients = multiplyDigits(digits1to10, COEFFICIENTS_FOR_DIGIT12);
        digits1to11withCoefficients = ArrayUtils.add(digits1to11withCoefficients, digit11 * COEFFICIENTS_FOR_DIGIT12[10]);

        int digit12 = calculateN(digits1to11withCoefficients);

        for (int digit : digits1to10) {
            this.ITN.add(digit);
        }
        this.ITN.add(digit11);
        this.ITN.add(digit12);
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
        int[] result = new int[digits.length];
        for (int i = 0; i < digits.length; i++) {
            result = ArrayUtils.add(result, digits[i] * coefficients[i]);
        }
        return result;
    }
}
