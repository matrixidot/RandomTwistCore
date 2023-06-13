package me.neo.randomtwistcore.util;

import me.neo.randomtwistcore.RandomTwistCore;

import java.util.List;

public class RandomElements {

    public static <T> T randomItem(List<T> list) {
        return list.get(RandomTwistCore.random.nextInt(0, list.size()));
    }

    public static <T> T pullRandomItem(List<T> list)
    {
        int index = RandomTwistCore.random.nextInt(0, list.size());
        T obj = list.get(index);
        list.remove(index);
        return obj;
    }
}
