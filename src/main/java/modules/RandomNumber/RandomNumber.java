package modules.RandomNumber;

import java.util.concurrent.ThreadLocalRandom;

public class RandomNumber {

    public RandomNumber() {}

    public int generateWithoutStart(int bound) {
        return ThreadLocalRandom.current().nextInt(0, bound);
    }

    public int generateWithStart(int start, int bound) {
        return ThreadLocalRandom.current().nextInt(start, bound);
    }
}
