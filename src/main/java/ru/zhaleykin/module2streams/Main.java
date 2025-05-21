package ru.zhaleykin.module2streams;

import java.util.*;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {

        List<SomeEntity> list = new ArrayList<>();
        for (int i = 0; i < 10; i++)
            list.add(getRandomSomeEntity());

        Map<Property, Map<Boolean, Integer>> result = list.stream()
                .collect(new CustomCollector());

        for (Property property : Property.values()) {
            int t=0, f=0;
            for (SomeEntity someEntity : list) {
                if (!someEntity.properties().containsKey(property)) continue;
                if (someEntity.properties().get(property)) {
                    t++;
                } else {
                    f++;
                }
            }
            Map<Boolean, Integer> res = result.get(property);
            System.out.println((res.get(true) == t) + " " + (res.get(false) == f));
            System.out.println(property + ": true=" + t + ", false=" + f);
        }
        System.out.println(result);


    }

    private static SomeEntity getRandomSomeEntity() {
        Random r = new Random();
        Map<Property, Boolean> props = new HashMap<>();
        int len = r.nextInt(10);
        for (int i = 0; i < len; i++) {
            props.put(Property.values()[r.nextInt(Property.values().length)], r.nextBoolean());
        }
        props = new HashMap<>();
        return new SomeEntity(r.nextInt(), "qwe", r.nextBoolean(), props);
    }
}