package main.modules;

import java.util.concurrent.ThreadLocalRandom;

class RandomNumber {

    private int randomNumber;
    private int start = 0;

    RandomNumber(int start, int bound) {
        this.start = start;

        this.randomNumber = generateRandomNumber(bound);
    }

    RandomNumber(int bound) {
        this.randomNumber = generateRandomNumber(bound);
    }

    private int generateRandomNumber(int bound) {
        return ThreadLocalRandom.current().nextInt(this.start, bound);
    }

    int get() {
        return this.randomNumber;
    }

    String getString() {
        return Integer.toString(this.randomNumber);
    }
}
