package modules;

import java.util.concurrent.ThreadLocalRandom;

class RandomNumber {

    RandomNumber() {}

    int generateWithoutStart(int bound) {
        return ThreadLocalRandom.current().nextInt(0, bound);
    }

    int generateWithStart(int start, int bound) {
        return ThreadLocalRandom.current().nextInt(start, bound);
    }
}
