package modules.ITNGenerator;

import org.apache.commons.lang3.ArrayUtils;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.IntStream;

/*
 * Создаёт валидный ИНН физического лица (12 цифр)
 */
public class ITNGenerator {

    private ArrayList<Integer> itn;

    private final int[] COEFFICIENTS_FOR_N11 = {7, 2, 4, 10, 3, 5, 9, 4, 6, 8}; // коэффициенты для вычисления 11 цифры
    private final int[] COEFFICIENTS_FOR_N12 = {3, 7, 2, 4, 10, 3, 5, 9, 4, 6, 8}; // -//- 12 цифры

    public ITNGenerator(int regionNumber) {
        this.itn = new ArrayList<>();

        int[] digitsFrom1to10 = {regionNumber / 10, regionNumber % 10, 0, 0, 0, 0, 0, 0, 0, 0}; // цифры от 1 до 10, первые две определены регионом
        for (int i = 2; i < 10; i++) {
            digitsFrom1to10[i] = ThreadLocalRandom.current().nextInt(0, 10);
        }

        int[] digitsFrom1to10withCoefficients = multiplyDigits(digitsFrom1to10, COEFFICIENTS_FOR_N11);

        int digit11 = calculateModules(digitsFrom1to10withCoefficients);

        int[] digitsFrom1to11withCoefficients = multiplyDigits(digitsFrom1to10, COEFFICIENTS_FOR_N12);
        digitsFrom1to11withCoefficients = ArrayUtils.add(digitsFrom1to11withCoefficients, digit11 * COEFFICIENTS_FOR_N12[10]);

        int digit12 = calculateModules(digitsFrom1to11withCoefficients);

        for (int digit : digitsFrom1to10) {
            this.itn.add(digit);
        }
        this.itn.add(digit11);
        this.itn.add(digit12);
    }

    @Override
    public String toString() {
        StringBuilder result = new StringBuilder();
        for (int digit : this.itn) {
            result.append(digit);
        }
        return result.toString();
    }

    private int calculateModules(int[] digitArray) {
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
