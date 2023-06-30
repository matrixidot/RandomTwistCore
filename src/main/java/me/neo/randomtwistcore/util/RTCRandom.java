package me.neo.randomtwistcore.util;

import java.util.List;
import java.util.Random;

/**
 * A class that houses methods used for obtaining random elements from a list.
 */
public class RTCRandom {
    private static final Random random = new Random();

    /**
     * Returns a random element from the list.
     * @param list The list to get a random element from.
     * @param <T> The Type of objects contained in the list.
     * @return The random element picked of type T
     */
    public static <T> T randomItem(List<T> list) {
        return list.get(random.nextInt(0, list.size()));
    }

    /**
     * Returns a random element from the list and removes it from the list.
     * @param list The list to get and remove a random element from.
     * @param <T> The type of objects contained in the list.
     * @return The random element picked and removed of type T
     */
    public static <T> T pullRandomItem(List<T> list)
    {
        int index = random.nextInt(0, list.size());
        T obj = list.get(index);
        list.remove(index);
        return obj;
    }
}
