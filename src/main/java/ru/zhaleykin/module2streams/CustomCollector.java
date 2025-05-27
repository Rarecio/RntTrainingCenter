package ru.zhaleykin.module2streams;

import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collector;

// <T> – входящие элементы
// <A> – изменяемый аккумулятор
// <R> – результат

//<T> -> SomeEntity
//<A> = <R> = EnumMap<Property, Map<Boolean, Integer>

public class CustomCollector implements Collector<SomeEntity, Map<Property, Map<Boolean, Integer>>, Map<Property, Map<Boolean, Integer>>> {
    @Override
    public Supplier<Map<Property, Map<Boolean, Integer>>> supplier() {
        return HashMap::new;
    }

    @Override
    public BiConsumer<Map<Property, Map<Boolean, Integer>>, SomeEntity> accumulator() {
        return (map, se) -> se.properties()
                .forEach((key, value) -> map
                        .computeIfAbsent(key, k -> new HashMap<>())
                        .merge(value, 1, Integer::sum));
    }

    @Override
    public BinaryOperator<Map<Property, Map<Boolean, Integer>>> combiner() {
        return (map1, map2) -> {
            map1.forEach((property, boolIntMap1) ->
                map2.merge(property, boolIntMap1, (valueMap2, valueMap1) -> {
                    valueMap1.forEach((flag, count) -> valueMap2.merge(flag, count, Integer::sum));
                    return valueMap2;
                })
            );
            return map2;
        };
    }

    @Override
    public Function<Map<Property, Map<Boolean, Integer>>, Map<Property, Map<Boolean, Integer>>> finisher() {
        return e -> e;
    }

    @Override
    public Set<Characteristics> characteristics() {
        return EnumSet.of(Characteristics.IDENTITY_FINISH, Characteristics.UNORDERED);
    }
}