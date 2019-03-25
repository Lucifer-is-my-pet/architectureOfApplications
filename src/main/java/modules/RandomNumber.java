package main.java.modules;

import java.util.concurrent.ThreadLocalRandom;

class RandomNumber {

    private int start = 0;

    RandomNumber() {
    }

    int generateWithoutStart(int bound) {
        return ThreadLocalRandom.current().nextInt(this.start, bound);
    }

    int generateWithStart(int start, int bound) {
        return ThreadLocalRandom.current().nextInt(start, bound);
    }

}
